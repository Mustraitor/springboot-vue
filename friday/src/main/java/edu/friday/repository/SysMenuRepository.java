package edu.friday.repository;

import edu.friday.repository.custom.SysMenuCustomRepository;
import edu.friday.model.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 用户表 数据层
 */
@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, Long>, SysMenuCustomRepository {

    String SELECT_DISTINCT = " select distinct m.menu_id, m.parent_id, m.menu_name, m.path, m.component, m.visible, ifnull(m.perms,'') as perms, m.is_frame, m.menu_type, m.icon, m.order_num, m.create_by, m.create_time, m.update_by, m.update_time, m.remark from sys_menu m ";
    String JOIN_ROLE_MENU = " left join sys_role_menu rm on m.menu_id = rm.menu_id ";
    String JOIN_USER_ROLE = " left join sys_user_role ur on rm.role_id = ur.role_id ";
    String JOIN_ROLE = " left join sys_role ro on ur.role_id = ro.role_id ";
    String JOIN_USER = " left join sys_user u on ur.user_id = u.user_id ";
    String ORDER_BY = " order by m.parent_id, m.order_num ";

    @Query(value = " select distinct m.perms from sys_menu m "
            + JOIN_ROLE_MENU + JOIN_USER_ROLE + " where ur.user_id = :userId ", nativeQuery = true)
    List<String> selectMenuPermsByUserId(@Param("userId") Long userId);

    @Query(value = " select count(*) from sys_role_menu where menu_id = :menuId ", nativeQuery = true)
    long checkMenuExistRole(@Param("menuId") Long menuId);

    @Query(value = SELECT_DISTINCT +
            "  where m.menu_type in ('M', 'C') and m.visible = 0 " +
            ORDER_BY, nativeQuery = true)
    List<SysMenu> selectMenuTreeAll();

    @Query(value = SELECT_DISTINCT + JOIN_ROLE_MENU + JOIN_USER_ROLE + JOIN_ROLE+ JOIN_USER +
            " where u.user_id = :userId and m.menu_type in ('M', 'C') and m.visible = 0  AND ro.status = 0 " +
            ORDER_BY, nativeQuery = true)
    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId);
}