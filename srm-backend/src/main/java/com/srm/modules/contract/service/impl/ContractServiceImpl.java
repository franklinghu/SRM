package com.srm.modules.contract.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.srm.modules.contract.entity.Contract;
import com.srm.modules.contract.mapper.ContractMapper;
import com.srm.modules.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractMapper contractMapper;

    @Override
    public IPage<Contract> getContractPage(Integer pageNum, Integer pageSize, String contractName, String status) {
        Page<Contract> page = new Page<>(pageNum, pageSize);
        return contractMapper.selectPageList(page, contractName, status, 1L);
    }

    @Override
    public Contract getContractById(Long id) {
        return contractMapper.selectById(id);
    }

    @Override
    @Transactional
    public Contract createContract(Contract contract) {
        contract.setTenantId(1L);
        contractMapper.insert(contract);
        return contract;
    }

    @Override
    @Transactional
    public Contract updateContract(Contract contract) {
        contractMapper.updateById(contract);
        return contract;
    }

    @Override
    @Transactional
    public void deleteContract(Long id) {
        contractMapper.deleteById(id);
    }
}
