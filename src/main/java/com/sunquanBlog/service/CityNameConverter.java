package com.sunquanBlog.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CityNameConverter {
    private Map<String, String> pinyinToChineseMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = new ClassPathResource("city-mapping.json").getInputStream()) {
            pinyinToChineseMap = mapper.readValue(is,
                    new TypeReference<Map<String, String>>() {});
        }
    }

    public String convert(String pinyin) {
        if (pinyin == null || pinyin.trim().isEmpty()) {
            return "未知";
        }
        return pinyinToChineseMap.getOrDefault(pinyin.trim(), pinyin) + "市";
    }
}