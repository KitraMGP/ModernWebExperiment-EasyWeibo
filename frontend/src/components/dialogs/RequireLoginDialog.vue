<script setup lang="ts">
import router from '@/router';
import { useRequireLoginDialogStore } from '@/stores/userData';

const hideDialog = () => {
  dialogStore.set(false)
}

function login() {
  hideDialog()
  router.push("/login")
}

const dialogStore = useRequireLoginDialogStore()
</script>

<template>
  <el-dialog v-model="dialogStore.requireLoginDialog" title="提示" :align-center="true" @close="hideDialog()">
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
