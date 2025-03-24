import axios, { type AxiosResponse } from 'axios'

// API地址，开发前端时使用mock地址
const api = axios.create({
  baseURL: 'http://127.0.0.1:4523/m1/6077610-5767993-default/',
  timeout: 10000,
})

export interface ApiResponse<T> {
  code: number
  data: T
  msg: string
}

/* 获取帖子列表 */
export interface PostItem {
  id: number
  time: number
  userId: string
  nickname: string
  content: string
}

export interface Posts {
  posts: PostItem[]
}

export const getPosts = () => api.get<ApiResponse<Posts>>('/posts')

/* 登录 */
export interface LoginRequest {
  userId: string
  password: string
}

export interface LoginResponseData {
  userId: string
  nickname: string
}

export const login = (r: LoginRequest) =>
  api.post<LoginRequest, AxiosResponse<ApiResponse<LoginResponseData>>>('/login', r)

/* 发布帖子 */
export interface SubmitPostRequest {
  userId: string
  content: string
}

export interface SubmitPostResponseData {
  postId: number
}

export const submitPost = (r: SubmitPostRequest) =>
  api.post<SubmitPostRequest, AxiosResponse<ApiResponse<SubmitPostResponseData>>>('/newpost', r)
