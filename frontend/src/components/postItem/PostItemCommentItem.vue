<script setup lang="ts">
import { checkSuccessful, getErrorMsg, showFailMessage, showSuccessfulMessage } from '@/services/api';
import type { CommentDataItem } from '@/services/dto/postDto';
import { deleteComment } from '@/services/postApi';
import { getAvatarUrlFromComment } from '@/utils/avatarUtil';
import { getLoginUser, isLogin } from '@/utils/isLogin';
import dayjs from 'dayjs';
import { computed, ref } from 'vue';

const props = defineProps<{
  comment: CommentDataItem,
  postAuthorId: number | null
}>()
const emit = defineEmits(["delete-comment"])
// 控制是否显示删除评论按钮
// 显示条件：用户已登录，且是自己的评论；用户已登录，且是自己的帖子下的评论
const showDeleteButton = computed(() => isLogin() && (getLoginUser().role === 'admin' || getLoginUser().userId === props.comment.userId || getLoginUser().userId === props.postAuthorId))
// 控制是否显示确认删除对话框
const showDeleteDialog = ref(false)
// 删除评论
function doDeleteComment() {
  showDeleteDialog.value = false
  deleteComment({
    id: props.comment.id
  }).then(resp => {
    if (!checkSuccessful(resp)) {
      showFailMessage("删除失败", getErrorMsg(resp))
      return
    }
    showSuccessfulMessage("删除成功")
    emit("delete-comment", props.comment.id)
  }).catch(e => {
    showFailMessage("删除失败", e)
  })
}
</script>

<template>
  <div class="comment-item">
    <div class="comment-header">
      <div class="user-info">
        <SimpleAvatar size="small" :avatarUrl="getAvatarUrlFromComment(props.comment)" />
        <div class="user-info-text">
          <span class="nickname">{{ props.comment.nickname }}</span>
          <span class="userid">{{ "@" + props.comment.username }}</span>
          <span class="date">{{ dayjs.unix(props.comment.time).format("lll") }}</span>
        </div>
      </div>
      <el-link class="delete-comment" v-if="showDeleteButton" @click="showDeleteDialog = true">删除评论</el-link>
    </div>
    <div class="comment-body">
      {{ props.comment.content }}
    </div>
  </div>
  <el-dialog v-model="showDeleteDialog" title="删除评论" :align-center="true">
    真的要删除这个评论吗？此操作不可撤销。<br><br>
    <el-button @click="showDeleteDialog = false">取消</el-button>
    <el-button type="danger" @click="doDeleteComment">
      确认删除
    </el-button>
  </el-dialog>
</template>

<style lang="css" scoped>
.comment-item {
  display: flex;
  flex-direction: column;
}

.comment-item:hover {
  .delete-comment {
    visibility: visible;
    opacity: 1;
  }
}

.comment-header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.user-info {
  display: flex;
  flex-direction: row;
  column-gap: 1rem;
  align-items: center;
}

.user-info-text {
  display: flex;
  flex-direction: row;
  column-gap: 0.4rem;
}

.nickname {
  font-weight: bold;
}

.userid {
  color: var(--el-color-info);
}

.date {
  color: var(--el-color-info);
}

.delete-comment {
  visibility: hidden;
  opacity: 0;
  transition: opacity 300ms;
}
</style>
