var mongoskin = require('mongoskin'),
    db = require('./../util/mongo'),
    header = require('./../util/header'),
    config = require('./../config'),
    USER_TYPE = require('./user_type');


var SERVER_URL = config.BASE_URL;
var collectionName = 'user',
    str2ObjId = mongoskin.helper.toObjectID,
/*
 + 基础的URL，例会头像的服务器URL,默认头像的URL等
 + 可能包括静态文件服务器，所以，数据资源必须独立URL
 */
    BASE_URL = {
        'AVATAR': '/',
        'AVATAR_DEFAULT': ''
    };
db.bind(collectionName);
/*
 + 用途：用户鉴权，包括注册、登录以及接口调用等
 + 作者：vczero
 + 版本：v2.1
 + 更新：摒弃封装MongoSkin Util的作法；合并USER_TYPE.js & user.js
 + 切记：摒弃希望一个服务承载多个功能的做法;每个服务负责一个功能
 +
 */
module.exports = {
    demo: function(req, res) {
        header.set(req, res);
        return res.send({
            status: '请求成功'
        });
    }
};