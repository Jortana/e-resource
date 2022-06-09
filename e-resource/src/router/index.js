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
        component: () => import('@/view/Account/Home'),
        meta: {
          requireAuth: true,
          title: '个人中心'
        }
      }
    ]
  },
  // 收藏
  {
    path: '/package',
    name: 'Package',
    component: () => import('@/view/Package'),
    meta: {
      requireAuth: true,
      title: '资源包'
    }
  },
  // 资源页面
  {
    path: '/resource/:resourceID',
    name: 'Resource',
    component: () => import('@/view/Resource')
  },
  // 排行页面
  {
    path: '/rate',
    name: 'Rate',
    component: () => import('@/view/Rate')
  },
  // 知识页面
  {
    path: '/knowledge/:entityName',
    name: 'Knowledge',
    component: () => import('@/view/Knowledge')
  }
]

const router = new VueRouter({
  mode: 'hash',
  // base: '/e-resource/',
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
  if (to.meta.title) {
    // 判断是否有标题
    document.title = to.meta.title
  }

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
