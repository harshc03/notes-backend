package rs.lab.notes.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.lab.notes.data.mapping.NoteMapper;
import rs.lab.notes.services.PublicNoteService;

@RestController
@RequestMapping("/public")
@Tag(name = "Public")
public class PublicController {

    @Autowired
    private PublicNoteService publicNoteService;
    @Autowired
    private NoteMapper mapper;

    @GetMapping("/notes.get/{id}")
    public ResponseEntity<?> getNote(@PathVariable("id") Long id) {
        var returnValue = publicNoteService.getPublicNote(id);
        return returnValue.isPresent()
                ? ResponseEntity.ok(mapper.toNoteDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/notes.list")
    public ResponseEntity<?> noteList(@ParameterObject Pageable pageable) {
        var returnValue = publicNoteService.listPublicNotes(pageable);
        return ResponseEntity.ok(mapper.toPageDto(returnValue));
    }
}
