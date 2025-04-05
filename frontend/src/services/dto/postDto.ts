export interface PostDataItem {
  id: number
  username: string
  nickname: string
  hasAvatar: boolean
  time: number
  content: string
  images: string[]
  likes: number
  comments: number
  isLiked: boolean
}

export interface ListPostsResponse {
  posts: PostDataItem[]
}

export interface CommentDataItem {
  id: number
  username: string
  nickname: string
  hasAvatar: boolean
  time: number
  content: string
}

export interface ListCommentsResponse {
  comments: CommentDataItem[]
}

export interface NewPostRequest {
  content: string
  images: string[]
}

export interface NewPostResponse {
  id: number
}

export interface UpdatePostRequest {
  postId: number
  content: string
  images: string[]
}

export interface DeletePostRequest {
  postId: number
}

export interface UploadImageResponse {
  fileName: string
}

export interface LikeRequest {
  postId: number
  like: boolean
}

export interface CommentRequest {
  postId: number
  content: string
}

export interface CommentResponse {
  commentId: number
}

export interface DeleteCommentRequest {
  id: number
}
