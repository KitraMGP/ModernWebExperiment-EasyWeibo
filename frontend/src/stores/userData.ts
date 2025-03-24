import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// 全局存储userId
export const useUserIdStore = defineStore('userId', () => {
  const userId = ref('')
  const value = computed(() => userId.value)
  function set(value: string) {
    userId.value = value
  }

  return { userId, value, set }
})

// 全局存储nickname
export const useNicknameStore = defineStore('nickname', () => {
  const nickname = ref('')
  const value = computed(() => nickname.value)
  function set(value: string) {
    nickname.value = value
  }

  return { nickname, value, set }
})
