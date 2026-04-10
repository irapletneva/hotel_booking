package ru.etu.hotel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.etu.hotel.model.dto.request.ClassificationElementRequest;
import ru.etu.hotel.model.dto.response.ClassificationElementResponse;
import ru.etu.hotel.model.dto.response.IdResponse;
import ru.etu.hotel.service.ClassificationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/classification")
@RequiredArgsConstructor
@Slf4j
public class ClassificationController {

    private final ClassificationService classificationService;

    // Управление структурой

    @PostMapping
    public ResponseEntity<IdResponse> addElement(@RequestBody ClassificationElementRequest request) {
        Integer id = classificationService.addElement(request);
        log.info("Created element with id={}", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteElement(@PathVariable("id") Integer id) {
        classificationService.deleteElement(id);
        return ResponseEntity.ok(Map.of("message", "deleted"));
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<Map<String, String>> moveElement(
            @PathVariable("id") Integer id,
            @RequestParam("newParent") Integer newParentId) {
        classificationService.moveElement(id, newParentId);
        return ResponseEntity.ok(Map.of("message", "moved successfully"));
    }

    @PutMapping("/{id}/order")
    public ResponseEntity<Map<String, String>> changeOrder(
            @PathVariable("id") Integer id,
            @RequestParam("newOrder") Integer newSortOrder) {
        classificationService.changeOrder(id, newSortOrder);
        return ResponseEntity.ok(Map.of("message", "order updated"));
    }

    @PutMapping("/{id}/unit")
    public ResponseEntity<Map<String, String>> changeUnit(
            @PathVariable("id") Integer id,
            @RequestParam("unit") String newUnit) {
        classificationService.changeUnit(id, newUnit);
        return ResponseEntity.ok(Map.of("message", "unit of measure updated"));
    }

    // Информационный поиск

    @GetMapping("/{id}/children")
    public ResponseEntity<List<ClassificationElementResponse>> getChildren(@PathVariable("id") Integer id) {
        List<ClassificationElementResponse> children = classificationService.getChildren(id);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/{id}/parents")
    public ResponseEntity<List<ClassificationElementResponse>> getParents(@PathVariable("id") Integer id) {
        List<ClassificationElementResponse> parents = classificationService.getParents(id);
        return ResponseEntity.ok(parents);
    }

    @GetMapping("/terminal")
    public ResponseEntity<List<ClassificationElementResponse>> getTerminalNodes() {
        List<ClassificationElementResponse> terminal = classificationService.getTerminalNodes();
        return ResponseEntity.ok(terminal);
    }

    @GetMapping("/{id}/terminal")
    public ResponseEntity<List<ClassificationElementResponse>> getTerminalInBranch(@PathVariable("id") Integer id) {
        List<ClassificationElementResponse> terminal = classificationService.getTerminalInBranch(id);
        return ResponseEntity.ok(terminal);
    }

    @GetMapping("/siblings")
    public ResponseEntity<List<ClassificationElementResponse>> getSiblings(
            @RequestParam(required = false) Integer parentId) {
        List<ClassificationElementResponse> siblings = classificationService.getSiblings(parentId);
        return ResponseEntity.ok(siblings);
    }
}
