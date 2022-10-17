package com.jm.crm.workbench.service;

import com.jm.crm.workbench.pojo.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> queryContactsByFuzzyName(String contactsName);
}
