package com.jm.crm.workbench.service;

import com.jm.crm.workbench.pojo.TranRemark;

import java.util.List;

public interface TranRemarkService {
    List<TranRemark> queryTranRemarkForDetailByTranId(String tranId);

}
