package com.lee.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.common.Constast;
import com.lee.common.DataGridView;
import com.lee.common.PageModel;
import com.lee.common.ResultObj;
import com.lee.entity.SysRole;
import com.lee.entity.SysRoleUser;
import com.lee.entity.SysUser;
import com.lee.mapper.SysDeptMapper;
import com.lee.service.SysRoleService;
import com.lee.service.SysRoleUserService;
import com.lee.service.SysUserService;
import com.lee.vo.SysUserVO;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2024-04-14
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRoleUserService roleUserService;



    @RequestMapping("/loadAllUser")
    public List<SysUser> loadAllUser(){
        return userService.list();
    }


    @RequestMapping("/list")
    public DataGridView list(PageModel pageModel){
        Page<SysUser> page = new Page<>(pageModel.getPage(), pageModel.getLimit());
        Page<SysUser> resultPage = this.userService.page(page);
        List<SysUserVO> list = new ArrayList<>();
        for (SysUser record : resultPage.getRecords()) {
            SysUserVO vo = new SysUserVO();
            BeanUtils.copyProperties(record, vo);
            vo.setDeptName(this.deptMapper.getDeptNameById(record.getDeptid()));
            list.add(vo);
        }
        return new DataGridView(resultPage.getTotal(), list);
    }


    @RequestMapping("/addUser")
    public ResultObj addUser(SysUser user){
        boolean save = this.userService.save(user);
        if(save) return ResultObj.ADD_SUCCESS;
        return ResultObj.ADD_ERROR;
    }

    @RequestMapping("/updateUser")
    public ResultObj updateUser(SysUser user){
        boolean updateById = this.userService.updateById(user);
        if(updateById) return ResultObj.UPDATE_SUCCESS;
        return ResultObj.UPDATE_ERROR;
    }

    @RequestMapping("/deleteUser")
    public ResultObj deleteUser(Integer id){
        boolean removeById = this.userService.removeById(id);
        if(removeById) return ResultObj.DELETE_SUCCESS;
        return ResultObj.DELETE_ERROR;
    }


    @RequestMapping("/initRoleByUserId")
    public DataGridView initRoleByUserId(Integer id){
        //查询全部可用角色
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Map<String, Object>> maps = this.roleService.listMaps(queryWrapper);
        //查询当前用户所拥有的角色
        QueryWrapper<SysRoleUser> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("uid", id);
        List<SysRoleUser> roleUserList = this.roleUserService.list(queryWrapper1);
        for (Map<String, Object> map : maps) {
            Boolean LAY_CHECKED = false;
            Integer roleid = (Integer) map.get("id");
            for (SysRoleUser roleUser : roleUserList) {
                if(roleUser.getRid().equals(roleid)) LAY_CHECKED = true;
            }
            map.put("LAY_CHECKED",LAY_CHECKED);
        }
        return new DataGridView(Long.valueOf(maps.size()), maps);
    }

    @RequestMapping("/updateRole")
    public ResultObj updateRole(Integer[] ids){
        QueryWrapper<SysRoleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", ids[0]);
        this.roleUserService.remove(queryWrapper);
        List<SysRoleUser> list = new ArrayList<>();
        for (int i = 1; i < ids.length; i++) {
            SysRoleUser roleUser = new SysRoleUser();
            roleUser.setUid(ids[0]);
            roleUser.setRid(ids[i]);
            list.add(roleUser);
        }
        boolean saveBatch = this.roleUserService.saveBatch(list);
        if(saveBatch) return ResultObj.DISPATCH_SUCCESS;
        return ResultObj.DISPATCH_ERROR;
    }




}

