let Monge = require("../Public/module/Mongo");

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
    var Token = require('../Public/module/token');

    Monge('Users', 'Read', { CnName: 'sunq' }, function (Result) {
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

module.exports = {DealPara:DealPara,GetPara:GetPara,GetParaCheckToken:GetParaCheckToken};