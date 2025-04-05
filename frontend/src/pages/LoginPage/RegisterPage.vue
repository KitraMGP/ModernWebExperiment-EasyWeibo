<script setup lang="ts">
import RegisterComponent from '@/components/account/RegisterComponent.vue';
import TopBar from '@/components/TopBar.vue';

</script>

<template>
  <TopBar />
  <RegisterComponent class="register" />
</template>

<style lang="css" scoped>
.register {
  margin-top: 2rem;
}
</style>
