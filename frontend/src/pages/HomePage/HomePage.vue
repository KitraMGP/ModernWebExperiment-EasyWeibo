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

// 从前端的帖子列表中删除帖子
function deletePostFromList(postId: number) {
  if (!posts.value) return
  for (let i = 0; i < posts.value.length; i++) {
    if (posts.value[i].id === postId) {
      posts.value.splice(i, 1)
      break
    }
  }
}

// 删除评论时，将前端显示的评论数量减一
function onDeleteComment(postId: number) {
  if (!posts.value) return
  for (let i = 0; i < posts.value.length; i++) {
    if (posts.value[i].id === postId) {
      posts.value[i].comments--
      break
    }
  }
}

// 发送评论时，将前端显示的评论数量加一
function onNewComment(postId: number) {
  if (!posts.value) return
  for (let i = 0; i < posts.value.length; i++) {
    if (posts.value[i].id === postId) {
      posts.value[i].comments++
      break
    }
  }
}

// 重新加载评论时，将前端现实的评论数量更新
function onUpdateCommentCount(postId: number, count: number) {
  if (!posts.value) return
  for (let i = 0; i < posts.value.length; i++) {
    if (posts.value[i].id === postId) {
      posts.value[i].comments = count
      break
    }
  }
}

fetchPosts()

</script>

<template>
  <TopBar />
  <EditArea @new-post-submitted="fetchPosts()" class="post-edit-container" />
  <PostItem v-for="item in posts" :key="item.id" @delete-post="deletePostFromList" :post-data-item="item"
    class="margin-top" @delete-comment="onDeleteComment" @new-comment="onNewComment"
    @update-comment-count="onUpdateCommentCount" />
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
