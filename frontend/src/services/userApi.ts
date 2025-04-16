import type { AxiosResponse } from 'axios'
import { api } from './api'
import type { ApiResponse } from './dto/commonDto'
import type {
  ChangePasswordRequest,
  GetUserInfoResponse,
  LoginRequest,
  RegisterRequest,
  UpdateUserInfoRequest,
} from './dto/userDto'

export const register = (r: RegisterRequest) =>
  api.post<RegisterRequest, AxiosResponse<ApiResponse<null>>>('/user/register', r)

export const login = (r: LoginRequest) =>
  api.post<LoginRequest, AxiosResponse<ApiResponse<null>>>('/user/login', r)

export const logout = () => api.get<ApiResponse<null>>('/user/logout')

export const updateUserInfo = (r: UpdateUserInfoRequest) =>
  api.post<UpdateUserInfoRequest, AxiosResponse<ApiResponse<null>>>('/user/updateUserInfo', r)

export const changePassword = (r: ChangePasswordRequest) =>
  api.post<ChangePasswordRequest, AxiosResponse<ApiResponse<null>>>('/user/changePassword', r)

export const uploadAvatar = (r: FormData) =>
  api.postForm<ApiResponse<null>>('/user/uploadAvatar', r)

export const userInfo = (user?: string) =>
  api.get<ApiResponse<GetUserInfoResponse>>('/user/info', { params: { user } })
