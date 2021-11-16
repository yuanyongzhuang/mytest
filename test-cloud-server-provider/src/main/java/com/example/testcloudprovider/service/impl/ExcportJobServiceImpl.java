package com.example.testcloudprovider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.testcloudprovider.entity.ExportJob;
import com.example.testcloudprovider.enums.ExportJobProvessEnum;
import com.example.testcloudprovider.mapper.ExportJobMapper;
import com.example.testcloudprovider.service.IExportJobService;
import com.example.testcloudprovider.task.ExportJobAsync2Task;
import com.example.testcloudprovider.task.ExportJobAyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExcportJobServiceImpl extends ServiceImpl<ExportJobMapper, ExportJob> implements IExportJobService {

    @Autowired
    ExportJobAyncTask exportJobAyncTask;
    @Autowired
    ExportJobAsync2Task exportJobAync2Task;

    @Resource
    private ExportJobMapper exportJobMapper;

    @Override
    public ExportJob addExportJob(ExportJob exportJob) {
        exportJobMapper.insert(exportJob);
        return exportJob;
    }

    @Override
    public void checkExportAuthority(Integer userId) throws Exception {
        LambdaQueryWrapper<ExportJob> qw = new LambdaQueryWrapper<>();
        qw.ne(ExportJob::getProcess,ExportJobProvessEnum.OVER.getValue());
        qw.ne(ExportJob::getProcess, ExportJobProvessEnum.FAIL.getValue());
        qw.eq(ExportJob::getCreateUserGroupId,userId);
        List<ExportJob> list = this.list(qw);
        if(list != null && !list.isEmpty()){
            throw new Exception("数据正在导出中，请稍后再试");
        }
    }

    @Override
    public String export() {
//        exportJobAyncTask.asyncExportData();
        exportJobAync2Task.asyncExportData();
        return "成功";
    }
}
