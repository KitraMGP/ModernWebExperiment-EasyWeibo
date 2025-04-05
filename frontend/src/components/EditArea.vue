<script setup lang="ts">
import { checkSuccessful, getErrorMsg, showFailMessage, showSuccessfulMessage } from '@/services/api';
import { newPost } from '@/services/postApi';
import { isLogin } from '@/utils/isLogin';
import { requireLogin } from '@/utils/requireLogin';
import { ref } from 'vue';

const inputText = ref('')
// 控制发布帖子按钮是否可用
const submitDisabled = ref(false)
// 获取全局存储的userId
// 定义事件
const emit = defineEmits(["newPostSubmitted"])

// 发布帖子
function submit() {
  if (!isLogin()) {
    requireLogin()
    return
  }
  if (inputText.value.trim().length == 0) {
    showFailMessage("发布失败", "请输入帖子内容")
    return
  }
  submitDisabled.value = true
  newPost({
    content: inputText.value,
    images: []
  }).then(
    resp => {
      if (!checkSuccessful(resp)) {
        showFailMessage("发布失败", getErrorMsg(resp))
        return
      }
      // 触发自定义事件，在父组件刷新帖子列表
      emit("newPostSubmitted")
      inputText.value = ""
      showSuccessfulMessage("发布成功")
    }
  ).catch(e => showFailMessage("发布失败", e))
    .finally(() => submitDisabled.value = false)
}
</script>

<template>
  <div class="post-edit-area">
    <div class="edit-area-title">
      编辑帖子
    </div>
    <el-input v-model="inputText" maxlength="140" :rows="3" type="textarea" placeholder="请输入内容…" />
    <div class="edit-area-bottom">
      <span class="edit-area-wordscount">已输入{{ inputText.length }}字，您最多可以输入140字</span>
      <el-button type="primary" @click="submit" :disabled="submitDisabled">发表</el-button>
    </div>
  </div>
</template>

<style lang="css" scoped>
.post-edit-area {
  width: 620px;
  border: 1px solid var(--el-border-color-darker);
  border-radius: var(--el-border-radius-base);
  box-shadow: var(--el-box-shadow);
  padding: 1rem 1rem 0.5rem 1rem;
}

.edit-area-title {
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.edit-area-bottom {
  display: flex;
  justify-content: space-between;
  margin-top: 0.5rem;
}

.edit-area-wordscount {
  color: var(--el-color-info);
}
</style>
