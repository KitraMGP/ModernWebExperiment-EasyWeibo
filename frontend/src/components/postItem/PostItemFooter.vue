<script setup lang="ts">
import { checkSuccessful, getErrorMsg, showFailMessage } from '@/services/api';
import type { PostDataItem } from '@/services/dto/postDto';
import { likePost } from '@/services/postApi';
import { isLogin } from '@/utils/isLogin';
import { requireLogin } from '@/utils/requireLogin';
import { ref } from 'vue';

const props = defineProps<{
  postItem: PostDataItem
}>()

const emit = defineEmits(["switchShowComments"])

// 使用响应式变量，使其可以修改并且同步更新页面
// 因为不能直接修改props
const isLiked = ref(props.postItem.isLiked)
const likesCount = ref(props.postItem.likes)

function changeLike(like: boolean) {
  if (!isLogin()) {
    requireLogin()
    return
  }
  likePost({ postId: props.postItem.id, like: like })
    .then(resp => {
      if (!checkSuccessful(resp)) {
        showFailMessage("点赞操作失败", getErrorMsg(resp))
        return
      }
      isLiked.value = like
      likesCount.value += like ? 1 : -1
    }).catch(e => showFailMessage("点赞操作失败", e))
}
</script>

<template>
  <div class="post-footer">
    <div class="post-footer-item">
      <i v-if="isLiked" @click="changeLike(false)" class="iconfont icon-like-fill"></i>
      <i v-if="!isLiked" @click="changeLike(true)" class="iconfont icon-like"></i>
      {{ likesCount }}
    </div>
    <div class="post-footer-item"><i class="iconfont icon-comment" @click="emit('switchShowComments')"></i>{{
      props.postItem.comments }}</div>
  </div>
</template>

<style lang="css" scoped>
.post-footer {
  display: flex;
  justify-content: space-around;
  margin-top: 0.5rem;
}

.iconfont {
  color: var(--el-color-info);
  margin-right: 0.5rem;
  cursor: pointer;
}
</style>
