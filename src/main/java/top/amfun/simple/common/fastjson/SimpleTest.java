package top.amfun.simple.common.fastjson;

import com.alibaba.fastjson.JSON;

import java.time.LocalDateTime;

/**
 * @author zhaoxg
 * @date 2020/11/20 15:29
 */
public class SimpleTest {

    public static void main(String[] args) {
        serialize();
        deserialize();
    }
    public static void serialize() {
        UserTest user = new UserTest();
        user.setId(100001L);
        user.setName("xiaozhan");
        user.setCreateTime(LocalDateTime.now());
        String jsonString = JSON.toJSONString(user);
        System.out.println(jsonString);
    }

    public static void deserialize() {
        String userString = "{\"createTime\":\"2020-11-20 15:36:19\",\"id\":100001,\"name\":\"xiaozhan\"}";
        UserTest userTest = JSON.parseObject(userString, UserTest.class);
        System.out.println(userTest.getId());
        System.out.println(userTest.getName());
        System.out.println(userTest.getCreateTime());
    }
}
