<script setup lang="ts">
import { toggleDark } from '@/main';
import router from '@/router';
import { checkSuccessful, getErrorMsg, showFailMessage } from '@/services/api';
import { logout } from '@/services/userApi';
import { useUserDataStore } from '@/stores/userData';
import { ref, watch } from 'vue';

const props = defineProps<{
  showBackLink?: boolean // 决定是否显示“返回到上一页”按钮
}>()
// 获取全局存储的userId和nickname
const userDataStore = useUserDataStore()
const userData = ref(userDataStore.value)
// 监听全局存储数据的变化并更新变量
watch(() => [userDataStore.value], ([newUserData]) => {
  userData.value = newUserData
  isUserLoggedIn.value = newUserData != null
})
// 判断是否已登录
const isUserLoggedIn = ref(userData.value != null)

function doLogout() {
  logout().then(resp => {
    if (!checkSuccessful(resp)) {
      showFailMessage("退出登录失败", getErrorMsg(resp))
      return
    }
    userDataStore.clear()
    router.go(0)
  }
  ).catch(e => showFailMessage("退出登录失败", e))
}
</script>

<template>
  <div class="topbar">
    <h1 class="topbar-title" @click="router.push('/')">
      EasyWeibo
      <span v-if="isUserLoggedIn && userData?.role === 'admin'">(管理员)</span>
    </h1>
    <span class="flex-center" v-if="isUserLoggedIn">用户名：{{ userData?.username }}，昵称：{{ userData?.nickname
    }}</span>
    <!-- 若 showBackToMainPage 未定义，则默认为 false -->
    <el-link v-if="props.showBackLink ? props.showBackLink : false" :underline="false" class="topbar-item"
      @click="router.back()">返回上一页</el-link>
    <el-link v-if="!isUserLoggedIn" :underline="false" class="topbar-item"
      @click="router.push('/register')">注册</el-link>
    <el-link v-if="!isUserLoggedIn" :underline="false" class="topbar-item" @click="router.push('/login')">登录</el-link>
    <el-link :underline="false" class="topbar-item" @click="toggleDark()">主题切换</el-link>
    <el-link v-if="isUserLoggedIn" :underline="false" class="topbar-item"
      @click="router.push('/editUserInfo')">个人中心</el-link>
    <el-link v-if="isUserLoggedIn" :underline="false" class="topbar-item" @click="doLogout()">退出登录</el-link>
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

.topbar-title {
  display: flex;
  margin-right: auto;
  font-size: 1.5rem;
  cursor: pointer;
}

.topbar-item {
  margin-left: 1rem;
}

.flex-center {
  display: flex;
  align-items: center;
}
</style>
