let option;

let pageApi = function () {
    let App = option,
        dealObj = require("./commonFunction"),
        DealPara = dealObj.DealPara,
        ObjectId = require('mongodb').ObjectId,
        Monge = require('../Public/module/Mongo');

    App.post('/getScore/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            let stuId = Para.stuId;
            // console.log('stuid',stuId);
            const xlsx = require('node-xlsx');
            const path = require('path');

            // console.log('xlsx',xlsx);
    
            const filePath = path.join(__dirname, '../Public/module/scores.xlsx'); // Excel文件路径
            const sheetIndex = 0; // Excel文件中的表格索引，从0开始
    
            // 解析Excel文件
            const workSheets = xlsx.parse(filePath);

            // console.log('workSheets',workSheets);
    
            // 获取指定表格
            const sheet = workSheets[sheetIndex];
    
            // 获取表格数据
            const data = sheet.data;
    
            // 查询数据
            const result = data.filter(row => row[0] === stuId);
            Response.json(result);
        });
    });
}

module.exports = function (app) {
    option = app;
    return pageApi;
}