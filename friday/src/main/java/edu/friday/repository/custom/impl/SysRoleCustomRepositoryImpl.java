package edu.friday.repository.custom.impl;

import edu.friday.repository.custom.SysRoleCustomRepository;
import edu.friday.utils.SqlUtil;
import org.springframework.data.jpa.repository.Modifying;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

public class SysRoleCustomRepositoryImpl implements SysRoleCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Modifying
    public int batchInsertRoleMenu(Long[] roles, Long[] menus) {
        int length = roles.length > menus.length ? menus.length : roles.length;
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into sys_role_menu (role_id, menu_id) values ");
        sql.append(SqlUtil.getBatchInsertSqlStr(length, 2));
        Query query = entityManager.createNativeQuery(sql.toString());
        int paramIndex = 1;
        for (int i = 0; i < length; i++) {
            query.setParameter(paramIndex++, roles[i]);
            query.setParameter(paramIndex++, menus[i]);
        }
        return query.executeUpdate();
    }
}

