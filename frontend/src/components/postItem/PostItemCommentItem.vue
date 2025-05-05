<script setup lang="ts">
import type { CommentDataItem } from '@/services/dto/postDto';
import { getAvatarUrlFromComment } from '@/utils/avatarUtil';

const props = defineProps<{
  comment: CommentDataItem
}>()
</script>

<template>
  <div class="comment-item">
    <div class="comment-header">
      <SimpleAvatar size="small" :avatarUrl="getAvatarUrlFromComment(props.comment)" />
      <div class="user-info">
        <span class="nickname">{{ props.comment.nickname }}</span>
        <span class="userid">{{ "@" + props.comment.username }}</span>
      </div>
    </div>
    <div class="comment-body">
      {{ props.comment.content }}
    </div>
  </div>
</template>

<style lang="css" scoped>
.comment-item {
  display: flex;
  flex-direction: column;
}

.comment-header {
  display: flex;
  flex-direction: row;
  column-gap: 1rem;
  align-items: center;
}

.user-info {
  display: flex;
  flex-direction: row;
  column-gap: 0.4rem;
}

.nickname {
  font-weight: bold;
}

.userid {
  color: var(--el-color-info);
}

.comment-body {}
</style>
