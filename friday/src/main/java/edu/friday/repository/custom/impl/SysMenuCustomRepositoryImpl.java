package edu.friday.repository.custom.impl;

import edu.friday.model.SysMenu;
import edu.friday.repository.custom.SysMenuCustomRepository;
import edu.friday.utils.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

public class SysMenuCustomRepositoryImpl implements SysMenuCustomRepository {

    String SELECT = " select distinct m.* from sys_menu m ";
    String JOIN_ROLE_MENU = " left join sys_role_menu rm on m.menu_id = rm.menu_id ";
    String JOIN_USER_ROLE = " left join sys_user_role ur on rm.role_id = ur.role_id ";
    String JOIN_ROLE = " left join sys_role ro on ur.role_id = ro.role_id ";
    String ORDER_BY = " order by parent_id, order_num ";

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<SysMenu> selectMenuListByUserId(SysMenu sysMenu, Long userId) {
        boolean queryMenuName = null != sysMenu && !StringUtils.isBlank(sysMenu.getMenuName());
        boolean queryVisible = null != sysMenu && !StringUtils.isBlank(sysMenu.getVisible());
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT);
        sql.append(JOIN_ROLE_MENU);
        sql.append(JOIN_USER_ROLE);
        sql.append(" where ur.user_id = :userId ");
        if (queryMenuName) {
            sql.append(" AND menu_name like concat('%', :menuName, '%') ");
        }
        if (queryVisible) {
            sql.append("  AND visible = :visible ");
        }
        sql.append(ORDER_BY);
        Query query = entityManager.createNativeQuery(sql.toString(), SysMenu.class)
                .setParameter("userId", userId);
        if (queryMenuName) {
            query.setParameter("menuName", sysMenu.getMenuName());
        }
        if (queryVisible) {
            query.setParameter("visible", sysMenu.getVisible());
        }
        return query.getResultList();
    }

    @Override
    public List<SysMenu> selectMenuListByRoleId(Long roleId) {
        StringBuffer sql = new StringBuffer();
        sql.append(SELECT);
        sql.append(JOIN_ROLE_MENU);
        sql.append(" where rm.role_id = :roleId ");
        sql.append(" and m.menu_id not in ( ");
        sql.append(" 	select DISTINCT m.parent_id from sys_menu m  ");
        sql.append(" 	inner join sys_role_menu rm on m.menu_id = rm.menu_id  ");
        sql.append(" 	and rm.role_id = :roleId ");
        sql.append(" ) ");
        sql.append(ORDER_BY);
        Query query = entityManager.createNativeQuery(sql.toString(), SysMenu.class)
                .setParameter("roleId", roleId);
        return query.getResultList();
    }
}

