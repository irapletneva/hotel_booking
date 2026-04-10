package ru.etu.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.etu.hotel.model.entity.ClassificationElement;

import java.util.List;

@Repository
public interface ClassificationElementRepository extends JpaRepository<ClassificationElement, Integer> {

    boolean existsByClassCode(String classCode);

    @Query(value = "SELECT insert_classification_element(" +
            ":classCode, :name, :isTerminal, :sortOrder, :unitOfMeasure, :parentId)",
            nativeQuery = true)
    Integer insertElement(@Param("classCode") String classCode,
                          @Param("name") String name,
                          @Param("isTerminal") Boolean isTerminal,
                          @Param("sortOrder") Integer sortOrder,
                          @Param("unitOfMeasure") String unitOfMeasure,
                          @Param("parentId") Integer parentId);

    @Query(value = "SELECT swap_parent(:elementId, :newParentId)", nativeQuery = true)
    Boolean swapParent(@Param("elementId") Integer elementId,
                       @Param("newParentId") Integer newParentId);

    @Query(value = "SELECT delete_classification_element(:elementId)", nativeQuery = true)
    Boolean deleteElement(@Param("elementId") Integer elementId);

    @Query(value = "SELECT change_sort_order(:elementId, :newSortOrder)", nativeQuery = true)
    Boolean changeSortOrder(@Param("elementId") Integer elementId,
                            @Param("newSortOrder") Integer newSortOrder);

    @Query(value = "SELECT change_unit_of_measure(:elementId, :newUnit)", nativeQuery = true)
    Boolean changeUnitOfMeasure(@Param("elementId") Integer elementId,
                                @Param("newUnit") String newUnit);

    @Query(value = "SELECT * FROM find_children(:elementId)", nativeQuery = true)
    List<Object[]> findChildren(@Param("elementId") Integer elementId);

    @Query(value = "SELECT * FROM find_parents(:elementId)", nativeQuery = true)
    List<Object[]> findParents(@Param("elementId") Integer elementId);

    @Query(value = "SELECT * FROM find_terminal_nodes()", nativeQuery = true)
    List<Object[]> findTerminalNodes();

    @Query(value = "SELECT * FROM find_terminal_in_branch(:elementId)", nativeQuery = true)
    List<Object[]> findTerminalInBranch(@Param("elementId") Integer elementId);

    @Query("SELECT c FROM ClassificationElement c WHERE c.parentId = :parentId ORDER BY c.sortOrder")
    List<ClassificationElement> findByParentIdOrderBySortOrder(@Param("parentId") Integer parentId);

    List<ClassificationElement> findByIsTerminalTrueOrderBySortOrder();
}
