<script setup lang="ts">
import SimpleAvatar from '../SimpleAvatar.vue';
const props = defineProps<{
  nickname: string,
  userid: string
}>()
</script>

<template>
  <div class="header">
    <SimpleAvatar />
    <div class="user-info">
      <span class="nickname">{{ props.nickname }}</span>
      <span class="userid">{{ "@" + props.userid }}</span>
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
