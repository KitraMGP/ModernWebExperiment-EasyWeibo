<script setup lang="ts">
import type { PostDataItem } from '@/services/dto/postDto';
import SimpleAvatar from '../SimpleAvatar.vue';
import { getAvatarUrlFromPost } from '@/utils/avatarUtil';
import { computed, ref } from 'vue';
import { getLoginUser, isLogin } from '@/utils/isLogin';
import { deletePost } from '@/services/postApi';
import { checkSuccessful, getErrorMsg, showFailMessage, showSuccessfulMessage } from '@/services/api';
import router from '@/router';

const props = defineProps<{
  postItem: PostDataItem
}>()
const emit = defineEmits(["delete-post"])
// 用来判断用户已登录，且帖子是自己发表的
const showDeleteButton = computed(() => isLogin() && (getLoginUser().role === 'admin' || getLoginUser().userId === props.postItem.userId))
// 用于控制确认删除帖子对话框的显示
const showDeleteDialog = ref(false)

// 删除帖子
function doDeletePost() {
  showDeleteDialog.value = false
  deletePost({
    postId: props.postItem.id
  }).then(resp => {
    if (!checkSuccessful(resp)) {
      showFailMessage("删除失败", getErrorMsg(resp))
      return
    }
    showSuccessfulMessage("帖子删除成功")
    emit("delete-post")
  }).catch(e => {
    showFailMessage("删除失败", e)
  })
}

// 处理用户点击帖子头像的行为。若用户存在，打开用户详情页面；否则不进行任何操作
function onAvatarClick() {
  if (props.postItem.userId !== null) {
    router.push('/user/' + props.postItem.username)
  }
}
</script>

<template>
  <div class="header">
    <div class="user-info">
      <div @click="onAvatarClick()" class="avatar" title="点击查看个人信息">
        <SimpleAvatar size="normal" :avatarUrl="getAvatarUrlFromPost(props.postItem)" />
      </div>
      <div class="user-info-text">
        <span class="nickname">{{ props.postItem.nickname }}</span>
        <span class="userid">{{ "@" + props.postItem.username }}</span>
      </div>
    </div>
    <div v-if="showDeleteButton" class="delete-post">
      <el-link href="javascript:void(0)" @click="showDeleteDialog = true">删除帖子</el-link>
    </div>
  </div>
  <el-dialog v-model="showDeleteDialog" title="删除帖子" :align-center="true">
    真的要删除这个帖子吗？此操作不可撤销，该帖子所有的评论也会被删除。<br><br>
    <el-button @click="showDeleteDialog = false">取消</el-button>
    <el-button type="danger" @click="doDeletePost">
      确认删除
    </el-button>
  </el-dialog>
</template>

<style lang="css" scoped>
.header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.header:hover {
  .delete-post {
    visibility: visible;
    opacity: 1;
  }
}

.user-info {
  display: flex;
  flex-direction: row;
  column-gap: 1rem;
}

.avatar {
  cursor: pointer;
}

.user-info-text {
  display: flex;
  flex-direction: column;
}

.nickname {
  font-weight: bold;
}

.userid {
  color: var(--el-color-info);
}

.delete-post {
  visibility: hidden;
  opacity: 0;
  transition: opacity 300ms;
}
</style>
