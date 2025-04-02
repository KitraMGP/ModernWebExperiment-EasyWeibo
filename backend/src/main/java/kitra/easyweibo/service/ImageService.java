package kitra.easyweibo.service;

import kitra.easyweibo.dao.ImageDao;
import kitra.easyweibo.dao.PostImageDao;
import kitra.easyweibo.dao.UserDao;
import kitra.easyweibo.entity.ImageEntity;
import kitra.easyweibo.entity.PostImageEntity;
import kitra.easyweibo.entity.UserEntity;
import kitra.easyweibo.exception.ImageNotFoundException;
import kitra.easyweibo.exception.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static kitra.easyweibo.util.DataBaseUtil.checkResult;

@Service
@Transactional
public class ImageService {
    private final UserDao userDao;
    private final ImageDao imageDao;
    private final PostImageDao postImageDao;

    private static final Logger logger = LogManager.getLogger(ImageService.class);

    public ImageService(UserDao userDao, ImageDao imageDao, PostImageDao postImageDao) {
        this.userDao = userDao;
        this.imageDao = imageDao;
        this.postImageDao = postImageDao;
    }

    /**
     * 接收并保存用户上传的头像，这个方法不会检查文件类型和大小，你应该在Controller中进行检查
     */
    public void saveAvatarImage(int userId, MultipartFile file) throws IOException {
        UserEntity userEntity = userDao.getUserById(userId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        userEntity.setHasAvatar(true);
        // 保存图片
        Path path = Paths.get("./avatarUploads/" + userId + "_avatar" + ".jpg");
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        // 更新数据库
        checkResult(userDao.updateUser(userEntity));
    }

    /**
     * 接收并保存用户上传的帖子图片，这个方法不会检查文件类型和大小，你应该在Controller中进行检查
     * @return 上传的图片文件名
     */
    public String savePostImage(int userId, MultipartFile file) throws IOException {
        UserEntity userEntity = userDao.getUserById(userId);
        if (userEntity == null) {
            throw new UserNotFoundException();
        }
        // 生成文件名和时间
        String fileName = UUID.randomUUID() + ".jpg";
        long time = System.currentTimeMillis() / 1000;
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setUserId(userId);
        imageEntity.setFileName(fileName);
        imageEntity.setUserId(userId);
        imageEntity.setTime(time);
        imageEntity.setIsUsed(false);
        // 保存文件
        Path path = Paths.get("./postImageUploads/" + fileName);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        // 更新数据库
        checkResult(imageDao.insertImage(imageEntity));
        return fileName;
    }

    /**
     * 更新帖子和图片的对应关系，并删除不再被这条帖子使用的无用图片
     * 每次发布帖子或修改帖子后应该调用
     */
    public void setPostImage(int postId, String[] imageFileNames) {
        // 若是修改图片列表，则主动删除修改后未使用的图片
        List<PostImageEntity> oldImageList = postImageDao.getPostImages(postId);
        // 原来的帖子图片id列表
        List<Integer> oldImageIdList = new ArrayList<>(oldImageList.size());
        for(PostImageEntity imageEntity : oldImageList) {
            oldImageIdList.add(imageEntity.getImage().getId());
        }
        // 新的帖子图片id列表
        List<Integer> newImageIdList = new ArrayList<>(imageFileNames.length);
        for(String imageFileName : imageFileNames) {
            Integer imageId = imageDao.getImageIdByFileName(imageFileName);
            if(imageId == null) {
                throw new ImageNotFoundException(imageFileName);
            }
            newImageIdList.add(imageId);
        }
        // 要删除的图片id列表
        List<Integer> imageIdToDelete = new ArrayList<>(oldImageIdList.size());
        for(Integer imageId : oldImageIdList) {
            if(!newImageIdList.contains(imageId)) {
                imageIdToDelete.add(imageId);
            }
        }
        // 对要删除的无用图片进行删除
        for(Integer imageId : imageIdToDelete) {
            String fileName = imageDao.getImageFileName(imageId);
            Path path = Paths.get("./postImageUploads/" + fileName);
            // 从数据库中删除记录
            checkResult(imageDao.deleteImage(imageId));
            // 删除图片文件
            if (!Files.exists(path)) {
                logger.error("无法删除图片，文件不存在：id={}，path={}", imageId, path.toString());
            } else {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    logger.error("无法删除图片，出现错误：id={}，path={}", imageId, path.toString());
                    logger.error(e);
                }

            }
        }
        // 清空帖子的帖子-图片对应关系，准备重新插入新的对应关系
        // 此处返回值不为1也是正常情况
        postImageDao.deletePostImages(postId);
        // 插入新的图片记录，同时设置图片顺序
        for (int i = 0; i < imageFileNames.length; i++) {
            Integer imageId = imageDao.getImageIdByFileName(imageFileNames[i]);
            if(imageId == null) {
                throw new ImageNotFoundException(imageFileNames[i]);
            }
            checkResult(postImageDao.insertPostImage(imageId, postId, i));
            checkResult(imageDao.setIsUsed(imageId, true));
        }
    }

}
