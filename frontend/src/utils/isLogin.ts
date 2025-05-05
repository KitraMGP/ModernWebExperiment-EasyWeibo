import type { GetUserInfoResponse } from '@/services/dto/userDto'
import { useUserDataStore } from '@/stores/userData'

export const isLogin = (): boolean => {
  const userDataStore = useUserDataStore()
  return userDataStore.value != null
}

/**
 * 获取当前登录的用户的信息
 *
 * 调用该函数必须确保用户已登录，否则会抛出异常
 */
export const getLoginUser = (): GetUserInfoResponse => {
  const userData = useUserDataStore().value
  if (userData === null) {
    throw new Error('userData 为 null，用户未登录')
  } else {
    return userData
  }
}
