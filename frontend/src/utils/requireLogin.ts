import { isLogin } from './isLogin'
import { useRequireLoginDialogStore } from '@/stores/userData'

/**
 * 检测用户是否已登录，若未登录，弹窗提示用户需要登录
 */
export const requireLogin = () => {
  if (!isLogin()) {
    const store = useRequireLoginDialogStore()
    store.set(true)
  }
}
