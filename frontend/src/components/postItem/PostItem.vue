<script setup lang="ts">
import PostItemHeader from './PostItemHeader.vue';
const props = defineProps<{
  nickname: string,
  userid: string,
  content: string
}>()
</script>

<template>
  <div class="post-item">
    <PostItemHeader :nickname="props.nickname" :userid="props.userid" />
    <div class="content">{{ props.content }}</div>
  </div>
</template>

<style lang="css" scoped>
.post-item {
  display: flex;
  flex-direction: column;
  width: 620px;
  border: 1px solid var(--el-border-color-darker);
  border-radius: var(--el-border-radius-base);
  box-shadow: var(--el-box-shadow);
  padding: 1rem 1rem 1rem 1rem;
}

.content {
  margin-top: 0.5rem;
}
</style>
