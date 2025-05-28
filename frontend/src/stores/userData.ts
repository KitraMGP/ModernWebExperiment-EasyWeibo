import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import type { GetUserInfoResponse } from '@/services/dto/userDto'

// 全局存储userData
export const useUserDataStore = defineStore('userData', () => {
  const userData = ref<GetUserInfoResponse | null>(null)
  const value = computed(() => userData.value)
  function set(value: GetUserInfoResponse) {
    userData.value = value
  }
  function clear() {
    userData.value = null
  }

  return { userData: userData, value, set, clear }
})

// 控制是否显示登录提示对话框
export const useRequireLoginDialogStore = defineStore('requireLoginDialog', () => {
  const store = ref(false)
  const value = computed(() => store.value)
  function set(value: boolean) {
    store.value = value
  }
  return { requireLoginDialog: store, value, set }
})
