<script setup lang="ts">
import router from '@/router';
import { ref } from 'vue';

const visible = ref(false)
// 父组件在此事件处理器中对组件进行卸载和销毁
const emit = defineEmits(["closed"])

const showDialog = () => {
  visible.value = true
}

const hideDialog = () => {
  visible.value = false
  emit("closed")
}

function login() {
  hideDialog()
  router.push("/login")
}

defineExpose({ showDialog, hideDialog })
</script>

<template>
  <el-dialog v-model="visible" title="提示" :align-center="true">
    <span>您需要登录才能使用相关功能，是否登录？</span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="hideDialog()">取消</el-button>
        <el-button type="primary" @click="login()">
          登录
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
