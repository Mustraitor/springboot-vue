package edu.friday.controller;

import edu.friday.common.base.BaseController;
import edu.friday.common.constant.UserConstants;
import edu.friday.common.result.RestResult;
import edu.friday.common.security.LoginUser;
import edu.friday.common.security.service.TokenService;
import edu.friday.model.SysMenu;
import edu.friday.model.vo.SysMenuVO;
import edu.friday.service.SysMenuService;
import edu.friday.utils.BeanUtils;
import edu.friday.utils.http.ServletUtils;
import edu.friday.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
    @Autowired
    private SysMenuService menuService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public RestResult list(SysMenuVO menu) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return RestResult.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/{menuId}")
    public RestResult getInfo(@PathVariable Long menuId) {
        return RestResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public RestResult treeselect(SysMenuVO menu) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        List<SysMenuVO> menuVOS = BeanUtils.copyProperties(menus, SysMenuVO.class);
        return RestResult.success(menuService.buildMenuTreeSelect(menuVOS));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public RestResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        SecurityUtils.getAuthentication();
        List<SysMenu> menus = menuService.selectMenuList(userId);
        RestResult ajax = RestResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        List<SysMenuVO> menuVOS = BeanUtils.copyProperties(menus, SysMenuVO.class);
        ajax.put("menus", menuService.buildMenuTreeSelect(menuVOS));
        return ajax;
    }

    /**
     * 新增菜单
     */
    @PostMapping
    public RestResult add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return RestResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        // TODO 后续可以从token获取用户
        menu.setCreateBy("system");
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @PutMapping
    public RestResult edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return RestResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        // TODO 后续可以从token获取用户
        menu.setUpdateBy("system");
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public RestResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return RestResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return RestResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}