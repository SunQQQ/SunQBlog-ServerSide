let option;

let pageApi = function () {
    let App = option,
        ObjectId = require('mongodb').ObjectId,
        dealObj = require("./commonFunction"),
        DealPara = dealObj.DealPara,
        Monge = require('../Public/module/Mongo'),
        Util = require('../Public/module/util'),
        util = new Util(),
        nodemailer = require('nodemailer'); // 发邮件组件

    // 留言页面相关
    App.post('/MessageCreate/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var city = util.isXssString(Para.LocationCityName),
                date = util.isXssString(Para.MessageLeaveDate),
                user = util.isXssString(Para.MessageLeaveName),
                text = util.isXssStringLeaveMessage(Para.MessageText);

            if (city && date && user && text) {
                Monge('LeaveMessage', 'Insert', Para, function () {
                    var Json = { status: '0', data: '插入成功' };
                    Response.json(Json);

                    let transporter = nodemailer.createTransport({
                        'host': 'smtp.qq.com',    // 主机
                        'secureConnection': true,    // 使用 SSL
                        'service': 'qq',
                        'port': 465,    // SMTP 端口
                        'auth': {
                            'user': '1585437938@qq.com',    // 账号
                            'pass': 'xeczefvioweyihde' // 授权码
                        }
                    });
                    let mailContent = {
                        from: '1585437938@qq.com', // 发件人地址
                        to: '1585437938@qq.com', // 收件人地址
                        subject: '博客留言内容', // 主题
                        html: `${Para.MessageLeaveName} : ${Para.MessageText}` // html body
                    };

                    // 发送邮件
                    transporter.sendMail(mailContent, (err, info) => {
                        if (err) {
                            console.log('发邮件出错了', err);
                        } else {
                            console.log('邮件发送成功');
                        }
                    });
                });
            } else {
                var Json = { status: '1', data: '有xss风险，不予通过' };
                Response.json(Json);
            }
        });
    });
    App.post('/MessageRead/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var PagnationData = Para.PagnationData ? Para.PagnationData : { SKip: 0, Limit: 1000 };
            Monge('LeaveMessage', 'ReadByOrder', [{}, { MessageLeaveDate: -1 }, PagnationData], function (Result) {
                var Json = { status: '0', data: Result };
                Response.json(Json);
            });
        });
    });
    App.post('/MessageLeaveDelete/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var Object = {};
            Object._id = ObjectId(Para._id);

            Monge('LeaveMessage', 'Delete', Object, function () {
                var Json = { status: '0', data: '友链删除成功' };
                Response.json(Json);
            });
        });
    });

    App.post('/MessageLeaveEdit/:accesstype', function (Request, Response) {
        var WhereId = {}, UpdateStr = { $set: {} };

        DealPara(Request, Response, function (Para) {
            WhereId._id = ObjectId(Para._id);
            delete Para._id;
            UpdateStr.$set = Para;
            Monge('LeaveMessage', 'Update', [WhereId, UpdateStr], function (Result) {
                var Json = { status: '0' };
                Json.data = 'Update Success';
                Response.json(Json);
            });
        });
    });
    // 留言数量
    App.post('/getmessagenum', function (Request, Response) {
        Monge('LeaveMessage', 'GetNum', {}, function (Result) {
            var Json = { status: '0', data: Result };
            Response.json(Json);
        });
    });
}

module.exports = function (app) {
    option = app;
    return pageApi;
}
