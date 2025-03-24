<script setup lang="ts">
import LoginComponent from '@/components/account/LoginComponent.vue';
import TopBar from '@/components/TopBar.vue';

</script>

<template>
  <TopBar />
  <LoginComponent class="login" />
</template>

<style lang="css" scoped>
.login {
  margin-top: 2rem;
}
</style>
