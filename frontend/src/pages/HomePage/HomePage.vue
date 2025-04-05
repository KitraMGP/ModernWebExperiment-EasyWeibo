<script setup lang="ts">
import EditArea from '@/components/EditArea.vue';
import PostItem from '@/components/postItem/PostItem.vue';
import TopBar from '@/components/TopBar.vue';
import { checkSuccessful, getErrorMsg, showFailMessage } from '@/services/api';
import type { PostDataItem } from '@/services/dto/postDto';

import { postList } from '@/services/postApi';
import { ref } from 'vue';

const posts = ref<PostDataItem[] | null>(null)
// 用来指定是否还有帖子可以加载，用来决定“加载更多”按钮是否显示
const noMorePosts = ref(false)
// 决定“加载更多”按钮是否禁用
const morePostsButtonDisabled = ref(false)

// 从后端获取帖子列表
function fetchPosts() {
  postList().then(resp => {
    if (!checkSuccessful(resp)) {
      showFailMessage("帖子加载失败", getErrorMsg(resp))
      return
    }
    posts.value = resp.data.data.posts

  }).catch(e => showFailMessage("帖子加载失败", e))
}

// 获取更多帖子
function fetchMorePosts() {
  if (!posts.value) return
  const lastIndex = posts.value[posts.value.length - 1].id
  morePostsButtonDisabled.value = true
  postList(lastIndex).then(resp => {
    if (!checkSuccessful(resp)) {
      showFailMessage("帖子加载失败", getErrorMsg(resp))
      return
    }
    if (resp.data.data.posts.length === 0) {
      noMorePosts.value = true
      return
    }
    posts.value = posts.value!.concat(resp.data.data.posts)

  }).catch(e => showFailMessage("帖子加载失败", e)).finally(() => morePostsButtonDisabled.value = false)
}

fetchPosts()

</script>

<template>
  <TopBar />
  <EditArea @new-post-submitted="fetchPosts()" class="post-edit-container" />
  <PostItem v-for="item in posts" :key="item.id" :post-data-item="item" class="margin-top" />
  <el-button v-if="!noMorePosts" type="primary" @click="fetchMorePosts()" :disabled="morePostsButtonDisabled"
    class="margin-top">加载更多</el-button>
  <span v-if="noMorePosts" class="margin-top no-more-posts-tip">我也是有底线的</span>
</template>

<style lang="css" scoped>
.post-edit-container {
  margin-top: 2rem;
}

.margin-top {
  margin-top: 2rem;
}

.no-more-posts-tip {
  color: var(--el-color-info);
}
</style>
