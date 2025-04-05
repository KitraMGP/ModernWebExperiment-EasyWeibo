import { useUserDataStore } from '@/stores/userData'
import { checkSuccessful, showFailMessage } from './api'
import { userInfo } from './userApi'

/**
 * 在页面初始化的时候调用这个函数来拉取登录信息
 */
export const setupLogin = () => {
  const userDataStore = useUserDataStore()
  userInfo()
    .then((resp) => {
      if (!checkSuccessful(resp)) {
        return
      }
      userDataStore.set(resp.data.data)
    })
    .catch((e) => showFailMessage('获取登录信息失败', e))
}
