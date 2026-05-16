package com.srm.modules.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.srm.modules.contract.entity.Contract;

public interface ContractService {
    IPage<Contract> getContractPage(Integer pageNum, Integer pageSize, String contractName, String status);
    Contract getContractById(Long id);
    Contract createContract(Contract contract);
    Contract updateContract(Contract contract);
    void deleteContract(Long id);
}
