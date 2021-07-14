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
 * 修改资源包文件夹信息
 * @param {Object} packageInfo - 资源包文件夹的基本信息
 * @param {Object} packageInfo.folderID - 资源包 ID
 * @param {String} packageInfo.name - 资源包名称
 * @param {String} [packageInfo.introduction] - 资源包简介
 * @returns {AxiosPromise}
 */
export const updateFolder = (packageInfo) => {
  return http.put(`${baseURL}/private/updateFolder`, packageInfo)
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

/**
 * 根据资源包 ID 删除资源包
 * @param {String} id
 * @returns {AxiosPromise}
 */
export const deleteFolder = (id) => {
  return http.delete(`${baseURL}/private/folder/${id}`)
}
