package edu.friday.repository.custom;

import org.springframework.stereotype.Repository;

/**
 * 用户表 数据层
 */
@Repository
public interface SysUserCustomRepository {

    int batchInsertUserRole(Long[] userIds, Long[] roles);
}