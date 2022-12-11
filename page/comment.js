let option;

let pageApi = function () {
    let App = option,
        DealPara = require("./commonFunction"),
        Monge = require('../Mongo'),
        Util = require('../util'),
        util = new Util(),
        nodemailer = require('nodemailer'); // 发邮件组件


    // 所有评论列表
    App.post('/CommentRead/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var PagnationData = Para.PagnationData ? Para.PagnationData : { SKip: '', Limit: '' };
            Monge.Mongo('articlecomment', 'ReadByOrder', [{}, { _id: -1 }, PagnationData], function (Result) {
                var Json = { status: '0', data: Result };
                Response.json(Json);
            });
        });
    });

    //评论总数
    App.post('/getCommentNum', function (Request, Response) {
        Monge.Mongo('articlecomment', 'GetNum', {}, function (Result) {
            var Json = { status: '0', data: Result };
            Response.json(Json);
        });
    });

    // 删除评论
    App.post('/CommentDelete/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var Object = {};
            Object._id = ObjectId(Para._id);

            Monge.Mongo('articlecomment', 'Delete', Object, function () {
                var Json = { status: '0', data: '标签删除成功' };
                Response.json(Json);
            });
        });
    });

    // 新增评论
    App.post('/ArticleCommentCreate/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var date = util.isXssString(Para.ArticleCommentDate),
                email = util.isXssString(Para.ArticleCommentEmail),
                nickName = util.isXssString(Para.ArticleCommentNickName),
                text = util.isXssStringLeaveMessage(Para.ArticleCommentText),
                url = util.isXssString(Para.ArticleCommentUrl),
                id = util.isXssString(Para.ArticleId),
                cityName = util.isXssString(Para.LocationCityName);

            if (date && email && nickName && text && url && id && cityName) {
                Monge.Mongo('articlecomment', 'Insert', Para, function () {
                    var Json = { status: '0', data: '添加评论成功' };
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
                        subject: `${Para.ArticleCommentNickName} 评论博客文章`, // 主题
                        html: `${Para.ArticleName} : ${Para.ArticleCommentText}` // html body
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

    /**
     * 本接口用于修改文章评论文本
     * 前端需要传入文章的id
     */
    App.post('/ArticleCommentUpdate/:accesstype', function (Request, Response) {
        var WhereId = {}, UpdateStr = { $set: {} };

        DealPara(Request, Response, function (Para) {
            WhereId._id = ObjectId(Para._id);
            delete Para._id;
            UpdateStr.$set = Para;
            Monge.Mongo('articlecomment', 'Update', [WhereId, UpdateStr], function (Result) {
                var Json = { status: '0' };
                Json.data = 'Update Success';
                Response.json(Json);
            });
        });
    });

    /**
     * 本接口用于修改文章评论数
     * 前端需要传入文章的id,操作类型字段type（add新增/delete删除）
     * 先去文章表里查对应文章的评论数
     * 给查出来的评论数+1，或者-1
     * 再修改文章表里，该文章的评论数
     */
    App.post('/ArticleCommentNumUpdate/:accesstype', function (Request, Response) {
        var WhereId = {},
            UpdataStr = {
                $set: {
                    CommentNum: 0
                }
            };

        DealPara(Request, Response, function (Para) {
            WhereId._id = ObjectId(Para._id);
            // 获取当前文章Id的当前评论数
            Monge.Mongo('runoob', 'Read', WhereId, function (CurrentNum) {
                // 判断删除还是新增
                if (Para.type == 'add') {
                    UpdataStr.$set.CommentNum = parseInt(CurrentNum[0].CommentNum) + 1;
                } else if (Para.type == 'delete') {
                    UpdataStr.$set.CommentNum = parseInt(CurrentNum[0].CommentNum) - 1;
                }
                // 修改文章表里，对应id文章的评论数字段
                Monge.Mongo('runoob', 'Update', [WhereId, UpdataStr], function () {
                    var Json = { status: '0' };
                    Json.data = 'ArticleCommentNum Update Success';
                    Response.json(Json);
                });
            });
        });
    });

    // 返回对应文章的评论
    App.post('/ArticleCommentRead/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            /*var Key = {ArticleId:ObjectId(Para.ArticleId)};*/
            var Key = { ArticleId: Para.ArticleId };
            Monge.Mongo('articlecomment', 'ReadByOrder', [Key], function (Result) {
                var Json = { status: '0', data: Result };
                Response.json(Json);
            });
        });
    });
    // 评论个数
    App.post('/getcommentnum', function (Request, Response) {
        Monge.Mongo('articlecomment', 'GetNum', {}, function (Result) {
            var Json = { status: '0', data: Result };
            Response.json(Json);
        });
    });
}

module.exports = function (app) {
    option = app;
    return pageApi;
}