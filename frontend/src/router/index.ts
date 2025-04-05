import HomePage from '@/pages/HomePage/HomePage.vue'
import LoginPage from '@/pages/LoginPage/LoginPage.vue'
import RegisterPage from '@/pages/LoginPage/RegisterPage.vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: HomePage,
    },
    {
      path: '/register',
      component: RegisterPage,
    },
    {
      path: '/login',
      component: LoginPage,
    },
  ],
})

export default router
