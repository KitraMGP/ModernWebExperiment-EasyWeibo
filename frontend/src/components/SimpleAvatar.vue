<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  avatarUrl: string,
  largeAvatar?: boolean
}>()

const avatarClass = computed(() => ({
  normal: !props.largeAvatar,
  large: props.largeAvatar
}))

</script>

<template>
  <div class="avatar" :class="avatarClass">
    <img :src="props.avatarUrl" width="100%" height="100%">
  </div>
</template>

<style lang="css" scoped>
.avatar {
  border: 1px solid var(--el-border-color-darker);
  border-radius: 50%;
  overflow: hidden;
}

.normal {
  height: 3rem;
  width: 3rem;
}

.large {
  height: 6rem;
  width: 6rem;
}
</style>
