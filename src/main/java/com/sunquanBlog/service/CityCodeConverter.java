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
public class CityCodeConverter{
    // 使用ConcurrentHashMap保证线程安全
    private final Map<String, String> nameToCodeMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = new ClassPathResource("citycode-mapping.json").getInputStream()) {
            Map<String, String> rawMap = mapper.readValue(is, new TypeReference<Map<String, String>>() {});
            nameToCodeMap.putAll(rawMap);
        }
    }

    /**
     * 根据中文名获取citycode
     * @param name 中文名称（如"北京市"）
     * @return 返回citycode，未找到时返回null
     */
    public String getCityCode(String name) {
        return nameToCodeMap.get(name.trim());
    }
}