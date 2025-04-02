package kitra.easyweibo.controller.post;

import cn.dev33.satoken.stp.StpUtil;
import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.dto.ApiResponse;
import kitra.easyweibo.dto.post.UploadImageResponse;
import kitra.easyweibo.exception.FileSizeExceededLimitException;
import kitra.easyweibo.exception.IllegalFileFormatException;
import kitra.easyweibo.service.ImageService;
import kitra.easyweibo.util.ApiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@PostRestController
public class PostImageUploadController {
    private final ImageService imageService;

    public PostImageUploadController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/uploadImg")
    public ApiResponse<UploadImageResponse> uploadPostImage(@RequestParam("image") MultipartFile file) throws IOException {
        if (!StpUtil.isLogin()) {
            return ApiUtil.unauthorizedResponse();
        }
        // 检查Content-Type
        if(file.getContentType() == null || !file.getContentType().startsWith("image")) {
            throw new IllegalFileFormatException();
        }
        if(file.isEmpty()) {
            throw new IllegalFileFormatException();
        }
        // 图片大小检查
        if(file.getSize() >= 5 * 1024 * 1024) {
            throw new FileSizeExceededLimitException("5MB");
        }
        String fileName = imageService.savePostImage(StpUtil.getLoginIdAsInt(), file);
        return ApiUtil.successfulResponse(new UploadImageResponse(fileName));
    }
}
