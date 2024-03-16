package org.demo.billingservice.services;

import jakarta.ws.rs.NotFoundException;
import org.demo.billingservice.dto.InvoiceRequestDTO;
import org.demo.billingservice.dto.InvoiceResponseDTO;
import org.demo.billingservice.entities.Customer;
import org.demo.billingservice.entities.Invoice;
import org.demo.billingservice.exceptions.InvalidCustomerException;
import org.demo.billingservice.mappers.InvoiceMapper;
import org.demo.billingservice.openfeign.CustomerRestClient;
import org.demo.billingservice.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService{

    private InvoiceRepository invoiceRepository;
    private CustomerRestClient customerRestClient;
    private InvoiceMapper invoiceMapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, CustomerRestClient customerRestClient, InvoiceMapper invoiceMapper){
        this.invoiceRepository = invoiceRepository;
        this.customerRestClient = customerRestClient;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public InvoiceResponseDTO save(InvoiceRequestDTO invoiceRequestDTO) throws InvalidCustomerException {
        try{
            Customer customer = customerRestClient.getCustomer(invoiceRequestDTO.getCustomerId());
            Invoice invoice = invoiceMapper.fromInvoiceRequestDTOToInvoice(invoiceRequestDTO);
            invoice.setId(UUID.randomUUID().toString());
            invoice.setDate(new Date());
            Invoice savedInvoice = invoiceRepository.save(invoice);
            savedInvoice.setCustomer(customer);
            return invoiceMapper.fromInvoiceToInvoiceResponseDTO(savedInvoice);
        }
        catch (NotFoundException exception){
            throw new InvalidCustomerException();
        }
    }

    @Override
    public InvoiceResponseDTO getInvoice(String invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if(invoice != null){
            Customer customer = customerRestClient.getCustomer(invoice.getCustomerId());
            invoice.setCustomer(customer);
        }
        return invoiceMapper.fromInvoiceToInvoiceResponseDTO(invoice);

    }

    @Override
    public List<InvoiceResponseDTO> getInvoicesByCustomerId(String customerId) {
        List<Invoice> invoices = invoiceRepository.findByCustomerId(customerId);
        Customer customer = customerRestClient.getCustomer(customerId);
        invoices.forEach(invoice -> {
            invoice.setCustomer(customer);
        });
        return invoices.stream().map(invoice -> invoiceMapper.fromInvoiceToInvoiceResponseDTO(invoice)).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponseDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        List<Customer> customers = customerRestClient.getCustomers();
        invoices.forEach(invoice -> {
            invoice.setCustomer(customers.stream().filter(customer -> customer.getId().equals(invoice.getCustomerId())).findFirst().orElse(null));
        });

        return invoices.stream().map(invoice -> invoiceMapper.fromInvoiceToInvoiceResponseDTO(invoice)).collect(Collectors.toList());
    }
}
