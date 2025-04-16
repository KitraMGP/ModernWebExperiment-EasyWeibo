<script setup lang="ts">
import SimpleAvatar from '@/components/SimpleAvatar.vue';
import TopBar from '@/components/TopBar.vue';
import { useUserDataStore } from '@/stores/userData';
import defaultAvatarImage from '@/assets/default_avatar.jpg'
import { reactive, ref, watch } from 'vue';
import { changePassword, updateUserInfo, uploadAvatar } from '@/services/userApi';
import { checkSuccessful, getErrorMsg, showFailMessage, showSuccessfulMessage } from '@/services/api';
import type { UploadUserFile } from 'element-plus';
import axios from 'axios';
import router from '@/router';

const userDataStore = useUserDataStore()
const avatarUrl = ref(defaultAvatarImage)
const saveButtonDisabled = ref(false)
const showChangePasswdDialog = ref(false)
const savePasswordButtonDisabled = ref(false)

const form = reactive({
  username: userDataStore.value?.username,
  nickname: userDataStore.value?.nickname,
  description: userDataStore.value?.description
})

const changePasswdForm = reactive({
  oldPassword: "",
  newPassword: ""
})

const uploadAvatarForm = reactive({
  image: null as (File | null)  // 改为使用原生File类型
})

const uploadFileList = ref<UploadUserFile[]>([])

watch(userDataStore, (newValue) => {
  // 若未登录，要求用户登录
  if (newValue.value == null) {
    router.push("/login")
  }
  getAvatarUrl()
  form.username = newValue.value?.username
  form.nickname = newValue.value?.nickname
  form.description = newValue.value?.description
})

// 若未登录，要求用户登录
if (userDataStore.value == null) {
  router.push("/login")
}

function getAvatarUrl() {
  if (!userDataStore.value || !userDataStore.value.hasAvatar) {
    avatarUrl.value = defaultAvatarImage
  } else {
    avatarUrl.value = "http://127.0.0.1:9000/avatars/" + userDataStore.value.userId + "_avatar.jpg"
  }
}

async function saveUserData() {
  saveButtonDisabled.value = true;
  try {
    // 先上传头像
    if (uploadAvatarForm.image) {
      const formData = new FormData();
      formData.append('image', uploadAvatarForm.image); // 注意使用接口要求的字段名

      const uploadResp = await uploadAvatar(formData);
      if (!checkSuccessful(uploadResp)) {
        showFailMessage("头像上传失败", getErrorMsg(uploadResp));
        return;
      }
      showSuccessfulMessage("头像上传成功");
    }

    // 更新用户信息
    const updateResp = await updateUserInfo({
      username: form.username || "",
      nickname: form.nickname || "",
      description: form.description || ""
    });

    if (!checkSuccessful(updateResp)) {
      showFailMessage("信息更新失败", getErrorMsg(updateResp));
      return;
    }

    showSuccessfulMessage("个人信息保存成功");
    // 清空上传状态
    uploadFileList.value = [];
    uploadAvatarForm.image = null;
    router.go(0)
  } catch (e) {
    // 类型安全处理
    if (axios.isAxiosError(e)) {
      showFailMessage("操作失败", e)
    } else if (typeof e === 'string') {
      showFailMessage("操作失败", e)
    } else {
      showFailMessage("操作失败", e instanceof Error ? e.message : '未知错误')
    }
  } finally {
    saveButtonDisabled.value = false
  }
}

function clearPasswordForm() {
  changePasswdForm.oldPassword = ""
  changePasswdForm.newPassword = ""
}

function changePasswd() {
  savePasswordButtonDisabled.value = true
  changePassword(changePasswdForm).then(resp => {
    if (!checkSuccessful(resp)) {
      showFailMessage("修改密码失败", getErrorMsg(resp))
      return
    }
    showSuccessfulMessage("修改密码成功")
    showChangePasswdDialog.value = false
    clearPasswordForm()
  }).catch(e => showFailMessage("修改密码失败", e))
    .finally(() => {
      savePasswordButtonDisabled.value = false
    })
}
</script>

<template>
  <TopBar :show-back-link="true" />

  <SimpleAvatar class="avatar" :avatar-url="avatarUrl" :large-avatar="true" />

  <span class="big-text">个人信息</span>
  <div class="user-info-area">
    <el-form label-width="auto" size="large">
      <el-form-item label="用户名" prop="username">
        <el-input placeholder="用户唯一名称" v-model="form.username">
          <template #prepend>@</template>
        </el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input placeholder="用户昵称" v-model="form.nickname"></el-input>
      </el-form-item>
      <el-form-item label="个人描述" prop="description">
        <el-input placeholder="个人描述" v-model="form.description"></el-input>
      </el-form-item>
      <el-form-item label="头像">
        <el-upload ref="upload" :limit="1" :auto-upload="false" v-model:file-list="uploadFileList"
          :on-change="(uploadFile) => uploadAvatarForm.image = uploadFile?.raw ?? null"
          :on-remove="() => uploadAvatarForm.image = null" accept="image/jpeg,image/gif,image/png">
          <template #trigger>
            <el-button>选择头像文件</el-button>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item label="密码">
        <el-button @click="showChangePasswdDialog = true; savePasswordButtonDisabled = false;">更改密码</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :disabled="saveButtonDisabled" @click="saveUserData()">保存个人信息</el-button>
      </el-form-item>
    </el-form>
  </div>
  <el-dialog v-model="showChangePasswdDialog" title="更改密码" width="300" :align-center="true">
    <el-form v-model="changePasswdForm">
      <el-form-item label="原密码">
        <el-input placeholder="输入原密码..." type="password" v-model="changePasswdForm.oldPassword"></el-input>
      </el-form-item>
      <el-form-item label="新密码">
        <el-input placeholder="输入新密码..." type="password" v-model="changePasswdForm.newPassword"></el-input>
      </el-form-item>
      <div class="password-form-buttons">
        <el-button @click="showChangePasswdDialog = false">取消</el-button>
        <el-button type="primary" native-type="submit" @click="changePasswd()"
          :disabled="savePasswordButtonDisabled">更改密码</el-button>
      </div>
    </el-form>
  </el-dialog>
</template>

<style lang="css" scoped>
.user-info-container {
  margin-top: 2rem;
  text-align: center;
}

.avatar {
  margin-top: 2rem;
}

.user-info-area {
  margin-top: 2rem;
}

.big-text {
  font-size: 1.5rem;
}

.password-form-buttons {
  width: 100%;
  text-align: right;
}

.button-gap {
  margin-left: 1em;
}
</style>
