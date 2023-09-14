/**
 * Created by sunquan on 2018/5/18.
 */
var MongoClient = require("mongodb").MongoClient;
var Url = "mongodb://localhost:27017/";

/**
 * node直接操作mongodb
 * @param Collection 要操作的表名
 * @param Type 要操作的类型，比如增删改查直接操作库
 * @param data 如果是只有一个参数，则直接传递该参数；如果有两个参数，传递数组
 * @param CallBack 操作成功的回调函数
 * @constructor
 */

function Mongo(Collection, Type, data, CallBack) {
  MongoClient.connect(Url, { useUnifiedTopology: true }, function (err, db) {
    var DB = db.db("test");
    if (Type == 'Insert') {
      DB.collection(Collection).insertOne(data, function (err, res) {
        if (err) throw err;
        db.close();
        CallBack(res);
      });
    } else if (Type == 'Delete') {
      DB.collection(Collection).deleteOne(data, function (err, res) {
        if (err) throw err;
        db.close();
        CallBack(res);
      });
    } else if (Type == 'Read') {
      DB.collection(Collection).find(data).toArray(function (err, res) {
        if (err) throw err;
        db.close();
        CallBack(res);
      });
    } else if (Type == 'Update') {
      DB.collection(Collection).updateOne(data[0], data[1], function (err, res) {
        if (err) throw err;
        db.close();
        CallBack(res);
      })
      // 获取列表数量
    } else if (Type == 'GetNum') {
      DB.collection(Collection).find(data).toArray(function (err, res) {
        if (err) throw err;
        db.close();
        CallBack(res.length);
      });
      // 获取排序后的列表
    } else if (Type == 'ReadByOrder') {
      if (data[2]) {
        DB.collection(Collection).find(data[0]).sort(data[1]).skip(data[2].Skip).limit(data[2].Limit).toArray(function (err, res) {
          if (err) throw err;
          db.close();
          CallBack(res);
        });
      } else {
        DB.collection(Collection).find(data[0]).sort(data[1]).toArray(function (err, res) {
          if (err) throw err;
          db.close();
          CallBack(res);
        });
      }
    } else if (Type == 'twoLeavlList') {
      // 查询二级评论数据
      DB.collection(Collection).aggregate([
        {
          $sort: {
            _id: -1
          }
        },
        {
          $lookup: { // 将评论文档与其父级文档关联
            from: Collection,
            localField: "_id",
            foreignField: "parentId",
            as: "son"
          }
        },
        {
          $match: { // 筛选出只有父级文档的评论文档，排除顶级评论档
            parentId: { $in: ["", null] }
          }
        },
        {
          $skip: data.Skip
        }, // 跳过(page-1)*page_size条文档数
        {
          $limit: data.Limit// 限制返回的文档数为page_size
        }
      ]).toArray(function (err, res) {
        if (err) throw err;
        db.close();
        CallBack(res);
      });
    }
  });
}

module.exports = Mongo;