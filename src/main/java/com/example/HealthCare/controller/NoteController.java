package com.example.HealthCare.controller;

import com.example.HealthCare.Util.SecurityUtil;
import com.example.HealthCare.dto.request.note.AddNoteRequest;
import com.example.HealthCare.dto.request.note.UpdateNoteRequest;
import com.example.HealthCare.model.Note;
import com.example.HealthCare.model.User;
import com.example.HealthCare.repository.NoteRepository;
import com.example.HealthCare.service.NoteService;
import com.example.HealthCare.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class NoteController {
    private final NoteService noteService;
    private final NoteRepository noteRepository;

    private final UserService userService;

    public NoteController(NoteService noteService, NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.noteService = noteService;
        this.userService = userService;
    }
    @PostMapping("/notes")
    public ResponseEntity<?> addNote(@Valid @RequestBody AddNoteRequest addNoteRequest) {
        // Lấy email của người dùng hiện tại
        String email = SecurityUtil.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("User not found"));
        User user = this.userService.handleGetUserByEmail(email);

        // Tạo đối tượng Note mới từ request
        Note note = Note.builder()
                .user(user)
                .title(addNoteRequest.getTitle())
                .content(addNoteRequest.getContent())
                .createAt(addNoteRequest.getCreateAt())
                .noteIndex(addNoteRequest.getNoteIndex())
                .build();

        log.info(note.toString());

        // Gọi service để thêm note vào hệ thống
        Note createdNote = noteService.addNote(note);
        return new ResponseEntity<>(createdNote, HttpStatus.OK);
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateNoteRequest updateNoteRequest) {

        // Lấy email của người dùng hiện tại
        String email = SecurityUtil.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("User not found"));
        User user = this.userService.handleGetUserByEmail(email);

        // Tạo đối tượng Note mới từ request và ID
        Note note = Note.builder()
                .id(id)
                .user(user)
                .title(updateNoteRequest.getTitle())
                .content(updateNoteRequest.getContent())
                .createAt(updateNoteRequest.getCreateAt())
                .noteIndex(updateNoteRequest.getNoteIndex())
                .build();

        // Gọi service để cập nhật note
        Note updatedNote = noteService.updateNote(note);
        return new ResponseEntity<>(updatedNote, HttpStatus.OK);
    }
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable("id") Integer id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().body("Xoá thành công");
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") Integer id) {
        Optional<Note> note = noteService.getNoteById(id);
        return note.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/notes")
    public ResponseEntity<List<Note>> getAllNotes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User user = this.userService.handleGetUserByEmail(email);

        Page<Note> notesPage = noteService.getAllNotes(page, size, keyword, user.getId());
        List<Note> notes = notesPage.getContent();

        return new ResponseEntity<>(notes, HttpStatus.OK);
    }
}