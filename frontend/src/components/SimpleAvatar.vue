<script setup lang="ts">
const props = defineProps<{
  avatarUrl: string
}>()
</script>

<template>
  <div class="avatar">
    <img :src="props.avatarUrl" width="100%" height="100%">
  </div>
</template>

<style lang="css" scoped>
.avatar {
  height: 3rem;
  width: 3rem;
  border: 1px solid var(--el-border-color-darker);
  border-radius: 50%;
  overflow: hidden;
}
</style>
