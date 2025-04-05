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
