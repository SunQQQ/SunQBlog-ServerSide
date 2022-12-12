let option;

let pageApi = function () {
    let App = option,
    dealObj = require("./commonFunction"),
    DealPara = dealObj.DealPara,

    Monge = require('../Public/module/Mongo'),
    Token = require('../Public/module/token'),
    ObjectId = require('mongodb').ObjectId,
    Path = require("path"),
    Formidable = require("formidable"),
    Util = require('../Public/module/util'),
    util = new Util();


    // 检查token 管理后台，初始化时验证是否合法，不要求必须是管理员
    App.post('/checkToken', function (Request, Response) {
        var Para = Request.body;

        if (Para.Token && Token.token.checkToken(Para.Token)) {
            var Json = { status: '0', data: { message: 'token合法' } };
            Response.json(Json);
        } else if (Para.Token && Token.token.checkToken(Para.Token) == 'TimeOut') {
            var Json = { status: '1', data: { message: '令牌超时' } };
            Response.json(Json);
        } else if (Para.Token && !Token.token.checkToken(Para.Token)) {
            var Json = { status: '1', data: { message: '令牌有误' } };
            Response.json(Json);
        } else if (!Para.Token) {
            var Json = { status: '1', data: { message: '无Token，请登录' } };
            Response.json(Json);
        } else {
            var Json = { status: '1', data: { message: 'nothing' } };
            Response.json(Json);
        }
    });

    /*文章管理相关*/
    App.post('/ArticleRead/:accesstype', function (req, res) {
        DealPara(req, res, function (Para) {
            var Key = Para.ArticleTag ? { ArticleTag: Para.ArticleTag } : {},  // 查询的依据，这里为文章分类
                PagnationData = Para.PagnationData ? Para.PagnationData : { SKip: 0, Limit: 10000 },  // 分页数据
                orderType = Para.orderType ? Para.orderType : { CreateDate: -1 };
            Monge.Mongo('runoob', 'ReadByOrder', [Key, orderType, PagnationData], function (Result) {
                var Json = { status: '0', data: Result };
                res.json(Json);
            });

        });
    });

    // 热门文章
    App.post('/HotArticleRead/:accesstype', function (req, res) {
        DealPara(req, res, function (Para) {
            Monge.Mongo('runoob', 'ReadByOrder', [{}, { CommentNum: -1 }, { Skip: 0, Limit: 6 }], function (Result) {
                var Json = { status: '0', data: Result };
                res.json(Json);
            });
        });
    });

    App.post('/ArticleReadOne/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var Key = { _id: ObjectId(Para._id) },
                UpdataStr = { $set: {} };

            Monge.Mongo('runoob', 'Read', Key, function (Result) {
                // 查出文章详情后，返回前端
                var Json = { status: '0' };
                Json.data = Result;
                Response.json(Json);

                // 给当前文章的阅读量+1
                UpdataStr.$set.articleReadNum = Result[0].articleReadNum ? Result[0].articleReadNum + 1 : 1;
                Monge.Mongo('runoob', 'Update', [Key, UpdataStr], function (Result) { });
            });
        });
    });

    App.post('/AddArticle/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            Para.CommentNum = 0
            Monge.Mongo('runoob', 'Insert', Para, function () {
                var Json = { status: '0', data: '插入成功' };
                Response.json(Json);
            });
        });
    });

    App.post('/ArticleDelete/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var IdObject = { _id: ObjectId(Para._id) };
            Monge.Mongo('runoob', 'Delete', IdObject, function () {
                var Json = { status: '0', data: '接口删除成功' };
                Response.json(Json);
            });
        });
    });

    App.post('/ArticleUpdate/:accesstype', function (Request, Response) {
        var WhereId = {}, UpdataStr = { $set: {} };

        DealPara(Request, Response, function (Para) {
            WhereId._id = ObjectId(Para._id);
            UpdataStr.$set.Title = Para.Title;
            UpdataStr.$set.Content = Para.Content;
            UpdataStr.$set.Summary = Para.Summary;
            UpdataStr.$set.CreateDate = Para.CreateDate;
            UpdataStr.$set.ArticleTag = Para.ArticleTag;
            UpdataStr.$set.ArticleCover = Para.ArticleCover;
            UpdataStr.$set.CommentNum = Para.CommentNum;
            UpdataStr.$set.order = Para.order;
            Monge.Mongo('runoob', 'Update', [WhereId, UpdataStr], function (Result) {
                var Json = { status: '0' };
                Json.data = 'Update Success';
                Response.json(Json);
            });
        });
    });

    // 文章数量
    App.post('/getarticlenum/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var Key = Para ? (Para.ArticleTag ? { ArticleTag: Para.ArticleTag } : {}) : {};
            Monge.Mongo('runoob', 'GetNum', Key, function (Result) {
                var Json = { status: '0', data: Result };
                Response.json(Json);
            });
        });
    });

    /*上传图片*/
    App.post('/UploadImg', function (Request, Response) {
        var From = new Formidable.IncomingForm();
        //设置保存 文件路径
        var TargetFile = Path.join(__dirname, './Public/');
        From.uploadDir = TargetFile;

        From.parse(Request, function (err, fields, files) {
            if (err) throw err;
            var FilePath = files.Content.path;

            var NewPath = Path.join(Path.dirname(FilePath), files.Content.name);
            FS.rename(FilePath, NewPath, function (err) {
                if (err) throw err;

                var MyJson = {
                    errno: 0,
                    data: ['http://39.104.22.73:8888/' + files.Content.name]
                };
                Response.json(MyJson);
            });
        });
    });

    /*标签管理相关*/
    App.post('/TagCreate/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            Monge.Mongo('Tags', 'Insert', Para, function () {
                var Json = { status: '0', data: '插入成功' };
                Response.json(Json);
            });
        });
    });

    App.post('/TagRead/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            Monge.Mongo('Tags', 'Read', {}, function (Result) {
                var Json = { status: '0', data: Result };
                Response.json(Json);
            });
        });
    });

    App.post('/TagDelete/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var Object = {};
            Object._id = ObjectId(Para._id);
            Monge.Mongo('Tags', 'Delete', Object, function () {
                var Json = { status: '0', data: '标签删除成功' };
                Response.json(Json);
            });
        });
    });

    App.post('/TagEdit/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var WhereId = {}, UpdataStr = { $set: {} };
            if (!Para.TagId) {
                delete Para.TagId;
                Monge.Mongo('Tags', 'Insert', Para, function () {
                    var Json = { status: '0', data: '插入成功' };
                    Response.json(Json);
                });
            } else {
                WhereId._id = ObjectId(Para.TagId);
                UpdataStr.$set.TagName = Para.TagName;
                UpdataStr.$set.TagNo = Para.TagNo;
                Monge.Mongo('Tags', 'Update', [WhereId, UpdataStr], function (Result) {
                    var Json = { status: '0' };
                    Json.data = 'Update Success';
                    Response.json(Json);
                });
            }
        });
    });

    //友链相关
    App.post('/FriendUrlCreate/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var adress = util.isXssString(Para.FriendUrlAdress),
                descript = util.isXssString(Para.FriendUrlDescript),
                iconUrl = util.isXssString(Para.FriendUrlIcoUrl),
                nickName = util.isXssString(Para.FriendUrlNickName);

            if (adress && descript && iconUrl && nickName) {
                Monge.Mongo('FriendsUrl', 'Insert', Para, function () {
                    var Json = { status: '0', data: '插入成功' };
                    Response.json(Json);
                });
            } else {
                var Json = { status: '1', data: '入参有XSS风险，不予通过' };
                Response.json(Json);
            }
        });
    });
    // 友链数量
    App.post('/getfriendurlnum', function (Request, Response) {
        Monge.Mongo('FriendsUrl', 'GetNum', {}, function (Result) {
            var Json = { status: '0', data: Result };
            Response.json(Json);
        });
    });
    App.post('/FriendUrlRead/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var PagnationData = Para.PagnationData ? Para.PagnationData : { SKip: '', Limit: '' };
            Monge.Mongo('FriendsUrl', 'ReadByOrder', [{}, { _id: -1 }, PagnationData], function (Result) {
                var Json = { status: '0', data: Result };
                Response.json(Json);
            });
        });
    });
    App.post('/FriendUrlEditor/:accesstype', function (Request, Response) {
        var WhereId = {}, UpdateStr = { $set: {} };

        DealPara(Request, Response, function (Para) {
            if (!Para._id) {
                delete Para._id;

                Monge.Mongo('FriendsUrl', 'Insert', Para, function () {
                    var Json = { status: '0', data: '插入成功' };
                    Response.json(Json);
                });
            } else {
                WhereId._id = ObjectId(Para._id);
                delete Para._id;
                UpdateStr.$set = Para;
                Monge.Mongo('FriendsUrl', 'Update', [WhereId, UpdateStr], function (Result) {
                    var Json = { status: '0' };
                    Json.data = 'Update Success';
                    Response.json(Json);
                });
            }
        });
    });
    // 删除友链
    App.post('/FriendUrlDelete/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var Object = {};
            Object._id = ObjectId(Para._id);

            Monge.Mongo('FriendsUrl', 'Delete', Object, function () {
                var Json = { status: '0', data: '友链删除成功' };
                Response.json(Json);
            });
        });
    });
}

module.exports = function(app){
    option = app;
    return pageApi;
}
