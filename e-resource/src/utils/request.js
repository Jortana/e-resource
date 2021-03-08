import axios from 'axios'

let baseURL = ''

if (process.env.NODE_ENV === 'production') {
  baseURL = process.env.E_RESOURCE_API
} else {
  baseURL = process.env.E_RESOURCE_API_DEV
}

const service = axios.create({
  baseURL,
  withCredentials: true,
  timeout: 15000
})

service.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    const errMessage = `error: ${error}`
    console.log(errMessage)
    return Promise.reject(error)
  }
)

export default service
