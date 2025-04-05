import { useUserDataStore } from '@/stores/userData'

export const isLogin = (): boolean => {
  const userDataStore = useUserDataStore()
  return userDataStore.value != null
}
