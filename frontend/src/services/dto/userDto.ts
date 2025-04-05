export interface RegisterRequest {
  username: string
  nickname: string
  password: string
}

export interface LoginRequest {
  username: string
  password: string
}

export interface UpdateUserInfoRequest {
  username: string
  nickname: string
  description: string
}

export interface ChangePasswordRequest {
  oldPassword: string
  newPassword: string
}

export interface GetUserInfoResponse {
  username: string
  nickname: string
  description: string
  hasAvatar: boolean
}
