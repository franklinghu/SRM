package com.srm.modules.purchase.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.common.result.Result;
import com.srm.modules.purchase.entity.PurchaseRequest;
import com.srm.modules.purchase.entity.PurchaseOrder;
import com.srm.modules.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    // 采购需求相关接口
    @GetMapping("/requests")
    public Result<IPage<PurchaseRequest>> getRequestPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status) {
        IPage<PurchaseRequest> page = purchaseService.getRequestPage(pageNum, pageSize, title, status);
        return Result.success(page);
    }

    @GetMapping("/requests/{id}")
    public Result<PurchaseRequest> getRequestById(@PathVariable Long id) {
        PurchaseRequest request = purchaseService.getRequestById(id);
        return Result.success(request);
    }

    @PostMapping("/requests")
    public Result<PurchaseRequest> createRequest(@RequestBody PurchaseRequest request) {
        PurchaseRequest result = purchaseService.createRequest(request);
        return Result.success(result);
    }

    @PutMapping("/requests/{id}")
    public Result<PurchaseRequest> updateRequest(@PathVariable Long id, @RequestBody PurchaseRequest request) {
        request.setId(id);
        PurchaseRequest result = purchaseService.updateRequest(request);
        return Result.success(result);
    }

    @DeleteMapping("/requests/{id}")
    public Result<Void> deleteRequest(@PathVariable Long id) {
        purchaseService.deleteRequest(id);
        return Result.success();
    }

    // 采购订单相关接口
    @GetMapping("/orders")
    public Result<IPage<PurchaseOrder>> getOrderPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String status) {
        IPage<PurchaseOrder> page = purchaseService.getOrderPage(pageNum, pageSize, orderNo, status);
        return Result.success(page);
    }

    @GetMapping("/orders/{id}")
    public Result<PurchaseOrder> getOrderById(@PathVariable Long id) {
        PurchaseOrder order = purchaseService.getOrderById(id);
        return Result.success(order);
    }

    @PostMapping("/orders")
    public Result<PurchaseOrder> createOrder(@RequestBody PurchaseOrder order) {
        PurchaseOrder result = purchaseService.createOrder(order);
        return Result.success(result);
    }

    @PutMapping("/orders/{id}")
    public Result<PurchaseOrder> updateOrder(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        order.setId(id);
        PurchaseOrder result = purchaseService.updateOrder(order);
        return Result.success(result);
    }

    @DeleteMapping("/orders/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        purchaseService.deleteOrder(id);
        return Result.success();
    }
}
