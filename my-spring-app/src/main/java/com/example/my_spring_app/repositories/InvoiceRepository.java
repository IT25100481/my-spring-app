package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // We name it something simple so there are no naming convention errors
    @Query("SELECT i FROM Invoice i WHERE i.booking.vendor.user.email = :email")
    List<Invoice> findAllInvoicesByEmail(@Param("email") String email);
}