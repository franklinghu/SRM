package com.srm.modules.purchase.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.purchase.entity.PurchaseRequest;
import com.srm.modules.purchase.entity.PurchaseOrder;
import com.srm.modules.purchase.mapper.PurchaseRequestMapper;
import com.srm.modules.purchase.mapper.PurchaseOrderMapper;
import com.srm.modules.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRequestMapper purchaseRequestMapper;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public IPage<PurchaseRequest> getRequestPage(Integer pageNum, Integer pageSize, String title, String status) {
        Page<PurchaseRequest> page = new Page<>(pageNum, pageSize);
        return purchaseRequestMapper.selectPageList(page, title, status, 1L);
    }

    @Override
    public PurchaseRequest getRequestById(Long id) {
        return purchaseRequestMapper.selectById(id);
    }

    @Override
    @Transactional
    public PurchaseRequest createRequest(PurchaseRequest request) {
        request.setTenantId(1L);
        purchaseRequestMapper.insert(request);
        return request;
    }

    @Override
    @Transactional
    public PurchaseRequest updateRequest(PurchaseRequest request) {
        purchaseRequestMapper.updateById(request);
        return request;
    }

    @Override
    @Transactional
    public void deleteRequest(Long id) {
        purchaseRequestMapper.deleteById(id);
    }

    @Override
    public IPage<PurchaseOrder> getOrderPage(Integer pageNum, Integer pageSize, String orderNo, String status) {
        Page<PurchaseOrder> page = new Page<>(pageNum, pageSize);
        return purchaseOrderMapper.selectPageList(page, orderNo, status, 1L);
    }

    @Override
    public PurchaseOrder getOrderById(Long id) {
        return purchaseOrderMapper.selectById(id);
    }

    @Override
    @Transactional
    public PurchaseOrder createOrder(PurchaseOrder order) {
        order.setTenantId(1L);
        purchaseOrderMapper.insert(order);
        return order;
    }

    @Override
    @Transactional
    public PurchaseOrder updateOrder(PurchaseOrder order) {
        purchaseOrderMapper.updateById(order);
        return order;
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        purchaseOrderMapper.deleteById(id);
    }
}
