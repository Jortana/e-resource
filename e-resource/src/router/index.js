import Vue from 'vue'
import VueRouter from 'vue-router'
import { authentication } from '@/api/auth'
// import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'HomePage',
    component: () => import('@/view/HomePage')
  },
  {
    path: '/search',
    name: 'ResourceCenter',
    component: () => import('@/view/ResourceCenter')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/view/Sign')
  },
  {
    path: '/account',
    name: 'Account',
    component: () => import('@/view/Account/Account'),
    meta: {
      requireAuth: true
    },
    redirect: '/account/home',
    children: [
      {
        path: 'home',
        name: 'UserInfo',
        component: () => import('@/view/Account/Home')
      }
    ]
  },
  // 收藏
  {
    path: '/package',
    name: 'Package',
    component: () => import('@/view/Package'),
    meta: {
      requireAuth: true
    }
  },
  // 资源页面
  {
    path: '/resource/:resourceID',
    name: 'Resource',
    component: () => import('@/view/Resource')
  },
  // 知识页面
  {
    path: '/knowledge/:entityName',
    name: 'Knowledge',
    component: () => import('@/view/Knowledge')
  }
]

const router = new VueRouter({
  mode: 'history',
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { x: 0, y: 0 }
    }
  }
})

router.beforeEach((to, from, next) => {
  // 如果访问的页面需要登录
  if (to.meta.requireAuth) {
    authentication().then((response) => {
      const {
        data: { code }
      } = response
      if (code !== 200) {
        // 未登录
        next({
          path: 'login',
          query: {
            redirect: to.fullPath
          }
        })
      } else {
        next()
      }
    })
  } else {
    next()
  }
})

export default router
