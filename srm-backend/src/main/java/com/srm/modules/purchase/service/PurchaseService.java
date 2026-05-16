package com.srm.modules.purchase.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.modules.purchase.entity.PurchaseRequest;
import com.srm.modules.purchase.entity.PurchaseOrder;

public interface PurchaseService {
    // 采购需求相关
    IPage<PurchaseRequest> getRequestPage(Integer pageNum, Integer pageSize, String title, String status);
    PurchaseRequest getRequestById(Long id);
    PurchaseRequest createRequest(PurchaseRequest request);
    PurchaseRequest updateRequest(PurchaseRequest request);
    void deleteRequest(Long id);
    
    // 采购订单相关
    IPage<PurchaseOrder> getOrderPage(Integer pageNum, Integer pageSize, String orderNo, String status);
    PurchaseOrder getOrderById(Long id);
    PurchaseOrder createOrder(PurchaseOrder order);
    PurchaseOrder updateOrder(PurchaseOrder order);
    void deleteOrder(Long id);
}
