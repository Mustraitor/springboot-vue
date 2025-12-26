import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * constantRoutes
 * 所有用户都能访问的基础路由
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'Dashboard', icon: 'dashboard', affix: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * 根据用户角色动态加载的路由
 */
// export const asyncRoutes = [
//   {
//     path: '/system',
//     component: Layout,
//     redirect: '/system/user',
//     name: 'System', // 父路由唯一name
//     meta: { title: '系统管理', icon: 'guide'},
//     children: [
//       {
//         path: 'user',
//         component: () => import('@/views/system/user/index'),
//         name: 'SystemUser', // 唯一name
//         meta: { title: '用户管理', icon: 'user' }
//       },
//       {
//         path: 'role',
//         component: () => import('@/views/system/role/index'),
//         name: 'SystemRole', // 唯一name
//         meta: { title: '角色管理', icon: 'role'}
//       },
//       {
//         path: 'menu',
//         component: () => import('@/views/system/menu/index'),
//         name: 'SystemMenu',
//         meta: { title: '菜单管理', icon: 'menu' }
//       }
//     ]
//   },
//   // 404 页面必须放在最后
//   { path: '*', redirect: '/404', hidden: true }
// ]

const createRouter = () => new Router({
  // mode: 'history', // 需要服务端支持
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // 重置路由
}

export default router
