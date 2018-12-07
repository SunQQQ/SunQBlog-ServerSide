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

App.use(cors());
App.use(BodyParse.json());
App.use(BodyParse.urlencoded({extended:true}));

/*
根据路由参数判断是前端、后端接口
如果是前端接口，接收参数，执行操作数据库的方法
如何是后端接口，接收参数，判断token，执行操作数据库方法

如果不需要验证就用foreend，如果需要验证就用backend
*/
var DealPara = function (Request,Response,OperationResponse) {
  if(Request.params.accesstype == 'foreend'){
    // 前端有时也要传递过来参数
    GetPara(Request,Response,OperationResponse);
  }else if(Request.params.accesstype == 'backend'){
    // 后端肯定要接收参数，token是肯定要接收的
    GetParaCheckToken(Request,Response,OperationResponse);
  }
}

// 获取传递的参数
var GetPara = function (Request,Response,OperationResponse) {
  var Para = Request.body;

  if(JSON.stringify(Para) == '{}'){
    OperationResponse();
  }else {
    OperationResponse(Para);
  }
}

// 获取传递的参数、并验证token
var GetParaCheckToken = function (Request,Response,OperationResponse) {
  var Para = Request.body;

  if(Para.Token && Token.token.checkToken(Para.Token) == true){
    OperationResponse(Para);
  }else if(Para.Token && Token.token.checkToken(Para.Token) == 'TimeOut'){
    var Json = {status: '1', data: {message:'令牌超时'}};
    Response.json(Json);
  } else if(Para.Token && Token.token.checkToken(Para.Token) == false){
    var Json = {status: '1', data: {message:'令牌有误'}};
    Response.json(Json);
  }else if(!Para.Token){
    var Json = {status: '1', data: {message:'无Token，请登录'}};
    Response.json(Json);
  }else {
    var Json = {status: '1', data: {message: 'nothing'}};
    Response.json(Json);
  }
}

/*文章管理相关*/
App.post('/ArticleRead/:accesstype', function (req, res) {
  DealPara(req,res,function (Para) {
    var Key = Para.ArticleTag ? {ArticleTag:Para.ArticleTag} : {},
    PagnationData = Para.PagnationData ? Para.PagnationData : {SKip:0,Limit:10000};
    Monge.Mongo('runoob','ReadByOrder', [Key,{CreateDate:-1},PagnationData], function (Result) {
      var Json = {status: '0', data: Result};
      res.json(Json);
    });
  });
});

// 热门文章
App.post('/HotArticleRead/:accesstype', function (req, res) {
  DealPara(req,res,function (Para) {
    Monge.Mongo('runoob','ReadByOrder', [{},{CommentNum:-1},{Skip:0,Limit:6}], function (Result) {
      var Json = {status: '0', data: Result};
      res.json(Json);
    });
  });
});

App.post('/ArticleReadOne/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    var Key = {_id:ObjectId(Para._id)};
    Monge.Mongo('runoob','Read', Key, function (Result) {
      var Json = {status: '0'};
      Json.data = Result;
      Response.json(Json);
    });
  });
});

App.post('/AddArticle/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    Para.CommentNum = 0
    Monge.Mongo('runoob','Insert', Para, function () {
      var Json = {status: '0', data: '插入成功'};
      Response.json(Json);
    });
  });
});

App.post('/ArticleDelete/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    var IdObject = {_id:ObjectId(Para._id)};
    Monge.Mongo('runoob','Delete', IdObject, function () {
      var Json = {status: '0', data: '接口删除成功'};
      Response.json(Json);
    });
  });
});

App.post('/ArticleUpdate/:accesstype',function (Request,Response) {
  var WhereId = {}, UpdataStr = {$set: {}};

  DealPara(Request,Response,function (Para) {
    WhereId._id = ObjectId(Para._id);
    UpdataStr.$set.Title = Para.Title;
    UpdataStr.$set.Content = Para.Content;
    UpdataStr.$set.Summary = Para.Summary;
    UpdataStr.$set.CreateDate = Para.CreateDate;
    UpdataStr.$set.ArticleTag = Para.ArticleTag;
    UpdataStr.$set.ArticleCover = Para.ArticleCover;
    UpdataStr.$set.CommentNum = Para.CommentNum;
    Monge.Mongo('runoob','Update', [WhereId, UpdataStr], function (Result) {
      var Json = {status: '0'};
      Json.data = 'Update Success';
      Response.json(Json);
    });
  });
});

// 文章数量
App.post('/getarticlenum/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    var Key = Para ? (Para.ArticleTag ? {ArticleTag: Para.ArticleTag} : {}) : {};
    Monge.Mongo('runoob','GetNum',Key, function (Result) {
      var Json = {status: '0', data: Result};
      Response.json(Json);
    });
  });
});

//修改文章评论数
App.post('/ArticleCommentNumUpdate/:accesstype',function (Request,Response) {
  var WhereId = {}, UpdataStr = {$set: {}};

  DealPara(Request,Response,function (Para) {
    WhereId._id = ObjectId(Para._id);
    // 获取当前文章Id的当前评论数
    Monge.Mongo('runoob','Read',WhereId, function (CurrentNum) {
      UpdataStr.$set.CommentNum = CurrentNum[0].CommentNum + 1;
      Monge.Mongo('runoob','Update',[WhereId, UpdataStr],function () {
        var Json = {status: '0'};
        Json.data = 'ArticleCommentNum Update Success';
        Response.json(Json);
      });
    });
  });
});

/*上传图片*/
App.post('/UploadImg',function (Request,Response) {
  var From = new Formidable.IncomingForm();
  //设置保存 文件路径
  var TargetFile = Path.join(__dirname, './Public/');
  From.uploadDir = TargetFile;

  From.parse(Request,function (err, fields, files) {
    if (err) throw err;
    var FilePath = files.Content.path;

    var NewPath = Path.join(Path.dirname(FilePath), files.Content.name);
    FS.rename(FilePath, NewPath, function (err) {
      if (err) throw err;

      var MyJson = {
        errno: 0,
        data:['http://www.sunq.xyz:8888/' + files.Content.name]
      };
      Response.json(MyJson);
    });
  });
});

/*标签管理相关*/
App.post('/TagCreate/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    Monge.Mongo('Tags','Insert', Para, function () {
      var Json = {status: '0', data: '插入成功'};
      Response.json(Json);
    });
  });
});

App.post('/TagRead/:accesstype', function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    Monge.Mongo('Tags','Read', {}, function (Result) {
      var Json = {status: '0', data: Result};
      Response.json(Json);
    });
  });
});

App.post('/TagDelete/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    var Object = {};
    Object._id = ObjectId(Para._id);
    Monge.Mongo('Tags','Delete', Object, function () {
      var Json = {status: '0', data: '标签删除成功'};
      Response.json(Json);
    });
  });
});

App.post('/TagEdit/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    var WhereId = {}, UpdataStr = {$set: {}};
    if(!Para.TagId){
      delete Para.TagId;
      Monge.Mongo('Tags','Insert', Para, function () {
        var Json = {status: '0', data: '插入成功'};
        Response.json(Json);
      });
    }else {
      WhereId._id = ObjectId(Para.TagId);
      UpdataStr.$set.TagName = Para.TagName;
      UpdataStr.$set.TagNo = Para.TagNo;
      Monge.Mongo('Tags','Update', [WhereId, UpdataStr], function (Result) {
        var Json = {status: '0'};
        Json.data = 'Update Success';
        Response.json(Json);
      });
    }
  });
});

/*用户管理相关*/
App.post('/UserCreate',function (Request,Response) {
  GetParaCheckToken(Request,Response,function (Para) {
    Monge.Mongo('Users','Insert', Para, function () {
      var Json = {status: '0', data: '用户新建成功'};
      Response.json(Json);
    });
  });
});

App.post('/UserReadOne',function (Request,Response) {
  GetPara(Request,Response,function (Para) {
    var Key = {CnName:Para.CnName,UserType:Para.UserType};
    Monge.Mongo('Users','Read', Key, function (Result) {
      if(Result[0] && Result[0].PassWord == Para.PassWord){
        var NewToken = Token.token.createToken({},60*60);
        var Json = {
          status: '0',
          data:{
            Token:NewToken
          }
        };
        Response.json(Json);
      }else {
        var Json = {status: '1'};
        Response.json(Json);
      }
    });
  });
});

// 评论相关
App.post('/ArticleCommentCreate/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    Monge.Mongo('articlecomment','Insert', Para, function () {
      var Json = {status: '0', data: '添加评论成功'};
      Response.json(Json);
    });
  });
});
App.post('/ArticleCommentRead/:accesstype', function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    /*var Key = {ArticleId:ObjectId(Para.ArticleId)};*/
    var Key = {ArticleId:Para.ArticleId};
    Monge.Mongo('articlecomment','ReadByOrder',[Key,{ArticleCommentDate:-1}], function (Result) {
      var Json = {status: '0', data: Result};
      Response.json(Json);
    });
  });
});
// 评论个数
App.post('/getcommentnum',function (Request,Response) {
  Monge.Mongo('articlecomment','GetNum',{}, function (Result) {
    var Json = {status: '0', data: Result};
    Response.json(Json);
  });
});

// 留言页面相关
App.post('/MessageCreate/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    Monge.Mongo('LeaveMessage','Insert', Para, function () {
      var Json = {status: '0', data: '插入成功'};
      Response.json(Json);
    });
  });
});
App.post('/MessageRead/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    var PagnationData = Para.PagnationData ? Para.PagnationData : {SKip:0,Limit:1000};
    Monge.Mongo('LeaveMessage','ReadByOrder',[{},{MessageLeaveDate:-1},PagnationData], function (Result) {
      var Json = {status: '0', data: Result};
      Response.json(Json);
    });
  });
});
// 留言数量
App.post('/getmessagenum',function (Request,Response) {
  Monge.Mongo('LeaveMessage','GetNum',{}, function (Result) {
    var Json = {status: '0', data: Result};
    Response.json(Json);
  });
});

//友链相关
App.post('/FriendUrlCreate/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    Monge.Mongo('FriendsUrl','Insert', Para, function () {
      var Json = {status: '0', data: '插入成功'};
      Response.json(Json);
    });
  });
});
// 友链数量
App.post('/getfriendurlnum',function (Request,Response) {
  Monge.Mongo('FriendsUrl','GetNum',{}, function (Result) {
    var Json = {status: '0', data: Result};
    Response.json(Json);
  });
});
App.post('/FriendUrlRead/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    var PagnationData = Para.PagnationData ? Para.PagnationData : {SKip:'',Limit:''};
    Monge.Mongo('FriendsUrl','ReadByOrder',[{},{_id:-1},PagnationData], function (Result) {
      var Json = {status: '0', data: Result};
      Response.json(Json);
    });
  });
});
App.post('/FriendUrlEditor/:accesstype',function (Request,Response) {
  var WhereId = {},UpdateStr = {$set:{}};

  DealPara(Request,Response,function (Para) {
    if(!Para._id){
      delete Para._id;

      Monge.Mongo('FriendsUrl','Insert', Para, function () {
        var Json = {status: '0', data: '插入成功'};
        Response.json(Json);
      });
    }else{
      WhereId._id = ObjectId(Para._id);
      delete Para._id;
      UpdateStr.$set = Para;
      Monge.Mongo('FriendsUrl','Update', [WhereId, UpdateStr], function (Result) {
        var Json = {status: '0'};
        Json.data = 'Update Success';
        Response.json(Json);
      });
    }
  });
});
// 删除友链
App.post('/FriendUrlDelete/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    var Object = {};
    Object._id = ObjectId(Para._id);

    Monge.Mongo('FriendsUrl','Delete', Object, function () {
      var Json = {status: '0', data: '友链删除成功'};
      Response.json(Json);
    });
  });
});

// 新增时间轴
App.post('/TimeLineCreate/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    Monge.Mongo('TimeLine','Insert', Para, function () {
      var Json = {status: '0', data: '插入成功'};
      Response.json(Json);
    });
  });
});
// 获取时间轴
App.post('/TimeLineRead/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    Monge.Mongo('TimeLine','ReadByOrder',[{},{CreateDate:-1}], function (Result) {
      var Json = {status: '0', data: Result};
      Response.json(Json);
    });
  });
});

// 获取心声
App.post('/HeartfeltRead/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function () {
    Monge.Mongo('Heartfelt','ReadByOrder',[{},{CreateDate:-1}], function (Result) {
      var Json = {status: '0', data: Result};
      Response.json(Json);
    });
  });
});
//心声数量
App.post('/getheartfeltnum',function (Request,Response) {
  Monge.Mongo('Heartfelt','GetNum',{}, function (Result) {
    var Json = {status: '0', data: Result};
    Response.json(Json);
  });
});
// 新增和修改 心声
App.post('/HeartfeltEditor/:accesstype',function (Request,Response) {
  var WhereId = {}, UpdataStr = {$set: {}};

  DealPara(Request,Response,function (Para) {
    if(!Para._id){
      delete Para._id;

      Monge.Mongo('Heartfelt','Insert', Para, function () {
        var Json = {status: '0', data: '插入成功'};
        Response.json(Json);
      });
    }else{
      WhereId._id = ObjectId(Para._id);
      UpdataStr.$set.HeartfeltContent = Para.HeartfeltContent;
      UpdataStr.$set.HeartfeltWriter = Para.HeartfeltWriter;
      UpdataStr.$set.CreateDate = Para.CreateDate;
      Monge.Mongo('Heartfelt','Update', [WhereId, UpdataStr], function (Result) {
        var Json = {status: '0'};
        Json.data = 'Update Success';
        Response.json(Json);
      });
    }
  });
});
// 删除心声
App.post('/HeartfeltDelete/:accesstype',function (Request,Response) {
  DealPara(Request,Response,function (Para) {
    var Object = {};
    Object._id = ObjectId(Para._id);

    Monge.Mongo('Heartfelt','Delete', Object, function () {
      var Json = {status: '0', data: '标签删除成功'};
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
