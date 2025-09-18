package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags="通用接口")
public class CommonController {


    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload( MultipartFile file) {
        log.info("上传图片");
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
