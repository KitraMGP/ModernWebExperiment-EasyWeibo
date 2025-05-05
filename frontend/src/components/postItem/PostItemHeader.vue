<script setup lang="ts">
import type { PostDataItem } from '@/services/dto/postDto';
import SimpleAvatar from '../SimpleAvatar.vue';
import { getAvatarUrlFromPost } from '@/utils/avatarUtil';

const props = defineProps<{
  postItem: PostDataItem
}>()
</script>

<template>
  <div class="header">
    <SimpleAvatar size="normal" :avatarUrl="getAvatarUrlFromPost(props.postItem)" />
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
