package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByBooking_Id(Long bookingId);
    List<Invoice> findByVendor_Id(Long vendorId);
    List<Invoice> findByCustomer_Id(Long customerId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByVendor_IdAndStatus(Long vendorId, String status);
    List<Invoice> findByVendor_IdOrderByInvoiceDateDesc(Long vendorId);
}
