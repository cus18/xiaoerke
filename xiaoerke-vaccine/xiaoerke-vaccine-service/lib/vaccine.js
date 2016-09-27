var mongoskin = require('mongoskin'),
    db = require('./../util/mongo'),
    header = require('./../util/header'),
    config = require('./../config'),
    USER_TYPE = require('./user_type');


var SERVER_URL = config.BASE_URL;
var collectionName = 'user',
    str2ObjId = mongoskin.helper.toObjectID,
/*
 + ������URL������ͷ��ķ�����URL,Ĭ��ͷ���URL��
 + ���ܰ�����̬�ļ������������ԣ�������Դ�������URL
 */
    BASE_URL = {
        'AVATAR': '/',
        'AVATAR_DEFAULT': ''
    };
db.bind(collectionName);
/*
 + ��;���û���Ȩ������ע�ᡢ��¼�Լ��ӿڵ��õ�
 + ���ߣ�vczero
 + �汾��v2.1
 + ���£�������װMongoSkin Util���������ϲ�USER_TYPE.js & user.js
 + �мǣ�����ϣ��һ��������ض�����ܵ�����;ÿ��������һ������
 +
 */
module.exports = {
    demo: function(req, res) {
        header.set(req, res);
        return res.send({
            status: '����ɹ�'
        });
    }
};