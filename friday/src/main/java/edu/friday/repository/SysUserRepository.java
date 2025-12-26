package edu.friday.repository;

import edu.friday.model.SysUser;
import edu.friday.repository.custom.SysUserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long>, SysUserCustomRepository {
    @Modifying
    @Query(value = " update sys_user set del_flag = '2' where user_id in :userIds ", nativeQuery = true)
    int deleteUserByIds(@Param("userIds") Long[] userIds);
    @Modifying
    @Query(value = " delete from sys_user_role where user_id=:userId ", nativeQuery = true)
    int deleteUserRoleByUserId(@Param("userId")Long userId);

    SysUser findByUserName(String username);
}
