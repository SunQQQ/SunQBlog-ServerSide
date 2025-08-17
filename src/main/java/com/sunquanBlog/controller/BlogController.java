package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.DictionaryMapper;
import com.sunquanBlog.model.Dictionary;
import com.sunquanBlog.service.BlogService;
import com.sunquanBlog.service.DictionaryService;
import com.sunquanBlog.service.LogService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class BlogController {
    @Value("${file.upload-dir}") // 从配置文件中读取文件存储路径
    private String uploadDir;

    @Autowired
    private BlogService blogService;

    @Autowired
    private LogService logService;

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @PostMapping("/getBlogList")
    public ApiResponse getBlogList(HttpServletRequest request,@RequestBody Map<String,Object> requestBody){
        // 从token获取用户Id
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        Integer tagId = (Integer) requestBody.get("tag");
        Integer start = (Integer) requestBody.get("start");
        Integer size = (Integer) requestBody.get("size");

        logService.createLog(request,"管理后台", "文章列表页", "查询" , "文章列表", "：第"+ (Integer.parseInt(start.toString())+1) + "页");

        return blogService.getBlogList(userId,tagId,start,size);
    }

    // 给用户端使用，无需登录
    @PostMapping("/getUserBlogList")
    public ApiResponse getUserBlogList(@RequestBody Map<String,Object> requestBody,HttpServletRequest request){
        List<Integer> tagList = (List<Integer>) requestBody.get("tag");
        Integer[] tagId = tagList.toArray(new Integer[0]);
        Integer start = (Integer) requestBody.get("start");
        Integer size = (Integer) requestBody.get("size");

        // 记录日志
        Integer curPage = (start / size) + 1;
        String tagNames = getTagName(Arrays.asList(1),tagList);

        if(curPage == 1) {
            logService.createLog(request, "用户端","首页", "筛选" , "博客列表", "分类为" + tagNames);
        }else {
            logService.createLog(request, "用户端","首页", "下拉" , "博客列表", "到第" + curPage + "页,分类为:" + tagNames);
        }

        return blogService.getUserBlogList(tagId,start,size);
    }

    /**
     * 获取标签名称
     * @param parentTag 父标签id，用于获取所有子标签
     * @param tagArr 需要转换的子标签id数组
     * @return 标签名称拼接字符串
     */
    public String getTagName(List<Integer> parentTag,List<Integer> tagArr){
        List<Dictionary> allTags = dictionaryMapper.getDictionaryList(parentTag);

        // 提取allTags中的所有ID
        Set<Integer> allTagIds = allTags.stream()
                .map(Dictionary::getId)
                .collect(Collectors.toSet());
        // 如果子标签全部都在allTags中，且长度相同，则直接返回“全部”
        if (tagArr.size() == allTags.size() && allTagIds.containsAll(tagArr)) {
            return "全部";
        }

        String result = "";
        for (Integer tagId : tagArr) {
            for (Dictionary tag : allTags) {
                if (tag.getId().equals(tagId)) {
                    result += tag.getName() + " ";
                    break;  // 找到第一个匹配项后跳出内层循环
                }
            }
        }

        return result.trim();
    }

    /**
     * 根据评论数得到热门文章列表
     * @return
     */
    @PostMapping("/getHotList")
    public ApiResponse getHotList(@RequestBody Map<String,Object> requestBody){
        Integer topNum = (Integer) requestBody.get("topNum");

        return blogService.getHotList(topNum);
    }

    @PostMapping("/getBlogDetail")
    public ApiResponse getBlogDetail(@RequestBody Map<String,Object> requestBody,HttpServletRequest request){
        Integer blogId = (Integer) requestBody.get("id");

        return blogService.getBlogDetail(blogId,request);
    }

    @PostMapping("/createBlog")
    public ApiResponse createBlog(@RequestBody Map<String,Object> requestBody,HttpServletRequest request){
        // 从token获取用户Id
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);
        requestBody.put("userId",userId);

        return blogService.insertBlog(requestBody);
    }

    @PostMapping("/deleteBlog")
    public ApiResponse deleteBlog(@RequestBody Map<String,Object> requestBody){
        Integer blogId = (Integer) requestBody.get("id");

        return blogService.deleteBlog(blogId);
    }

    @PostMapping("/updateBlog")
    public ApiResponse updateBlog(@RequestBody Map<String,Object> requestBody){
        return blogService.updateBlog(requestBody);
    }

    @PostMapping("/UploadImg")
    public ApiResponse uploadImage(@RequestParam("content") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error(500,"文件不能为空");
        }

        try {
            // 确保上传目录存在
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一的文件名
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // 保存文件
            Files.copy(file.getInputStream(), filePath);

            System.out.println("Absolute file path: " + filePath.toAbsolutePath());

            // 返回文件的访问 URL
            String fileUrl = "/uploads/" + fileName; // 假设文件可以通过 /uploads 路径访问
            return ApiResponse.success(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.error(500,"图片上传失败");
        }
    }
}
