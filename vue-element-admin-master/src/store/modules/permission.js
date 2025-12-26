import { constantRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'

const permission = {
  state: {
    routes: [],
    addRoutes: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    }
  },
  actions: {
    generateRoutes({ commit }) {
      return new Promise(resolve => {
        getRouters().then(res => {
          const accessedRoutes = filterAsyncRouter(res.data)
          accessedRoutes.push({
            path: '*',
            redirect: '/404',
            hidden: true
          })
          commit('SET_ROUTES', accessedRoutes)
          resolve(accessedRoutes)
        })
      })
    }
  }
}

function filterAsyncRouter(asyncRouterMap) {
  return asyncRouterMap.filter(route => {
    if (route.component) {
      if (route.component === 'Layout') {
        route.component = Layout
      } else {
        route.component = loadView(route.component)
        // if (!view) return false
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    }
    return true
  })
}

// export const loadView = view => {
//   return resolve => require([`@/views/${view}`], resolve)
// }

export const loadView = view => {
  return resolve => {
    if (view.endsWith('index')) {
      require([`@/views/${view}.vue`], resolve)
    } else {
      require([`@/views/${view}/index.vue`], resolve)
    }
  }
}

export default permission
