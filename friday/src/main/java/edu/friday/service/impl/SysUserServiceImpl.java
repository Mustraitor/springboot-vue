package edu.friday.service.impl;

import edu.friday.common.constant.UserConstants;
import edu.friday.common.result.TableDataInfo;
import edu.friday.model.SysUser;
import edu.friday.model.vo.SysUserVO;
import edu.friday.repository.SysUserRepository;
import edu.friday.service.SysUserService;
import edu.friday.utils.BeanUtils;
import edu.friday.utils.RedisCache;
import edu.friday.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SysUserServiceImpl implements SysUserService{
    @Autowired
    SysUserRepository sysUserRepository;

    @Override
    public TableDataInfo selectUserList(SysUserVO user, Pageable page) {
        SysUser sysuser = new SysUser();
        BeanUtils.copyPropertiesIgnoreEmpty(user, sysuser);
        sysuser.setDelFlag("0");
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("phonenumber", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<SysUser> example = Example.of(sysuser, exampleMatcher);
        Page<SysUser> rs = sysUserRepository.findAll(example, page);

        return TableDataInfo.success(rs.toList(), rs.getTotalElements());
    }
    @Override
    @Transactional
    public boolean insertUser(SysUserVO user) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(user, sysUser);
        sysUser.setDelFlag("0");
        // 新增用户信息
        sysUserRepository.save(sysUser);
//        BeanUtils.copyProperties(,user);
        user.setUserId(sysUser.getUserId());
        // 新增用户与角色管理
        insertUserRole(user);

        return null != sysUser.getUserId();
    }
    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    @Transactional
    public int insertUserRole(SysUserVO user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNull(roles) || roles.length == 0) {
            return 0;
        }
        Long[] userIds = new Long[roles.length];
        Arrays.fill(userIds, user.getUserId());
        return sysUserRepository.batchInsertUserRole(userIds, roles);
    }
    @Override
    public SysUser findOne(Example<SysUser> example) {
        List<SysUser> list = sysUserRepository.findAll(example, PageRequest.of(0, 1)).toList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    @Override
    public String checkUserNameUnique(String userName) {
        SysUser sysuser = new SysUser();
        sysuser.setUserName(userName);
        return count(sysuser);
    }


    @Override
    public String checkPhoneUnique(SysUserVO userInfo) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userInfo, user);
        return checkUnique(user);
    }


    @Override
    public String checkEmailUnique(SysUserVO userInfo) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userInfo, user);
        return checkUnique(user);
    }
    @Override
    public String count(SysUser sysuser) {
        Example<SysUser> example = Example.of(sysuser);
        long count = sysUserRepository.count(example);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
    @Override
    public String checkUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        Example<SysUser> example = Example.of(user);
        SysUser info = findOne(example);
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        return sysUserRepository.deleteUserByIds(userIds);
    }
    @Override
    @Transactional
    public boolean updateUser(SysUserVO user) {
        Long userId = user.getUserId();
        Optional<SysUser> op = sysUserRepository.findById(userId);
        if (!op.isPresent()) {
            return false;
        }
        // 删除用户与角色关联
        sysUserRepository.deleteUserRoleByUserId(userId);
        SysUser sysUser = op.get();
        BeanUtils.copyPropertiesIgnoreNull(user, sysUser);
        // 用户信息
        sysUserRepository.save(sysUser);
        // 新增用户与角色管理
        insertUserRole(user);

        return null != sysUser.getUserId();
    }
    @Override
    public SysUser selectUserById(Long userId) {
        SysUser sysuser = new SysUser();
        sysuser.setUserId(userId);
        sysuser.setDelFlag("0");
        return sysUserRepository.findOne(Example.of(sysuser)).get();
    }
    @Override
    public SysUser selectUserByUserName(String userName) {
        SysUser sysuser = new SysUser();
        sysuser.setUserName(userName);
        sysuser.setDelFlag("0");
        Example<SysUser> example = Example.of(sysuser);
        return findOne(example);
    }
}
