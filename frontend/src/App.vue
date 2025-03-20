<script setup lang="ts">
import EditArea from './components/EditArea.vue';
import TopBar from './components/TopBar.vue';

</script>

<template>
  <TopBar />
  <div id="post-edit-container">
    <EditArea />
  </div>

</template>

<style lang="css" scoped>
#post-edit-container {
  margin-top: 2rem;
}
</style>
