package com.example.testcloudprovider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.testcloudprovider.entity.ExportJob;

public interface IExportJobService extends IService<ExportJob> {
    String export();

    void checkExportAuthority(Integer userId) throws Exception;

    ExportJob addExportJob(ExportJob exportJob);

}
