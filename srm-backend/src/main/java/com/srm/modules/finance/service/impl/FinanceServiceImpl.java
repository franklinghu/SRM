package com.srm.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.finance.entity.Invoice;
import com.srm.modules.finance.entity.PaymentRequest;
import com.srm.modules.finance.mapper.InvoiceMapper;
import com.srm.modules.finance.mapper.PaymentRequestMapper;
import com.srm.modules.finance.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final InvoiceMapper invoiceMapper;
    private final PaymentRequestMapper paymentRequestMapper;

    @Override
    public IPage<Invoice> getInvoicePage(Integer pageNum, Integer pageSize, String invoiceNo, String status) {
        Page<Invoice> page = new Page<>(pageNum, pageSize);
        return invoiceMapper.selectPageList(page, invoiceNo, status, 1L);
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceMapper.selectById(id);
    }

    @Override
    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        invoice.setTenantId(1L);
        invoiceMapper.insert(invoice);
        return invoice;
    }

    @Override
    @Transactional
    public Invoice updateInvoice(Invoice invoice) {
        invoiceMapper.updateById(invoice);
        return invoice;
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        invoiceMapper.deleteById(id);
    }

    @Override
    public IPage<PaymentRequest> getPaymentPage(Integer pageNum, Integer pageSize, String requestNo, String status) {
        Page<PaymentRequest> page = new Page<>(pageNum, pageSize);
        return paymentRequestMapper.selectPageList(page, requestNo, status, 1L);
    }

    @Override
    public PaymentRequest getPaymentById(Long id) {
        return paymentRequestMapper.selectById(id);
    }

    @Override
    @Transactional
    public PaymentRequest createPayment(PaymentRequest payment) {
        payment.setTenantId(1L);
        paymentRequestMapper.insert(payment);
        return payment;
    }

    @Override
    @Transactional
    public PaymentRequest updatePayment(PaymentRequest payment) {
        paymentRequestMapper.updateById(payment);
        return payment;
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        paymentRequestMapper.deleteById(id);
    }
}
