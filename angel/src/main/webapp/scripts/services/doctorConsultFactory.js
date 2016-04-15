var public = ''

angular.module('services', ['ngResource'])
    //获取医生管理下的患者列表数据和查找某个患者接口
    //获取今天会话排名列表
    .factory('getTodayRankingList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/rankList');
    }])
    //获取用户登陆状态
    .factory('GetUserLoginStatus', ['$resource', function ($resource) {
        return $resource(public + 'auth/info/loginStatus');
    }])
    //获取聊天记录
    .factory('GetUserRecordList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/recordList');
    }])
    //获取在线医生列表
    .factory('getOnlineDoctorList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/doctorList');
    }])

    //获取医生下的目前正在咨询的用户列表
    .factory('getCurrentUserConsultListInfo', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/getCurrentUserList');
    }])
    //获取客户列表(或咨询过某个医生的客户)详细信息,按照时间降序排序
    .factory('getUserConsultListInfo', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/getUserList');
    }])
    //获取在线用户列表
    .factory('getPatientSession', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/session');
    }])
    //获取公共回复列表
    .factory('getCommonAnswerList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/commonAnswer');
    }])
    //获取我的回复列表
    .factory('getMyAnswerList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/commonAnswer');
    }])
    //获取客服医生列表
    .factory('getCSDoctorList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/getCSDoctorList');
    }])
    .factory('getMessageRecordInfo', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/recordSearchList');
    }])
    //根据userId获取某个客服医生
    .factory('CSInfoByUserId', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/getUserList');
    }])
    //修改回复
    .factory('answerModify', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/Answer/modify');
    }])

    //获取客户的聊天记录
    .factory('getUserRecordDetail', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/recordList');
    }])
    //医生创建新的自己的回复1
    .factory('getMyAnswerListCreate1', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/myAnswer/create/first');
    }])
    //医生创建新的自己的回复2
    .factory('getMyAnswerListCreate2', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/myAnswer/create/second');
    }])
    //医生修改的自己的回复
    .factory('getMyAnswerModify', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/Answer/modify');
    }])
    //医生删除的自己的回复
    .factory('getMyAnswerDelete', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/myAnswer/delete');
    }])
    //生成聊天记录ChathHistory
    .factory('getChathHistory', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/produceRecord');
    }])
    //获取聊天记录分页
    .factory('getRecordList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/recordList');
    }])
    //发起会话建立请求
    .factory('getSessionBuild', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/sessionBuild');
    }])
    //主动终止会话
    .factory('getSessionEnd', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/sessionEnd');
    }])
    //获取等待加入用户列表
    .factory('getWaitJoinList', ['$resource', function ($resource) {
        return $resource(public + 'consult/waitJoinList');
    }])
    //选择用户，删除或增加
    .factory('getWaitJoinListOperation', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/waitJoinList/operation');
    }])
    //医生获取当前会话用户的N条聊天记录
    .factory('getHistoryRecordPatient', ['$resource', function ($resource) {
        return $resource(public + 'consult/historyRecord/patient');
    }])
    //用户获取当前会话用户的N条聊天记录
    .factory('getHistoryRecordDoctor', ['$resource', function ($resource) {
        return $resource(public + 'consult/historyRecord/doctor');
    }])
    //转发会话到其他客服
    .factory('getTransfer', ['$resource', function ($resource) {
        return $resource(public + 'consult/transfer');
    }])

