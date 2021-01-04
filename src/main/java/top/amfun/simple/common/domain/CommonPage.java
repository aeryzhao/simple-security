package top.amfun.simple.common.domain;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @date 2020/11/4 10:55
 * @description: 封装分页信息
 */
@Getter
@Setter
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> T;

    /**
     * 将Mybatis-plus分页结果转换
     * @param pageResult
     * @param <T>
     */
    public static <T> CommonPage<T> restPage(Page<T> pageResult) {
        CommonPage<T> commonPage = new CommonPage<>();
        commonPage.setPageNum(Convert.toInt(pageResult.getPages()));
        commonPage.setPageSize(Convert.toInt(pageResult.getSize()));
        commonPage.setTotal(pageResult.getTotal());
        commonPage.setTotalPage(Convert.toInt(pageResult.getTotal())/Convert.toInt(pageResult.getSize()) + 1);
        commonPage.setT(pageResult.getRecords());
        return commonPage;
    }
}
