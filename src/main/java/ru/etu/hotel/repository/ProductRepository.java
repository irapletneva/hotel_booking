package ru.etu.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.etu.hotel.model.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT insert_product(:name, :shortName, :classId)", nativeQuery = true)
    Integer insertProduct(@Param("name") String name,
                          @Param("shortName") String shortName,
                          @Param("classId") Integer classId);

    @Query(value = "SELECT delete_product(:productId)", nativeQuery = true)
    Boolean deleteProduct(@Param("productId") Integer productId);

    @Query(value = "SELECT swap_product_class(:productId, :newClassId)", nativeQuery = true)
    Boolean swapProductClass(@Param("productId") Integer productId,
                             @Param("newClassId") Integer newClassId);

    @Query(value = "SELECT update_product(:productId, :name, :shortName)", nativeQuery = true)
    Boolean updateProduct(@Param("productId") Integer productId,
                          @Param("name") String name,
                          @Param("shortName") String shortName);

    @Query(value = "SELECT * FROM find_products(:classId, :limit, :offset)", nativeQuery = true)
    List<Object[]> findProducts(@Param("classId") Integer classId,
                                @Param("limit") Integer limit,
                                @Param("offset") Integer offset);
}
