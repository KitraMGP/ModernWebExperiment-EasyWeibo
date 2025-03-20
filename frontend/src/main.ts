import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/dark/css-vars.css'

import App from './App.vue'
import router from './router'
import { useDark, useToggle } from '@vueuse/core'

const app = createApp(App)

// 实现深色模式切换
export const isDark = useDark()
export const toggleDark = useToggle(isDark)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')
