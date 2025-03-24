<script setup lang="ts">
import { isDark, toggleDark } from '@/main';
import router from '@/router';
import { useNicknameStore, useUserIdStore } from '@/stores/userData';
import { ref, watch } from 'vue';

// 获取全局存储的userId和nickname
const userIdStore = useUserIdStore()
const nicknameStore = useNicknameStore()
const userId = ref(userIdStore.value)
const nickname = ref(nicknameStore.value)
// 监听全局存储数据的变化并更新变量
watch(() => [userIdStore.value, nicknameStore.value], ([newUserId, newNickname]) => {
  userId.value = newUserId
  nickname.value = newNickname
})
// 判断是否已登录
const isUserLoggedIn = ref(userIdStore.value !== "")
</script>

<template>
  <div class="topbar">
    <el-link v-if="!isUserLoggedIn" :underline="false" class="topbar-item">注册</el-link>
    <el-link v-if="!isUserLoggedIn" :underline="false" class="topbar-item" @click="router.push('/login')">登录</el-link>
    <span v-if="isUserLoggedIn">用户名：{{ userId }}，昵称：{{ nickname }}</span>
    <!-- <SimpleAvatar v-if="isUserLoggedIn" /> -->
    <el-link :underline="false" class="topbar-item" @click="toggleDark()">{{ isDark ? "暗色模式" : "亮色模式" }}</el-link>
  </div>
</template>

<style lang="css" scoped>
.topbar {
  display: flex;
  width: 100%;
  padding: 0 1rem 1rem 0;
  justify-content: end;
  border-bottom: 1px solid;
}

.topbar-item {
  margin-left: 1rem;
}
</style>
