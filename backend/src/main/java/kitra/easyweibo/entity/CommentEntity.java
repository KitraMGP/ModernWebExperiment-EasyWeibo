package kitra.easyweibo.entity;

import org.apache.ibatis.type.Alias;

@Alias("comment")
public class CommentEntity {
    private int id;
    private UserEntity user;
    private long time;
    private String content;

    public CommentEntity() {}

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
