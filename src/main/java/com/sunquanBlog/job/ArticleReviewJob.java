package com.sunquanBlog.job;

import com.sunquanBlog.mapper.BlogMapper;
import com.sunquanBlog.model.Blog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArticleReviewJob {
    @Autowired
    private BlogMapper blogMapper;

    @Scheduled(cron = "0 */2 * * * ?") // 每5分钟执行一次
//    @Scheduled(cron = "0 0/60 * * * ?")  // 每1小时执行一次
//    @Scheduled(cron = "0 0 1,13 * * ?") // 每天1点和13点各执行一次
    public void ArticleReview() {
        log.info("开始执行定时任务：文章审核任务");

        // 查询文章表中系统审核时间为空的文章id
        blogMapper.getNoCheckList().forEach(blog -> {
            // 对每篇文章进行审核
            reviewArticle(blog);
        });
    }

    public void reviewArticle(Blog blog) {
        String title = blog.getTitle();
        String summary = blog.getSummary();
        String content = blog.getContent();

        String Comment = "";

        if(!isMeaningful(title)){
            Comment = "标题无意义";
        }

        if(!isMeaningful(summary)){
            Comment += "简介无意义";
        }

        if(!isMeaningful(content)){
            Comment += "内容无意义";
        }

        if(Comment != null && !Comment.isEmpty()){
            blog.setSystemReviewResult(0);
            blog.setSystemReviewComment(Comment);
            log.info("审核文章：{},{}", blog.getTitle(), Comment);
        }else {
            blog.setSystemReviewResult(1);
            blog.setSystemReviewComment("审核通过");
            log.info("审核文章：{},{}", blog.getTitle(), "审核通过");
        }

        blogMapper.updateReviewInfo(blog); // 假设toMap()方法将Blog对象转换为Map
    }

    /**
     * 判断字符串是否有意义
     * @param str 输入字符串
     * @return true=有意义，false=无意义
     */
    public static boolean isMeaningful(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false; // 空字符串或null视为无意义
        }

        // 检查是否全是数字、全是字母、长度<20
        boolean isAllDigits = str.matches("\\d+");
        boolean isAllLetters = str.matches("[a-zA-Z]+");
        boolean isTooShort = str.length() < 20;

        // 如果满足任意一条，则无意义
        return !(isAllDigits || isAllLetters || isTooShort);
    }
}
