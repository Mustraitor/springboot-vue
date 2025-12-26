package edu.friday.service;

import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysRole;
import edu.friday.model.vo.SysRoleVO;
import edu.friday.model.vo.SysUserVO;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface SysRoleService {
    List<SysRole> selectRoleAll();
    List<Long> selectRoleListByUserId(Long userId);
    /**
     * 根据条件分页查询用户列表
     *
     * @param role 用户信息
     * @return 用户信息集合信息
     */
    TableDataInfo selectRoleList(SysRoleVO role, Pageable page);
    @Transactional
    int insertRole(SysRoleVO role);
    String checkRoleNameUnique(SysRoleVO role);

    String checkRoleKeyUnique(SysRoleVO role);
    @Transactional
    int updateRole(SysRoleVO role);
    void checkRoleAllowed(SysRoleVO role);
    SysRole selectRoleById(Long roleId);
    @Transactional
    int updateRoleStatus(SysRoleVO role);
    Set<String> selectRolePermissionByUserId(Long userId);
    @Transactional
    int deleteRoleByIds(Long[] roleIds);
    int countUserRoleByRoleId(Long roleId);
}
