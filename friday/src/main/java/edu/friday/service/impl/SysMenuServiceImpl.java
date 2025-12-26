package edu.friday.service.impl;

import edu.friday.common.constant.UserConstants;
import edu.friday.common.result.TreeSelect;
import edu.friday.model.SysMenu;
import edu.friday.model.vo.MetaVo;
import edu.friday.model.vo.RouterVo;
import edu.friday.model.vo.SysMenuVO;
import edu.friday.model.vo.SysUserVO;
import edu.friday.repository.SysMenuRepository;
import edu.friday.service.SysMenuService;
import edu.friday.utils.BeanUtils;
import edu.friday.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理（严格校验版）
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    SysMenuRepository sysMenuRepository;

    // ------------------- 数据操作 -------------------

    @Override
    public List<SysMenu> selectMenuList(Long userId) {
        return selectMenuList(new SysMenuVO(), userId);
    }

    @Override
    public List<SysMenu> selectMenuList(SysMenuVO menu, Long userId) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyPropertiesIgnoreEmpty(menu, sysMenu);
        Sort sort = Sort.by("parentId", "orderNum");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("menuName", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<SysMenu> example = Example.of(sysMenu, matcher);

        if (SysUserVO.isAdmin(userId)) {
            return sysMenuRepository.findAll(example, sort);
        } else {
            return sysMenuRepository.selectMenuListByUserId(sysMenu, userId);
        }
    }

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = sysMenuRepository.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public List<SysMenuVO> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = SysUserVO.isAdmin(userId)
                ? sysMenuRepository.selectMenuTreeAll()
                : sysMenuRepository.selectMenuTreeByUserId(userId);
        List<SysMenuVO> menuVOS = BeanUtils.copyProperties(menus, SysMenuVO.class);
        return getChildPerms(menuVOS, 0);
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        List<Long> rs = new ArrayList<>();
        List<SysMenu> menuList = sysMenuRepository.selectMenuListByRoleId(roleId);
        for (SysMenu sysMenu : menuList) {
            rs.add(sysMenu.getMenuId());
        }
        return rs;
    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        return sysMenuRepository.findById(menuId).orElse(null);
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(menuId);
        return sysMenuRepository.count(Example.of(sysMenu)) > 0;
    }

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        return sysMenuRepository.checkMenuExistRole(menuId) > 0;
    }

    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(menuId);
        sysMenu.setMenuName(menu.getMenuName());
        Page<SysMenu> list = sysMenuRepository.findAll(Example.of(sysMenu), PageRequest.of(0, 1));
        if (list.hasContent() && list.getContent().get(0).getMenuId().longValue() != menuId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertMenu(SysMenu menu) {
        validateMenu(menu); // 严格校验
        sysMenuRepository.save(menu);
        return (menu != null && menu.getMenuId() != null) ? 1 : 0;
    }

    @Override
    public int updateMenu(SysMenu menu) {
        validateMenu(menu); // 严格校验
        SysMenu sysMenu = sysMenuRepository.findById(menu.getMenuId())
                .orElseThrow(() -> new RuntimeException("菜单不存在"));
        BeanUtils.copyPropertiesIgnoreNull(menu, sysMenu);
        sysMenuRepository.save(sysMenu);
        return 1;
    }

    @Override
    public int deleteMenuById(Long menuId) {
        sysMenuRepository.deleteById(menuId);
        return 1;
    }

    // ------------------- 路由构建 -------------------

    @Override
    public List<RouterVo> buildMenus(List<SysMenuVO> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenuVO menu : menus) {
            RouterVo router = new RouterVo();
            String path = getRouterPath(menu);

            router.setName(StringUtils.capitalize(path));
            router.setPath(path);
            router.setComponent("M".equals(menu.getMenuType()) ? "Layout" : menu.getComponent());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));

            List<SysMenuVO> cMenus = menu.getChildren();
            if (cMenus != null && !cMenus.isEmpty() && "M".equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }

            routers.add(router);
        }
        return routers;
    }

    public String getRouterPath(SysMenuVO menu) {
        if (StringUtils.isEmpty(menu.getPath())) {
            throw new RuntimeException(menu.getMenuType() + " 菜单 path 不能为空");
        }
        if ("M".equals(menu.getMenuType()) && menu.getParentId() == 0 && !menu.getPath().startsWith("/")) {
            return "/" + menu.getPath();
        }
        return menu.getPath();
    }

    // ------------------- 树结构 -------------------

    @Override
    public List<SysMenuVO> buildMenuTree(List<SysMenuVO> menus) {
        List<SysMenuVO> returnList = new ArrayList<>();
        for (SysMenuVO t : menus) {
            if (t.getParentId() == 0) {
                recursionFn(menus, t);
                returnList.add(t);
            }
        }
        return returnList.isEmpty() ? menus : returnList;
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenuVO> menus) {
        return buildMenuTree(menus).stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public List<SysMenuVO> getChildPerms(List<SysMenuVO> list, int parentId) {
        List<SysMenuVO> returnList = new ArrayList<>();
        for (SysMenuVO t : list) {
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    private void recursionFn(List<SysMenuVO> list, SysMenuVO t) {
        List<SysMenuVO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenuVO tChild : childList) {
            if (hasChild(list, tChild)) {
                for (SysMenuVO n : childList) {
                    recursionFn(list, n);
                }
            }
        }
    }

    private List<SysMenuVO> getChildList(List<SysMenuVO> list, SysMenuVO t) {
        return list.stream()
                .filter(n -> n.getParentId().longValue() == t.getMenuId().longValue())
                .collect(Collectors.toList());
    }

    private boolean hasChild(List<SysMenuVO> list, SysMenuVO t) {
        return !getChildList(list, t).isEmpty();
    }

    // ------------------- 严格校验 -------------------

    // 严格校验
    private void validateMenu(SysMenu menu) {
        if ("M".equals(menu.getMenuType())) {
            if (StringUtils.isEmpty(menu.getPath())) {
                throw new RuntimeException("目录菜单 path 不能为空");
            }
            // 取消自动加 / 和固定 component
            if (StringUtils.isEmpty(menu.getComponent())) {
                // 允许为空
            }
        } else if ("C".equals(menu.getMenuType())) {
            if (StringUtils.isEmpty(menu.getPath())) {
                throw new RuntimeException("菜单 path 不能为空");
            }
            if (StringUtils.isEmpty(menu.getComponent())) {
                throw new RuntimeException("菜单 component 不能为空");
            }
        } else if ("F".equals(menu.getMenuType())) {
            if (StringUtils.isEmpty(menu.getPerms())) {
                throw new RuntimeException("按钮 perms 不能为空");
            }
        } else {
            throw new RuntimeException("未知菜单类型");
        }
    }

}
