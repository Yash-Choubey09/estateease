package com.estateease.repository;

import com.estateease.model.*;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    @Query("select p from Property p where " +
           "(:query is null or lower(p.location) like lower(concat('%',:query,'%')) or lower(p.title) like lower(concat('%',:query,'%')) or lower(p.description) like lower(concat('%',:query,'%'))) " +
           "and (:type is null or p.type = :type) " +
           "and (:availability is null or p.availability = :availability) " +
           "and (:maxPrice is null or p.price <= :maxPrice) " +
           "and (:minBedrooms is null or p.bedrooms >= :minBedrooms) " +
           "order by p.createdAt desc")
    List<Property> search(
        @Param("query") String query,
        @Param("type") PropertyType type,
        @Param("availability") Availability availability,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("minBedrooms") Integer minBedrooms
    );

    List<Property> findTop6ByAvailabilityOrderByCreatedAtDesc(Availability availability);

    long countByOwnerId(Long ownerId);

    long countByOwnerIdAndAvailability(Long ownerId, Availability availability);
}
