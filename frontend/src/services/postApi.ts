import type { AxiosResponse } from 'axios'
import { api } from './api'
import type { ApiResponse } from './dto/commonDto'
import type {
  ListPostsResponse,
  ListCommentsResponse,
  NewPostRequest,
  NewPostResponse,
  UpdatePostRequest,
  DeletePostRequest,
  UploadImageResponse,
  LikeRequest,
  CommentRequest,
  CommentResponse,
  DeleteCommentRequest,
} from './dto/postDto'

export const postList = (lastId?: number) =>
  api.get<ApiResponse<ListPostsResponse>>('/post/list', { params: { lastId } })

export const commentList = (post: number) =>
  api.get<ApiResponse<ListCommentsResponse>>('/post/comments', { params: { post } })

export const newPost = (r: NewPostRequest) =>
  api.post<NewPostRequest, AxiosResponse<ApiResponse<NewPostResponse>>>('/post/new', r)

export const updatePost = (r: UpdatePostRequest) =>
  api.post<UpdatePostRequest, AxiosResponse<ApiResponse<null>>>('/post/update', r)

export const deletePost = (r: DeletePostRequest) =>
  api.post<DeletePostRequest, AxiosResponse<ApiResponse<null>>>('/post/delete', r)

export const uploadImg = (r: FormData) =>
  api.postForm<AxiosResponse<ApiResponse<UploadImageResponse>>>('/post/uploadImg', r)

export const likePost = (r: LikeRequest) =>
  api.post<LikeRequest, AxiosResponse<ApiResponse<null>>>('/post/like', r)

export const newComment = (r: CommentRequest) =>
  api.post<CommentRequest, AxiosResponse<ApiResponse<CommentResponse>>>('/post/comment', r)

export const deleteComment = (r: DeleteCommentRequest) =>
  api.post<DeleteCommentRequest, AxiosResponse<ApiResponse<null>>>('/post/deleteComment', r)
