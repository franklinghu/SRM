package com.srm.modules.contract.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.common.result.Result;
import com.srm.modules.contract.entity.Contract;
import com.srm.modules.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping
    public Result<IPage<Contract>> getContractPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String contractName,
            @RequestParam(required = false) String status) {
        IPage<Contract> page = contractService.getContractPage(pageNum, pageSize, contractName, status);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<Contract> getContractById(@PathVariable Long id) {
        Contract contract = contractService.getContractById(id);
        return Result.success(contract);
    }

    @PostMapping
    public Result<Contract> createContract(@RequestBody Contract contract) {
        Contract result = contractService.createContract(contract);
        return Result.success(result);
    }

    @PutMapping("/{id}")
    public Result<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        contract.setId(id);
        Contract result = contractService.updateContract(contract);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return Result.success();
    }
}
