<script setup lang="ts">
import EditArea from '@/components/EditArea.vue';
import PostItem from '@/components/postItem/PostItem.vue';
import TopBar from '@/components/TopBar.vue';
import { getPosts, type ApiResponse, type Posts } from '@/services/api';
import { ref } from 'vue';

const posts = ref<ApiResponse<Posts> | null>(null)

// 从后端获取帖子列表
function fetchPosts() {
  getPosts().then(response => {
    posts.value = response.data
    console.log("fetched posts!")
  })
}

fetchPosts()
</script>

<template>
  <TopBar />
  <EditArea @new-post-submitted="fetchPosts()" class="post-edit-container" />
  <PostItem v-for="(item, index) in posts?.data.posts" :key="index" :nickname="item.nickname" :userid="item.userId"
    :content="item.content" class="post-item" />
</template>

<style lang="css" scoped>
.post-edit-container {
  margin-top: 2rem;
}

.post-item {
  margin-top: 2rem;
}
</style>
