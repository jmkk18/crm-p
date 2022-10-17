package com.jm.crm.workbench.service.impl;

import com.jm.crm.workbench.mapper.ClueActivityRelationMapper;
import com.jm.crm.workbench.pojo.ClueActivityRelation;
import com.jm.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list) {
        return clueActivityRelationMapper.insertClueActivityRelationByList(list);
    }

    @Override
    public int deleteClueActivityRelationByActivityIdClueId(Map<String, Object> map) {
        return clueActivityRelationMapper.deleteClueActivityRelationByActivityIdClueId(map);
    }
}
