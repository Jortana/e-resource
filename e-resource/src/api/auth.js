import request from '@/utils/request'

/**
 * 注册
 * - username: 用户名
 * - email: 邮箱
 * - password: 密码
 * - confirmPassword: 确认密码
 * - period: 学段代码（2: 小学, 3: 初中, 4: 高中）
 * - grade: 年级
 * @param {Object} params
 * @param {string} params.username
 * @param {string} params.email
 * @param {string} params.password
 * @param {string} params.confirmPassword
 * @param {Number} params.period
 * @param {Number} params.grade
 */
export function register (params) {
  return request({
    url: '/register',
    method: 'post',
    params
  })
}

/**
 * 登录
 * - username: 用户名
 * - password: 密码
 * @param {Object} params
 * @param {string} params.username
 * @param {string} params.password
 */
export function login (params) {
  return request({
    url: '/login',
    method: 'post',
    params
  })
}
