let option;

let pageApi = function () {
    let App = option,
        dealObj = require("./commonFunction"),
        Monge = require('../Mongo'),
        Token = require('../token'),
        GetParaCheckToken = dealObj.GetParaCheckToken,
        GetPara = dealObj.GetPara;


    /*用户管理相关*/
    App.post('/UserCreate', function (Request, Response) {
        GetParaCheckToken(Request, Response, function (Para) {
            Monge('Users', 'Insert', Para, function () {
                var Json = { status: '0', data: '用户新建成功' };
                Response.json(Json);
            });
        });
    });

    App.post('/UserReadOne', function (Request, Response) {
        GetPara(Request, Response, function (Para) {
            var Key = { CnName: Para.CnName, UserType: Para.UserType };
            Monge('Users', 'Read', Key, function (Result) {
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

}

module.exports = function (app) {
    option = app;
    return pageApi;
}

