package com.srm.modules.supplier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.supplier.entity.Supplier;
import com.srm.modules.supplier.entity.SupplierCertificate;
import com.srm.modules.supplier.entity.SupplierContact;
import com.srm.modules.supplier.mapper.SupplierMapper;
import com.srm.modules.supplier.mapper.SupplierContactMapper;
import com.srm.modules.supplier.mapper.SupplierCertificateMapper;
import com.srm.modules.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierMapper supplierMapper;
    private final SupplierContactMapper supplierContactMapper;
    private final SupplierCertificateMapper supplierCertificateMapper;

    @Override
    public IPage<Supplier> getSupplierPage(Integer pageNum, Integer pageSize, String supplierName, Integer status) {
        Page<Supplier> page = new Page<>(pageNum, pageSize);
        return supplierMapper.selectPageList(page, supplierName, status, 1L);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierMapper.selectById(id);
    }

    @Override
    @Transactional
    public Supplier createSupplier(Supplier supplier) {
        supplier.setTenantId(1L);
        supplierMapper.insert(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public Supplier updateSupplier(Supplier supplier) {
        supplierMapper.updateById(supplier);
        return supplier;
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        supplierMapper.deleteById(id);
    }

    @Override
    public List<SupplierContact> getContactsBySupplierId(Long supplierId) {
        LambdaQueryWrapper<SupplierContact> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupplierContact::getSupplierId, supplierId);
        wrapper.orderByDesc(SupplierContact::getIsPrimary);
        return supplierContactMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public SupplierContact createContact(SupplierContact contact) {
        supplierContactMapper.insert(contact);
        return contact;
    }

    @Override
    @Transactional
    public void updateContact(SupplierContact contact) {
        supplierContactMapper.updateById(contact);
    }

    @Override
    @Transactional
    public void deleteContact(Long id) {
        supplierContactMapper.deleteById(id);
    }

    @Override
    public List<SupplierCertificate> getCertificatesBySupplierId(Long supplierId) {
        LambdaQueryWrapper<SupplierCertificate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupplierCertificate::getSupplierId, supplierId);
        return supplierCertificateMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public SupplierCertificate createCertificate(SupplierCertificate certificate) {
        supplierCertificateMapper.insert(certificate);
        return certificate;
    }

    @Override
    @Transactional
    public void updateCertificate(SupplierCertificate certificate) {
        supplierCertificateMapper.updateById(certificate);
    }

    @Override
    @Transactional
    public void deleteCertificate(Long id) {
        supplierCertificateMapper.deleteById(id);
    }

    @Override
    public List<SupplierCertificate> getExpiringCertificates(int days) {
        LocalDate expireDate = LocalDate.now().plusDays(days);
        LambdaQueryWrapper<SupplierCertificate> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(SupplierCertificate::getExpireDate, expireDate);
        wrapper.eq(SupplierCertificate::getStatus, 1);
        return supplierCertificateMapper.selectList(wrapper);
    }
}
