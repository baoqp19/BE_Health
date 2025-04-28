package com.example.HealthCare.service.impl;

import com.example.HealthCare.model.Note;
import com.example.HealthCare.repository.NoteRepository;
import com.example.HealthCare.service.NoteService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository){
        this.noteRepository = noteRepository;
    }
    @Transactional
    @Override
    public Note addNote(Note note) {
        return noteRepository.save(note);  // Lưu mới note
    }

    @Transactional
    @Override
    public Note updateNote(Note note) {
        // Kiểm tra sự tồn tại của Note và thực hiện cập nhật
        Note existingNote = noteRepository.findById(note.getId())
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        // Cập nhật các trường cần thiết
        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());
        existingNote.setCreateAt(note.getCreateAt());
        existingNote.setNoteIndex(note.getNoteIndex());

        // Lưu lại note đã cập nhật
        return noteRepository.save(existingNote);
    }

    @Override
    public void deleteNote(Integer noteID) {
        Note check = noteRepository.findById(noteID)
                .orElseThrow(() -> new IllegalArgumentException("Allergy not found"));
        noteRepository.deleteById(check.getId());
    }

    @Override
    public Optional<Note> getNoteById(Integer noteID) {
        return noteRepository.findById(noteID);
    }

    @Override
    public Page<Note> getAllNotes(int page, int size, String keyword, Integer userID) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return noteRepository.findByKeyword(keyword, pageable,userID);
        }
        return noteRepository.getNotesByUserID(userID,pageable);
    }

}
