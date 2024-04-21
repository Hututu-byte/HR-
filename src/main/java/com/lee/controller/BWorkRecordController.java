package com.lee.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.common.DataGridView;
import com.lee.common.PageModel;
import com.lee.common.ResultObj;
import com.lee.common.WorkStatusEnum;
import com.lee.entity.BWorkRecord;
import com.lee.entity.SysUser;
import com.lee.mapper.BWorkRecordMapper;
import com.lee.service.BWorkRecordService;
import com.lee.util.WebUtils;
import com.lee.vo.BWorkRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.util.ArrayList;
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
@RequestMapping("/workRecord")
public class BWorkRecordController {

    @Autowired
    private BWorkRecordService workRecordService;

    @Autowired
    private BWorkRecordMapper bWorkRecordMapper;

    @Value("${work-up-time}")
    private Integer upTime;
    @Value("${work-down-time}")
    private Integer downTime;
    @Value("${work-over-time}")
    private Integer overTime;

    @RequestMapping("/loadAllWorkRecord")
    public DataGridView loadAllWorkRecord(PageModel pageModel){
        Page<BWorkRecord> page = new Page<>(pageModel.getPage(),pageModel.getLimit());
        Page<BWorkRecord> resultpage = this.workRecordService.page(page);
        List<BWorkRecordVO> list = new ArrayList<>();
        for(BWorkRecord record : resultpage.getRecords()){
            BWorkRecordVO vo = new BWorkRecordVO();
            BeanUtils.copyProperties(record,vo);
            vo.setName(bWorkRecordMapper.getUserNameById(record.getId()));
            list.add(vo);
        }
        return new DataGridView(resultpage.getTotal(),list);
    }

    @RequestMapping("/addWorkRecord")
    public ResultObj addWorkRecord(BWorkRecord workRecord){
        SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
        workRecord.setUid(user.getId());
        //判断上班或下班
        QueryWrapper<BWorkRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", workRecord.getUid())
                .eq("work_date", workRecord.getWorkDate());
        BWorkRecord one = this.workRecordService.getOne(queryWrapper);
        if(one == null){
            //上班
            //判断是否迟到
            Integer workRecordUpTime = Integer.valueOf(workRecord.getUpTime().substring(0, 2));
            if(workRecordUpTime > upTime || (workRecordUpTime == upTime && (Integer.valueOf(workRecord.getUpTime().substring(3, 5))) > 0)){
                workRecord.setStatus(WorkStatusEnum.LATE.getCode());
            } else {
                workRecord.setStatus(WorkStatusEnum.NORMAL.getCode());
            }
            boolean save = this.workRecordService.save(workRecord);
            if(save) return ResultObj.ADD_WORK_SUCCESS;
            return ResultObj.ADD_WORK_ERROR;
        } else {
            //判断当天是否已完成打卡
            if (one.getDownTime()!=null) {
                return ResultObj.ADD_WORK_ERROR_1;
            }
            //下班
            one.setDownTime(workRecord.getUpTime());
            //判断是否早退或加班
            Integer workRecordDownTime = Integer.valueOf(workRecord.getUpTime().substring(0, 2));
            if(workRecordDownTime < downTime){
                //早退
                one.setStatus(WorkStatusEnum.LEAVE.getCode());
            } else {
                //加班
                if(workRecordDownTime > overTime || (workRecordDownTime == overTime && (Integer.valueOf(workRecord.getUpTime().substring(3, 5))) > 0)){
                    one.setStatus(WorkStatusEnum.OVERTIME.getCode());
                }
            }
            boolean updateById = this.workRecordService.updateById(one);
            if(updateById) return ResultObj.ADD_WORK_SUCCESS;
            return ResultObj.ADD_WORK_ERROR;
        }
    }
}

