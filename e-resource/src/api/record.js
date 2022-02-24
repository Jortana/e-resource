import http from '@/utils/http'
import store from '@/store'

const baseURL = '/v1.0'

/**
 * 添加一条用户的浏览记录
 * - resourceID: 资源ID
 * - entityName: 知识点名
 * @param {Object} recordInfo
 * @param {Number} [recordInfo.resourceID] - 资源ID
 * @param {String} [recordInfo.entityName] - 知识点名
 * @returns {AxiosPromise}
 */
export const record = (recordInfo) => {
  if (store.state.user !== '') {
    recordInfo['userId'] = store.state.user['userId']
  }
  return http.post(`${baseURL}/public/record`, recordInfo)
}
