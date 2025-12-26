package edu.friday.controller;

import edu.friday.common.base.BaseController;
import edu.friday.common.constant.UserConstants;
import edu.friday.common.result.RestResult;
import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysUser;
import edu.friday.model.vo.SysUserVO;
import edu.friday.service.SysRoleService;
import edu.friday.service.SysUserService;
import edu.friday.utils.StringUtils;
import edu.friday.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    SysUserService userService;

    @Autowired
    private SysRoleService roleService;
    /**
     * 显示用户表 所有数据
     */
    @GetMapping("/list")
    public TableDataInfo list(SysUserVO user, Pageable page) {
        int pageNum = page.getPageNumber() - 1;
        pageNum = pageNum <= 0 ? 0 : pageNum;
        page = PageRequest.of(pageNum, page.getPageSize());
        return userService.selectUserList(user, page);
    }
    /**
     * 新增用户
     */
    @PostMapping
    public RestResult add(@RequestBody SysUserVO user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return RestResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return RestResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return RestResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy("system");
          user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
//        user.setPassword(user.getPassword());
        boolean flag = userService.insertUser(user);
        return toAjax(flag ? 1 : 0);
    }
    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    public RestResult remove(@PathVariable Long[] userIds) {
        return toAjax(userService.deleteUserByIds(userIds));
    }
    /**
     * 修改用户
     */
    @PutMapping
    public RestResult edit(@Validated @RequestBody SysUserVO user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return RestResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return RestResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy("system");
        boolean flag = userService.updateUser(user);
        return toAjax(flag ? 1 : 0);
    }
    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/", "/{userId}"})
    public RestResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        RestResult ajax = RestResult.success();
        ajax.put("roles", roleService.selectRoleAll());
        if (StringUtils.isNotNull(userId)) {
            ajax.put(RestResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

}
