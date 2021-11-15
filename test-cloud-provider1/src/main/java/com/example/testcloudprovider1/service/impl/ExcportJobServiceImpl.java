package com.example.testcloudprovider1.service.impl;

import com.example.testcloudprovider1.service.IExportJobService;
import com.example.testcloudprovider1.task.ExportJobAyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcportJobServiceImpl implements IExportJobService {
    @Autowired
    ExportJobAyncTask exportJobAyncTask;

    @Override
    public String export() {
        this.exportJobAyncTask.asyncExportData();
        return "成功";
    }
}
