package com.srm.modules.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.common.result.Result;
import com.srm.modules.finance.entity.Invoice;
import com.srm.modules.finance.entity.PaymentRequest;
import com.srm.modules.finance.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/invoices")
    public Result<IPage<Invoice>> getInvoicePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String invoiceNo,
            @RequestParam(required = false) String status) {
        IPage<Invoice> page = financeService.getInvoicePage(pageNum, pageSize, invoiceNo, status);
        return Result.success(page);
    }

    @GetMapping("/invoices/{id}")
    public Result<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = financeService.getInvoiceById(id);
        return Result.success(invoice);
    }

    @PostMapping("/invoices")
    public Result<Invoice> createInvoice(@RequestBody Invoice invoice) {
        Invoice result = financeService.createInvoice(invoice);
        return Result.success(result);
    }

    @PutMapping("/invoices/{id}")
    public Result<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice invoice) {
        invoice.setId(id);
        Invoice result = financeService.updateInvoice(invoice);
        return Result.success(result);
    }

    @DeleteMapping("/invoices/{id}")
    public Result<Void> deleteInvoice(@PathVariable Long id) {
        financeService.deleteInvoice(id);
        return Result.success();
    }

    @GetMapping("/payments")
    public Result<IPage<PaymentRequest>> getPaymentPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String requestNo,
            @RequestParam(required = false) String status) {
        IPage<PaymentRequest> page = financeService.getPaymentPage(pageNum, pageSize, requestNo, status);
        return Result.success(page);
    }

    @GetMapping("/payments/{id}")
    public Result<PaymentRequest> getPaymentById(@PathVariable Long id) {
        PaymentRequest payment = financeService.getPaymentById(id);
        return Result.success(payment);
    }

    @PostMapping("/payments")
    public Result<PaymentRequest> createPayment(@RequestBody PaymentRequest payment) {
        PaymentRequest result = financeService.createPayment(payment);
        return Result.success(result);
    }

    @PutMapping("/payments/{id}")
    public Result<PaymentRequest> updatePayment(@PathVariable Long id, @RequestBody PaymentRequest payment) {
        payment.setId(id);
        PaymentRequest result = financeService.updatePayment(payment);
        return Result.success(result);
    }

    @DeleteMapping("/payments/{id}")
    public Result<Void> deletePayment(@PathVariable Long id) {
        financeService.deletePayment(id);
        return Result.success();
    }
}
