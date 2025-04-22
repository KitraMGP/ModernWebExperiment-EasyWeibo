<script setup lang="ts">
import SimpleAvatar from '@/components/SimpleAvatar.vue';
import TopBar from '@/components/TopBar.vue';
import { useUserDataStore } from '@/stores/userData';
import defaultAvatarImage from '@/assets/default_avatar.jpg'
import { reactive, ref } from 'vue';
import { changePassword, updateUserInfo, uploadAvatar } from '@/services/userApi';
import { checkSuccessful, getErrorMsg, showFailMessage, showSuccessfulMessage } from '@/services/api';
import type { UploadUserFile } from 'element-plus';
import router from '@/router';
import { getLoginUserAvatarUrl } from '@/utils/avatarUtil';
import { isLogin } from '@/utils/isLogin';

const userDataStore = useUserDataStore()
const avatarUrl = ref(defaultAvatarImage)
const saveButtonDisabled = ref(false)
const showChangePasswdDialog = ref(false)
const savePasswordButtonDisabled = ref(false)

// 各个输入表单定义

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

// 供头像上传控件暂存图片，用来显示图片上传列表
const uploadFileList = ref<UploadUserFile[]>([])

// 若未登录，要求用户登录
if (!isLogin()) {
  router.push("/login")
}
// 获取当前用户的头像
avatarUrl.value = getLoginUserAvatarUrl()

/**
 * 当用户在 input 中选择图片（onChange，onRemove）时调用，
 * 用于将相应的文件填入表单，并且执行其他处理逻辑
 */
function selectAvatarFile(imgFile: File | null) {
  if (imgFile == null) {
    uploadAvatarForm.image = null
    avatarUrl.value = getLoginUserAvatarUrl() // 重置到更改前的头像
  } else {
    // 检查文件类型是否合法
    if (!["image/jpeg", "image/gif", "image/png"].includes(imgFile.type)) {
      showFailMessage("只能选择jpg、png、gif格式的图片", "")
      uploadFileList.value = [] // 清除刚刚上传的图片
      return
    }
    // 成功，填入表单，生成临时 URL 来让用户预览头像更改
    uploadAvatarForm.image = imgFile
    const url = URL.createObjectURL(imgFile)
    avatarUrl.value = url
  }
}

/**
 * 点击“保存个人信息”按钮逻辑
*/
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

    // 更新其他用户信息
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
  } catch (e) {
    showFailMessage("发生错误", e)
  } finally {
    saveButtonDisabled.value = false
  }
}

// 修改密码功能相关函数

/**
 * 在修改密码对话框关闭的时候，清空用户填写密码的表单
 */
function clearPasswordForm() {
  changePasswdForm.oldPassword = ""
  changePasswdForm.newPassword = ""
}

/**
 * 修改密码处理逻辑
 */
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
          :on-change="(uploadFile) => selectAvatarFile(uploadFile?.raw ?? null)"
          :on-remove="() => selectAvatarFile(null)" accept="image/jpeg,image/gif,image/png">
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
