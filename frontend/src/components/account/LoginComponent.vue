<script setup lang="ts">
import router from '@/router';
import { login, type LoginRequest } from '@/services/api';
import { useNicknameStore, useUserIdStore } from '@/stores/userData';
import { type FormRules } from 'element-plus';
import { reactive, ref } from 'vue';

const form = reactive({
  userId: "",
  password: ""
})
// 使用全局存储的userId, nickname
const userIdStore = useUserIdStore()
const nicknameStore = useNicknameStore()

// 控制登录按钮是否可用
const loginDisabled = ref(false)

// 用户名校验函数
const validateUserId = (rule: unknown, value: string, callback: (error?: string | Error) => void) => {
  if (value.trim().length === 0) {
    callback(new Error("请输入用户名"))
  } else if (value.indexOf("@") != -1) {
    callback(new Error("用户名中不需要输入@"))
  } else {
    callback()
  }
}

// 密码校验函数
const validatePassword = (rule: unknown, value: string, callback: (error?: string | Error) => void) => {
  if (value.trim().length === 0) {
    callback(new Error("请输入密码"))
  } else {
    callback()
  }
}

// 表单校验规则
const rules = reactive<FormRules<typeof form>>({
  userId: [{ validator: validateUserId, trigger: "blur" }],
  password: [{ validator: validatePassword, trigger: "blur" }]
})

// 登录
function onLogin() {
  const request: LoginRequest = {
    userId: form.userId.trim(),
    password: form.password.trim()
  }
  if (request.userId === "" || request.password === "") return;
  if (request.userId.indexOf("@") != -1) return;
  loginDisabled.value = true;
  console.log("login!")
  // 发送登录请求
  login(request).then(resp => {
    console.log(resp.data)
    ElNotification({
      type: "success",
      title: "登录成功"
    })
    // 将用户信息存入全局存储
    userIdStore.set(resp.data.data.userId)
    nicknameStore.set(resp.data.data.nickname)
    router.back()
  }, e => {
    console.log(e)
    loginDisabled.value = false
    ElNotification({
      type: "error",
      title: "登录失败",
      message: e
    })
  })
}
</script>

<template>
  <div class="login">
    <span class="title">
      登录
    </span>
    <el-form :model="form" label-width="auto" size="large" :rules="rules">
      <el-form-item label="用户名" prop="userId">
        <el-input placeholder="用户唯一名称" v-model="form.userId">
          <template #prepend>@</template>
        </el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" placeholder="登录密码" v-model="form.password"></el-input>
      </el-form-item>
    </el-form>
    <el-button type="primary" @click="onLogin()" :disabled="loginDisabled" class="login-button">登录</el-button>
  </div>
</template>

<style lang="css" scoped>
.login {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 400px;
  border: 1px solid var(--el-border-color-darker);
  border-radius: var(--el-border-radius-base);
  box-shadow: var(--el-box-shadow);
  padding: 1rem 4rem 1.5rem 4rem;
}

.title {
  text-align: center;
  font-size: 2rem;
  margin-top: 0.5rem;
  margin-bottom: 1rem;
}

.login-button {
  width: 5rem;
}
</style>
