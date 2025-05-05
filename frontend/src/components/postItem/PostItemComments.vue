<script setup lang="ts">
import type { CommentDataItem } from '@/services/dto/postDto';
import { computed, ref } from 'vue';
import PostItemCommentItem from './PostItemCommentItem.vue';
import { commentList, newComment } from '@/services/postApi';
import { checkSuccessful, getErrorMsg, showFailMessage, showSuccessfulMessage } from '@/services/api';
import { isLogin } from '@/utils/isLogin';
import { requireLogin } from '@/utils/requireLogin';

const props = defineProps<{
  postId: number,
  postAuthorId: number | null
}>()
const emit = defineEmits(["delete-comment", "new-comment", "update-comment-count"])

// 用于控制“加载中”和“加载失败”状态
const isLoading = ref(true)
const isFailed = ref(false)
// 评论输入框内容
const commentInput = ref("")
// 控制评论按钮禁用
const commentDisabled = ref(false)
// 判断评论是否为空
const isCommentEmpty = computed(() => commentInput.value.trim().length == 0)

const comments = ref<CommentDataItem[]>([])

// 发送评论
function sendComment() {
  if (!isLogin()) {
    requireLogin()
    return
  }
  commentDisabled.value = true
  newComment({
    postId: props.postId,
    content: commentInput.value
  }).then(resp => {
    // 检查错误码
    if (!checkSuccessful(resp)) {
      showFailMessage("评论发送失败", getErrorMsg(resp))
      return
    }
    // 发送成功，清空输入框，重新获取评论
    commentInput.value = ""
    showSuccessfulMessage("评论发送成功")
    emit("new-comment")
    fetchComments()
  }).catch(e => {
    showFailMessage("评论发送失败", e)
  }).finally(() => {
    commentDisabled.value = false
  })
}

// 拉取评论列表
function fetchComments() {
  isFailed.value = false
  isLoading.value = true
  commentList(props.postId).then(resp => {
    // 检查返回数据是否有错误码
    if (!checkSuccessful(resp)) {
      isFailed.value = true
      showFailMessage("评论加载失败", getErrorMsg(resp))
      return
    }
    // 评论加载成功
    comments.value = resp.data.data.comments
    emit("update-comment-count", comments.value.length)
  }).catch(e => {
    // 处理 AxiosError
    isFailed.value = true
    showFailMessage("评论加载失败", e)
  }).finally(() => {
    isLoading.value = false
  })
}

// 从前端的列表中删除评论，并且更新显示的评论数量
function deleteCommentFromList(id: number) {
  for (let i = 0; i < comments.value.length; i++) {
    if (comments.value[i].id === id) {
      comments.value.splice(i, 1)
      emit("delete-comment")
      break
    }
  }
}

fetchComments()
</script>

<template>
  <div class="comments">
    <span class="title">评论</span>
    <div class="center">
      <span v-if="isLoading">加载中</span>
      <span v-if="isFailed">加载失败！<el-link href="javascript:void(0)" @click="fetchComments()">点击重试</el-link></span>
      <span v-if="!isLoading && !isFailed && comments.length == 0">没有评论</span>
    </div>
    <div v-if="!isLoading && !isFailed">
      <PostItemCommentItem v-for="item in comments" :key="item.id" :comment="item" :post-author-id="props.postAuthorId"
        @delete-comment="deleteCommentFromList" />
    </div>
    <div class="comment-input">
      <el-input v-model="commentInput" placeholder="请输入评论内容" :maxlength="140"></el-input>
      <el-button class="comment-button" :disabled="commentDisabled || isCommentEmpty"
        @click="sendComment()">发送评论</el-button>
    </div>
  </div>
</template>

<style lang="css" scoped>
.comments {
  display: flex;
  flex-direction: column;
  margin-top: 0.5rem;
  column-gap: 1rem;
}

/* 分割线 */
.comments::before {
  content: "";
  border-top: 1px solid var(--el-border-color-darker);
  width: 100%;
  align-self: center;
}

.title {
  margin-top: 0.5rem;
  font-size: 1rem;
  font-weight: 600;
}

.center {
  text-align: center;
}

.comment-button {
  width: min-content;
}

.comment-input {
  display: flex;
  flex-direction: row;
  margin-top: 1rem;
}
</style>
