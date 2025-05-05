<script setup lang="ts">
import { computed } from 'vue';

type Size = "small" | "normal" | "large"

// 通过指定 Size 来设定头像的显示大小
const props = defineProps<{
  avatarUrl: string,
  size: Size
}>()

const avatarClass = computed(() => ({
  small: props.size == "small",
  normal: props.size == "normal",
  large: props.size == "large"
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

.small {
  height: 2rem;
  width: 2rem;
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
