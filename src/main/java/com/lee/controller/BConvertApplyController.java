package com.lee.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.common.DataGridView;
import com.lee.common.PageModel;
import com.lee.common.ResultObj;
import com.lee.entity.BConvertApply;
import com.lee.entity.SysUser;
import com.lee.mapper.SysUserMapper;
import com.lee.service.BConvertApplyService;
import com.lee.util.WebUtils;
import com.lee.vo.BConvertApplyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
@RequestMapping("/convertApply")
public class BConvertApplyController {
    @Autowired
    private BConvertApplyService convertApplyService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @RequestMapping("/loadAllconvertApply")
    public DataGridView loadAllconvertApply(PageModel pageModel){
        Page<BConvertApply> page = new Page<>(pageModel.getPage(),pageModel.getLimit());
        Page<BConvertApply> resultPage = this.convertApplyService.page(page);
        List<BConvertApplyVO> list = new ArrayList<>();
        for(BConvertApply record : resultPage.getRecords()){
            BConvertApplyVO vo = new BConvertApplyVO();
            BeanUtils.copyProperties(record,vo);
            vo.setApplyName(sysUserMapper.getUserNameById(record.getApplyUserId()));
            vo.setApprovalName(sysUserMapper.getUserNameById(record.getApprovalUserId()));
            list.add(vo);
        }
        return new DataGridView(resultPage.getTotal(),list);
    }

    @RequestMapping("/addConvertApply")
    public ResultObj addConvertApply(BConvertApply bConvertApply){
        bConvertApply.setStatus(0);
        SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
        bConvertApply.setApplyUserId(user.getId());
        boolean save = this.convertApplyService.save(bConvertApply);
        if(save) return ResultObj.ADD_SUCCESS;
        return ResultObj.ADD_ERROR;
    }

    @RequestMapping("/updateConvertApply")
    public ResultObj updateConvertApply(BConvertApply convertApply){
        convertApply.setApprovalDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
        //不能自己审核自己
        if(user.getId().equals(convertApply.getApplyUserId())) return ResultObj.APPROVAL_ALREADY_ERROR;
        convertApply.setApprovalUserId(user.getId());
        boolean updateById = this.convertApplyService.updateById(convertApply);
        if(updateById) return ResultObj.APPROVAL_SUCCESS;
        return ResultObj.APPROVAL_ERROR;
    }

}

