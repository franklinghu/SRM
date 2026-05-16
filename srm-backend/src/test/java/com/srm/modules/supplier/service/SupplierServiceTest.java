package com.srm.modules.supplier.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.srm.modules.supplier.entity.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SupplierServiceTest {

    @Autowired
    private SupplierService supplierService;

    private Supplier testSupplier;

    @BeforeEach
    void setUp() {
        testSupplier = new Supplier();
        testSupplier.setName("测试供应商");
        testSupplier.setCode("TEST001");
        testSupplier.setStatus(1);
    }

    @Test
    void testSaveSupplier() {
        boolean result = supplierService.save(testSupplier);
        assertNotNull(testSupplier.getId());
    }

    @Test
    void testGetById() {
        supplierService.save(testSupplier);
        Supplier found = supplierService.getById(testSupplier.getId());
        assertNotNull(found);
    }
}
