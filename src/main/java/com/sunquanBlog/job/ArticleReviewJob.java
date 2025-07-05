package com.sunquanBlog.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArticleReviewJob {
    @Scheduled(cron = "0 0/60 * * * ?")  // 每5分钟执行一次
    public void ArticleReview() {
        log.info("开始执行定时任务：文章审核任务");

        // 查询文章表中系统审核时间为空的文章id

        // 遍历如上文章，核验文章中标题、简介、内容等字段

        // 根据审核结果，设置审核意见、审核结果、审核时间

        // 检查文本，如果包含敏感词/全部是汉字/全部是英文/全部是数字/不包含汉字则返回false
    }
}
