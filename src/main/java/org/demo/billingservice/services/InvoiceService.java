package org.demo.billingservice.services;

import org.demo.billingservice.dto.InvoiceRequestDTO;
import org.demo.billingservice.dto.InvoiceResponseDTO;
import org.demo.billingservice.exceptions.InvalidCustomerException;

import java.util.List;

public interface InvoiceService {
    InvoiceResponseDTO save(InvoiceRequestDTO invoiceRequestDTO) throws InvalidCustomerException;
    InvoiceResponseDTO getInvoice(String invoiceId);
    List<InvoiceResponseDTO> getInvoicesByCustomerId(String customerId);
    List<InvoiceResponseDTO> getAllInvoices();
}
