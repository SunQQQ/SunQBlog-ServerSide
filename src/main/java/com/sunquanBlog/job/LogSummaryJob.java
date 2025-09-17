package com.sunquanBlog.job;

import com.sunquanBlog.common.util.DateUtils;
import com.sunquanBlog.mapper.LogMapper;
import com.sunquanBlog.mapper.LogSummaryMapper;
import com.sunquanBlog.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
@Slf4j
@Service
public class LogSummaryJob {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LogSummaryMapper logSummaryMapper;
    @Autowired
    private LogService logService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 应用启动后自动回填历史数据（只会执行一次）
     */
    public String backfillHistoryData() {
        // 先清空数据
        logSummaryMapper.cleanAll();

        // 定义需要回填的日期范围
        // 网站从4月1日上线，批量回填数据到当日
        // 配合10分钟定时任务，该任务不停的汇总当日数据，即可覆盖所有时间日志数据
        LocalDate startDate = LocalDate.of(2025, 4, 1);
        LocalDate endDate = LocalDate.now();

        List<LocalDate> datesToProcess = new ArrayList<>();
        LocalDate currentDate = startDate;

        // 填充日期数组
        while (!currentDate.isAfter(endDate)) {
            datesToProcess.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        // 并行处理以提高速度（如果数据量大）
        datesToProcess.parallelStream().forEach(this::processDate);

        System.out.println(DateUtils.getCurrentDateTime() + ": 所有数据回填完成！");
        return DateUtils.getCurrentDateTime() + ": 所有数据回填完成！";
    }

    /**
     * 每日定时任务：处理前一天的数据
     * 每天凌晨2点30分执行
     */
    /*@Scheduled(cron = "0 30 2 * * ?") // 秒 分 时 日 月 周
    public void dailySummaryTask() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        processDate(yesterday);
        log.info("每日数据汇总完成，处理日期：" + yesterday);
        System.out.println("每日数据汇总完成，处理日期：" + yesterday);
    }*/


    /**
     * 每10分钟汇总log表中的数据到中间表
     */
    @Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次（秒 分 时 日 月 周）
    public void periodicSummaryTask() {
        // 每次汇总数据，先清空中间表中当天的汇总数据
        logSummaryMapper.deleteToday();

        LocalDate today = LocalDate.now();
        processDate(today);
        System.out.println("当日日志数据汇总完成，处理日期：" + DateUtils.getCurrentDateTime());
    }

    // 汇总某一条函数
    private void processDate(LocalDate date){
        String dateStr = date.format(dateFormatter);
        String nextDateStr = date.plusDays(1).format(dateFormatter);
        // 获取需要排除IP的SQL字符串（sunq的访问ip要剔除掉）
        String excludeSunqSql = logService.excludeSunqSql(dateStr + " 00:00:00",nextDateStr + " 00:00:00");

        logSummaryMapper.insertDailyIpSummary(dateStr, dateStr + " 00:00:00", nextDateStr + " 00:00:00", excludeSunqSql);
    }

    /**
     * 已用更高效的新方法替代，可删除
     * 处理指定日期的数据汇总
     * @param date 要处理的日期
     */
    private void processDateOld(LocalDate date) {
        String dateStr = date.format(dateFormatter);
        String nextDateStr = date.plusDays(1).format(dateFormatter);

        // 读取 SQL 文件内容
        String sql;
        try (InputStream inputStream = getClass().getResourceAsStream("/sql/log_daily_ip_summary.sql");
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {

            sql = scanner.useDelimiter("\\A").next();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load SQL file", e);
        }

        try {
            jdbcTemplate.update(sql, dateStr, dateStr + " 00:00:00", nextDateStr + " 00:00:00");
            System.out.println("成功处理日期: " + dateStr);
        } catch (Exception e) {
            System.err.println("处理日期 " + dateStr + " 时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 手动触发某天的数据处理（用于测试或补数据）
     */
    public void manualProcessDate(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);
        processDate(date);
    }
}
