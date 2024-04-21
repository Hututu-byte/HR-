package com.lee.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lee.common.DataGridView;
import com.lee.common.PageModel;
import com.lee.common.ResultObj;
import com.lee.common.TreeNode;
import com.lee.entity.SysPermission;
import com.lee.entity.SysRole;
import com.lee.entity.SysRolePermission;
import com.lee.service.SysPermissionService;
import com.lee.service.SysRolePermissionService;
import com.lee.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysRolePermissionService rolePermissionService;

    @RequestMapping("/loadAllRole")
    public DataGridView loadAllRole(PageModel pageModel){
        Page<SysRole> page = new Page<>(pageModel.getPage(), pageModel.getLimit());
        Page<SysRole> resultPage = this.roleService.page(page);
        return new DataGridView(resultPage.getTotal(), resultPage.getRecords());
    }

    @RequestMapping("/addRole")
    public ResultObj addRole(SysRole role){
        boolean save = this.roleService.save(role);
        if(save) return ResultObj.ADD_SUCCESS;
        return ResultObj.ADD_ERROR;
    }

    @RequestMapping("/updateRole")
    public ResultObj updateRole(SysRole role){
        boolean updateById = this.roleService.updateById(role);
        if(updateById) return ResultObj.UPDATE_SUCCESS;
        return ResultObj.UPDATE_ERROR;
    }

    @RequestMapping("/deleteRole")
    public ResultObj deleteRole(Integer id){
        boolean removeById = this.roleService.removeById(id);
        if(removeById) return ResultObj.DELETE_SUCCESS;
        return ResultObj.DELETE_ERROR;
    }

    @RequestMapping("/initPermissionByRoleId")
    public DataGridView initPermissionByRoleId(Integer id){
        //查询所有菜单
        List<SysPermission> permissionList = this.permissionService.list();
        List<TreeNode> treeNodeList = new ArrayList<>();
        QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rid",id);
        List<SysRolePermission> rolePermissionList = this.rolePermissionService.list(queryWrapper);
        for (SysPermission sysPermission : permissionList) {
            TreeNode treeNode = new TreeNode(sysPermission.getId(), sysPermission.getPid(), sysPermission.getTitle(), true);
            //获取当前角色所拥有的权限
            for (SysRolePermission rolePermission : rolePermissionList) {
                if (sysPermission.getId().equals(rolePermission.getPid())) treeNode.setCheckArr("1");
            }
            treeNodeList.add(treeNode);
        }
        return new DataGridView(treeNodeList);
    }

    @RequestMapping("/updatePermission")
    public ResultObj updatePermission(Integer[] ids){
        QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rid", ids[0]);
        this.rolePermissionService.remove(queryWrapper);
        List<SysRolePermission> list = new ArrayList<>();
        for (int i = 1; i < ids.length; i++) {
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setRid(ids[0]);
            rolePermission.setPid(ids[i]);
            list.add(rolePermission);
        }
        boolean saveBatch = this.rolePermissionService.saveBatch(list);
        if(saveBatch) return ResultObj.DISPATCH_SUCCESS;
        return ResultObj.DISPATCH_ERROR;
    }
}

