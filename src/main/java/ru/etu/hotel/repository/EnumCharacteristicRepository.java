package ru.etu.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.etu.hotel.model.entity.EnumCharacteristic;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnumCharacteristicRepository extends JpaRepository<EnumCharacteristic, Integer> {
    
    List<EnumCharacteristic> findByClassIdOrderBySortOrderAsc(Integer classId);
    
    Optional<EnumCharacteristic> findByClassIdAndCharacteristicName(Integer classId, String characteristicName);
    
    @Query(value = "SELECT * FROM get_class_characteristics(:classId)", nativeQuery = true)
    List<Object[]> getClassCharacteristicsNative(@Param("classId") Integer classId);
    
    @Query(value = "SELECT * FROM get_all_characteristics()", nativeQuery = true)
    List<Object[]> getAllCharacteristicsNative();
    
    @Query(value = "SELECT * FROM get_characteristic_value(:classId, :name)", nativeQuery = true)
    List<Object[]> getCharacteristicValueNative(@Param("classId") Integer classId, @Param("name") String name);
    
    @Modifying
    @Transactional
    @Query(value = "SELECT reorder_enum_value(:valueId, :newOrder)", nativeQuery = true)
    Boolean reorderEnumValue(@Param("valueId") Integer valueId, @Param("newOrder") Integer newOrder);
    
    @Modifying
    @Transactional
    @Query(value = "SELECT update_characteristic_value(:id, :name, :valueNumber, :valueString, :valueImage, :unit, :sortOrder)", nativeQuery = true)
    Boolean updateCharacteristicValue(@Param("id") Integer id,
                                       @Param("name") String name,
                                       @Param("valueNumber") BigDecimal valueNumber,
                                       @Param("valueString") String valueString,
                                       @Param("valueImage") String valueImage,
                                       @Param("unit") String unit,
                                       @Param("sortOrder") Integer sortOrder);
    
    @Modifying
    @Transactional
    @Query(value = "SELECT delete_characteristic(:id)", nativeQuery = true)
    Boolean deleteCharacteristicById(@Param("id") Integer id);
}