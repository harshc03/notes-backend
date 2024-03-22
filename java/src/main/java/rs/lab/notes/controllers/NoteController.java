package rs.lab.notes.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.lab.notes.data.model.NoteStateEnum;
import rs.lab.notes.data.mapping.NoteMapper;
import rs.lab.notes.data.mapping.SharedNoteMapper;
import rs.lab.notes.data.model.Note;
import rs.lab.notes.data.model.SharedNote;
import rs.lab.notes.dto.NoteDto;
import rs.lab.notes.dto.SharedNoteModifyDto;
import rs.lab.notes.services.NoteService;
import rs.lab.notes.services.SharedNoteService;

@RestController
@Validated
@PreAuthorize("hasRole('USER')")
@Tag(name = "Note")
public class NoteController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private SharedNoteService sharedNoteService;
    @Autowired
    private NoteMapper mapper;
    @Autowired
    private SharedNoteMapper sharedMapper;

    @GetMapping("/notes.get/{id}")
    public ResponseEntity<?> getNote(@PathVariable("id") Long id) {
        var returnValue = noteService.getNote(id);
        return returnValue.isPresent()
                ? ResponseEntity.ok(mapper.toNoteDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/notes.modify/{id}")
    public ResponseEntity<?> modifyNote(@PathVariable("id") Long id, @Valid @RequestBody NoteDto noteDto) {
        var returnValue = noteService.modifyNote(
                id,
                noteDto.getCaption(),
                NoteStateEnum.valueOf(noteDto.getState().toUpperCase()),
                noteDto.getBody(),
                noteDto.getCategoryName()
        );

        return returnValue.isPresent()
                ? ResponseEntity.ok(mapper.toNoteDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/notes.create")
    public ResponseEntity<?> createNote(@Valid @RequestBody NoteDto noteDto) {
        var returnValue = noteService.createNote(
                noteDto.getCaption(),
                NoteStateEnum.valueOf(noteDto.getState().toUpperCase()),
                noteDto.getBody(),
                noteDto.getCategoryName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toNoteDto(returnValue));
    }

    @GetMapping("/notes.list")
    public ResponseEntity<?> noteList(@ParameterObject Pageable pageable) {
        var returnValue = noteService.listNotes(pageable);
        return ResponseEntity.ok(mapper.toPageDto(returnValue));
    }

    @GetMapping("/notes.search")
    public ResponseEntity<?> noteSearch(@RequestParam(value = "q", required = false) String query, @ParameterObject Pageable pageable) {
        Page<Note> returnValue;
        if (StringUtils.hasLength(query)) {
            returnValue = noteService.searchByCaption(query, pageable);
        } else {
            returnValue = noteService.listNotes(pageable);
        }
        return ResponseEntity.ok(mapper.toPageDto(returnValue));
    }

    @DeleteMapping("/notes.delete/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable("id") Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shared/notes.list")
    public ResponseEntity<?> noteShared(@ParameterObject Pageable pageable) {
        var returnValue = sharedNoteService.listUserShares(pageable);
        return ResponseEntity.ok(sharedMapper.toPageDto(returnValue));
    }

    @GetMapping("/shared/notes.search")
    public ResponseEntity<?> noteSharedSearch(@RequestParam(value = "q", required = false) String query, @ParameterObject Pageable pageable) {
        Page<SharedNote> returnValue;
        if (StringUtils.hasLength(query)) {
            returnValue = sharedNoteService.searchByNoteCaption(query, pageable);
        } else {
            returnValue = sharedNoteService.listUserShares(pageable);
        }
        return ResponseEntity.ok(sharedMapper.toPageDto(returnValue));
    }

    @PutMapping("/shared/notes.modify/{id}")
    public ResponseEntity<?> modifySharedNote(@PathVariable("id") Long id, @Valid @RequestBody SharedNoteModifyDto shareNoteDto) {
        var returnValue = sharedNoteService.modifySharedNote(id, shareNoteDto.getBody());
        return returnValue.isPresent()
                ? ResponseEntity.ok(sharedMapper.toSharedNoteDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/shared/notes.get/{id}")
    public ResponseEntity<?> getSharedNote(@PathVariable("id") Long id) {
        var returnValue = sharedNoteService.getSharedNoteNote(id);
        return returnValue.isPresent()
                ? ResponseEntity.ok(sharedMapper.toSharedNoteDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }
}
