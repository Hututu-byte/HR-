package com.lee.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.common.DataGridView;
import com.lee.common.PageModel;
import com.lee.common.ResultObj;
import com.lee.common.SalaryPageModel;
import com.lee.entity.BSalaryRecord;
import com.lee.mapper.BSalaryRecordMapper;
import com.lee.service.BSalaryRecordService;
import com.lee.vo.BSalaryRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2024-04-14
 */
@RestController
@RequestMapping("/salaryRecord")
public class BSalaryRecordController {

    @Autowired
    private BSalaryRecordService bSalaryRecordService;

    @Autowired
    private BSalaryRecordMapper bSalaryRecordMapper;

    @RequestMapping("/loadAllSalaryRecord")
    public DataGridView loadAllSalaryRecord(SalaryPageModel pageModel){
        Page<BSalaryRecord> page = new Page<>(pageModel.getPage(),pageModel.getLimit());
        QueryWrapper<BSalaryRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(pageModel.getSalaryMonth()), "salary_month", pageModel.getSalaryMonth());
        Page<BSalaryRecord> resultPage = bSalaryRecordService.page(page,queryWrapper);
        List<BSalaryRecordVO> list = new ArrayList<>();
        for( BSalaryRecord record : resultPage.getRecords()){
            BSalaryRecordVO vo = new BSalaryRecordVO();
            BeanUtils.copyProperties(record,vo);
            vo.setName(bSalaryRecordMapper.getUserNameById(record.getId()));
            vo.setDeptname(bSalaryRecordMapper.getDeptNameById(record.getId()));
            list.add(vo);
        }
        DataGridView dataGridView = new DataGridView(resultPage.getTotal(),list);
        return dataGridView;
    }


    @RequestMapping("/addSalaryRecord")
    public ResultObj addSalaryRecord(BSalaryRecord bSalaryRecord){
        boolean save  = bSalaryRecordService.save(bSalaryRecord);
        if(save) return ResultObj.ADD_SUCCESS;
        return ResultObj.ADD_ERROR;
    }


    @RequestMapping("/updateSalaryRecord")
    public ResultObj updateSalaryRecord(BSalaryRecord bSalaryRecord){
        boolean updateById  = bSalaryRecordService.updateById(bSalaryRecord);
        if(updateById) return ResultObj.UPDATE_SUCCESS;
        return ResultObj.UPDATE_ERROR;
    }

    @RequestMapping("/deleteSalaryRecord")
    public ResultObj deleteSalaryRecord(Integer id){
        boolean removeById  = bSalaryRecordService.removeById(id);
        if(removeById) return ResultObj.DELETE_SUCCESS;
        return ResultObj.DELETE_ERROR;
    }

    @RequestMapping("/batchDeleteSalaryRecord")
    public ResultObj batchDeleteSalaryRecord(Integer[] ids){
        List<Integer> idList = Arrays.asList(ids); //数组转化为List 要使用asList
        boolean removeByIds  = bSalaryRecordService.removeByIds(idList);
        if(removeByIds) return ResultObj.DELETE_SUCCESS;
        return ResultObj.DELETE_ERROR;
    }

}

