package com.sunquanBlog.model;

import java.time.LocalDateTime;

public class Blog {
    private Integer id;
    private String title;
    private String summary;
    private String content;
    private LocalDateTime createTime;
    private String articleTag;
    private String articleCover;
    private Integer commentNum;
    private String pageViewNum;
    private Integer createId;
    private String createName;
    private String articleTagName;
    private Integer systemReviewResult;
    private LocalDateTime systemReviewTime;
    private String systemReviewComment;
    private Integer systemIsReview;

    public Integer getSystemIsReview() {
        return systemIsReview;
    }

    public void setSystemIsReview(Integer systemIsReview) {
        this.systemIsReview = systemIsReview;
    }

    public Integer getSystemReviewResult() {
        return systemReviewResult;
    }

    public void setSystemReviewResult(Integer systemReviewResult) {
        this.systemReviewResult = systemReviewResult;
    }

    public LocalDateTime getSystemReviewTime() {
        return systemReviewTime;
    }

    public void setSystemReviewTime(LocalDateTime systemReviewTime) {
        this.systemReviewTime = systemReviewTime;
    }

    public String getSystemReviewComment() {
        return systemReviewComment;
    }

    public void setSystemReviewComment(String systemReviewComment) {
        this.systemReviewComment = systemReviewComment;
    }
    public Blog() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getArticleTag() {
        return articleTag;
    }

    public void setArticleTag(String articleTag) {
        this.articleTag = articleTag;
    }

    public String getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getPageViewNum() {
        return pageViewNum;
    }

    public void setPageViewNum(String pageViewNum) {
        this.pageViewNum = pageViewNum;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getArticleTagName() {
        return articleTagName;
    }

    public void setArticleTagName(String ArticleTagName) {
        this.articleTagName = ArticleTagName;
    }
}
