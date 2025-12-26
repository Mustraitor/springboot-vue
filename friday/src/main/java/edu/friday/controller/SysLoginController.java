package edu.friday.controller;

import edu.friday.common.constant.Constants;
import edu.friday.common.result.RestResult;
import edu.friday.common.security.LoginUser;
import edu.friday.common.security.service.SysLoginService;
import edu.friday.common.security.service.SysPermissionService;
import edu.friday.common.security.service.TokenService;
//import edu.friday.model.vo.SysMenuVO;
import edu.friday.model.SysRole;
import edu.friday.model.vo.SysMenuVO;
import edu.friday.model.vo.SysRoleVO;
import edu.friday.model.vo.SysUserVO;
import edu.friday.service.SysMenuService;
import edu.friday.service.SysRoleService;
import edu.friday.utils.BeanUtils;
import edu.friday.utils.http.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 登录验证
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysMenuService menuService;
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SysRoleService roleService;

    /**
     * 登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    @PostMapping({"/login", "/"})
    public RestResult login(String username, String password, String uuid) {
        System.out.println("/login: username["+username+"]"+"password: ["+password+"]"+"uuid: ["+uuid+"]");
        RestResult ajax = RestResult.success();
        // 生成令牌
        String token = loginService.login(username, password, "123", uuid);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public RestResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUserVO user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        RestResult ajax = RestResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }


    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public RestResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUserVO user = loginUser.getUser();
        List<SysMenuVO> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return RestResult.success(menuService.buildMenus(menus));
    }
}
