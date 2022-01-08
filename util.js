function Util() {
    // 判断是否是大于等于0的正整数
    this.isInteger = function (para) {
        return para >= 0 && typeof para === 'number' && para % 1 === 0; //是整数，则返回true，否则返回false
    };

    // 判断字符串是否包含script、style、<>等标签
    this.isXssString = function (strings) {
        var script = strings.indexOf('script'),
            style = strings.indexOf('style'),
            tags = /<.*>/.test(strings);

        if(script==-1 && style==-1 && !tags){
            return true;
        }else {
            return false;
        }
    };

    // 与上面逻辑基础上放开<>标签，但是不能存在onerror。留言表情用到<>
    this.isXssStringLeaveMessage = function (strings) {
        var script = strings.indexOf('script'),
            all = strings.indexOf('*'),
            onerror = strings.indexOf('onerror'),
            tags = /<\/.*>/.test(strings);

        if(script==-1 && all==-1 && onerror==-1 && !tags){
            return true;
        }else {
            return false;
        }
    };

    // 判断是否是数组
    this.isArray = function (arr){
        return Object.prototype.toString.call(arr) === '[object Array]';
    };


    // 数组去重
    this.dedupe = function(array){
        return Array.from(new Set(array));
    };
};
module.exports = Util;