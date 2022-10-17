package com.jm.crm.workbench.service;

import com.jm.crm.workbench.pojo.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);

    int deleteClueActivityRelationByActivityIdClueId(Map<String,Object> map);

}
