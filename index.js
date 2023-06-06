/**
 * author: sunquan
 * 本文件实现孙权的博客的所有接口
 */
let express = require('express');
let App = express();
let cors = require('cors');
let BodyParse = require('body-parser');
// 各个页面使用的接口
let blogIndex = require("./page/blogIndex.js")(App);
let comment = require("./page/comment.js")(App);
let heartFelt = require("./page/heartFelt.js")(App);
let messageCreate = require("./page/messageCreate.js")(App);
let timeLine = require("./page/timeLine.js")(App);
let user = require("./page/user.js")(App);
let visitCount = require("./page/visitCount.js")(App);
let student = require("./page/student.js")(App);

App.use(cors());
App.use(BodyParse.json());
App.use(BodyParse.urlencoded({ extended: true }));

blogIndex();
comment();
heartFelt();
messageCreate();
timeLine();
user();
visitCount();
student();

let server = App.listen(8888, function () {
    let host = server.address().address
    let port = server.address().port

    console.log("Node执行地址 http://%s:%s", host, port)
});
// 静态资源路径
App.use(express.static('Public'));