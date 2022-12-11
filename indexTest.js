/**
 * author: sunquan
 * 本文件实现孙权的博客的所有接口
 */
 var express = require('express');
 var os = require('os');
 
 var FS = require('fs');
 var App = express();
 var cors = require('cors');
 var BodyParse = require('body-parser');
 
let blogIndex = require("./page/blogIndex.js")(App);
let comment = require("./page/comment.js")(App);
let heartFelt = require("./page/heartFelt.js")(App);
let messageCreate = require("./page/messageCreate.js")(App);
let timeLine = require("./page/timeLine.js")(App);
let user = require("./page/user.js")(App);
let visitCount = require("./page/visitCount.js")(App);

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
 
var server = App.listen(8088, function () {
 
     var host = server.address().address
     var port = server.address().port
 
     console.log("Node执行地址 http://%s:%s", host, port)
 
 });
 // 静态资源路径
 App.use(express.static('Public'));