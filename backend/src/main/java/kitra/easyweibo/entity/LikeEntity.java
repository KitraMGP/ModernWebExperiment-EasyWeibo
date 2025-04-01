package kitra.easyweibo.entity;

import org.apache.ibatis.type.Alias;

@Alias("like")
public class LikeEntity {
    private int id;
    private PostEntity post;
    private UserEntity user;

    public LikeEntity() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
