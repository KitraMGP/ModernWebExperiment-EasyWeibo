import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import localizedFormat from 'dayjs/plugin/localizedFormat'
import 'element-plus/theme-chalk/dark/css-vars.css'
// IconFont
import '@/assets/iconfont/iconfont.css'

import App from './App.vue'
import router from './router'
import { useDark, useToggle } from '@vueuse/core'
import { setupLogin } from './services/setupLogin'

const app = createApp(App)

// 实现深色模式切换
export const isDark = useDark()
export const toggleDark = useToggle(isDark)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

setupLogin()
dayjs.extend(localizedFormat)
dayjs.locale('zh-cn')

app.mount('#app')
