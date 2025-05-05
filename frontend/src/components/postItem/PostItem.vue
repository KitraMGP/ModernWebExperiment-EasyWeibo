<script setup lang="ts">
import type { PostDataItem } from '@/services/dto/postDto';
import PostItemHeader from './PostItemHeader.vue';
import dayjs from 'dayjs';
import PostItemFooter from './PostItemFooter.vue';
import { ref } from 'vue';
import PostItemComments from './PostItemComments.vue';
const props = defineProps<{
  postDataItem: PostDataItem
}>()
const emit = defineEmits(["delete-post", "delete-comment", "new-comment", "update-comment-count"])

const showComments = ref(false)

function onDeleteComment() {
  emit("delete-comment", props.postDataItem.id)
}

function onNewComment() {
  emit("new-comment", props.postDataItem.id)
}

function onUpdateCommentCount(count: number) {
  emit("update-comment-count", props.postDataItem.id, count)
}
</script>

<template>
  <div class="post-item">
    <PostItemHeader :post-item="props.postDataItem" @delete-post="emit('delete-post', props.postDataItem.id)" />
    <div class="content">{{ props.postDataItem.content }}</div>
    <div class="time">发布时间：{{ dayjs.unix(props.postDataItem.time).format("lll") }}</div>
    <PostItemFooter :post-item="postDataItem" @switch-show-comments="showComments = !showComments" />
    <PostItemComments :post-id="postDataItem.id" :post-author-id="postDataItem.userId" v-if="showComments"
      @delete-comment="onDeleteComment" @new-comment="onNewComment" @update-comment-count="onUpdateCommentCount" />
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
  padding: 1rem 1rem 0.5rem 1rem;
}

.content {
  margin-top: 0.5rem;
  /* 设置 pre-wrap 之后，帖子内容中的换行符就会被正确解析为换行 */
  white-space: pre-wrap;
}

.time {
  margin-top: 0.5rem;
  color: var(--el-color-info);
}
</style>
