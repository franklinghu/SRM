package com.srm.modules.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.modules.finance.entity.Invoice;
import com.srm.modules.finance.entity.PaymentRequest;

public interface FinanceService {
    IPage<Invoice> getInvoicePage(Integer pageNum, Integer pageSize, String invoiceNo, String status);
    Invoice getInvoiceById(Long id);
    Invoice createInvoice(Invoice invoice);
    Invoice updateInvoice(Invoice invoice);
    void deleteInvoice(Long id);

    IPage<PaymentRequest> getPaymentPage(Integer pageNum, Integer pageSize, String requestNo, String status);
    PaymentRequest getPaymentById(Long id);
    PaymentRequest createPayment(PaymentRequest payment);
    PaymentRequest updatePayment(PaymentRequest payment);
    void deletePayment(Long id);
}
