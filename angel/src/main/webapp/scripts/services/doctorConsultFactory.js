var public = ''

angular.module('services', ['ngResource'])
    //获取医生管理下的患者列表数据和查找某个患者接口
    //获取今天会话排名列表
    .factory('GetTodayRankingList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/rankList');
    }])
    //用户登陆状态
    .factory('DoctorBinding', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/login/doctorBinding');
    }])
    //用户绑定
    .factory('GetDoctorLoginStatus', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/login/getDoctorLoginStatus');
    }])
    //获取用户登陆状态
    .factory('GetUserLoginStatus', ['$resource', function ($resource) {
        return $resource(public + 'auth/info/loginStatus');
    }])
    //获取在线医生列表
    .factory('GetOnlineDoctorList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/doctorOnLineList');
    }])
    //医生选择一个用户后，主动发起跟用户创建会话
    .factory('CreateDoctorConsultSession', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/createDoctorConsultSession');
    }])
    //获取医生下的目前正在咨询的用户列表
    .factory('GetCurrentUserConsultListInfo', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/getCurrentUserList');
    }])
    //获取客户列表(或咨询过某个医生的客户)详细信息,按照时间降序排序
    .factory('GetUserConsultListInfo', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/getUserList');
    }])
    //获取在线用户列表
    .factory('GetPatientSession', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/session');
    }])
    //获取公共回复列表
    .factory('GetAnswerValueList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/answerValue');
    }])
    //获取客服医生列表
    .factory('GetCSDoctorList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/GetCSDoctorList');
    }])
    .factory('GetMessageRecordInfo', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/recordSearchList');
    }])
    //获取客户的聊天记录
    .factory('GetUserRecordDetail', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/recordList');
    }])
    //医生修改的自己的回复
    .factory('GetMyAnswerModify', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/answer/modify');
    }])
    //医生删除的自己的回复
    .factory('GetMyAnswerDelete', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/myAnswer/delete');
    }])
    //生成聊天记录ChathHistory
    .factory('GetChatHistory', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/produceRecord');
    }])
    //获取系统时间
    .factory('GetSystemTime', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/getSystemTime');
    }])
    //获取聊天记录分页
    .factory('GetRecordList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/recordList');
    }])
    //发起会话建立请求
    .factory('GetSessionBuild', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/sessionBuild');
    }])
    //主动终止会话
    .factory('SessionEnd', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/sessionEnd');
    }])
    //获取等待加入用户列表
    .factory('GetWaitJoinList', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/waitJoinList');
    }])
    //医生获取当前会话用户的N条聊天记录
    .factory('GetHistoryRecordPatient', ['$resource', function ($resource) {
        return $resource(public + 'consult/historyRecord/patient');
    }])
    //用户获取当前会话用户的N条聊天记录
    .factory('GetHistoryRecordDoctor', ['$resource', function ($resource) {
        return $resource(public + 'consult/historyRecord/doctor');
    }])
    //用户获取当前会话用户的接入次数
    .factory('GetUserSessionTimesByUserId', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/getUserSessionTimesByUserId');
    }])
    //转发会话到其他客服
    .factory('TransferToOtherCsUser', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/transfer');
    }])
    .factory('GetCurrentUserHistoryRecord', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/getCurrentUserHistoryRecord');
    }])
    .factory('React2Transfer', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/react2Transfer');
    }])
    .factory('CancelTransfer', ['$resource', function ($resource) {
        return $resource(public + 'consult/doctor/cancelTransfer');
    }])
    //查询所有的专科转移
    .factory('GetFindTransferSpecialist', ['$resource', function ($resource) {
        return $resource(public + 'consult/transfer/findConsultTransferList');
    }])
    //删除的转接列表的科室
    .factory('GetRemoveTransferSpecialist', ['$resource', function ($resource) {
        return $resource(public + 'consult/transfer/updateConsultTransferByPrimaryKey');
    }])
    //添加转接科室
    .factory('GetAddTransferSpecialist', ['$resource', function ($resource) {
        return $resource(public + 'consult/transfer/saveConsultTransfer');
    }])
    //查询所有专科列表
    .factory('GetFindAllTransferSpecialist', ['$resource', function ($resource) {
        return $resource(public + 'consult/transfer/findDoctorDepartment');
    }])

    //添加定时回访
    .factory('ModifyUserConsultNum', ['$resource', function ($resource) {
        return $resource(public + 'consult/user/modifyUserConsultNum');
    }])
    //发起专科的会话
    .factory('CreateTransferSpecialist', ['$resource', function ($resource) {
        return $resource(public + 'consultSession/transfer/createMoreUserConsultSession');
    }])
    //获取医生所属的科室
    .factory('GetCurrentDoctorDepartment', ['$resource', function ($resource) {
        return $resource(public + 'consult/transfer/getCurrentDoctorDepartment');
    }])
    //根据openid获取历史咨询
    .factory('GetCustomerLogByOpenID', ['$resource', function ($resource) {
        return $resource(public + 'customer/getCustomerLogByOpenID');
    }])
    //添加诊断记录
    .factory('SaveCustomerLog', ['$resource', function ($resource) {
        return $resource(public + 'customer/saveCustomerLog');
    }])
    //查找所属科室
    .factory('SearchIllnessList', ['$resource', function ($resource) {
        return $resource(public + 'customer/searchIllnessList');
    }])
    //查找宝宝的初始信息
    .factory('SearchBabyInfo', ['$resource', function ($resource) {
        return $resource(public + 'customer/searchBabyInfo');
    }])
    //添加定时回访
    .factory('SaveReturnService', ['$resource', function ($resource) {
        return $resource(public + '/angel/customer/saveReturnService');
    }])
    //查询栏目列表接口  根据id获取目录
    .factory('GetCategoryList',['$resource',function ($resource){
        return $resource(public + 'knowledge/category/findByParentId');
    }])
    //根据文章栏目或标题查询文章接口 根据目录id和分页信息获取文章列表
    .factory('GetArticleList',['$resource',function ($resource){
        return $resource(public + 'knowledge/article/articleList');
    }])
    //查询文章内容接口 获取文章详情
    .factory('GetArticleDetail',['$resource',function ($resource){
        return $resource(public + 'knowledge/article/articleDetail');
    }])





