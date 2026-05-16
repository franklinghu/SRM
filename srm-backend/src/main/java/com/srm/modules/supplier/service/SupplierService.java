package com.srm.modules.supplier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.modules.supplier.entity.Supplier;
import com.srm.modules.supplier.entity.SupplierContact;
import com.srm.modules.supplier.entity.SupplierCertificate;
import java.util.List;

public interface SupplierService {
    IPage<Supplier> getSupplierPage(Integer pageNum, Integer pageSize, String supplierName, Integer status);
    Supplier getSupplierById(Long id);
    Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Supplier supplier);
    void deleteSupplier(Long id);
    
    List<SupplierContact> getContactsBySupplierId(Long supplierId);
    SupplierContact createContact(SupplierContact contact);
    void updateContact(SupplierContact contact);
    void deleteContact(Long id);
    
    List<SupplierCertificate> getCertificatesBySupplierId(Long supplierId);
    SupplierCertificate createCertificate(SupplierCertificate certificate);
    void updateCertificate(SupplierCertificate certificate);
    void deleteCertificate(Long id);
    List<SupplierCertificate> getExpiringCertificates(int days);
}
