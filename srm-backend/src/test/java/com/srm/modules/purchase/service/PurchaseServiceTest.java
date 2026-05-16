package com.srm.modules.purchase.service;

import com.srm.modules.purchase.entity.PurchaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PurchaseServiceTest {

    @Autowired
    private PurchaseService purchaseService;

    private PurchaseRequest testRequest;

    @BeforeEach
    void setUp() {
        testRequest = new PurchaseRequest();
        testRequest.setTitle("测试采购申请");
        testRequest.setRequestNo("PR20260516001");
        testRequest.setStatus(1);
        testRequest.setTotalAmount(new BigDecimal("10000.00"));
    }

    @Test
    void testSavePurchaseRequest() {
        boolean result = purchaseService.save(testRequest);
        assertNotNull(testRequest.getId());
    }
}
