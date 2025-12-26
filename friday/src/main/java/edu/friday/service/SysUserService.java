package edu.friday.service;

import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysUser;
import edu.friday.model.vo.SysUserVO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SysUserService {
    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    TableDataInfo selectUserList(SysUserVO user, Pageable page);
    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean insertUser(SysUserVO user);
    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    String checkUserNameUnique(String userName);
    /**
     * 校验用户手机是否唯一
     *
     * @param userInfo 用户信息
     * @return
     */
    String checkPhoneUnique(SysUserVO userInfo);

    /**
     * 校验email是否唯一
     *
     * @param userInfo 用户信息
     * @return
     */
    String checkEmailUnique(SysUserVO userInfo);
    /**
     * 统计用户的通用方法
     *
     * @param sysuser
     * @return 结果
     */
    String count(SysUser sysuser);
    /**
     * 根据字段校验用户是否唯一
     *
     * @param user 用户信息
     * @return
     */
    String checkUnique(SysUser user);
//    /**
//     * 通过用户ID查询用户
//     *
//     * @param userId 用户ID
//     * @return 用户对象信息
//     */
//     SysUser selectUserById(Long userId);
    SysUser findOne(Example<SysUser> example);
    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(Long[] userIds);
    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean updateUser(SysUserVO user);
    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long userId);
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

}
