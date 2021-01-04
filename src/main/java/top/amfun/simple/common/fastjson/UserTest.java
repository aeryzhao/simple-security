package top.amfun.simple.common.fastjson;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhaoxg
 * @date 2020/11/20 15:25
 */
@Data
public class UserTest {
    private Long id;
    private String name;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
