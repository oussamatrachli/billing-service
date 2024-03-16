package org.demo.billingservice.web;

import org.demo.billingservice.dto.InvoiceRequestDTO;
import org.demo.billingservice.dto.InvoiceResponseDTO;
import org.demo.billingservice.exceptions.InvalidCustomerException;
import org.demo.billingservice.services.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @GetMapping(path = "/invoice/{id}")
    public InvoiceResponseDTO getInvoice(@PathVariable(name = "id") String invoiceId){
        return invoiceService.getInvoice(invoiceId);
    }

    @GetMapping(path = "/invoices/{customerId}")
    public List<InvoiceResponseDTO> getInvoicesByCustomerId(@PathVariable(name = "customerId") String customerId){
        return invoiceService.getInvoicesByCustomerId(customerId);
    }

    @PostMapping(path = "/invoice")
    public InvoiceResponseDTO saveInvoice(@RequestBody InvoiceRequestDTO invoiceRequestDTO) throws InvalidCustomerException {
        return invoiceService.save(invoiceRequestDTO);
    }

    @GetMapping(path = "/invoices")
    public List<InvoiceResponseDTO> getAllInvoices(){
        return invoiceService.getAllInvoices();
    }
}
