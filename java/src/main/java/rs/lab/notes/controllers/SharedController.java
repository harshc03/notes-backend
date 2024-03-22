package rs.lab.notes.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.lab.notes.data.mapping.SharedNoteMapper;
import rs.lab.notes.data.model.SharedNoteAccessEnum;
import rs.lab.notes.dto.SharedNoteDto;
import rs.lab.notes.services.SharedNoteService;

@RestController
@Validated
@PreAuthorize("hasRole('USER')")
@Tag(name = "Shared")
public class SharedController {

    @Autowired
    private SharedNoteService sharedNoteService;
    @Autowired
    private SharedNoteMapper mapper;
    
    @PostMapping("/shared.create/{id}")
    public ResponseEntity<?> createSharedNote(@PathVariable("id") Long id, @Valid @RequestBody SharedNoteDto sharedDto) {
        sharedNoteService.createSharedNote(
                id,
                sharedDto.getUserEmail(),
                SharedNoteAccessEnum.valueOf(sharedDto.getAccess().toUpperCase())
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shared.get/{id}")
    public ResponseEntity<?> getSharedNote(@PathVariable("id") Long id) {
        var returnValue = sharedNoteService.getSharedNote(id);
        return (returnValue.isPresent())
                ? ResponseEntity.ok(mapper.toSharedNoteDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/shared.list")
    public ResponseEntity<?> noteList(@ParameterObject Pageable pageable) {
        var returnValue = sharedNoteService.listSharedByMe(pageable);
        return ResponseEntity.ok(mapper.toPageDto(returnValue));
    }

    @DeleteMapping("/shared.delete/{id}")
    public ResponseEntity<?> deleteShare(@PathVariable("id") Long id) {
        sharedNoteService.deleteShare(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/shared.modifyAccess/{id}")
    public ResponseEntity<?> modifyAccess(@PathVariable("id") Long id, @Valid @RequestBody SharedNoteDto sharedDto) {
        var returnValue = sharedNoteService.modifyShareAccess(id, SharedNoteAccessEnum.valueOf(sharedDto.getAccess().toUpperCase()));
        return (returnValue.isPresent())
                ? ResponseEntity.ok(mapper.toSharedNoteDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/shared.users/{id}")
    public ResponseEntity<?> sharedNoteUsers(@PathVariable("id") Long id, @ParameterObject Pageable pageable) {
        var returnValue = sharedNoteService.getAllUserByNoteId(id, pageable);
        return ResponseEntity.ok(mapper.toPageSharedUserDto(returnValue));
    }
}
