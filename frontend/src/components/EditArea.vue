<script setup lang="ts">
import { submitPost } from '@/services/api';
import { useUserIdStore } from '@/stores/userData';
import { ref } from 'vue';

const inputText = ref('')
// 控制发布帖子按钮是否可用
const submitDisabled = ref(false)
// 获取全局存储的userId
const userIdStore = useUserIdStore()
// 定义事件
const emit = defineEmits(["newPostSubmitted"])

// 发布帖子
function submit() {
  submitDisabled.value = true
  submitPost({
    userId: userIdStore.value,
    content: inputText.value
  }).then(
    (response) => {
      console.log("postId: " + response.data.data.postId)
      ElNotification({
        type: "success",
        title: "发布成功"
      })
      // 触发自定义事件，在父组件刷新帖子列表
      emit("newPostSubmitted")
    },
    (reason) => {
      console.log(reason)
      ElNotification({
        type: "error",
        title: "发布失败",
        message: reason
      })
    }
  ).finally(() => submitDisabled.value = false)
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
