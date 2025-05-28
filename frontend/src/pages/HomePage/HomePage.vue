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
// 加载状态
const isLoading = ref(false)
const isError = ref(false)
// 决定“重试”按钮获取最新帖子还是获取更多帖子
// 若为 true，则应调用 fetchMorePosts() 获取帖子
const firstPostsFetched = ref(false)
const lastIndex = ref(-1)
// 从后端获取最新帖子
function fetchPosts() {
  isLoading.value = true
  isError.value = false
  postList().then(resp => {
    if (!checkSuccessful(resp)) {
      showFailMessage("帖子加载失败", getErrorMsg(resp))
      isError.value = true
      return
    }
    firstPostsFetched.value = true
    posts.value = resp.data.data.posts
    // 若没有获取到任何帖子，不要再读取列表元素的id属性了
    if (posts.value.length !== 0) {
      lastIndex.value = posts.value[posts.value.length - 1].id
    } else {
      lastIndex.value = -1
    }
    noMorePosts.value = resp.data.data.noMorePosts
  }).catch(e => {
    showFailMessage("帖子加载失败", e)
    isError.value = true
  }).finally(() => isLoading.value = false)
}

// 获取更多帖子
function fetchMorePosts() {
  isLoading.value = true
  isError.value = false
  if (!posts.value) return
  postList(lastIndex.value).then(resp => {
    if (!checkSuccessful(resp)) {
      showFailMessage("帖子加载失败", getErrorMsg(resp))
      isError.value = true
      return
    }
    noMorePosts.value = resp.data.data.noMorePosts
    posts.value = posts.value!.concat(resp.data.data.posts)
    // 若帖子列表为空，不要读取列表元素的id
    if (posts.value.length !== 0) {
      lastIndex.value = posts.value[posts.value.length - 1].id
    } else {
      // 这是一种异常情况
      showFailMessage("发生错误，请刷新页面重试", new Error())
    }
  }).catch(e => {
    showFailMessage("帖子加载失败", e)
    isError.value = true
  }).finally(() => {
    isLoading.value = false
  })
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

// 加载失败的重试功能
function retryFetchPosts() {
  if (firstPostsFetched.value) {
    fetchMorePosts()
  } else {
    fetchPosts()
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
  <el-button v-if="!noMorePosts && !isLoading && firstPostsFetched && !isError" type="primary" @click="fetchMorePosts()"
    class="margin-top">加载更多</el-button>
  <span v-if="isLoading" class="margin-top">加载中…</span>
  <span v-if="isError" class="margin-top">加载失败，<el-link href="javascript:void(0)"
      @click="retryFetchPosts()">点击重试</el-link></span>
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
