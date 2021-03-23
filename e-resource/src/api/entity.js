import http from '@/utils/http'

let baseURL = '/v1.0'

/**
 * 根据关键词查询相关联的知识点
 * - keyword: 查找的关键词
 * @param {string} keyword
 * @returns {AxiosPromise}
 */
export const relatedEntity = (keyword) => {
  return http.get(`${baseURL}/public/relatedEntity?keyword=${keyword}`)
}
