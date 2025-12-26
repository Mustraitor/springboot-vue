package edu.friday.service.impl;

import edu.friday.common.constant.UserConstants;
import edu.friday.common.exception.CustomException;
import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysRole;
import edu.friday.model.SysUser;
import edu.friday.model.vo.SysRoleVO;
import edu.friday.model.vo.SysUserVO;
import edu.friday.repository.SysRoleRepository;
import edu.friday.service.SysRoleService;
import edu.friday.utils.BeanUtils;
import edu.friday.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    SysRoleRepository sysRoleRepository;
    @Override
    public TableDataInfo selectRoleList(SysRoleVO role, Pageable page) {
        SysRole sysrole= new SysRole();
        BeanUtils.copyPropertiesIgnoreEmpty(role, sysrole);
        sysrole.setDelFlag("0");
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("phonenumber", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<SysRole> example = Example.of(sysrole, exampleMatcher);
        Page<SysRole> rs = sysRoleRepository.findAll(example, page);

        return TableDataInfo.success(rs.toList(), rs.getTotalElements());
    }
    @Override
    @Transactional
    public int insertRole(SysRoleVO role) {
        // 新增角色信息
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(role, sysRole);
        sysRole.setDelFlag("0");
        sysRoleRepository.save(sysRole);
        role.setRoleId(sysRole.getRoleId());
        return insertRoleMenu(role);
    }
    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRoleVO role) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(role, sysRole);
        return checkUnique(sysRole);
    }
    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRoleVO role) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(role, sysRole);
        return checkUnique(sysRole);
    }
    public String checkUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        Example<SysRole> example = Example.of(role);
        SysRole info = findOne(example);
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
    public SysRole findOne(Example<SysRole> example) {
        List<SysRole> list = sysRoleRepository.findAll(example, PageRequest.of(0, 1)).toList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRoleVO role) {
        // 新增用户与角色管理
        Long[] menus = role.getMenuIds();
        if (StringUtils.isNull(menus) || menus.length == 0) {
            return 0;
        }
        Long[] roles = new Long[menus.length];
        Arrays.fill(roles, role.getRoleId());
        return sysRoleRepository.batchInsertRoleMenu(roles, menus);
    }
    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRoleVO role) {
        Optional<SysRole> op = sysRoleRepository.findById(role.getRoleId());
        if (!op.isPresent()) {
            return 0;
        }
        SysRole sysRole = op.get();
        BeanUtils.copyPropertiesIgnoreNull(role, sysRole);
        // 修改角色信息
        sysRoleRepository.save(sysRole);
        // 删除角色与菜单关联
        sysRoleRepository.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
//         insertRoleMenu(role);
//         return 1;
    }
    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return sysRoleRepository.findAll();
    }
    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return sysRoleRepository.selectRoleIdsByUserId(userId);
    }
    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        Optional<SysRole> op = sysRoleRepository.findById(roleId);
        return op.isPresent() ? op.get() : null;
    }
    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRoleVO role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new CustomException("不允许操作超级管理员角色");
        }
    }
    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRoleStatus(SysRoleVO role) {
        Optional<SysRole> op = sysRoleRepository.findById(role.getRoleId());
        if (!op.isPresent()) {
            return 0;
        }
        SysRole sysRole = op.get();
        sysRole.setStatus(role.getStatus());
        sysRoleRepository.save(sysRole);
        return 1;
    }
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = sysRoleRepository.selectRoleByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }
    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRoleVO(roleId));
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
            sysRoleRepository.deleteRoleMenuByRoleId(roleId);
        }
        return sysRoleRepository.deleteRoleByIds(roleIds);
    }
    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId) {
        return sysRoleRepository.countUserRoleByRoleId(roleId);
    }
}
