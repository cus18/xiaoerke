/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = 'consultPhone/'
var user_appoint = ''
var wxChat = 'wechatInfo/'
var healthRecord='healthRecord/'
var babyCoin = 'babyCoin/'

define(['appNonRealTimeConsult'], function (app) {
    app
        .factory('ListHospitalDoctor',['$resource',function ($resource){
            return $resource(user_h5 + 'consultPhoneDoctor/getDoctorListByHospital');
        }])

        .factory('ListSecondIllnessDoctor',['$resource',function ($resource){
            return $resource(user_h5 + 'consultPhoneDoctor/getDoctorListByIllness');
        }])
        //获取某个预约日期下的可预约医生
        .factory('ListAppointmentTimeDoctor', ['$resource', function ($resource) {
            return $resource(user_h5 + 'consultPhoneDoctor/getDoctorListByTime');
        }])

        //获取医院下的所有科室
        .factory('ListHospitalDepartment', ['$resource', function ($resource) {
            return $resource(user_appoint + 'sys/hospital/department');
        }])
        //查询二类疾病关联的医生
        .factory('ListHospitalByIllnessSecond', ['$resource', function ($resource) {
            return $resource(user_appoint + 'sys/illness/second/hospital');
        }])
        //查询科室关联的医院
        .factory('ListDepartmentHospital', ['$resource', function ($resource) {
            return $resource(user_appoint + 'sys/hospital/listDepartmentHospital');
        }])
        //获取某个预约日期下的可预约的医院
        .factory('ListAppointmentTimeHospital', ['$resource', function ($resource) {
            return $resource(user_appoint + 'register/user/time/hospital');
        }])
        //获取一个医生的详细信息
        .factory('DoctorDetail', ['$resource', function ($resource) {
            return $resource(user_h5 + 'consultPhoneDoctor/doctorDetail');
        }])
        //获取一个医生的详细信息
        .factory('DoctorVisitInfoByLocationInWeek', ['$resource', function ($resource) {
            return $resource(user_h5 + 'consultPhoneDoctor/getConsultInfo');
        }])
        //获取一个医生的某个加号的详情信息
        .factory('DoctorAppointmentInfoDetail', ['$resource', function ($resource) {
            return $resource(user_appoint + 'consultPhone/user/doctor/time');
        }])

        //获取电话咨询订单详情
        .factory('PhoneConsultRegisterInfo', ['$resource', function ($resource) {
            return $resource(user_h5 + 'consultOrder/getOrderInfo');
        }])

        //获取电话咨询订单详情
        .factory('ConsultReconnection', ['$resource', function ($resource) {
            return $resource("baodaifu/"+user_h5 + 'consultReconnect');
        }])


        //获取预约挂号订单详情
        .factory('AppointRegisterInfo', ['$resource', function ($resource) {
            return $resource(user_appoint + '/titan/order/user/orderDetail');
        }])

        //查询最早医生的预约挂号时间
        .factory('EarliestVisiteInfo', ['$resource', function ($resource) {
            return $resource(user_h5 + '/consultPhoneDoctor/earliestVisiteInfo');
        }])
        //用户关注医生功能
        .factory('AttentionDoctor', ['$resource', function ($resource) {
            return $resource(user_appoint + 'interaction/user/doctorConcern');
        }])


    ///consultPhone/user/doctor/time






        //获取医生的7天内的出诊地点和位置信息
        .factory('GetDoctorVisitInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'sys/user/doctorVisitInfo');
        }])

        .factory('ReturnPay',['$resource',function ($resource){
            return $resource(user_h5 +'account/user/returnPay');
        }])
        //搜索下拉框自动提示
        .factory('AutoPrompting',['$resource',function ($resource){
            return $resource(user_h5 +'search/user/autoPrompting');
        }])
        .factory('UserPay',['$resource',function ($resource){
            return $resource(user_h5 +'account/user/userPay');
        }])

        //获取系统内所有医院
        .factory('ListHospital', ['$resource', function ($resource) {
            return $resource(user_h5 + 'sys/hospital');
        }])
        //获取合作医院的相关信息
        .factory('GetCooperationHospitalInfo', ['$resource', function ($resource) {
            return $resource(user_h5 + 'sys/hospital/cooperation');
        }])


        //获取医院的某个科室的医生
        .factory('ListHospitalDepartmentDoctor', ['$resource', function ($resource) {
            return $resource(user_h5 + 'sys/hospital/department/doctor');
        }])
        //获取医生集团的信息
        .factory('DoctorGroupInfo', ['$resource', function ($resource) {
            return $resource(user_h5 + 'sys/doctor/group/info');
        }])
        //获取医生集团的信息
        .factory('DoctorListInDoctorGroup', ['$resource', function ($resource) {
            return $resource(user_h5 + 'sys/doctor/group/doctorList');
        }])
        //获取一类疾病的列表
        .factory('ListFirstIllness', ['$resource', function ($resource) {
            return $resource(user_h5 + 'sys/illness/first');
        }])
        .factory('FindDoctorCaseEvaluation',['$resource',function ($resource){
            return $resource(user_appoint + 'sys/user/findDoctorCaseEvaluation');
        }])
        //获取二类疾病的列表
        .factory('ListSecondIllness', ['$resource', function ($resource) {
            return $resource(user_h5 + 'sys/illness/second');
        }])
        //根据二类疾病的ID，获取相关联的医生
        //.factory('ListSecondIllnessDoctor', ['$resource', function ($resource) {
        //    return $resource(user_h5 + 'sys/illness/second/doctor');
        //}])
        .factory('GetHospitalInfoById',['$resource',function ($resource){
            return $resource(user_h5 + 'sys/hospital/info');
        }])
        //获取个人中心的首页信息
        .factory('MyselfInfo', ['$resource', function ($resource) {
            return $resource(user_appoint + 'info/user');
        }])
        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource(user_appoint + 'auth/info/loginStatus');
        }])
        //获取医生某个出诊地点的7天内的可预约时间
        .factory('GetDoctorDateInfo',['$resource',function ($resource){
            return $resource(user_h5 + 'sys/user/getConsultDateInfo');
        }])
        //获取医生所有的出诊地点
        .factory('FindDoctorLocationByDoctorId',['$resource',function ($resource){
            return $resource(user_h5 + 'sys/user/findDoctorLocationByDoctorId');
        }])

        //获取预约时间
        .factory('ListAppointmentTime', ['$resource', function ($resource) {
            return $resource(user_h5 + 'register/user/time');
        }])

        //获取一个医生的某天的加号信息
        .factory('DoctorconsultPhoneInfo', ['$resource', function ($resource) {
            return $resource(user_h5 + '/consultPhone/user/doctor/time');
        }])

        //获取医生某个出诊地点的预约时间详情
        .factory('AppointmentStatus',['$resource',function ($resource){
            return $resource(user_h5 + 'register/user/sourceRoute');
        }])
        //获取个人的各种预约信息列表
        .factory('MyselfInfoAppointment', ['$resource', function ($resource) {
            return $resource(user_appoint + 'order/user/orderList1');
        }])
        //电话咨询订单列表
        .factory('MyselfInfoPhoneConsult',['$resource',function ($resource){
            return $resource(user_h5 + 'order/user/orderList');
        }])
        //取消订单
        .factory('OrderCancel',['$resource',function ($resource){
            return $resource(user_h5 + 'cancelOrder');
        }])

        //获取所有订单列表
        .factory('getOrderListAll',['$resource',function ($resource){
            return $resource(user_h5 + 'order/user/orderListAll');
        }])

        .factory('checkUserOrder',['$resource',function ($resource){
            return $resource(user_appoint + 'order/user/checkOrderInfo');
        }])
        //根据用户的业务注册号，获取预约信息详情
        .factory('MyselfInfoAppointmentDetail', ['$resource', function ($resource) {
            return $resource(user_h5 + 'order/user/orderDetail');
        }])
        //对预约的状态进行操作
        .factory('OrderAppointOperation', ['$resource', function ($resource) {
            return $resource(user_h5 + 'order/user/orderAppointOperation');
        }])
        .factory('OrderPayOperation', ['$resource', function ($resource) {
            return $resource(user_h5 + 'order/user/orderPayOperation');
        }])
        .factory('OrderPraiseOperation', ['$resource', function ($resource) {
            return $resource(user_appoint + 'order/user/orderPraiseOperation');
        }])
        .factory('OrderShareOperation', ['$resource', function ($resource) {
            return $resource(user_appoint + 'order/user/info/orderShareOperation');
        }])
        .factory('OrderCancelOperation', ['$resource', function ($resource) {
            return $resource(user_h5 + 'order/user/info/orderCancelOperation');
        }])
        //我的消息
        .factory('MsgAppointment', ['$resource', function ($resource) {
            return $resource(user_h5 + 'order/user/msgAppointment');
        }])
        .factory('MsgAppointmentStatus', ['$resource', function ($resource) {
            return $resource(user_h5 + 'order/user/msgAppointmentStatus');
        }])
        //检查不正常约号的情况
        .factory('GetCheckOrder',['$resource',function ($resource){
            return $resource(user_h5 + 'order/getCheckOrder');
        }])

        //检测用户是否已经关注该医生
        .factory('CheckAttentionDoctor', ['$resource', function ($resource) {
            return $resource(user_appoint + 'interaction/user/isConcerned');
        }])
        //我的关注
        .factory('MyAttention', ['$resource', function ($resource) {
            return $resource(user_h5 + 'interaction/user/myselfConcern');
        }])
        //待分享
        .factory('MyShare', ['$resource', function ($resource) {
            return $resource(user_h5 + 'interaction/user/share');
        }])
        //获取用户对某医生的评价（电话咨询）
        .factory('GetUserEvaluate',['$resource',function ($resource){
            return $resource(user_appoint + 'interaction/user/doctorEvaluate');
        }])
        //用户对客服的评价
        .factory('CustomerEvaluation',['$resource',function ($resource){
            return $resource(user_appoint + 'interaction/user/customerEvaluation');
        }])
        //问卷调查
        .factory('QuestionnaireSurvey',['$resource',function ($resource){
            return $resource(user_h5 + 'interaction/user/questionnaireSurvey');
        }])
        //调用阿里云进行搜索查询
        .factory('SearchDoctors',['$resource',function ($resource){
            return $resource(user_h5 + 'search/searchDoctors');
        }])
        //用户coocik搜索记录
        .factory('PatientSearchList',['$resource',function ($resource){
            return $resource(user_h5 + 'search/user/PatientSearchList');
        }])
        // 清除所有的搜索记录
        .factory('RemoveAllSearchHistory',['$resource',function ($resource){
            return $resource(user_h5 + 'search/user/RemoveAllSearchHistory');
        }])
        //发送验证码
        .factory('IdentifyUser', ['$resource', function ($resource) {
            return $resource(user_h5 + 'util/user/getCode');
        }])
        .factory('RecordLogs',['$resource',function ($resource){
            return $resource(user_appoint + 'util/recordLogs');
        }])
        //获取会员信息
        .factory('ExtendMemberService',['$resource',function ($resource){
            return $resource(user_h5 + 'member/user/extendMemberService');
        }])
        //获取用户会员购买情况
        .factory('CheckIfAppScanDoctor',['$resource',function ($resource){
            return $resource(user_h5 + 'member/user/checkIfAppScanDoctor');
        }])
        //扫描会员关注操作
        .factory('OrderFreePayOperation',['$resource',function ($resource){
            return $resource(user_h5 + 'member/user/orderFreePayOperation');
        }])
        //查看用户会员状态
        .factory('GetMemberServiceStatus',['$resource',function ($resource){
            return $resource(user_h5 + 'member/user/getMemberServiceStatus');
        }])
        //查看用户会员状态
        .factory('GetUserMemberService',['$resource',function ($resource){
            return $resource(user_h5 + 'member/user/getUserMemberService');
        }])
        //通过会员的预约卷，进行预约
        .factory('OrderPayMemberServiceOperation',['$resource',function ($resource){
            return $resource(user_h5 + 'member/user/orderPayMemberServiceOperation');
        }])
        .factory('MemberServiceDetail',['$resource',function ($resource){
            return $resource(user_h5 + 'member/user/memberServiceDetail');
        }])
        .factory('SendAdvice',['$resource',function ($resource){
            return $resource(user_h5 + 'feedback/user/sendAdvice');
        }])
        .factory('WxPayAdvice',['$resource',function ($resource){
            return $resource(user_h5 + 'feedback/user/wxpayAdvice');
        }])
        .factory('GetQRCode',['$resource',function ($resource){
            return $resource(user_h5 + 'marketingInfo/getQRCode');
        }])
        .factory('GetAttentionFromMarketer',['$resource',function ($resource){
            return $resource(user_h5 + 'marketingInfo/getAttentionFromMarketer');
        }])
        .factory('ConsultVisit',['$resource',function ($resource){
            return $resource(user_h5 + 'feedback/user/consultVisit');
        }])
        //send WechatMessage To User
        .factory('SendWechatMessageToUser',['$resource',function ($resource){
            return $resource(wxChat + 'postInfoToWechat');
        }])

        //获取用户下宝宝信息
        .factory('getBabyinfoList',['$resource',function ($resource){
            return $resource(healthRecord + 'getBabyinfoList');
        }])
        //获取用户下宝宝的订单信息
        .factory('getAppointmentInfo',['$resource',function ($resource){
            return $resource(healthRecord + 'getAppointmentInfo');
        }])
        //添加宝宝信息
        .factory('saveBabyInfo',['$resource',function ($resource){
            return $resource(healthRecord + 'saveBabyInfo');
        }])
        //修改宝宝信息
        .factory('updateBabyInfo',['$resource',function ($resource){
            return $resource(healthRecord + 'updateBabyInfo');
        }])
        //获取宝宝咨询详情记录
        .factory('getConsultationInfo',['$resource',function ($resource){
            return $resource(healthRecord + 'getConsultationInfo');
        }])
        //添加病情描述
        .factory('modifyBabyIndfo',['$resource',function ($resource){
            return $resource(healthRecord + 'modifyBabyIndfo');
        }])
        //查询用户下宝宝的就诊信息
        .factory('getOrderInfoByOpenid',['$resource',function ($resource){
            return $resource(user_h5 + 'customer/getOrderInfoByOpenid');
        }])
        .factory('getCustomerLogByOpenID',['$resource',function ($resource){
            return $resource(user_h5 + 'customer/getCustomerLogByOpenID');
        }])
        .factory('deleteBabyInfo',['$resource',function ($resource){
            return $resource(healthRecord + 'deleteBabyInfo');
        }])
        .factory('getLastOrderBabyInfo',['$resource',function ($resource){
            return $resource(healthRecord + 'getLastOrderBabyInfo');
        }])
        .factory('addDoctorCase',['$resource',function ($resource){
            return $resource(healthRecord + 'addDoctorCase');
        }])
        //获取用户宝宝币
        .factory('BabyCoinInit',['$resource',function ($resource){
            return $resource(babyCoin + 'babyCoinInit');
        }])
        //根据用户的登陆状态，进行相应的操作
        .factory('resolveUserLoginStatus', ['GetUserLoginStatus','$state',
            function(GetUserLoginStatus,$state) {
                var runFirstRequest = function(redirectParam,routeParam,goParam,hrefParam,action) {
                    var routePath = "";
                    if(action=="go"){
                        routePath = "/phoneConsultBBBBBB/"+redirectParam+"/"+routeParam;
                    }
                    else if(action=="notGo"){
                        routePath = "/phoneConsultBBBBBB/"+redirectParam;
                    }
                    if(hrefParam!=""&&routeParam==""){
                        routePath = "/appointBBBBBB/"+redirectParam;
                    }
                    if(hrefParam!=""&&routeParam!=""){
                        routePath = "/appointBBBBBB/"+redirectParam+"/"+routeParam;
                    }
                    GetUserLoginStatus.save({routePath:routePath},function(data){
                        if(data.status=="9") {
                            window.location.href = data.redirectURL;
                        }
                        else if(data.status=="8"){
                            window.location.href = data.redirectURL+"?targeturl="+routePath;
                        }
                        else {
                            if(action=="notGo"){
                                if(hrefParam==""){
                                    $state.go(redirectParam);
                                }else{
                                    window.location.href = hrefParam;
                                }
                            }
                            else if(action=="go"){
                                $state.go(redirectParam,goParam);
                            }
                        }
                    });
                };
                return {
                    events: function(redirectParam,routeParam,goParam,hrefParam,action) {
                        return runFirstRequest(redirectParam,routeParam,goParam,hrefParam,action);
                    }};
            }])
})
