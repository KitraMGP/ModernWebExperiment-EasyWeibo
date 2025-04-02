package kitra.easyweibo.dao;

import kitra.easyweibo.entity.ImageEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ImageDao {
    @Insert("INSERT INTO image (fileName, user, time, isUsed) VALUES (#{fileName}, #{userId}, #{time}, #{isUsed})")
    int insertImage(ImageEntity image);

    @Select("SELECT fileName from image WHERE id=#{imageId}")
    String getImageFileName(int imageId);

    @Select("SELECT id from image WHERE fileName=#{fileName}")
    Integer getImageIdByFileName(String fileName);

    @Update("UPDATE image SET isUsed=#{isUsed} WHERE id=#{id}")
    int setIsUsed(int id, boolean isUsed);

    @Delete("DELETE FROM image WHERE id=#{imageId}")
    int deleteImage(int imageId);
}
