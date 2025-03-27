package kitra.easyweibo.dao;

import kitra.easyweibo.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserDao {
    @Select("SELECT * FROM user WHERE name = #{name}")
    UserEntity getUser(String name);

    @Insert("INSERT INTO user (name, nickname, password) VALUES (#{name}, #{nickname}, #{password})")
    int insertUser(UserEntity user);
}
