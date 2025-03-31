package kitra.easyweibo.entity;

import org.apache.ibatis.type.Alias;

@Alias("post_image")
public class PostImageEntity {
    private int imageId;
    private int postId;
    private int order;

    public PostImageEntity() {}

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
