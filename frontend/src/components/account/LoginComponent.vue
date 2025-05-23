<script setup lang="ts">
import router from '@/router';
import { checkSuccessful, getErrorMsg, showFailMessage, showSuccessfulMessage } from '@/services/api';
import type { LoginRequest } from '@/services/dto/userDto';
import { login, userInfo } from '@/services/userApi';
import { useUserDataStore } from '@/stores/userData';
import { type FormRules } from 'element-plus';
import { reactive, ref, watch } from 'vue';

const form = reactive({
  username: "",
  password: ""
})
// 使用全局存储的用户数据
const userDataStore = useUserDataStore()

// 检测用户是否已登录，若已登录则回到上一页
watch(userDataStore, (newValue) => {
  // 若用户是因为进行登录操作而更改了userDataStore，则不进行这里的跳转
  if (loginDisabled.value) {
    return
  }
  if (newValue != null) {
    ElNotification({
      title: "您已登录",
      type: "info",
      duration: 2000
    })
    router.replace("/")
  }
})



// 控制登录按钮是否可用
const loginDisabled = ref(false)

// 用户名校验函数
const validateUsername = (rule: unknown, value: string, callback: (error?: string | Error) => void) => {
  if (value.trim().length === 0) {
    callback(new Error("请输入用户名"))
  } else if (value.indexOf("@") != -1) {
    callback(new Error("用户名中不需要输入@"))
  } else {
    callback()
  }
}

// 密码校验函数
const validatePassword = (_rule: unknown, value: string, callback: (error?: string | Error) => void) => {
  if (value.trim().length === 0) {
    callback(new Error("请输入密码"))
  } else {
    callback()
  }
}

// 表单校验规则
const rules = reactive<FormRules<typeof form>>({
  username: [{ validator: validateUsername, trigger: "blur" }],
  password: [{ validator: validatePassword, trigger: "blur" }]
})

// 登录
// 先请求登录，再请求个人信息
function onLogin() {
  userDataStore.clear()
  const request: LoginRequest = {
    username: form.username,
    password: form.password
  }
  loginDisabled.value = true;
  // 发送登录请求
  login(request).then(resp => {
    if (!checkSuccessful(resp)) {
      loginDisabled.value = false;
      showFailMessage("登录失败", getErrorMsg(resp))
      return;
    }
    // 获取用户信息，存入全局存储
    userInfo().then(resp => {
      if (!checkSuccessful(resp)) {
        loginDisabled.value = false;
        showFailMessage("获取用户信息失败：", getErrorMsg(resp))
        return;
      }
      userDataStore.set(resp.data.data)
      showSuccessfulMessage("登录成功")
      router.back() // 登录成功，返回上一页
    }).catch(e => {
      showFailMessage("获取用户信息失败", e);
      loginDisabled.value = false;
    })


  }).catch(e => {
    showFailMessage("登录失败", e)
    loginDisabled.value = false;
  })
}

</script>

<template>
  <div class="login">
    <span class="title">
      登录
    </span>
    <el-form :model="form" label-width="auto" size="large" :rules="rules">
      <el-form-item label="用户名" prop="username">
        <el-input placeholder="用户唯一名称" v-model="form.username">
          <template #prepend>@</template>
        </el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" placeholder="登录密码" v-model="form.password"></el-input>
      </el-form-item>
      <div class="login-button-container">
        <el-button type="primary" @click="onLogin()" native-type="submit" :disabled="loginDisabled"
          class="login-button">登录</el-button>
      </div>
    </el-form>

    <div class="tips">
      还不是EasyWeibo会员？<el-link @click="router.push('/register')">立即注册</el-link>
    </div>
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
  padding: 1rem 4rem 1rem 4rem;
}

.title {
  text-align: center;
  font-size: 2rem;
  margin-top: 0.5rem;
  margin-bottom: 1rem;
}

.login-button-container {
  width: 100%;
  text-align: center;
}

.login-button {
  width: 5rem;
}

.tips {
  color: var(--el-color-info);
}
</style>
