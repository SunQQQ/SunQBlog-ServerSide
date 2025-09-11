package com.sunquanBlog.job;

import com.sunquanBlog.mapper.LogSummaryMapper;
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
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 是否已经回填过历史数据的标志
    private boolean historyDataBackfilled = false;

    /**
     * 应用启动后自动回填历史数据（只会执行一次）
     */
    public String backfillHistoryData() {
        if (historyDataBackfilled) {
            return "历史数据已回填，无需重复操作。";
        }

        // 定义需要回填的日期范围：2025年2月1日到2月28日
        LocalDate startDate = LocalDate.of(2025, 4, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 9);

        List<LocalDate> datesToProcess = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            datesToProcess.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        // 并行处理以提高速度（如果数据量大）
        datesToProcess.parallelStream().forEach(this::processDate);

        historyDataBackfilled = true;
        System.out.println("历史数据回填完成！");
        return "历史数据回填完成！";
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

    @Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次（秒 分 时 日 月 周）
    public void periodicSummaryTask() {
        logSummaryMapper.deleteAll();

        LocalDate today = LocalDate.now();
        processDate(today);
        System.out.println("每日数据汇总完成，处理日期：" + today.format(dateFormatter));
    }

    /**
     * 处理指定日期的数据汇总
     * @param date 要处理的日期
     */
    private void processDate(LocalDate date) {
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
