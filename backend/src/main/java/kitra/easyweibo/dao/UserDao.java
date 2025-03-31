package kitra.easyweibo.dao;

import kitra.easyweibo.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserDao {
    @Select("SELECT * FROM user WHERE username = #{name}")
    UserEntity getUserByName(String name);

    @Select("SELECT * FROM user WHERE id = #{id}")
    UserEntity getUserById(int id);

    @Insert("INSERT INTO user (username, nickname, description, hasAvatar, password) VALUES (#{username}, #{nickname}, '', FALSE, #{password})")
    int insertUser(UserEntity user);

    @Update("UPDATE user SET username=#{username}, nickname=#{nickname}, description=#{description}, hasAvatar=#{hasAvatar}, password=#{password}")
    int updateUser(UserEntity user);
}
