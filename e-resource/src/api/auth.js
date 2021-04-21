import http from '@/utils/http'

let baseURL = '/v1.0'

/**
 * 注册
 * @param {Object} params
 * @param {String} params.username - 用户名
 * @param {String} params.email - 邮箱
 * @param {String} params.password - 密码
 * @param {String} params.confirmPassword - 密码
 * @param {Number} params.period - 学段代码（2: 小学, 3: 初中, 4: 高中）
 * @param {Number} params.grade - 年级
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
