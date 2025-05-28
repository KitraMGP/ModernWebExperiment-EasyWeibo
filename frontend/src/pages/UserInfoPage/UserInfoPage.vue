<script setup lang="ts">
import { useRoute } from 'vue-router';
import defaultAvatar from '@/assets/default_avatar.jpg'
import { ref, watch } from 'vue';
import { userInfo } from '@/services/userApi';
import { checkSuccessful } from '@/services/api';
import type { GetUserInfoResponse } from '@/services/dto/userDto';
import TopBar from '@/components/TopBar.vue';
import SimpleAvatar from '@/components/SimpleAvatar.vue';
import { getUserAvatarUrl } from '@/utils/avatarUtil';

// route 用于获取路径中的参数
const route = useRoute()
const avatarUrl = ref(defaultAvatar)
// 指示用户不存在
const userNotFound = ref(false)
// 指示正在加载用户信息
const isLoading = ref(true)
// 指示发生请求错误，若为 true，显示错误并让用户重试
const isError = ref(false)
const userData = ref<GetUserInfoResponse | null>(null)

// 在切换到其他用户个人信息的时候，重新渲染
watch(() => route.params.user, (newValue) => {
  fetchUserInfo(newValue)
})

// 获取用户信息
function fetchUserInfo(user: string | string[]) {
  isLoading.value = true
  isError.value = false
  userNotFound.value = false
  // 若为 string[]，表示用户名错误
  if (typeof user != "string") {
    isLoading.value = false
    userNotFound.value = true
    return
  }
  // 开始请求
  userInfo(user).then(resp => {
    // 后端返回错误码
    if (!checkSuccessful(resp)) {
      // 若错误为用户不存在
      if (resp.data.code === 4001) {
        userNotFound.value = true
      } else {
        isError.value = true
      }
      return
    }
    // 请求成功
    userData.value = resp.data.data
    // 更新头像链接
    if (userData.value.hasAvatar) {
      avatarUrl.value = getUserAvatarUrl(resp.data.data.userId)
    } else {
      avatarUrl.value = defaultAvatar
    }
  }).catch(() => {
    isError.value = true
  }).finally(() => {
    isLoading.value = false
  })
}

fetchUserInfo(route.params.user)
</script>

<template>
  <TopBar :show-back-link="true" />
  <div class="container margin-top2">
    <span v-if="isError">加载失败</span>
    <span v-if="userNotFound">用户不存在</span>
    <span v-if="isLoading">加载中</span>
    <div v-if="!isError && !userNotFound && !isLoading" class="container">
      <SimpleAvatar size="large" :avatar-url="avatarUrl" :large-avatar="true" />

      <span class="big-title">{{ userData?.nickname }}</span>
      <table border="0">
        <tbody>
          <tr>
            <td class="label">用户名：</td>
            <td>{{ userData?.username }}</td>
          </tr>
          <tr>
            <td class="label">昵称：</td>
            <td>{{ userData?.nickname }}</td>
          </tr>
          <tr>
            <td class="label">个人描述：</td>
            <td>{{ userData?.description.length === 0 ? "这个人很懒，没有写个人描述" : userData?.description }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style lang="css" scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.margin-top2 {
  margin-top: 2rem;
}

.big-title {
  font-size: 1.5rem;
}

td {
  padding: 0.5rem 1rem;
}

.label {
  text-align: right;
}
</style>
