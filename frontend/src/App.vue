<script setup lang="ts">
import RequireLoginDialog from './components/dialogs/RequireLoginDialog.vue';
</script>

<template>
  <RouterView />
  <RequireLoginDialog />
</template>
