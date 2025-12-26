package edu.friday.repository.custom.impl;

import edu.friday.repository.custom.SysUserCustomRepository;
import edu.friday.utils.SqlUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

public class SysUserCustomRepositoryImpl implements SysUserCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int batchInsertUserRole(Long[] userIds, Long[] roles) {
        int length = userIds.length > roles.length ? roles.length : userIds.length;
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into sys_user_role(user_id, role_id) values ");
        sql.append(SqlUtil.getBatchInsertSqlStr(length,2));
        Query query = entityManager.createNativeQuery(sql.toString());
        int paramIndex = 1;
        for (int i = 0; i < length; i++) {
            query.setParameter(paramIndex++, userIds[i]);
            query.setParameter(paramIndex++, roles[i]);
        }
        return query.executeUpdate();
    }
}

