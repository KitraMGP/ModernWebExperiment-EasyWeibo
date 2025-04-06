<script setup lang="ts">
import type { PostDataItem } from '@/services/dto/postDto';
import SimpleAvatar from '../SimpleAvatar.vue';
import defaultAvatarImage from "@/assets/default_avatar.jpg"
const props = defineProps<{
  postItem: PostDataItem
}>()

function getAvatarUrl(): string {
  if (!props.postItem.hasAvatar) {
    return defaultAvatarImage
  } else {
    return "http://127.0.0.1:9000/avatars/" + props.postItem.userId + "_avatar.jpg"
  }
}
</script>

<template>
  <div class="header">
    <SimpleAvatar :avatarUrl="getAvatarUrl()" />
    <div class="user-info">
      <span class="nickname">{{ props.postItem.nickname }}</span>
      <span class="userid">{{ "@" + props.postItem.username }}</span>
    </div>
  </div>
</template>

<style lang="css" scoped>
.header {
  display: flex;
  flex-direction: row;
  column-gap: 1rem;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.nickname {
  font-weight: bold;
}

.userid {
  color: var(--el-color-info);
}
</style>
