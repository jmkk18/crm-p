package com.jm.crm.workbench.service;

import com.jm.crm.workbench.pojo.FunnelVO;
import com.jm.crm.workbench.pojo.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    List<Tran> queryTransactionByConditionForPage(Map<String, Object> map);

    int queryCountOfTransactionByCondition(Map<String, Object> map);

    void saveCreateTran(Map<String,Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();

}
