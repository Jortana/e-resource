import Vue from 'vue'
import VueRouter from 'vue-router'

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
    path: '/account',
    name: 'Account',
    component: () => import('@/view/Account/Account'),
    redirect: '/account/home',
    children: [
      {
        path: 'home',
        name: 'UserInfo',
        component: () => import('@/view/Account/Home')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes,
  scrollBehavior (to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { x: 0, y: 0 }
    }
  }
})

export default router
