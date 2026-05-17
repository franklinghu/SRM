package com.srm.modules.supplier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.common.result.Result;
import com.srm.modules.supplier.entity.Supplier;
import com.srm.modules.supplier.entity.SupplierCertificate;
import com.srm.modules.supplier.entity.SupplierContact;
import com.srm.modules.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public Result<IPage<Supplier>> getSupplierPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) Integer status) {
        IPage<Supplier> page = supplierService.getSupplierPage(pageNum, pageSize, supplierName, status);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<Supplier> getSupplierById(@PathVariable Long id) {
        Supplier supplier = supplierService.getSupplierById(id);
        return Result.success(supplier);
    }

    @PostMapping
    public Result<Supplier> createSupplier(@RequestBody Supplier supplier) {
        Supplier result = supplierService.createSupplier(supplier);
        return Result.success(result);
    }

    @PutMapping("/{id}")
    public Result<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        supplier.setId(id);
        Supplier result = supplierService.updateSupplier(supplier);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return Result.success();
    }

    @GetMapping("/{id}/contacts")
    public Result<List<SupplierContact>> getContacts(@PathVariable Long id) {
        List<SupplierContact> contacts = supplierService.getContactsBySupplierId(id);
        return Result.success(contacts);
    }

    @PostMapping("/{id}/contacts")
    public Result<SupplierContact> createContact(@PathVariable Long id, @RequestBody SupplierContact contact) {
        contact.setSupplierId(id);
        SupplierContact result = supplierService.createContact(contact);
        return Result.success(result);
    }

    @PutMapping("/contacts/{contactId}")
    public Result<Void> updateContact(@PathVariable Long contactId, @RequestBody SupplierContact contact) {
        contact.setId(contactId);
        supplierService.updateContact(contact);
        return Result.success();
    }

    @DeleteMapping("/contacts/{contactId}")
    public Result<Void> deleteContact(@PathVariable Long contactId) {
        supplierService.deleteContact(contactId);
        return Result.success();
    }

    @GetMapping("/{id}/certificates")
    public Result<List<SupplierCertificate>> getCertificates(@PathVariable Long id) {
        List<SupplierCertificate> certificates = supplierService.getCertificatesBySupplierId(id);
        return Result.success(certificates);
    }

    @PostMapping("/{id}/certificates")
    public Result<SupplierCertificate> createCertificate(@PathVariable Long id, @RequestBody SupplierCertificate certificate) {
        certificate.setSupplierId(id);
        SupplierCertificate result = supplierService.createCertificate(certificate);
        return Result.success(result);
    }

    @PutMapping("/certificates/{certId}")
    public Result<Void> updateCertificate(@PathVariable Long certId, @RequestBody SupplierCertificate certificate) {
        certificate.setId(certId);
        supplierService.updateCertificate(certificate);
        return Result.success();
    }

    @DeleteMapping("/certificates/{certId}")
    public Result<Void> deleteCertificate(@PathVariable Long certId) {
        supplierService.deleteCertificate(certId);
        return Result.success();
    }

    @GetMapping("/certificates/expiring")
    public Result<List<SupplierCertificate>> getExpiringCertificates(@RequestParam(defaultValue = "30") int days) {
        List<SupplierCertificate> certificates = supplierService.getExpiringCertificates(days);
        return Result.success(certificates);
    }
}
