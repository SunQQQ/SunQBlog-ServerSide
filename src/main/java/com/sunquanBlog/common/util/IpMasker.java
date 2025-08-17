package com.sunquanBlog.common.util;

import java.util.regex.Pattern;

public class IpMasker {
    // 预编译正则表达式，提升性能
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$"
    );

    /**
     * 严格校验IPv4合法性
     */
    public static boolean isValidIpv4(String ipv4) {
        return ipv4 != null && IPV4_PATTERN.matcher(ipv4).matches();
    }

    /**
     * IPv4地址脱敏（保留首尾段）
     * @param ipv4 原始IPv4地址
     * @return 脱敏后的IP（如 "192.xxx.xxx.100"）
     * @throws IllegalArgumentException 如果IP格式无效
     */
    public static String mask(String ipv4) {
        if (ipv4 == null || !isValidIpv4(ipv4)) {
            return ipv4;
        }
        return ipv4.replaceAll("^(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)$", "$1.xxx.xxx.$4");
    }
}
