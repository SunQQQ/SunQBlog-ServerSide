let option;

let pageApi = function () {
    let App = option,
        dealObj = require("./commonFunction"),
        DealPara = dealObj.DealPara,
        Monge = require('../Mongo'),
        Util = require('../util'),
        util = new Util(),
        MongoClient = require("mongodb").MongoClient, // 数据库相关
        Url = "mongodb://localhost:27017/";
        
    // 创建访问记录
    App.post('/visitCreate/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            Monge.Mongo('VisitList', 'Insert', Para, function () {
                var Json = { status: '0', data: '插入成功' };
                Response.json(Json);
            });
        });
    });

    /**
     * 获取访问记录，管理后台首页使用
     * 直接根据分页数据查询 
     */
    App.post('/visitRead/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var PagnationData = Para.PagnationData ? Para.PagnationData : { SKip: 0, Limit: 10000 };

            Monge.Mongo('VisitList', 'ReadByOrder', [{}, { _id: -1 }, PagnationData], function (Result) {
                // 保护用户的IP地址，打上马赛克
                Result.forEach(function (item) {
                    if (item.clientIp) {
                        let array = item.clientIp.split('.');
                        item.clientIp = array[0] + '.' + array[1] + '.' + array[2] + '.***';
                    }
                });

                Monge.Mongo('VisitList', 'GetNum', {}, function (totalNum) {
                    var Json = {
                        status: '0',
                        data: {
                            list: Result,   // 当前分页下的数据
                            totalNum: totalNum   // 所有数据
                        }
                    };
                    Response.json(Json);
                });
            });
        });
    });

    // 删除访问记录
    App.post('/visitDelete/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (Para) {
            var Object = {};
            Object._id = ObjectId(Para._id);

            Monge.Mongo('VisitList', 'Delete', Object, function () {
                var Json = { status: '0', data: '访客记录删除成功' };
                Response.json(Json);
            });
        });
    });

    /** 访问统计接口
     * 传入最后一天，及需要的天数。返回传入日期前每一天的访问量\ip数
     * 传入(‘2021/12/11’，3)
     * 返回 [
     * {time:‘2021/12/09’,reading:5,ipNum:10},
     * {time:‘2021/12/10’,reading:2,ipNum:19},
     * {time:‘2021/12/11’,reading:15,ipNum:19}
     * ]
     */
    App.post('/visitCount/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (para) {
            let endTime = para.endTime, //20211124 从前端获取
                dayNum = para.dayNum,//7 从前端获取
                // 处理从前端获取的数据
                endTimeObject = new Date(endTime), //
                endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
                endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
                beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
                beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

                //此变量为mongodb查询时使用
                newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } }, // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }
                // 拿到库里数据后，node遍历计算次数
                dateArray = []; // 时间数组

            // 生成数组[‘2021/12/09’,‘2021/12/10’,‘2021/12/11’,...]
            for (let i = 0; i < dayNum; i++) {
                let dayObject, day, month;
                dayObject = new Date(endTimeObject.getTime() - i * 24 * 60 * 60 * 1000);
                day = dayObject.getDate() < 10 ? '0' + dayObject.getDate() : dayObject.getDate();
                month = dayObject.getMonth() + 1 < 10 ? '0' + (dayObject.getMonth() + 1) : dayObject.getMonth() + 1;
                dateArray.push(dayObject.getFullYear() + '/' + month + '/' + day);
            }

            // 查出上面时间数组范围内所有的记录，然后遍历时间数组的每一天，跟记录对比，得出每一天的访问量
            Monge.Mongo('VisitList', 'Read', newPara, function (Result) {
                let dateCountList = [], // 符合该时间数组中所有时间的所有记录
                    cityList = []; // 城市数组，供前端地图使用
                // 加入选中时间周期为30天，该时间周期下的日志一共是1460行。
                // 则统计每天的浏览量（即本接口），需要执行的遍历次数为30*1460=43800次
                for (let i = 0; i < dateArray.length; i++) {
                    let ipArray = [], // 符合当前时间的ip
                        object = new Object();

                    object.time = dateArray[i];
                    object.reading = 0;

                    Result.forEach(function (item) {
                        if (item.time.split(' ')[0] == dateArray[i]) {
                            object.reading += 1;
                            if (item.clientIp) ipArray.push(item.clientIp);
                        }
                        // 过滤掉重复的和名称是[]的城市，生成当前查询条件下的城市数组
                        if (cityList.indexOf(item.location) == -1 && typeof (item.location) == "string") {
                            cityList.push(item.location);
                        }
                    });

                    object.ipNum = util.dedupe(ipArray).length;

                    dateCountList.push(object);
                }

                var Json = {
                    status: '0',
                    data: {
                        dateCountList: dateCountList, // 数据结果类似=> [{time: "2022/01/08", reading: 25},{time: "2022/01/09", reading: 30}],供折线图使用
                        cityList: cityList, // 数据结果为库里记录直接返回，供地图使用
                    }
                };
                Response.json(Json);
            });
        });
    });

    /**
     * 
     */
    App.post('/messageCommentCount/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (para) {
            let endTime = para.endTime, //20211124 从前端获取
                dayNum = para.dayNum,//7 从前端获取
                // 处理从前端获取的数据
                endTimeObject = new Date(endTime), //
                endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
                endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
                beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
                beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

                //此变量为mongodb查询时使用
                newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } }, // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }
                // 拿到库里数据后，node遍历计算次数
                dateArray = []; // 时间数组

            // 生成数组[‘2021/12/09’,‘2021/12/10’,‘2021/12/11’,...]
            for (let i = 0; i < dayNum; i++) {
                let dayObject, day, month;
                dayObject = new Date(endTimeObject.getTime() - i * 24 * 60 * 60 * 1000);
                day = dayObject.getDate() < 10 ? '0' + dayObject.getDate() : dayObject.getDate();
                month = dayObject.getMonth() + 1 < 10 ? '0' + (dayObject.getMonth() + 1) : dayObject.getMonth() + 1;
                dateArray.push(dayObject.getFullYear() + '/' + month + '/' + day);
            }

            // 查出上面时间数组范围内所有的记录，然后遍历时间数组的每一天，跟记录对比，得出每一天的访问量
            Monge.Mongo('LeaveMessage', 'Read', newPara, function (Result) {
                let dateCountList = [], // 符合该时间数组中所有时间的所有记录
                    cityList = []; // 城市数组，供前端地图使用

                var Json = {
                    status: '0',
                    data: {
                        dateCountList: dateCountList, // 数据结果类似=> [{time: "2022/01/08", reading: 25},{time: "2022/01/09", reading: 30}],供折线图使用
                        cityList: cityList, // 数据结果为库里记录直接返回，供地图使用
                    }
                };
                Response.json(Json);
            });
        });
    });

    // 汇总每个ip的操作行为
    App.post('/getUserAction/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (para) {
            let endTime = para.endTime, // 2021/12/11 从前端获取
                dayNum = para.dayNum,//7 从前端获取
                dayArray = util.getDateArray(endTime, dayNum), // 前推指定天数，类似[‘2021/12/09’,‘2021/12/10’,‘2021/12/11’]
                nextDay = util.getOneDate(endTime, 1),  // 输出 2021/12/12
                nodePara = { 'time': { $gt: dayArray[0], $lt: nextDay } },

                ipArray = [], // ip数组 [ip1,ip2,ip3]
                userAction = {}; // { ip1:{action:[]}, ip2:{action:[]}}
            // 查出上面时间数组范围内所有的记录，然后遍历时间数组的每一天，跟记录对比，得出每一天的访问量
            Monge.Mongo('VisitList', 'Read', nodePara, function (Result) {
                Result.forEach(function (item) {
                    let currentIp = item.clientIp;
                    if (item.clientIp && ipArray.indexOf(currentIp) == -1) {
                        ipArray.push(currentIp);
                    }
                });
                ipArray.reverse(); // 最新的日期放在前面
                // 生成userAction，是最终返回的数据     { ip1:{action:[]}, ip2:{action:[]}}
                ipArray.forEach(function (item) {
                    userAction[item] = { action: [] };
                });

                // 遍历查询的数据，每一条都操作一遍。每一条都跟userAction比对，如果没有插入一次
                Result.forEach(function (item, i) {
                    let currentIp = item.clientIp, // 当前数据可能没有ip字段
                        actionarray = currentIp ? userAction[currentIp].action : '', // 当前ip下的行为数组
                        actionText = item.operateType ? item.operateType + ':' + item.operateContent : ''; // 当条日志下的操作字段

                    if (actionarray && actionText && actionarray.indexOf(actionText) == -1) { // 数组会过滤重复的操作，相同操作只会push一次
                        actionarray.push(actionText);
                    }

                    if (currentIp) {
                        userAction[currentIp].location = item.location ? item.location : '';
                        userAction[currentIp].browser = item.browser ? item.browser : '';
                        userAction[currentIp].time = item.time ? item.time : '';
                        if (item.fromUrl) userAction[currentIp].fromUrl = item.fromUrl;
                    }
                });

                var Json = {
                    status: '0',
                    data: {
                        userAction: userAction,
                        dateListTotal: Result.length, // 数据结果为库里记录直接返回，供地图使用
                        yourIp: para.clientIp
                    }
                };
                Response.json(Json);
            });
        });
    });

    /**
     * 根据时间返回用户操作类型，用于分析用户操作占比
     */
    App.post('/visitReadByDay/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (para) {
            let endTime = para.endTime, //20211124 从前端获取
                dayNum = para.dayNum,//7 从前端获取
                // 处理从前端获取的数据
                endTimeObject = new Date(endTime), //
                endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
                endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
                beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
                beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

                //此变量为mongodb查询时使用
                newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } }; // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }

            Monge.Mongo('VisitList', 'Read', newPara, function (Result) {
                let array = [];
                Result.forEach(function (item) {
                    array.push(item.operateType);
                });

                var Json = {
                    status: '0',
                    data: {
                        list: array,   // 当前分页下的数据
                    }
                };
                Response.json(Json);
            });
        });
    });

    /**
     * 根据时间返回点击菜单的数据，用于分析菜单访问占比
     */
    App.post('/menuClickByDay/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (para) {
            let endTime = para.endTime, //20211124 从前端获取
                dayNum = para.dayNum,//7 从前端获取
                // 处理从前端获取的数据
                endTimeObject = new Date(endTime), //
                endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
                endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
                beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
                beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

                //此变量为mongodb查询时使用
                newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } }; // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }

            Monge.Mongo('VisitList', 'Read', newPara, function (Result) {
                let array = [],
                    allMenuOperate = ['博文', '留言', '时间轴', '试验田', '关于', '访问统计', '管理后台'];
                Result.forEach(function (item) {
                    if (allMenuOperate.indexOf(item.operateContent) > -1) {
                        array.push(item.operateContent);
                    }
                });

                var Json = {
                    status: '0',
                    data: {
                        list: array,   // 当前分页下的数据
                    }
                };
                Response.json(Json);
            });
        });
    });

    /**
     * 根据时间返回新老用户的数据，用于分析新老用户占比
     * 
     * 定义查询日期往前推8个月的数据为老用戶
     * 1.先查出查询这段时间所有IP，一天天的查再拼接起来；
     * 2.再查出这段时间前8个月所有访问过的ip
     * 3.最终匹配出1中，有多少ip是老访客
     */
    App.post('/regularUserByDay/:accesstype', function (Request, Response) {
        DealPara(Request, Response, function (para) {
            let endTime = para.endTime, //20211124 从前端获取
                dayNum = para.dayNum,//7 从前端获取
                // 处理从前端获取的数据
                endTimeObject = new Date(endTime), //
                endTimeAddOneObject = new Date(endTimeObject.getTime() + 1 * 24 * 60 * 60 * 1000),
                endTimeAddOne = endTimeAddOneObject.getFullYear() + '/' + (endTimeAddOneObject.getMonth() + 1 < 10 ? '0' + (endTimeAddOneObject.getMonth() + 1) : endTimeAddOneObject.getMonth() + 1) + '/' + (endTimeAddOneObject.getDate() < 10 ? '0' + endTimeAddOneObject.getDate() : endTimeAddOneObject.getDate()),
                beginTimeObject = new Date(endTimeObject.getTime() - (dayNum - 1) * 24 * 60 * 60 * 1000), //开始时间由结束时间向前推得出
                beginTime = beginTimeObject.getFullYear() + '/' + (beginTimeObject.getMonth() + 1 < 10 ? '0' + (beginTimeObject.getMonth() + 1) : beginTimeObject.getMonth() + 1) + '/' + (beginTimeObject.getDate() < 10 ? '0' + beginTimeObject.getDate() : beginTimeObject.getDate()),

                //此变量为mongodb查询时使用
                newPara = { 'time': { $gt: beginTime, $lt: endTimeAddOne } },
                // 查询时间段往前推8个月。比如第一行是查询时间端14天，第二行是这个时间端往前推8个月
                // { '$gt': '2022/08/21', '$lt': '2022/09/04' } 注意mongodb最后一天不算，只查到0903
                // { '$gt': '2021/12/24', '$lt': '2022/08/21' }
                dayBefore8MonthObject = new Date(beginTimeObject.getTime() - 240 * 24 * 60 * 60 * 1000);
            dayBefore8Month = dayBefore8MonthObject.getFullYear() + '/' + (dayBefore8MonthObject.getMonth() + 1 < 10 ? '0' + (dayBefore8MonthObject.getMonth() + 1) : dayBefore8MonthObject.getMonth() + 1) + '/' + (dayBefore8MonthObject.getDate() < 10 ? '0' + dayBefore8MonthObject.getDate() : dayBefore8MonthObject.getDate()),
                before8Month = { 'time': { $gt: dayBefore8Month, $lt: beginTime } },

                // 查询
                backField = {
                    'clientIp': '1'
                }; // mongodb语法要求结束时间需要加一天, { time: { '$gt': '2021/12/11', '$lt': '2021/12/12' } }

            let selectedIp = [],
                before8MonthIp = [],
                dateArray = [],
                regularUserNum = 0;

            // 生成数组[‘2021/12/09’,‘2021/12/10’,‘2021/12/11’,...]
            for (let i = 0; i < dayNum; i++) {
                let dayObject, day, month;
                dayObject = new Date(endTimeObject.getTime() - i * 24 * 60 * 60 * 1000);
                day = dayObject.getDate() < 10 ? '0' + dayObject.getDate() : dayObject.getDate();
                month = dayObject.getMonth() + 1 < 10 ? '0' + (dayObject.getMonth() + 1) : dayObject.getMonth() + 1;
                dateArray.push(dayObject.getFullYear() + '/' + month + '/' + day);
            }

            console.log(newPara);
            console.log(before8Month);
            console.log(dateArray);

            MongoClient.connect(Url, function (err, db) {
                var DB = db.db("test");
                DB.collection('VisitList').find(newPara, backField).toArray(function (err, res) {
                    if (err) throw err;

                    dateArray.forEach((day) => {
                        let dayIp = []; //当天所有的ip
                        // 获取当天下所有ip，滤重
                        res.forEach((item) => {
                            if (day == item.time.split(' ')[0] && item.clientIp) dayIp.push(item.clientIp);
                        });
                        dayIp = util.dedupe(dayIp); // 数组滤重

                        // 把每天的ip拼接起来，就是一段时间所有的IP。这些IP允许有重复，因为今天访问了，昨天也访问了，都算这段时间的访客
                        selectedIp = selectedIp.concat(dayIp);
                    });



                    DB.collection('VisitList').find(before8Month, backField).toArray(function (err, result) {
                        if (err) throw err;
                        db.close();

                        // 查询时间段往前推8个月内所有ip，且滤重过
                        result.forEach((item) => {
                            if (item.clientIp) before8MonthIp.push(item.clientIp);
                        });
                        before8MonthIp = util.dedupe(before8MonthIp);

                        selectedIp.forEach((item) => {
                            if (before8MonthIp.indexOf(item) > -1) {
                                regularUserNum += 1;
                            }
                        })

                        var Json = {
                            status: '0',
                            data: {
                                regularUser: regularUserNum,
                                newUser: selectedIp.length - regularUserNum
                            }
                        };
                        Response.json(Json);
                    });
                });
            });
        });
})}

module.exports = function (app) {
    option = app;
    return pageApi;
}