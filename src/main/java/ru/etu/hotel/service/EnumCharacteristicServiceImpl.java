package ru.etu.hotel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.etu.hotel.model.dto.request.CharacteristicRequest;
import ru.etu.hotel.model.dto.request.UpdateCharacteristicRequest;
import ru.etu.hotel.model.dto.response.CharacteristicResponse;
import ru.etu.hotel.model.dto.response.CharacteristicValueResponse;
import ru.etu.hotel.model.entity.EnumCharacteristic;
import ru.etu.hotel.repository.EnumCharacteristicRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EnumCharacteristicServiceImpl implements EnumCharacteristicService {
    
    private final EnumCharacteristicRepository repository;
    
    @Override
    public Integer addCharacteristic(CharacteristicRequest request) {
        EnumCharacteristic characteristic = EnumCharacteristic.builder()
                .characteristicName(request.getCharacteristicName())
                .classId(request.getClassId())
                .valueNumber(request.getValueNumber())
                .valueString(request.getValueString())
                .valueImage(request.getValueImage())
                .unitOfMeasure(request.getUnitOfMeasure())
                .sortOrder(request.getSortOrder())
                .build();
        
        EnumCharacteristic saved = repository.save(characteristic);
        log.info("Added characteristic: id={}, name={}, classId={}", 
                 saved.getId(), saved.getCharacteristicName(), saved.getClassId());
        return saved.getId();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CharacteristicResponse> getCharacteristicsByClass(Integer classId) {
        List<Object[]> results = repository.getClassCharacteristicsNative(classId);
        return mapToResponseList(results);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CharacteristicResponse> getAllCharacteristics() {
        List<Object[]> results = repository.getAllCharacteristicsNative();
        return mapToResponseList(results);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CharacteristicValueResponse getCharacteristicValue(Integer classId, String name) {
        List<Object[]> results = repository.getCharacteristicValueNative(classId, name);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Characteristic not found: " + name + " for class " + classId);
        }
        Object[] row = results.get(0);
        return CharacteristicValueResponse.builder()
                .valueNumber(row[0] != null ? new BigDecimal(row[0].toString()) : null)
                .valueString((String) row[1])
                .valueImage((String) row[2])
                .unitOfMeasure((String) row[3])
                .build();
    }
    
    @Override
    public void reorderCharacteristic(Integer valueId, Integer newOrder) {
        Boolean result = repository.reorderEnumValue(valueId, newOrder);
        if (!result) {
            throw new EntityNotFoundException("Characteristic not found with id: " + valueId);
        }
        log.info("Reordered characteristic: id={}, newOrder={}", valueId, newOrder);
    }
    
    @Override
    public void updateCharacteristic(Integer id, UpdateCharacteristicRequest request) {
        Boolean result = repository.updateCharacteristicValue(
                id,
                request.getCharacteristicName(),
                request.getValueNumber(),
                request.getValueString(),
                request.getValueImage(),
                request.getUnitOfMeasure(),
                request.getSortOrder()
        );
        if (!result) {
            throw new EntityNotFoundException("Characteristic not found with id: " + id);
        }
        log.info("Updated characteristic: id={}", id);
    }
    
    @Override
    public void deleteCharacteristic(Integer id) {
        Boolean result = repository.deleteCharacteristicById(id);
        if (!result) {
            throw new EntityNotFoundException("Characteristic not found with id: " + id);
        }
        log.info("Deleted characteristic: id={}", id);
    }
    
    private List<CharacteristicResponse> mapToResponseList(List<Object[]> results) {
        List<CharacteristicResponse> responses = new ArrayList<>();
        for (Object[] row : results) {
            responses.add(CharacteristicResponse.builder()
                    .id(((Number) row[0]).intValue())
                    .characteristicName((String) row[1])
                    .classId(row[2] != null ? ((Number) row[2]).intValue() : null)
                    .valueNumber(row[3] != null ? new BigDecimal(row[3].toString()) : null)
                    .valueString((String) row[4])
                    .valueImage((String) row[5])
                    .unitOfMeasure((String) row[6])
                    .sortOrder(row[7] != null ? ((Number) row[7]).intValue() : null)
                    .build());
        }
        return responses;
    }
}