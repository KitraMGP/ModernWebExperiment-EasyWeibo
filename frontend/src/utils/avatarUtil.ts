import type { CommentDataItem, PostDataItem } from '@/services/dto/postDto'
import defaultAvatarImage from '@/assets/default_avatar.jpg'
import { useUserDataStore } from '@/stores/userData'

/**
 * 根据 PostDataItem 获取用户的头像
 */
export function getAvatarUrlFromPost(postItem: PostDataItem): string {
  if (!postItem.hasAvatar) {
    return defaultAvatarImage
  } else {
    return 'http://127.0.0.1:9000/avatars/' + postItem.userId + '_avatar.jpg'
  }
}

/**
 * 根据 CommentDataItem 获取用户的头像
 */
export function getAvatarUrlFromComment(commentItem: CommentDataItem): string {
  if (!commentItem.hasAvatar) {
    return defaultAvatarImage
  } else {
    return 'http://127.0.0.1:9000/avatars/' + commentItem.userId + '_avatar.jpg'
  }
}

/**
 * 从 Pinia 存储中获取当前登录用户的头像
 */
export function getLoginUserAvatarUrl(): string {
  const userDataStore = useUserDataStore()
  if (!userDataStore.value || !userDataStore.value.hasAvatar) {
    return defaultAvatarImage
  } else {
    return 'http://127.0.0.1:9000/avatars/' + userDataStore.value.userId + '_avatar.jpg'
  }
}

export function getUserAvatarUrl(userId: number): string {
  return 'http://127.0.0.1:9000/avatars/' + userId + '_avatar.jpg'
}
