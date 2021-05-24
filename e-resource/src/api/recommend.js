import http from '@/utils/http'
import store from '@/store'

let baseURL = '/v1.0'

/**
 * 搜索某一知识点后根据用户和知识点推荐资源
 * @param entity
 */
export const recommendByUserEntity = (entity) => {
  let userId = store.state.user !== '' ? store.state.user['userId'] : ''
  return http.get(`${baseURL}/public/recommendResource?userId=${userId}&entity=${entity}`)
}

/**
 * 用户点进某一个资源后，根据资源ID和用户ID推荐资源
 * @param {Number} resourceID - 资源ID
 * @returns {AxiosPromise}
 */
export const recommendByResourceUser = (resourceID) => {
  let userId = store.state.user !== '' ? store.state.user['userId'] : ''
  return http.get(`${baseURL}/public/recommendResource?resourceID=${resourceID}&userId=${userId}`)
}

/**
 * 获取热门资源
 * @returns {AxiosPromise}
 */
export const hotResource = () => {
  return http.get(`${baseURL}/public/queryHot`)
}

/**
 * 获取最新资源
 * @returns {AxiosPromise}
 */
export const newResource = () => {
  return http.get(`${baseURL}/public/queryTime`)
}

/**
 * 根据用户获取推荐资源
 * @returns {AxiosPromise}
 */
export const userRecommendResource = () => {
  let userId = store.state.user !== '' ? store.state.user['userId'] : ''
  return http.get(`${baseURL}/public/recommendUser?userId=${userId}`)
}
