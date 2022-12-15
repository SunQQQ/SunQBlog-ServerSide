function Util() {
    // 判断是否是大于等于0的正整数
    this.isInteger = function (para) {
        return para >= 0 && typeof para === 'number' && para % 1 === 0; //是整数，则返回true，否则返回false
    };

    // 判断字符串是否包含script、style、<>等标签
    this.isXssString = function (strings) {
        // 如果入参为空，直接通过校验
        var script = strings.indexOf('script'),
            style = strings.indexOf('style'),
            tags = /<.*>/.test(strings);

        if (script == -1 && style == -1 && !tags) {
            return true;
        } else {
            return false;
        }
    };

    // 与上面逻辑基础上放开<>标签，但是不能存在onerror。留言表情用到<>
    this.isXssStringLeaveMessage = function (strings) {
        var script = strings.indexOf('script'),
            all = strings.indexOf('*'),
            onerror = strings.indexOf('onerror'),
            tags = /<\/.*>/.test(strings);

        if (script == -1 && all == -1 && onerror == -1 && !tags) {
            return true;
        } else {
            return false;
        }
    };

    // 判断是否是数组
    this.isArray = function (arr) {
        return Object.prototype.toString.call(arr) === '[object Array]';
    };


    // 数组去重
    this.dedupe = function (array) {
        return Array.from(new Set(array));
    };

    /**
     * 传入日期，及需要前推的天数。返回相应的时间数组
     * @param beginDate ‘2021/12/11’
     * @param num 3
     * @returns [‘2021/12/09’,‘2021/12/10’,‘2021/12/11’]
     */
    this.getDateArray = function (beginDate, num) {
        let endTimeObject = new Date(beginDate),
            result = []; // 时间数组

        // 生成数组[‘2021/12/09’,‘2021/12/10’,‘2021/12/11’,...]
        for (let i = 0; i < num; i++) {
            let dayObject, day, month;
            dayObject = new Date(endTimeObject.getTime() - i * 24 * 60 * 60 * 1000);
            day = dayObject.getDate() < 10 ? '0' + dayObject.getDate() : dayObject.getDate();
            month = dayObject.getMonth() + 1 < 10 ? '0' + (dayObject.getMonth() + 1) : dayObject.getMonth() + 1;
            result.push(dayObject.getFullYear() + '/' + month + '/' + day);
        }

        return result.reverse();
    }

    /**
     * 获取相邻某一天
     * @param date ‘2021/12/11’
     * @param num -1
     * @returns '2021/12/10'
     */
    this.getOneDate = function (date, num) {
        let timeObject = new Date(date), // 时间字符串转为时间对象
            newTimeObject = new Date(timeObject.getTime() + num * 24 * 60 * 60 * 1000),   // 时间对象转为毫秒数计算后，再转为时间对象

            result = newTimeObject.getFullYear() + '/'
                + (newTimeObject.getMonth() + 1 < 10 ? '0' + (newTimeObject.getMonth() + 1) : newTimeObject.getMonth() + 1) + '/'
                + (newTimeObject.getDate() < 10 ? '0' + newTimeObject.getDate() : newTimeObject.getDate());

        return result;
    }
};

module.exports = Util;