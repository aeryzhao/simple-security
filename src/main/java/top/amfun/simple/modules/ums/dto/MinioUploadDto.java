package top.amfun.simple.modules.ums.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @date 2020/10/30 16:52
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "minio上传成功返回文件信息")
public class MinioUploadDto {
    @ApiModelProperty(value = "文件访问地址")
    private String url;
    @ApiModelProperty(value = "文件名称")
    private String fileName;
}
