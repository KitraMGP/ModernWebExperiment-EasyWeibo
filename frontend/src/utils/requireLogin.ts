import RequireLoginDialog from '@/components/dialogs/RequireLoginDialog.vue'
import { createApp } from 'vue'
import { isLogin } from './isLogin'

/**
 * 检测用户是否已登录，若未登录，弹窗提示用户需要登录
 */
export const requireLogin = () => {
  if (!isLogin()) {
    // 创建HTML元素并挂载对话框组件
    const mountNode = document.createElement('div')
    const instance = createApp(RequireLoginDialog, {
      onClosed: () => {
        // 等待一会后再销毁元素，让关闭对话框的动画能够正常显示
        setTimeout(() => {
          instance.unmount()
          document.body.removeChild(mountNode)
        }, 2000)
      },
    })
    document.body.appendChild(mountNode)
    instance.mount(mountNode)
    if (instance._instance?.exposed) {
      instance._instance.exposed.showDialog()
    }
  }
}
