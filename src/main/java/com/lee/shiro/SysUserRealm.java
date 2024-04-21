package com.lee.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lee.entity.SysUser;
import com.lee.mapper.SysUserMapper;
import com.lee.entity.SysUser;
import com.lee.mapper.SysUserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class SysUserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //验证用户名
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginname", authenticationToken.getPrincipal().toString());
        SysUser sysUser = this.userMapper.selectOne(queryWrapper);
        if(sysUser != null){
            //验证密码
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(sysUser);
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser, sysUser.getPassword(), this.getName());
            return info;
        }
        return null;
    }
}
