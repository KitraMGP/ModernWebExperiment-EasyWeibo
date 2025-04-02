package kitra.easyweibo.entity;

import org.apache.ibatis.type.Alias;

@Alias("post_image")
public class PostImageEntity {
    private ImageEntity image;
    private int postId;
    private int order;

    public PostImageEntity() {}

    public ImageEntity getImage() {
        return image;
    }

    public void setImage(ImageEntity image) {
        this.image = image;
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
