package com.estateease.repository;
import com.estateease.model.Inquiry; import java.util.*; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;
public interface InquiryRepository extends JpaRepository<Inquiry,Long>{
 @Query("select i from Inquiry i join fetch i.property p join fetch i.tenant t where p.owner.id=:ownerId order by i.createdAt desc") List<Inquiry> findForOwner(@Param("ownerId") Long ownerId);
 @Query("select i from Inquiry i join fetch i.property p where i.tenant.id=:tenantId order by i.createdAt desc") List<Inquiry> findForTenant(@Param("tenantId") Long tenantId);
 long countByPropertyOwnerId(Long ownerId);
}
