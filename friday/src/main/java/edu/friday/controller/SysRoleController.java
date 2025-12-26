package edu.friday.controller;

import edu.friday.common.base.BaseController;
import edu.friday.common.constant.UserConstants;
import edu.friday.common.result.RestResult;
import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysRole;
import edu.friday.model.vo.SysRoleVO;
import edu.friday.model.vo.SysUserVO;
import edu.friday.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController{
    @Autowired
    SysRoleService roleService;
    /**
     * 显示用户表 所有数据
     */
    @GetMapping("/list")
    public TableDataInfo list(SysRoleVO role, Pageable page) {
        int pageNum = page.getPageNumber() - 1;
        pageNum = pageNum <= 0 ? 0 : pageNum;
        page = PageRequest.of(pageNum, page.getPageSize());
        return roleService.selectRoleList(role, page);
    }
    /**
     * 新增角色
     */
    @PostMapping
    public RestResult add(@Validated @RequestBody SysRoleVO role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return RestResult.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return RestResult.error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy("system");
        return toAjax(roleService.insertRole(role));
    }
    /**
     * 修改保存角色
     */
    @PutMapping
    public RestResult edit(@Validated @RequestBody SysRoleVO role) {
//        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return RestResult.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return RestResult.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy("system");
        return toAjax(roleService.updateRole(role));
    }
    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/{roleId}")
    public RestResult getInfo(@PathVariable Long roleId) {
        return RestResult.success(roleService.selectRoleById(roleId));
    }
    /**
     * 获取角色选择框列表
     */
    @GetMapping({"/optionselect", "/"})
    public RestResult optionselect() {
        return RestResult.success(roleService.selectRoleAll());
    }


    /**
     * 状态修改
     */
    @PutMapping("/changeStatus")
    public RestResult changeStatus(@RequestBody SysRoleVO role) {
//        roleService.checkRoleAllowed(role);
        role.setUpdateBy("system");
        return toAjax(roleService.updateRoleStatus(role));
    }
    /**
     * 删除角色
     */
    @DeleteMapping("/{roleIds}")
    public RestResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

}
