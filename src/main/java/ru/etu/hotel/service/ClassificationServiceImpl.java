package ru.etu.hotel.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.etu.hotel.model.dto.request.ClassificationElementRequest;
import ru.etu.hotel.model.dto.response.ClassificationElementResponse;
import ru.etu.hotel.model.entity.ClassificationElement;
import ru.etu.hotel.repository.ClassificationElementRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClassificationServiceImpl implements ClassificationService {

    private final ClassificationElementRepository repository;

    @Override
    public Integer addElement(ClassificationElementRequest request) {
        if (repository.existsByClassCode(request.getClassCode())) {
            throw new IllegalArgumentException(
                    "Element with class_code '" + request.getClassCode() + "' already exists");
        }

        Integer id = repository.insertElement(
                request.getClassCode(),
                request.getName(),
                request.getIsTerminal() != null ? request.getIsTerminal() : false,
                request.getSortOrder() != null ? request.getSortOrder() : 0,
                request.getUnitOfMeasure(),
                request.getParentId()
        );

        log.info("Created classification element: id={}, code={}", id, request.getClassCode());
        return id;
    }

    @Override
    public void deleteElement(Integer id) {
        Boolean result = repository.deleteElement(id);
        if (!result) {
            throw new EntityNotFoundException("Classification element with id " + id + " not found");
        }
        log.info("Deleted classification element: id={}", id);
    }

    @Override
    public void moveElement(Integer id, Integer newParentId) {
        Boolean result = repository.swapParent(id, newParentId);
        if (!result) {
            throw new EntityNotFoundException("Classification element with id " + id + " not found");
        }
        log.info("Moved element: id={}, newParentId={}", id, newParentId);
    }

    @Override
    public void changeOrder(Integer id, Integer newSortOrder) {
        Boolean result = repository.changeSortOrder(id, newSortOrder);
        if (!result) {
            throw new EntityNotFoundException("Classification element with id " + id + " not found");
        }
        log.info("Changed sort order: id={}, newOrder={}", id, newSortOrder);
    }

    @Override
    public void changeUnit(Integer id, String newUnit) {
        Boolean result = repository.changeUnitOfMeasure(id, newUnit);
        if (!result) {
            throw new EntityNotFoundException("Classification element with id " + id + " not found");
        }
        log.info("Changed unit of measure: id={}, unit={}", id, newUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassificationElementResponse> getChildren(Integer id) {
        List<Object[]> results = repository.findChildren(id);
        return mapToObjectArray(results);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassificationElementResponse> getParents(Integer id) {
        List<Object[]> results = repository.findParents(id);
        return mapToObjectArray(results);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassificationElementResponse> getTerminalNodes() {
        List<Object[]> results = repository.findTerminalNodes();
        return mapToObjectArrayNoLevel(results);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassificationElementResponse> getTerminalInBranch(Integer id) {
        List<Object[]> results = repository.findTerminalInBranch(id);
        return mapToObjectArray(results);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassificationElementResponse> getSiblings(Integer parentId) {
        List<ClassificationElement> siblings = repository.findByParentIdOrderBySortOrder(parentId);
        return siblings.stream()
                .map(this::toResponse)
                .toList();
    }

    private List<ClassificationElementResponse> mapToObjectArray(List<Object[]> results) {
        List<ClassificationElementResponse> responseList = new ArrayList<>();
        for (Object[] row : results) {
            ClassificationElementResponse response = ClassificationElementResponse.builder()
                    .id(toInt(row[0]))
                    .classCode((String) row[1])
                    .name((String) row[2])
                    .isTerminal((Boolean) row[3])
                    .sortOrder(toInt(row[4]))
                    .unitOfMeasure((String) row[5])
                    .parentId(toInt(row[6]))
                    .level(toInt(row[7]))
                    .build();
            responseList.add(response);
        }
        return responseList;
    }

    private List<ClassificationElementResponse> mapToObjectArrayNoLevel(List<Object[]> results) {
        List<ClassificationElementResponse> responseList = new ArrayList<>();
        for (Object[] row : results) {
            ClassificationElementResponse response = ClassificationElementResponse.builder()
                    .id(toInt(row[0]))
                    .classCode((String) row[1])
                    .name((String) row[2])
                    .isTerminal((Boolean) row[3])
                    .sortOrder(toInt(row[4]))
                    .unitOfMeasure((String) row[5])
                    .parentId(toInt(row[6]))
                    .level(null)
                    .build();
            responseList.add(response);
        }
        return responseList;
    }

    private Integer toInt(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Number n) return n.intValue();
        return null;
    }

    private ClassificationElementResponse toResponse(ClassificationElement entity) {
        return ClassificationElementResponse.builder()
                .id(entity.getId())
                .classCode(entity.getClassCode())
                .name(entity.getName())
                .isTerminal(entity.getIsTerminal())
                .sortOrder(entity.getSortOrder())
                .unitOfMeasure(entity.getUnitOfMeasure())
                .parentId(entity.getParentId())
                .level(null)
                .build();
    }
}
