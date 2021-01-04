package top.amfun.simple;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zhaoxg
 * @date 2020/12/9 10:45
 */
public class Test {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        //时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("formate: "+ now.format(formatter));

        //当天的零点
        System.out.println("当天的零点:  "+LocalDateTime.of(now.toLocalDate(), LocalTime.MIN));

        //当天的最后时间
        System.out.println("当天的最后时间:  "+LocalDateTime.of(now.toLocalDate(), LocalTime.MAX));

        LocalDateTime start = LocalDateTime.parse("2020-12-01 01:11:11", formatter);
        LocalDateTime end = LocalDateTime.parse("2020-12-08 09:13:00", formatter);
        System.out.println("start= "+start.toLocalDate().toEpochDay()+";end = " +end.toLocalDate().toEpochDay());
        long differDays =  end.toLocalDate().toEpochDay() - start.toLocalDate().toEpochDay();
        System.out.println(differDays);

    }
}
