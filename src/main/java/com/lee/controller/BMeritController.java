package com.lee.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.common.DataGridView;
import com.lee.common.PageModel;
import com.lee.common.ResultObj;
import com.lee.entity.BMerit;
import com.lee.service.BMeritService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/merit")
public class BMeritController {

    @Autowired
    private BMeritService bMeritService;

    @GetMapping ("/loadAllMerit")
    public DataGridView loadAllMerit(PageModel pageModel){
        Page<BMerit> page = new Page<>(pageModel.getPage(), pageModel.getLimit());
        Page<BMerit> resultPage = this.bMeritService.page(page);
        DataGridView dataGridView = new DataGridView(resultPage.getTotal(), resultPage.getRecords());
        return dataGridView;
    }

    @RequestMapping("/addMerit")
    public ResultObj addMerit(BMerit bMerit){
        boolean save = bMeritService.save(bMerit);
        if(save) {
            return ResultObj.ADD_SUCCESS;
        }
        return ResultObj.ADD_ERROR;
    }

    @RequestMapping("/updateMerit")
    public ResultObj updateMerit(BMerit bMerit){
        boolean updateById = bMeritService.updateById(bMerit);
        if(updateById) {
            return ResultObj.UPDATE_SUCCESS;
        }
        return ResultObj.UPDATE_ERROR;
    }

    @RequestMapping("/deleteMerit")
    public ResultObj deleteMerit(Integer id){
        boolean remove = bMeritService.removeById(id);
        if(remove) {
            return ResultObj.DELETE_SUCCESS;
        }
        return ResultObj.DELETE_ERROR;
    }

    @RequestMapping("/batchDeleteMerit")
    public ResultObj batchDeleteMerit(Integer[] ids){
        List<Integer> idList = Arrays.asList(ids);
        boolean removeByIds = bMeritService.removeByIds(idList);
        if(removeByIds) {
            return ResultObj.DELETE_SUCCESS;
        }
        return ResultObj.DELETE_ERROR;
    }

}

