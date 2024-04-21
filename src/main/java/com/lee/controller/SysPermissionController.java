package com.lee.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lee.common.DataGridView;
import com.lee.common.TreeNode;
import com.lee.entity.SysPermission;
import com.lee.entity.SysUser;
import com.lee.mapper.SysPermissionMapper;
import com.lee.service.SysPermissionService;
import com.lee.util.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionMapper permissionMapper;

    @RequestMapping("/loadIndexLeftMenuJson")
    public DataGridView loadIndexLeftMenuJson(){
        //结合当前登录用户的角色来加载对应的权限
        SysUser user = (SysUser) WebUtils.getSession().getAttribute("user");
        List<SysPermission> list = this.permissionMapper.getByUserId(user.getId());
        //将list转成TreeNode
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (SysPermission sysPermission : list) {
            if(sysPermission.getPid().equals(0)){
                TreeNode parent = new TreeNode();
                BeanUtils.copyProperties(sysPermission, parent);
                treeNodeList.add(parent);
                List<TreeNode> children = new ArrayList<>();
                for (SysPermission permission : list) {
                    if(parent.getId().equals(permission.getPid())){
                        TreeNode child = new TreeNode();
                        BeanUtils.copyProperties(permission, child);
                        children.add(child);
                    }
                }
                parent.setChildren(children);
            }
        }
        return new DataGridView(treeNodeList);
    }

}

