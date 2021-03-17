import http from '@/utils/http'

let baseURL = '/v1.0'

export const resource = (params) => {
  let data = ''
  for (let info in params) {
    data += info + '=' + params[info] + '&'
  }
  return http.get(`${baseURL}/public/conditionalQueryResource?${data}`)
}
