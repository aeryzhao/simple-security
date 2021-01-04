package top.amfun.simple.modules.other.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import io.minio.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.amfun.simple.common.domain.CommonResult;
import top.amfun.simple.modules.ums.dto.BucketPolicyConfigDto;
import top.amfun.simple.modules.ums.dto.MinioUploadDto;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date 2020/10/30 12:03
 * @description:
 */
@Api(tags = "对象存储")
@RestController
@RequestMapping(value = "ignore/minio")
public class MinioController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioController.class);

    @Value("${minio.Endpoint}")
    private String ENDPOINT;
    @Value("${minio.AccessKey}")
    private String ACCESS_KEY;
    @Value("${minio.SecretKey}")
    private String SECRET_KEY;

    @ApiOperation(value = "文件上传", notes = "备注信息，需要传递桶名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucket", value = "桶名称", paramType = "query", dataType = "String", defaultValue = "default")
    })
    @PostMapping(value = "upload")
    public CommonResult upload(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "bucket", defaultValue = "default") String bucketName) {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY).build();
            boolean bucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (bucketExist) {
                LOGGER.info("桶已经存在");
            } else {
                // 创建存储桶并设置只读权限
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                BucketPolicyConfigDto bucketPolicyConfigDto = createBucketPolicyConfig(bucketName);
                SetBucketPolicyArgs bucketPolicyArgs = SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(JSONUtil.toJsonStr(bucketPolicyConfigDto))
                        .build();
                minioClient.setBucketPolicy(bucketPolicyArgs);
            }
            String filename = file.getOriginalFilename();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String objectName = dateFormat.format(new Date()) + "/" + filename;
            // 上传一个文件到桶内
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE).build();
            minioClient.putObject(putObjectArgs);
            LOGGER.info("上传成功");
            MinioUploadDto minioUploadDto = new MinioUploadDto();
            minioUploadDto.setUrl(ENDPOINT +"/"+ bucketName +"/"+ objectName);
            minioUploadDto.setFileName(filename);
            return CommonResult.success(minioUploadDto);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("上传错误：" + e.getMessage());
        }
        return CommonResult.failed();
    }

    private BucketPolicyConfigDto createBucketPolicyConfig(String bucketName) {
        BucketPolicyConfigDto.Statement statement = BucketPolicyConfigDto.Statement.builder()
                .Effect("Allow")
                .Principal("*")
                .Action("s3:GetObject")
                .Resource("arn:aws:s3:::" + bucketName + "/*.**").build();
        return BucketPolicyConfigDto.builder()
                .Version("2012-10-17")
                .Statement(CollUtil.toList(statement)).build();
    }

    @ApiOperation(value = "删除文件", notes = "备注信息：需要提供bucketName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "桶名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "objectName", value = "文件名称", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping(value = "delete")
    public CommonResult delete(@RequestParam(value = "bucketName", required = true, defaultValue = "default") String bucketName,
                               @RequestParam(value = "objectName", required = true) String objectName) {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY).build();
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("删除失败：" + e.getMessage());
        }
        return CommonResult.failed();
    }


}
