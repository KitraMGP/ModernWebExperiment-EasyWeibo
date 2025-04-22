<script setup lang="ts">
import type { CommentDataItem } from '@/services/dto/postDto';
import { ref } from 'vue';
import PostItemCommentItem from './PostItemCommentItem.vue';

const commentList = ref<CommentDataItem[]>([{
  id: 0,
  userId: 0,
  username: "1",
  nickname: "nickname",
  hasAvatar: false,
  time: 1,
  content: "content!"
}, {
  id: 1,
  userId: 0,
  username: "1",
  nickname: "nickname",
  hasAvatar: false,
  time: 1,
  content: "content!"
}])
</script>

<template>
  <div class="comments">
    <span class="title">评论</span>
    <PostItemCommentItem v-for="item in commentList" :key="item.id" :comment="item" />
  </div>
</template>

<style lang="css" scoped>
.comments {
  display: flex;
  flex-direction: column;
  margin-top: 0.5rem;
  column-gap: 1rem;
}

/* 分割线 */
.comments::before {
  content: "";
  border-top: 1px solid var(--el-border-color-darker);
  width: 100%;
  align-self: center;
}

.title {
  margin-top: 0.5rem;
  font-size: 1rem;
  font-weight: 600;
}
</style>
