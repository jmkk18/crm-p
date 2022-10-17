package com.jm.crm.workbench.service.impl;


import com.jm.crm.workbench.mapper.ContactsMapper;
import com.jm.crm.workbench.pojo.Contacts;
import com.jm.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;

    @Override
    public List<Contacts> queryContactsByFuzzyName(String contactsName) {
        return contactsMapper.selectContactsByFuzzyName(contactsName);
    }
}
