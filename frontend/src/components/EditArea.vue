<script setup lang="ts">
import { ref } from 'vue';

const inputText = ref('')
</script>

<template>
  <div class="post-edit-area">
    <div class="edit-area-title">
      编辑帖子
    </div>
    <el-input v-model="inputText" maxlength="140" :rows="3" type="textarea" placeholder="请输入内容…" />
    <div class="edit-area-bottom">
      <span class="edit-area-wordscount">已输入{{ inputText.length }}字，您最多可以输入140字</span>
      <el-button type="primary">发表</el-button>
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
