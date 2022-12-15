var crypto = require("crypto");
var Monge = require('./Mongo');
var token = {
    createToken: function (id, timeout) {
        var obj2 = {
            data: id,//payload
            created: parseInt(Date.now() / 1000),//token生成的时间的，单位秒
            exp: parseInt(timeout) || 10//token有效期
        };
        //payload信息
        var base64Str = Buffer.from(JSON.stringify(obj2), "utf8").toString("base64");
        //添加签名，防篡改
        var secret = "SQBlog.com";
        var hash = crypto.createHmac('sha256', secret);
        hash.update(base64Str);
        var signature = hash.digest('base64');

        return base64Str + "." + signature;
    },
    /*解码*/
    decodeToken: function (token) {
        var decArr = token.split(".");
        if (decArr.length < 2) {
            //token不合法
            return false;
        }
        var payload = {};
        //将payload json字符串 解析为对象
        try {
            payload = JSON.parse(Buffer.from(decArr[0], "base64").toString("utf8"));
        } catch (e) {
            return false;
        }
        //检验签名
        var secret = "SQBlog.com";
        var hash = crypto.createHmac('sha256', secret);
        hash.update(decArr[0]);
        var checkSignature = hash.digest('base64');

        return {
            payload: payload,
            signature: decArr[1],
            checkSignature: checkSignature
        }
    },
    checkToken: function (token) {
        var resDecode = this.decodeToken(token);
        if (!resDecode) {
            return false;
        }
        //是否过期
        var expState = (parseInt(Date.now() / 1000) - parseInt(resDecode.payload.created)) > parseInt(resDecode.payload.exp) ? false : true;
        if (!expState) {
            return 'TimeOut'
        }
        if (resDecode.signature === resDecode.checkSignature && expState) {
            return true;
        }
        return false;
    },
    getId:function (token) {
      var resDecode = this.decodeToken(token);
      return resDecode.payload.data;
    }

}

module.exports = token; 