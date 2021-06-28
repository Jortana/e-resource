import http from '@/utils/http'

const baseURL = '/v1.0'

/**
 * 创建一个资源包文件夹
 * @param {Object} packageInfo - 资源包文件夹的基本信息
 * @param {String} packageInfo.name - 资源包名称
 * @param {String} [packageInfo.introduction] - 资源包简介
 * @returns {AxiosPromise}
 */
export const createFolder = (packageInfo) => {
  return http.post(`${baseURL}/private/createFolder`, packageInfo)
}

/**
 * 获取当前用户的资源包
 * @returns {AxiosPromise}
 */
export const getFolders = () => {
  return http.get(`${baseURL}/private/folder`)
}

/**
 * 根据资源包 ID 获取其中所包含的资源
 * @param {String} id
 * @returns {AxiosPromise}
 */
export const getResources = (id) => {
  return http.get(`${baseURL}/private/folderResource/${id}`)
}
