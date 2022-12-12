let option;

let pageApi = function () {
    let App = option,
        dealObj = require("./commonFunction"),
        DealPara = dealObj.DealPara,
        ObjectId = require('mongodb').ObjectId,
        Monge = require('../Mongo');
        
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
}

module.exports = function (app) {
    option = app;
    return pageApi;
}