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
export const getFolders = (params) => {
  if (!params) {
    return http.get(`${baseURL}/private/folder`)
  }
  const { resourceID, content } = params || null
  if (resourceID) {
    return http.get(`${baseURL}/private/folder/?resourceID=${resourceID}`)
  } else if (content) {
    return http.get(`${baseURL}/private/folder/?content=${content}`)
  }
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
 * 将资源添加到资源包,后四个参数只能写一个
 * @param {Object} resource
 * @param {Array} resource.addFolderID - 资源添加资源包的 ID 的数组
 * @param {Array} resource.deleteFolderID - 资源从资源包删除的资源包的 ID 的数组
 * @param {String} [resource.resourceID] - 待添加的资源 ID
 * @param {String} [resource.content] - 待添加的文本资源
 * @param {String} [resource.goal] - 待添加的学习目标 ID
 * @param {String} [resource.key] - 待添加的学习重难点 ID
 * @returns {AxiosPromise}
 */
export const addResource = (resource) => {
  return http.post(`${baseURL}/private/putInFolder`, resource)
}

/**
 * 根据资源包 ID 删除资源包
 * @param {String} id
 * @returns {AxiosPromise}
 */
export const deleteFolder = (id) => {
  return http.delete(`${baseURL}/private/folder/${id}`)
}

/**
 * 删除资源包中的某个资源
 * @param {Object} resourceInfo
 * @param {Number} resourceInfo.folderID - 待删除资源所在资源包的 ID
 * @param {String} [resourceInfo.resourceID] - 待删除的资源 ID
 * @param {String} [resourceInfo.content] - 待删除的文本资源
 * @param {String} [resourceInfo.goal] - 待删除的学习目标 ID
 * @param {String} [resourceInfo.key] - 待删除的学习重难点 ID
 * @returns {AxiosPromise}
 */
export const deleteResource = (resourceInfo) => {
  return http.delete(`${baseURL}/private/delSingle`, resourceInfo)
}

/**
 * 根据资源包 ID 请求生成压缩包并获取压缩包 URL
 * @param {String} folderID - 资源包 ID
 * @returns {AxiosPromise}
 */
export const queryDownload = (folderID) => {
  return http.download(`${baseURL}/private/downloadFolder?folderID=${folderID}`)
}

/**
 * 批量删除资源包中的资源
 * @param {Object} resourceInfo
 * @param {Number} resourceInfo.folderID - 待删除资源所在资源包的 ID
 * @param {Array} resourceInfo.resourceIDs - 待删除的资源 ID 的数组
 * @returns {AxiosPromise}
 */
export const deleteMulti = (resourceInfo) => {
  return http.delete(`${baseURL}/private/delMulti`, resourceInfo)
}
