package edu.friday.repository;

import edu.friday.model.SysRole;
import edu.friday.repository.custom.SysRoleCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long>, SysRoleCustomRepository {
    @Modifying
    @Query(value = " update sys_role set del_flag = '2' where role_id in :roleIds ", nativeQuery = true)
    int deleteRoleByIds(@Param("roleIds") Long[] roleIds);
    @Modifying
    @Query(value = " delete from sys_role_menu where role_id=:roleId ", nativeQuery = true)
    int deleteRoleMenuByRoleId(@Param("roleId")Long roleId);
    @Query(value = "select count(*) from sys_user_role where role_id=:roleId ", nativeQuery = true)
    int countUserRoleByRoleId(@Param("roleId") Long roleId);
    //获取角色信息
    final String SELECT = " select distinct r.* from sys_role r ";
    final String JOIN_USER_ROLE = " left join sys_user_role ur on ur.role_id = r.role_id ";
    final String JOIN_USER = "left join sys_user u on u.user_id = ur.user_id ";

    @Query(value = SELECT + JOIN_USER_ROLE + JOIN_USER + "WHERE r.del_flag = '0' and ur.user_id = :userId ", nativeQuery = true)
    List<SysRole> selectRoleByUserId(@Param("userId") Long userId);

    @Query(value = " select r.role_id from sys_role r " + JOIN_USER_ROLE + JOIN_USER +
            "WHERE r.del_flag = '0' and u.user_id = :userId ", nativeQuery = true)
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

}
