/**
 * author: sunquan
 * 本文件实现孙权的博客的所有接口
 */
var express = require('express');

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

App.use(cors());
App.use(BodyParse.json());
App.use(BodyParse.urlencoded({extended: true}));

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

// 获取传递的参数、并验证token
var GetParaCheckToken = function (Request, Response, OperationResponse) {
    var Para = Request.body;

    if (Para.Token && Token.token.checkToken(Para.Token) == true) {
        OperationResponse(Para);
    } else if (Para.Token && Token.token.checkToken(Para.Token) == 'TimeOut') {
        var Json = {status: '1', data: {message: '令牌超时'}};
        Response.json(Json);
    } else if (Para.Token && Token.token.checkToken(Para.Token) == false) {
        var Json = {status: '1', data: {message: '令牌有误'}};
        Response.json(Json);
    } else if (!Para.Token) {
        var Json = {status: '1', data: {message: '无Token，请登录'}};
        Response.json(Json);
    } else {
        var Json = {status: '1', data: {message: 'nothing'}};
        Response.json(Json);
    }
}

/*文章管理相关*/
App.post('/ArticleRead/:accesstype', function (req, res) {
    DealPara(req, res, function (Para) {
        var Key = Para.ArticleTag ? {ArticleTag: Para.ArticleTag} : {},
            PagnationData = Para.PagnationData ? Para.PagnationData : {SKip: 0, Limit: 10000};
        Monge.Mongo('runoob', 'ReadByOrder', [Key, {CreateDate: -1}, PagnationData], function (Result) {
            var Json = {status: '0', data: Result};
            res.json(Json);
        });
    });
});

// 热门文章
App.post('/HotArticleRead/:accesstype', function (req, res) {
    DealPara(req, res, function (Para) {
        Monge.Mongo('runoob', 'ReadByOrder', [{}, {CommentNum: -1}, {Skip: 0, Limit: 6}], function (Result) {
            var Json = {status: '0', data: Result};
            res.json(Json);
        });
    });
});

App.post('/ArticleReadOne/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Key = {_id: ObjectId(Para._id)};
        Monge.Mongo('runoob', 'Read', Key, function (Result) {
            var Json = {status: '0'};
            Json.data = Result;
            Response.json(Json);
        });
    });
});

App.post('/AddArticle/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        Para.CommentNum = 0
        Monge.Mongo('runoob', 'Insert', Para, function () {
            var Json = {status: '0', data: '插入成功'};
            Response.json(Json);
        });
    });
});

App.post('/ArticleDelete/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var IdObject = {_id: ObjectId(Para._id)};
        Monge.Mongo('runoob', 'Delete', IdObject, function () {
            var Json = {status: '0', data: '接口删除成功'};
            Response.json(Json);
        });
    });
});

App.post('/ArticleUpdate/:accesstype', function (Request, Response) {
    var WhereId = {}, UpdataStr = {$set: {}};

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
            var Json = {status: '0'};
            Json.data = 'Update Success';
            Response.json(Json);
        });
    });
});

// 文章数量
App.post('/getarticlenum/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Key = Para ? (Para.ArticleTag ? {ArticleTag: Para.ArticleTag} : {}) : {};
        Monge.Mongo('runoob', 'GetNum', Key, function (Result) {
            var Json = {status: '0', data: Result};
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
                data: ['http://www.sunq.xyz:8888/' + files.Content.name]
            };
            Response.json(MyJson);
        });
    });
});

/*标签管理相关*/
App.post('/TagCreate/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        Monge.Mongo('Tags', 'Insert', Para, function () {
            var Json = {status: '0', data: '插入成功'};
            Response.json(Json);
        });
    });
});

App.post('/TagRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        Monge.Mongo('Tags', 'Read', {}, function (Result) {
            var Json = {status: '0', data: Result};
            Response.json(Json);
        });
    });
});

App.post('/TagDelete/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Object = {};
        Object._id = ObjectId(Para._id);
        Monge.Mongo('Tags', 'Delete', Object, function () {
            var Json = {status: '0', data: '标签删除成功'};
            Response.json(Json);
        });
    });
});

App.post('/TagEdit/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var WhereId = {}, UpdataStr = {$set: {}};
        if (!Para.TagId) {
            delete Para.TagId;
            Monge.Mongo('Tags', 'Insert', Para, function () {
                var Json = {status: '0', data: '插入成功'};
                Response.json(Json);
            });
        } else {
            WhereId._id = ObjectId(Para.TagId);
            UpdataStr.$set.TagName = Para.TagName;
            UpdataStr.$set.TagNo = Para.TagNo;
            Monge.Mongo('Tags', 'Update', [WhereId, UpdataStr], function (Result) {
                var Json = {status: '0'};
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
            var Json = {status: '0', data: '用户新建成功'};
            Response.json(Json);
        });
    });
});

App.post('/UserReadOne', function (Request, Response) {
    GetPara(Request, Response, function (Para) {
        var Key = {CnName: Para.CnName, UserType: Para.UserType};
        Monge.Mongo('Users', 'Read', Key, function (Result) {
            if (Result[0] && Result[0].PassWord == Para.PassWord) {
                var NewToken = Token.token.createToken({}, 60 * 60);
                var Json = {
                    status: '0',
                    data: {
                        Token: NewToken
                    }
                };
                Response.json(Json);
            } else {
                var Json = {status: '1'};
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

        if(city && date && user && text){
            Monge.Mongo('LeaveMessage', 'Insert', Para, function () {
                var Json = {status: '0', data: '插入成功'};
                Response.json(Json);
            });
        }else {
            var Json = {status: '1', data: '有xss风险，不予通过'};
            Response.json(Json);
        }
    });
});
App.post('/MessageRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var PagnationData = Para.PagnationData ? Para.PagnationData : {SKip: 0, Limit: 1000};
        Monge.Mongo('LeaveMessage', 'ReadByOrder', [{}, {MessageLeaveDate: -1}, PagnationData], function (Result) {
            var Json = {status: '0', data: Result};
            Response.json(Json);
        });
    });
});
App.post('/MessageLeaveDelete/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Object = {};
        Object._id = ObjectId(Para._id);

        Monge.Mongo('LeaveMessage', 'Delete', Object, function () {
            var Json = {status: '0', data: '友链删除成功'};
            Response.json(Json);
        });
    });
});

App.post('/MessageLeaveEdit/:accesstype', function (Request, Response) {
    var WhereId = {}, UpdateStr = {$set: {}};

    DealPara(Request, Response, function (Para) {
        WhereId._id = ObjectId(Para._id);
        delete Para._id;
        UpdateStr.$set = Para;
        Monge.Mongo('LeaveMessage', 'Update', [WhereId, UpdateStr], function (Result) {
            var Json = {status: '0'};
            Json.data = 'Update Success';
            Response.json(Json);
        });
    });
});
// 留言数量
App.post('/getmessagenum', function (Request, Response) {
    Monge.Mongo('LeaveMessage', 'GetNum', {}, function (Result) {
        var Json = {status: '0', data: Result};
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

        if(adress && descript && iconUrl && nickName){
            Monge.Mongo('FriendsUrl', 'Insert', Para, function () {
                var Json = {status: '0', data: '插入成功'};
                Response.json(Json);
            });
        }else {
            var Json = {status: '1', data: '入参有XSS风险，不予通过'};
            Response.json(Json);
        }
    });
});
// 友链数量
App.post('/getfriendurlnum', function (Request, Response) {
    Monge.Mongo('FriendsUrl', 'GetNum', {}, function (Result) {
        var Json = {status: '0', data: Result};
        Response.json(Json);
    });
});
App.post('/FriendUrlRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var PagnationData = Para.PagnationData ? Para.PagnationData : {SKip: '', Limit: ''};
        Monge.Mongo('FriendsUrl', 'ReadByOrder', [{}, {_id: -1}, PagnationData], function (Result) {
            var Json = {status: '0', data: Result};
            Response.json(Json);
        });
    });
});
App.post('/FriendUrlEditor/:accesstype', function (Request, Response) {
    var WhereId = {}, UpdateStr = {$set: {}};

    DealPara(Request, Response, function (Para) {
        if (!Para._id) {
            delete Para._id;

            Monge.Mongo('FriendsUrl', 'Insert', Para, function () {
                var Json = {status: '0', data: '插入成功'};
                Response.json(Json);
            });
        } else {
            WhereId._id = ObjectId(Para._id);
            delete Para._id;
            UpdateStr.$set = Para;
            Monge.Mongo('FriendsUrl', 'Update', [WhereId, UpdateStr], function (Result) {
                var Json = {status: '0'};
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
            var Json = {status: '0', data: '友链删除成功'};
            Response.json(Json);
        });
    });
});

// 新增时间轴
App.post('/TimeLineCreate/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        Monge.Mongo('TimeLine', 'Insert', Para, function () {
            var Json = {status: '0', data: '插入成功'};
            Response.json(Json);
        });
    });
});
// 获取时间轴
App.post('/TimeLineRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        Monge.Mongo('TimeLine', 'ReadByOrder', [{}, {CreateDate: -1}], function (Result) {
            var Json = {status: '0', data: Result};
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
            var Json = {status: '0', data: '时间轴删除成功'};
            Response.json(Json);
        });
    });
});

// 获取心声
App.post('/HeartfeltRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function () {
        Monge.Mongo('Heartfelt', 'ReadByOrder', [{}, {CreateDate: -1}], function (Result) {
            var Json = {status: '0', data: Result};
            Response.json(Json);
        });
    });
});
//心声数量
App.post('/getheartfeltnum', function (Request, Response) {
    Monge.Mongo('Heartfelt', 'GetNum', {}, function (Result) {
        var Json = {status: '0', data: Result};
        Response.json(Json);
    });
});
// 新增和修改 心声
App.post('/HeartfeltEditor/:accesstype', function (Request, Response) {
    var WhereId = {}, UpdataStr = {$set: {}};

    DealPara(Request, Response, function (Para) {
        if (!Para._id) {
            delete Para._id;

            Monge.Mongo('Heartfelt', 'Insert', Para, function () {
                var Json = {status: '0', data: '插入成功'};
                Response.json(Json);
            });
        } else {
            WhereId._id = ObjectId(Para._id);
            UpdataStr.$set.HeartfeltContent = Para.HeartfeltContent;
            UpdataStr.$set.HeartfeltWriter = Para.HeartfeltWriter;
            UpdataStr.$set.CreateDate = Para.CreateDate;
            Monge.Mongo('Heartfelt', 'Update', [WhereId, UpdataStr], function (Result) {
                var Json = {status: '0'};
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
            var Json = {status: '0', data: '标签删除成功'};
            Response.json(Json);
        });
    });
});

//获取访问者Ip
App.post('/GetUserIp', function (Request, Response) {
    var IpAdress = Request.headers['x-forwarded-for'] ||
        Request.connection.remoteAddress ||
        Request.socket.remoteAddress ||
        Request.connection.socket.remoteAddress;
    var Json = {
        status: '0',
        data: {
            IpAdress: IpAdress
        }
    };
    Response.json(Json);
});

// 所有评论列表
App.post('/CommentRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var PagnationData = Para.PagnationData ? Para.PagnationData : {SKip: '', Limit: ''};
        Monge.Mongo('articlecomment', 'ReadByOrder', [{}, {_id: -1}, PagnationData], function (Result) {
            var Json = {status: '0', data: Result};
            Response.json(Json);
        });
    });
});

//评论总数
App.post('/getCommentNum', function (Request, Response) {
    Monge.Mongo('articlecomment', 'GetNum', {}, function (Result) {
        var Json = {status: '0', data: Result};
        Response.json(Json);
    });
});

// 删除评论
App.post('/CommentDelete/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Object = {};
        Object._id = ObjectId(Para._id);

        Monge.Mongo('articlecomment', 'Delete', Object, function () {
            var Json = {status: '0', data: '标签删除成功'};
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
            cityName = util.isArray(Para.LocationCityName);

        if(date && email && nickName && text && url && id && cityName){
            Monge.Mongo('articlecomment', 'Insert', Para, function () {
                var Json = {status: '0', data: '添加评论成功'};
                Response.json(Json);
            });
        }else {
            var Json = {status: '1', data: '有xss风险，不予通过'};
            Response.json(Json);
        }
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
                var Json = {status: '0'};
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
        var Key = {ArticleId: Para.ArticleId};
        Monge.Mongo('articlecomment', 'ReadByOrder', [Key], function (Result) {
            var Json = {status: '0', data: Result};
            Response.json(Json);
        });
    });
});
// 评论个数
App.post('/getcommentnum', function (Request, Response) {
    Monge.Mongo('articlecomment', 'GetNum', {}, function (Result) {
        var Json = {status: '0', data: Result};
        Response.json(Json);
    });
});

// 创建访问记录
App.post('/visitCreate/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        Monge.Mongo('VisitList', 'Insert', Para, function () {
            var Json = {status: '0', data: '插入成功'};
            Response.json(Json);
        });
    });
});

// 获取访问记录
App.post('/visitRead/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var PagnationData = Para.PagnationData ? Para.PagnationData : {SKip: 0, Limit: 10000};

        Monge.Mongo('VisitList', 'ReadByOrder', [{},{_id: -1},PagnationData], function (Result) {
            var Json = {status: '0', data: Result};
            Response.json(Json);
        });
    });
});

// 删除访问记录
App.post('/visitDelete/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (Para) {
        var Object = {};
        Object._id = ObjectId(Para._id);

        Monge.Mongo('VisitList', 'Delete', Object, function () {
            var Json = {status: '0', data: '访客记录删除成功'};
            Response.json(Json);
        });
    });
});

/** 访问统计接口
 * 传入最后一天，及需要的天数。返回传入日期前每一天的访问量
 */
App.post('/visitCount/:accesstype', function (Request, Response) {
    DealPara(Request, Response, function (para) {
        let endTime = para.endTime, //20211124 从前端获取
            dayNum = para.dayNum,//7 从前端获取
            // 处理从前端获取的数据
            endTimeObject = new Date(endTime), //
            endTimeAddOneObject = new Date(endTimeObject.getTime() + 1*24*60*60*1000),
            endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth()+1) + '/' + endTimeAddOneObject.getDate(),
            beginTimeObject = new Date(endTimeObject.getTime() - (dayNum-1)*24*60*60*1000), //开始时间由结束时间向前推得出
            beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth()+1) + '/' + beginTimeObject.getDate(),
            //此变量为mongodb查询时使用
            newPara = {'time':{$gt:beginTime,$lt:endTimeAddOne}}, // mongodb语法要求结束时间需要加一天
            // 拿到库里数据后，node遍历计算次数
            dateArray = [], // 时间数组
            dayObject; //临时使用的变量

        for(let i=0;i<dayNum;i++){
            dayObject = new Date(endTimeObject.getTime() - i*24*60*60*1000);
            dateArray.push(dayObject.getFullYear() + '/' + (dayObject.getMonth()+1) + '/' + dayObject.getDate());
        }

        Monge.Mongo('VisitList', 'Read', newPara, function (Result) {
            let dateCountList = [];
            for(let i=0;i<dateArray.length;i++){
                let object = new Object();
                object.time = dateArray[i];
                object.reading = 0;
                for(let m=0;m<Result.length;m++){
                    if(Result[m].time.split(' ')[0] == dateArray[i]){
                        object.reading += 1;
                    }
                }
                dateCountList.push(object);
            }

            var Json = {
                status: '0',
                data: {
                    dateCountList:dateCountList,
                    dateList:Result
                }
            };
            Response.json(Json);
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