package edu.friday.repository.custom;

import org.springframework.stereotype.Repository;

/**
 * 角色表 数据层
 */
@Repository
public interface SysRoleCustomRepository {

    int batchInsertRoleMenu(Long[] roles, Long[] menus);
}
