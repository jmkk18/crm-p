package com.jm.crm.workbench.service;

import com.jm.crm.workbench.pojo.TranHistory;

import java.util.List;

public interface TranHistoryService {
    List<TranHistory> queryTranHistoryForDetailByTranId(String tranId);

}
