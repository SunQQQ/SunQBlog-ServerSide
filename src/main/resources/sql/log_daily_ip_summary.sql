INSERT INTO log_daily_ip_summary (
    visit_day, ip, page, city, browser, user_action,
    platform, user_id, entry_time, leave_time,pv,create_time
)
SELECT
    ? AS visit_day,
    ip,
    GROUP_CONCAT(DISTINCT page) AS page,
    GROUP_CONCAT(DISTINCT ip_city) AS city,
    GROUP_CONCAT(DISTINCT browser) AS browser,
    GROUP_CONCAT(
            CONCAT(`action`, action_object, action_desc)
                ORDER BY create_time
        SEPARATOR '+'
        ) AS user_action,
    GROUP_CONCAT(DISTINCT platform_type) AS platform,
    GROUP_CONCAT(DISTINCT CASE WHEN user_id > 0 THEN user_id END) AS user_id,
    MIN(create_time) AS entry_time,
    MAX(create_time) AS leave_time,
    COUNT(*) AS pv,
    now() create_time
FROM `log`
WHERE
        create_time >= ?
  AND create_time < ?
GROUP BY ip
    ON DUPLICATE KEY UPDATE
                         page = VALUES(page),
                        city = VALUES(city),
                         browser = VALUES(browser),
                        user_action = VALUES(user_action),
                         platform = VALUES(platform),
                         user_id = VALUES(user_id),
                         entry_time = VALUES(entry_time),
                         leave_time = VALUES(leave_time),
                         pv = VALUES(pv)