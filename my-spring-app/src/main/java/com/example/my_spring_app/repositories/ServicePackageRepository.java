package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.ServicePackage;
import com.example.my_spring_app.models.PackageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {
    List<ServicePackage> findByService_Id(Long serviceId);
    List<ServicePackage> findByService_Vendor_Id(Long vendorId);
    List<ServicePackage> findByPackageType(PackageType packageType);
}
