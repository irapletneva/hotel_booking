package ru.etu.hotel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.etu.hotel.model.dto.request.CharacteristicRequest;
import ru.etu.hotel.model.dto.request.UpdateCharacteristicRequest;
import ru.etu.hotel.model.dto.response.CharacteristicResponse;
import ru.etu.hotel.model.dto.response.CharacteristicValueResponse;
import ru.etu.hotel.model.dto.response.IdResponse;
import ru.etu.hotel.service.EnumCharacteristicService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/characteristics")
@RequiredArgsConstructor
@Slf4j
public class EnumCharacteristicController {
    
    private final EnumCharacteristicService characteristicService;
    
    // 1. Добавить новую характеристику
    @PostMapping
    public ResponseEntity<IdResponse> addCharacteristic(@RequestBody CharacteristicRequest request) {
        Integer id = characteristicService.addCharacteristic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new IdResponse(id));
    }
    
    // 2. Получить все характеристики для класса
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<CharacteristicResponse>> getByClass(@PathVariable Integer classId) {
        return ResponseEntity.ok(characteristicService.getCharacteristicsByClass(classId));
    }
    
    // 3. Получить все характеристики (для админки)
    @GetMapping
    public ResponseEntity<List<CharacteristicResponse>> getAll() {
        return ResponseEntity.ok(characteristicService.getAllCharacteristics());
    }
    
    // 4. Получить значение конкретной характеристики
    @GetMapping("/value")
    public ResponseEntity<CharacteristicValueResponse> getValue(
            @RequestParam Integer classId,
            @RequestParam String name) {
        return ResponseEntity.ok(characteristicService.getCharacteristicValue(classId, name));
    }
    
    // 5. Изменить порядок характеристики
    @PutMapping("/{valueId}/reorder")
    public ResponseEntity<Map<String, String>> reorderCharacteristic(
            @PathVariable Integer valueId,
            @RequestParam Integer newOrder) {
        characteristicService.reorderCharacteristic(valueId, newOrder);
        return ResponseEntity.ok(Map.of("message", "order updated"));
    }
    
    // 6. Обновить характеристику (название, значение, единицу, порядок)
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateCharacteristic(
            @PathVariable Integer id,
            @RequestBody UpdateCharacteristicRequest request) {
        characteristicService.updateCharacteristic(id, request);
        return ResponseEntity.ok(Map.of("message", "updated"));
    }
    
    // 7. Удалить характеристику
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCharacteristic(@PathVariable Integer id) {
        characteristicService.deleteCharacteristic(id);
        return ResponseEntity.ok(Map.of("message", "deleted"));
    }
}