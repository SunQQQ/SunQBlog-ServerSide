let option;

let pageApi = function () {
    let App = option,
        dealObj = require("./commonFunction"),
        DealPara = dealObj.DealPara,
        ObjectId = require('mongodb').ObjectId,
        Monge = require('../Mongo');

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
}

module.exports = function (app) {
    option = app;
    return pageApi;
}
