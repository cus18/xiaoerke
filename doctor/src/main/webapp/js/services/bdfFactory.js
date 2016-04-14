/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var doctor_h5 = ''

define(['app'], function (app) {
    app
        //获取当前医生个人信息
        .factory('GetDoctorInfo', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'sys/doctor/doctorInfo');
        }])
        //获取当前医生号
        .factory('DoctorRegister', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'register/doctor');
        }])
        //取消当前医生号源
        .factory('DeleteRegister', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'register/doctor/remove');
        }])
        //获取有号源的日期
        .factory('DatesRegister', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'register/doctor/date');
        }])
        //获取删除中已约号源的数量
        .factory('DeleteRegisterCount', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'register/doctor/bookedNum');
        }])
        .factory('SaveDoctorAppointmentInfo', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'register/doctor/saveDoctorAppointmentInfo');
        }])
        .factory('SaveDoctorArrange', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'register/doctor/arrange');
        }])
        .factory('GetDoctorTimeInfo', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'register/doctor/appointmentInfo');
        }])
        .factory('SettlementInfo', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'order/doctor/settlementInfo');
        }])
        //获取接诊提醒列表
        .factory('AcceptRemind', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'order/doctor/reminderOrder');
        }])
        .factory('GetWithDrawList', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'account/doctor/withDrawList');
        }])
        .factory('WithDrawMoney', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'account/doctor/withdraw');
        }])
        .factory('CheckBind', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'util/checkBind');
        }])

        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'auth/info/loginStatus');
        }])
        //发送验证码
        .factory('IdentifyDoctor', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'util/doctor/getCode');
        }])
        .factory('OutOfBind', ['$resource', function ($resource) {
            return $resource(doctor_h5 + 'util/logOut');
        }])
})
