import axios, { AxiosError, type AxiosResponse } from 'axios'
import type { ApiResponse } from './dto/commonDto'

// API地址，开发前端时使用mock地址
export const api = axios.create({
  //baseURL: 'http://127.0.0.1:4523/m1/6077610-5767993-default/',
  baseURL: '/api/',
  timeout: 10000,
  withCredentials: true,
})

/**
 * 根据ApiResponse获取错误信息
 */
export const getErrorMsg = (response: AxiosResponse<ApiResponse<unknown>>) => {
  return response.data.msg
}

/**
 * 检查请求是否成功。若成功返回true，否则返回false
 */
export const checkSuccessful = (response: AxiosResponse<ApiResponse<unknown>>) => {
  return response.data.code === 200
}
/**
 * 传入AxiosError或字符串，横幅展示错误信息
 */
export const showFailMessage = (title: string, e: AxiosError | string) => {
  console.error(e)
  ElNotification({
    type: 'error',
    title: title,
    message: e.toString(),
  })
}

/**
 * 横幅展示成功信息
 */
export const showSuccessfulMessage = (title: string) => {
  ElNotification({
    type: 'success',
    title: title,
    message: '',
    duration: 2000,
  })
}
