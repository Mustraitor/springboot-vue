package edu.friday.repository.custom;

import edu.friday.model.SysMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 */
@Repository
public interface SysMenuCustomRepository {

    List<SysMenu> selectMenuListByUserId(SysMenu sysMenu, Long userId);

    List<SysMenu> selectMenuListByRoleId(Long roleId);
}
