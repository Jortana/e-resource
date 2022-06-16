import http from '@/utils/http'
import store from '@/store'

const baseURL = '/v1.0'

/**
 * 搜索某一知识点后根据用户和知识点推荐资源
 * @param entity
 */
export const recommendByUserEntity = (entity) => {
  const userId = store.state.user !== '' ? store.state.user['userId'] : ''
  return http.get(
    `${baseURL}/public/recommendResource?userId=${userId}&entity=${entity}`
  )
}

/**
 * 用户点进某一个资源后，根据资源 ID 和用户 ID 推荐资源
 * @param {Number} resourceID - 资源 ID
 * @returns {AxiosPromise}
 */
export const recommendByResourceUser = (resourceID) => {
  const userId = store.state.user !== '' ? store.state.user['userId'] : ''
  return http.get(
    `${baseURL}/public/recommendResource?resourceID=${resourceID}&userId=${userId}`
  )
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
  const userId = store.state.user !== '' ? store.state.user['userId'] : ''
  return http.get(`${baseURL}/public/recommendUser?userId=${userId}`)
}

/**
 * 获取轮播图数据
 * @returns {AxiosPromise}
 */
export const carousel = () => {
  return http.get(`${baseURL}/public/getRotationChart`)
}

/**
 * 获取热门资源 条数多的版本
 * @returns {AxiosPromise}
 */
export const hotResourceMore = () => {
  return http.get(`${baseURL}/public/queryMoreHot`)
}

/**
 * 获取最新资源 条数多的版本
 * @returns {AxiosPromise}
 */
export const newResourceMore = () => {
  return http.get(`${baseURL}/public/queryMoreTime`)
}

/**
 * 获取下载量排行 条数多的版本
 * @returns {AxiosPromise}
 */
export const downloadResourceMore = () => {
  return http.get(`${baseURL}/public/queryMoreDownload`)
}

/**
 * 根据用户获取推荐资源 条数多的版本
 * @returns {AxiosPromise}
 */
export const userRecommendResourceMore = () => {
  const userId = store.state.user !== '' ? store.state.user['userId'] : ''
  return http.get(`${baseURL}/public/recommendMoreUser?userId=${userId}`)
}

/**
 * 根据学段搜索精品资源
 * @param {String} [grade] - 学段
 * @returns {AxiosPromise}
 */
export const boutiqueResource = (grade) => {
  if (grade) {
    return http.get(`${baseURL}/public/queryBoutique?grade=${grade}`)
  }
  return http.get(`${baseURL}/public/queryBoutique`)
}
