package com.srm.common.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResultTest {

    @Test
    void testSuccess() {
        Result<String> result = Result.success("测试成功");
        assertTrue(result.getSuccess());
        assertEquals("测试成功", result.getData());
    }

    @Test
    void testError() {
        Result<String> result = Result.error("测试错误");
        assertFalse(result.getSuccess());
        assertEquals("测试错误", result.getMessage());
    }

    @Test
    void testConstructor() {
        Result<String> result = new Result<>(true, "测试消息", "测试数据");
        assertTrue(result.getSuccess());
        assertEquals("测试消息", result.getMessage());
        assertEquals("测试数据", result.getData());
    }
}
