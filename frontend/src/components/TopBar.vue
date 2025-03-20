<script setup lang="ts">
import { isDark, toggleDark } from '@/main';
</script>

<template>
  <div id="topbar">
    <el-link :underline="false" class="topbar-item">注册</el-link>
    <el-link :underline="false" class="topbar-item">登录</el-link>
    <el-link :underline="false" class="topbar-item" @click="toggleDark()">{{ isDark ? "暗色模式" : "亮色模式" }}</el-link>
  </div>
</template>

<style lang="css" scoped>
#topbar {
  display: flex;
  width: 100%;
  padding: 0 1rem 1rem 0;
  justify-content: end;
  border-bottom: 1px solid;
}

.topbar-item {
  margin-left: 1rem;
}
</style>
