<script setup lang="ts">
import type { CommentDataItem } from '@/services/dto/postDto';

const props = defineProps<{
  comment: CommentDataItem
}>()
</script>

<template>
  <div class="comment-item">
    <div class="comment-header">

    </div>
    <div class="comment-body">
      {{ comment.content }}
    </div>
  </div>
</template>

<style lang="css" scoped>
.comment-item {
  display: flex;
  flex-direction: column;
}

.comment-header {}

.comment-body {}
</style>
