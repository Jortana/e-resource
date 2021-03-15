import http from '@/utils/http'

export const avatar = (url) => {
  return http.get(`/e-resource/api/file/avatar/${url}`)
}
