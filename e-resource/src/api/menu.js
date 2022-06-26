import http from '@/utils/http'

const baseURL = '/v1.0'
/**
 * 获取目录的列表
 * @returns {AxiosPromise}
 */
export const menus = () => {
  return http.get(`${baseURL}/public/classification`)
}

/**
 * 获取学科列表
 * @returns {AxiosPromise}
 */
export const getSubjects = () => {
  return http.get(`${baseURL}/public/subject`)
}
