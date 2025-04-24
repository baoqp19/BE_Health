package com.example.HealthCare.service;

import com.example.HealthCare.model.Note;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface NoteService {
    Note addNote(Note note);
    Note updateNote(Note note);
    void deleteNote(Integer noteID);
    Optional<Note> getNoteById(Integer noteID);
    Page<Note> getAllNotes(int page, int size, String keyword, Integer userID);
}
