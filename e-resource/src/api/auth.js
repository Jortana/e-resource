import http from '@/utils/http'

let baseURL = '/v1.0'

/**
 * 注册
 * - username: 用户名
 * - email: 邮箱
 * - password: 密码
 * - confirmPassword: 确认密码
 * - period: 学段代码（2: 小学, 3: 初中, 4: 高中）
 * - grade: 年级
 * @param {Object} params
 * @param {String} params.username
 * @param {String} params.email
 * @param {String} params.password
 * @param {Number} params.period
 * @param {Number} params.grade
 */
export const register = (params) => {
  return http.post(`${baseURL}/public/register`, params)
}

/**
 * 登录
 * - username: 用户名
 * - password: 密码
 * @param {Object} params
 * @param {string} params.username
 * @param {string} params.password
 */
export const login = (params) => {
  return http.post(`${baseURL}/public/login`, params)
}

/**
 * 登出
 */
export const logout = () => {
  return http.get(`${baseURL}/private/logout`)
}

/**
 * 每次切换页面都发送一个验证请求，防止偷鸡登录
 */
export const authentication = () => {
  return http.get(`${baseURL}/public/authentication`)
}
