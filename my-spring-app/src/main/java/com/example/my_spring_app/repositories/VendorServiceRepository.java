package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.VendorServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendorServiceRepository extends JpaRepository<VendorServiceEntity, Long> {
    List<VendorServiceEntity> findByVendor_Id(Long vendorId);
    List<VendorServiceEntity> findByVendor_IdAndIsAvailableTrue(Long vendorId);
    List<VendorServiceEntity> findByServiceName(String serviceName);
}
