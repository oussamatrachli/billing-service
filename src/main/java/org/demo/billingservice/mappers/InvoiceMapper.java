package org.demo.billingservice.mappers;

import org.demo.billingservice.dto.InvoiceRequestDTO;
import org.demo.billingservice.dto.InvoiceResponseDTO;
import org.demo.billingservice.entities.Invoice;
import org.demo.billingservice.repositories.InvoiceRepository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    Invoice fromInvoiceRequestDTOToInvoice(InvoiceRequestDTO invoiceRequestDTO);

    InvoiceResponseDTO fromInvoiceToInvoiceResponseDTO(Invoice invoice);
}
