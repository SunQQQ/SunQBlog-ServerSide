/**
 * author: sunquan
 * 本文件实现孙权的博客的所有接口
 */
var express = require('express');
var os = require('os');

var Token = require('./token');
var Monge = require('./Mongo');
var ObjectId = require('mongodb').ObjectId;
var Path = require("path");
var Formidable = require("formidable");
var FS = require('fs');
var App = express();
var cors = require('cors');
var BodyParse = require('body-parser');
var Util = require('./util');
var util = new Util();
// 数据库相关
var MongoClient = require("mongodb").MongoClient;
var Url = "mongodb://localhost:27017/";

App.use(cors());
App.use(BodyParse.json());
App.use(BodyParse.urlencoded({ extended: true }));

/*
根据路由参数判断是前端、后端接口
如果是前端接口，接收参数，执行操作数据库的方法
如何是后端接口，接收参数，判断token，执行操作数据库方法
如果不需要验证就用foreend，如果需要验证就用backend
*/
var DealPara = function (Request, Response, OperationResponse) {
    if (Request.params.accesstype == 'foreend') {
        // 前端有时也要传递过来参数
        GetPara(Request, Response, OperationResponse);
    } else if (Request.params.accesstype == 'backend') {
        // 后端肯定要接收参数，token是肯定要接收的
        GetParaCheckToken(Request, Response, OperationResponse);
    }
}

// 获取传递的参数
var GetPara = function (Request, Response, OperationResponse) {
    var Para = Request.body;

    if (JSON.stringify(Para) == '{}') {
        OperationResponse();
    } else {
        OperationResponse(Para);
    }
}

// 获取传递的参数、并验证token。在增删改查接口中使用，还要求必须是管理员账户
var GetParaCheckToken = function (Request, Response, OperationResponse) {
    var Para = Request.body;

    Monge.Mongo('Users', 'Read', { CnName: 'sunq' }, function (Result) {
        // token通过，并且token中的id等于sunq账号的id，才能操作
        if (Para.Token && Token.token.checkToken(Para.Token) && Token.token.getId(Para.Token) == Result[0]._id) {
            OperationResponse(Para);
        } else if (Para.Token && Token.token.checkToken(Para.Token) == 'TimeOut') {
            var Json = { status: '1', data: { message: '令牌超时' } };
            Response.json(Json);
        } else if (Para.Token && !Token.token.checkToken(Para.Token)) {
            var Json = { status: '1', data: { message: '令牌有误' } };
            Response.json(Json);
        } else if (!Para.Token) {
            var Json = { status: '1', data: { message: '无Token，请登录' } };
            Response.json(Json);
        } else if (Para.Token && Token.token.checkToken(Para.Token) && Token.token.getId(Para.Token) != Result[0]._id) {
            var Json = { status: '2', data: { message: '权限不足，无法操作数据' } };
            Response.json(Json);
        } else {
            var Json = { status: '1', data: { message: 'nothing' } };
            Response.json(Json);
        }
    });
}

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

/*用户管理相关*/
App.post('/UserCreate', function (Request, Response) {
    GetParaCheckToken(Request, Response, function (Para) {
        Monge.Mongo('Users', 'Insert', Para, function () {
            var Json = { status: '0', data: '用户新建成功' };
            Response.json(Json);
        });
    });
});

App.post('/UserReadOne', function (Request, Response) {
    GetPara(Request, Response, function (Para) {
        var Key = { CnName: Para.CnName, UserType: Para.UserType };
        Monge.Mongo('Users', 'Read', Key, function (Result) {
            // 账号密码通过后，将该用户的id放在token中
            if (Result[0] && Result[0].PassWord == Para.PassWord) {
                var NewToken = Token.token.createToken(Result[0]._id, 60 * 60);
                var Json = {
                    status: '0',
                    data: {
                        Token: NewToken
                    }
                };
                Response.json(Json);
            } else {
                var Json = { status: '1' };
                Response.json(Json);
            }
        });
    });
});

// 留言页面相关
App.post('/MessageCreate/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var city = util.isXssString(Para.LocationCityName),
            date = util.isXssString(Para.MessageLeaveDate),
            user = util.isXssString(Para.MessageLeaveName),
            text = util.isXssStringLeaveMessage(Para.MessageText);

        if (city && date && user && text) {
            Monge.Mongo('LeaveMessage', 'Insert', Para, function () {
                var Json = { status: '0', data: '插入成功' };
                Response.json(Json);
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
        Monge.Mongo('LeaveMessage', 'ReadByOrder', [{}, { MessageLeaveDate: -1 }, PagnationData], function (Result) {
            var Json = { status: '0', data: Result };
            Response.json(Json);
        });
    });
});
App.post('/MessageLeaveDelete/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Object = {};
        Object._id = ObjectId(Para._id);

        Monge.Mongo('LeaveMessage', 'Delete', Object, function () {
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
        Monge.Mongo('LeaveMessage', 'Update', [WhereId, UpdateStr], function (Result) {
            var Json = { status: '0' };
            Json.data = 'Update Success';
            Response.json(Json);
        });
    });
});
// 留言数量
App.post('/getmessagenum', function (Request, Response) {
    Monge.Mongo('LeaveMessage', 'GetNum', {}, function (Result) {
        var Json = { status: '0', data: Result };
        Response.json(Json);
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

// 新增时间轴
App.post('/TimeLineCreate/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        Monge.Mongo('TimeLine', 'Insert', Para, function () {
            var Json = { status: '0', data: '插入成功' };
            Response.json(Json);
        });
    });
});
// 获取时间轴
App.post('/TimeLineRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        Monge.Mongo('TimeLine', 'ReadByOrder', [{}, { CreateDate: -1 }], function (Result) {
            var Json = { status: '0', data: Result };
            Response.json(Json);
        });
    });
});
// 删除时间轴
App.post('/TimeLineDelete/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Object = {};
        Object._id = ObjectId(Para._id);

        Monge.Mongo('TimeLine', 'Delete', Object, function () {
            var Json = { status: '0', data: '时间轴删除成功' };
            Response.json(Json);
        });
    });
});

// 获取心声
App.post('/HeartfeltRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function () {
        Monge.Mongo('Heartfelt', 'ReadByOrder', [{}, { CreateDate: -1 }], function (Result) {
            var Json = { status: '0', data: Result };
            Response.json(Json);
        });
    });
});
//心声数量
App.post('/getheartfeltnum', function (Request, Response) {
    Monge.Mongo('Heartfelt', 'GetNum', {}, function (Result) {
        var Json = { status: '0', data: Result };
        Response.json(Json);
    });
});
// 新增和修改 心声
App.post('/HeartfeltEditor/:accesstype', function (Request, Response) {
    var WhereId = {}, UpdataStr = { $set: {} };

    DealPara(Request, Response, function (Para) {
        if (!Para._id) {
            delete Para._id;

            Monge.Mongo('Heartfelt', 'Insert', Para, function () {
                var Json = { status: '0', data: '插入成功' };
                Response.json(Json);
            });
        } else {
            WhereId._id = ObjectId(Para._id);
            UpdataStr.$set.HeartfeltContent = Para.HeartfeltContent;
            UpdataStr.$set.HeartfeltWriter = Para.HeartfeltWriter;
            UpdataStr.$set.CreateDate = Para.CreateDate;
            Monge.Mongo('Heartfelt', 'Update', [WhereId, UpdataStr], function (Result) {
                var Json = { status: '0' };
                Json.data = 'Update Success';
                Response.json(Json);
            });
        }
    });
});
// 删除心声
App.post('/HeartfeltDelete/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Object = {};
        Object._id = ObjectId(Para._id);

        Monge.Mongo('Heartfelt', 'Delete', Object, function () {
            var Json = { status: '0', data: '标签删除成功' };
            Response.json(Json);
        });
    });
});

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

// 创建访问记录
App.post('/visitCreate/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        Monge.Mongo('VisitList', 'Insert', Para, function () {
            var Json = { status: '0', data: '插入成功' };
            Response.json(Json);
        });
    });
});

/**
 * 获取访问记录，管理后台首页使用
 * 直接根据分页数据查询 
 */
App.post('/visitRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var PagnationData = Para.PagnationData ? Para.PagnationData : { SKip: 0, Limit: 10000 };

        Monge.Mongo('VisitList', 'ReadByOrder', [{}, { _id: -1 }, PagnationData], function (Result) {
            // 保护用户的IP地址，打上马赛克
            Result.forEach(function (item) {
                if (item.clientIp) {
                    let array = item.clientIp.split('.');
                    item.clientIp = array[0] + '.' + array[1] + '.' + array[2] + '.***';
                }
            });

            Monge.Mongo('VisitList', 'GetNum', {}, function (totalNum) {
                var Json = {
                    status: '0',
                    data: {
                        list: Result,   // 当前分页下的数据
                        totalNum: totalNum   // 所有数据
                    }
                };
                Response.json(Json);
            });
        });
    });
});

// 删除访问记录
App.post('/visitDelete/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Object = {};
        Object._id = ObjectId(Para._id);

        Monge.Mongo('VisitList', 'Delete', Object, function () {
            var Json = { status: '0', data: '访客记录删除成功' };
            Response.json(Json);
        });
    });
});

/** 访问统计接口
 * 传入最后一天，及需要的天数。返回传入日期前每一天的访问量\ip数
 * 传入(‘2021/12/11’，3)
 * 返回 [
 * {time:‘2021/12/09’,reading:5,ipNum:10},
 * {time:‘2021/12/10’,reading:2,ipNum:19},
 * {time:‘2021/12/11’,reading:15,ipNum:19}
 * ]
 */
App.post('/visitCount/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (para) {
        let endTime = para.endTime, //20211124 从前端获取
            dayNum = para.dayNum,//7 从前端获取
            // 处理从前端获取的数据
            endTimeObject = new Date(endTime), //
            endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
            endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
            beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
            beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

            //此变量为mongodb查询时使用
            newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } }, // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }
            // 拿到库里数据后，node遍历计算次数
            dateArray = []; // 时间数组

        // 生成数组[‘2021/12/09’,‘2021/12/10’,‘2021/12/11’,...]
        for (let i = 0; i < dayNum; i++) {
            let dayObject, day, month;
            dayObject = new Date(endTimeObject.getTime() - i * 24 * 60 * 60 * 1000);
            day = dayObject.getDate() < 10 ? '0' + dayObject.getDate() : dayObject.getDate();
            month = dayObject.getMonth() + 1 < 10 ? '0' + (dayObject.getMonth() + 1) : dayObject.getMonth() + 1;
            dateArray.push(dayObject.getFullYear() + '/' + month + '/' + day);
        }

        // 查出上面时间数组范围内所有的记录，然后遍历时间数组的每一天，跟记录对比，得出每一天的访问量
        Monge.Mongo('VisitList', 'Read', newPara, function (Result) {
            let dateCountList = [], // 符合该时间数组中所有时间的所有记录
                cityList = []; // 城市数组，供前端地图使用
            // 加入选中时间周期为30天，该时间周期下的日志一共是1460行。
            // 则统计每天的浏览量（即本接口），需要执行的遍历次数为30*1460=43800次
            for (let i = 0; i < dateArray.length; i++) {
                let ipArray = [], // 符合当前时间的ip
                    object = new Object();

                object.time = dateArray[i];
                object.reading = 0;

                Result.forEach(function (item) {
                    if (item.time.split(' ')[0] == dateArray[i]) {
                        object.reading += 1;
                        if (item.clientIp) ipArray.push(item.clientIp);
                    }
                    // 过滤掉重复的和名称是[]的城市，生成当前查询条件下的城市数组
                    if (cityList.indexOf(item.location) == -1 && typeof (item.location) == "string") {
                        cityList.push(item.location);
                    }
                });

                object.ipNum = util.dedupe(ipArray).length;

                dateCountList.push(object);
            }

            var Json = {
                status: '0',
                data: {
                    dateCountList: dateCountList, // 数据结果类似=> [{time: "2022/01/08", reading: 25},{time: "2022/01/09", reading: 30}],供折线图使用
                    cityList: cityList, // 数据结果为库里记录直接返回，供地图使用
                }
            };
            Response.json(Json);
        });
    });
});

/**
 * 
 */
 App.post('/messageCommentCount/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (para) {
        let endTime = para.endTime, //20211124 从前端获取
            dayNum = para.dayNum,//7 从前端获取
            // 处理从前端获取的数据
            endTimeObject = new Date(endTime), //
            endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
            endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
            beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
            beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

            //此变量为mongodb查询时使用
            newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } }, // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }
            // 拿到库里数据后，node遍历计算次数
            dateArray = []; // 时间数组

        // 生成数组[‘2021/12/09’,‘2021/12/10’,‘2021/12/11’,...]
        for (let i = 0; i < dayNum; i++) {
            let dayObject, day, month;
            dayObject = new Date(endTimeObject.getTime() - i * 24 * 60 * 60 * 1000);
            day = dayObject.getDate() < 10 ? '0' + dayObject.getDate() : dayObject.getDate();
            month = dayObject.getMonth() + 1 < 10 ? '0' + (dayObject.getMonth() + 1) : dayObject.getMonth() + 1;
            dateArray.push(dayObject.getFullYear() + '/' + month + '/' + day);
        }

        // 查出上面时间数组范围内所有的记录，然后遍历时间数组的每一天，跟记录对比，得出每一天的访问量
        Monge.Mongo('LeaveMessage', 'Read', newPara, function (Result) {
            let dateCountList = [], // 符合该时间数组中所有时间的所有记录
                cityList = []; // 城市数组，供前端地图使用

            var Json = {
                status: '0',
                data: {
                    dateCountList: dateCountList, // 数据结果类似=> [{time: "2022/01/08", reading: 25},{time: "2022/01/09", reading: 30}],供折线图使用
                    cityList: cityList, // 数据结果为库里记录直接返回，供地图使用
                }
            };
            Response.json(Json);
        });
    });
});

// 汇总每个ip的操作行为
App.post('/getUserAction/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (para) {
        let endTime = para.endTime, // 2021/12/11 从前端获取
            dayNum = para.dayNum,//7 从前端获取
            dayArray = util.getDateArray(endTime, dayNum), // 前推指定天数，类似[‘2021/12/09’,‘2021/12/10’,‘2021/12/11’]
            nextDay = util.getOneDate(endTime, 1),  // 输出 2021/12/12
            nodePara = { 'time': { $gt: dayArray[0], $lt: nextDay } },

            ipArray = [], // ip数组 [ip1,ip2,ip3]
            userAction = {}; // { ip1:{action:[]}, ip2:{action:[]}}
        // 查出上面时间数组范围内所有的记录，然后遍历时间数组的每一天，跟记录对比，得出每一天的访问量
        Monge.Mongo('VisitList', 'Read', nodePara, function (Result) {
            Result.forEach(function (item) {
                let currentIp = item.clientIp;
                if (item.clientIp && ipArray.indexOf(currentIp) == -1) {
                    ipArray.push(currentIp);
                }
            });
            ipArray.reverse(); // 最新的日期放在前面
            // 生成userAction，是最终返回的数据     { ip1:{action:[]}, ip2:{action:[]}}
            ipArray.forEach(function (item) {
                userAction[item] = { action: [] };
            });

            // 遍历查询的数据，每一条都操作一遍。每一条都跟userAction比对，如果没有插入一次
            Result.forEach(function (item, i) {
                let currentIp = item.clientIp, // 当前数据可能没有ip字段
                    actionarray = currentIp ? userAction[currentIp].action : '', // 当前ip下的行为数组
                    actionText = item.operateType ? item.operateType + ':' + item.operateContent : ''; // 当条日志下的操作字段

                if (actionarray && actionText && actionarray.indexOf(actionText) == -1) { // 数组会过滤重复的操作，相同操作只会push一次
                    actionarray.push(actionText);
                }

                if (currentIp) {
                    userAction[currentIp].location = item.location ? item.location : '';
                    userAction[currentIp].browser = item.browser ? item.browser : '';
                    userAction[currentIp].time = item.time ? item.time : '';
                    if (item.fromUrl) userAction[currentIp].fromUrl = item.fromUrl;
                }
            });

            var Json = {
                status: '0',
                data: {
                    userAction: userAction,
                    dateListTotal: Result.length // 数据结果为库里记录直接返回，供地图使用
                }
            };
            Response.json(Json);
        });
    });
});

/**
 * 根据时间返回用户操作类型，用于分析用户操作占比
 */
App.post('/visitReadByDay/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (para) {
        let endTime = para.endTime, //20211124 从前端获取
            dayNum = para.dayNum,//7 从前端获取
            // 处理从前端获取的数据
            endTimeObject = new Date(endTime), //
            endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
            endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
            beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
            beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

            //此变量为mongodb查询时使用
            newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } }; // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }

        Monge.Mongo('VisitList', 'Read', newPara, function (Result) {
            let array = [];
            Result.forEach(function (item) {
                array.push(item.operateType);
            });

            var Json = {
                status: '0',
                data: {
                    list: array,   // 当前分页下的数据
                }
            };
            Response.json(Json);
        });
    });
});

/**
 * 根据时间返回点击菜单的数据，用于分析菜单访问占比
 */
App.post('/menuClickByDay/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (para) {
        let endTime = para.endTime, //20211124 从前端获取
            dayNum = para.dayNum,//7 从前端获取
            // 处理从前端获取的数据
            endTimeObject = new Date(endTime), //
            endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
            endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
            beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
            beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

            //此变量为mongodb查询时使用
            newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } }; // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }

        Monge.Mongo('VisitList', 'Read', newPara, function (Result) {
            let array = [],
                allMenuOperate = ['博文', '留言', '时间轴', '试验田', '关于', '访问统计', '管理后台'];
            Result.forEach(function (item) {
                if (allMenuOperate.indexOf(item.operateContent) > -1) {
                    array.push(item.operateContent);
                }
            });

            var Json = {
                status: '0',
                data: {
                    list: array,   // 当前分页下的数据
                }
            };
            Response.json(Json);
        });
    });
});

/**
 * 根据时间返回新老用户的数据，用于分析新老用户占比
 * 
 * 定义查询日期往前推8个月的数据为老用戶
 * 1.先查出查询这段时间所有IP，一天天的查再拼接起来；
 * 2.再查出这段时间前8个月所有访问过的ip
 * 3.最终匹配出1中，有多少ip是老访客
 */
App.post('/regularUserByDay/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (para) {
        let endTime = para.endTime, //20211124 从前端获取
            dayNum = para.dayNum,//7 从前端获取
            // 处理从前端获取的数据
            endTimeObject = new Date(endTime), //
            endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
            endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
            beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
            beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

            //此变量为mongodb查询时使用
            newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } },
            // 查询时间段往前推8个月。比如第一行是查询时间端14天，第二行是这个时间端往前推8个月
            // { '$gt': '2022/08/21', '$lt': '2022/09/04' } 注意mongodb最后一天不算，只查到0903
            // { '$gt': '2021/12/24', '$lt': '2022/08/21' }
            dayBefore8MonthObject = new Date(beginTimeObject.getTime() - 240*24*60*60*1000);
            dayBefore8Month = dayBefore8MonthObject.getFullYear() + '/' + (dayBefore8MonthObject.getMonth() + 1 < 10 ? '0' + (dayBefore8MonthObject.getMonth() + 1) : dayBefore8MonthObject.getMonth() + 1) + '/' + (dayBefore8MonthObject.getDate() < 10 ? '0' + dayBefore8MonthObject.getDate() : dayBefore8MonthObject.getDate()),
            before8Month = {'time':{$gt:dayBefore8Month,$lt:beginTime}},
            
            // 查询
            backField = {
                'clientIp': '1'
            }; // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }

        let selectedIp = [],
            before8MonthIp = [],
            dateArray = [],
            regularUserNum = 0;   

        // 生成数组[‘2021/12/09’,‘2021/12/10’,‘2021/12/11’,...]
        for (let i = 0; i < dayNum; i++) {
            let dayObject, day, month;
            dayObject = new Date(endTimeObject.getTime() - i * 24 * 60 * 60 * 1000);
            day = dayObject.getDate() < 10 ? '0' + dayObject.getDate() : dayObject.getDate();
            month = dayObject.getMonth() + 1 < 10 ? '0' + (dayObject.getMonth() + 1) : dayObject.getMonth() + 1;
            dateArray.push(dayObject.getFullYear() + '/' + month + '/' + day);
        }

        console.log(newPara);
        console.log(before8Month);
        console.log(dateArray);

        MongoClient.connect(Url, function (err, db) {
            var DB = db.db("test");
            DB.collection('VisitList').find(newPara,backField).toArray(function (err, res) {
                if(err) throw err;
        
                dateArray.forEach((day)=>{
                    let dayIp = []; //当天所有的ip
                    // 获取当天下所有ip，滤重
                    res.forEach((item)=>{
                        if(day==item.time.split(' ')[0] && item.clientIp) dayIp.push(item.clientIp);
                    });
                    dayIp = util.dedupe(dayIp); // 数组滤重

                    // 把每天的ip拼接起来，就是一段时间所有的IP。这些IP允许有重复，因为今天访问了，昨天也访问了，都算这段时间的访客
                    selectedIp = selectedIp.concat(dayIp);
                });
                
                

                DB.collection('VisitList').find(before8Month,backField).toArray(function (err, result) {
                    if(err) throw err;
                    db.close();

                    // 查询时间段往前推8个月内所有ip，且滤重过
                    result.forEach((item)=>{
                        if(item.clientIp) before8MonthIp.push(item.clientIp);
                    });
                    before8MonthIp = util.dedupe(before8MonthIp);

                    selectedIp.forEach((item)=>{
                        if(before8MonthIp.indexOf(item) > -1){
                            regularUserNum += 1;
                        }
                    })

                    var Json = {
                        status: '0',
                        data: {
                            regularUser: regularUserNum,
                            newUser: selectedIp.length - regularUserNum
                        }
                    };
                    Response.json(Json);
                });
            });
        });
    });
});


var server = App.listen(8888, function () {

    var host = server.address().address
    var port = server.address().port

    console.log("Node执行地址 http://%s:%s", host, port)

});
// 静态资源路径
App.use(express.static('Public'));