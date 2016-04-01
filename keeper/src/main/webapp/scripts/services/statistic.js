var public = '/xiaoerke-wxapp/ap/statistic/'

angular.module('services', ['ngResource'])
    //获取医生管理下的患者列表数据和查找某个患者接口
    .factory('GetStatisticData', ['$resource', function ($resource) {
        return $resource(public + 'userOperationData');
    }])
    .factory('GetOverallStatisticData', ['$resource', function ($resource) {
        return $resource(public + 'getOverallStatisticData');
    }])
    .factory('GetYYStatisticData', ['$resource', function ($resource) {
        return $resource(public + 'getYYStatisticData');
    }])
    .factory('GetQDStatisticData', ['$resource', function ($resource) {
        return $resource(public + 'getTuiStatisticData');
    }])
    .factory('GetXWTJStatisticData', ['$resource', function ($resource) {
        return $resource(public + 'getXWTJStatisticData');
    }])
    .factory('GetZHStatisticData', ['$resource', function ($resource) {
        return $resource(public + 'getZHStatisticData');
    }])
    .factory('GetCSStatisticData', ['$resource', function ($resource) {
        return $resource(public + 'getConsultStatisticData');
    }])
    .factory('GetDoctorStatisticData', ['$resource', function ($resource) {
        return $resource(public + 'getDoctorStatisticData');
    }])
    .factory('userPageStatistic', ['$resource', function ($resource) {
        return $resource(public + 'userPageStatistic');
    }])
    .factory('getUserFullToDoList', ['$resource', function ($resource) {
        return $resource(public + 'getUserFullToDoList');
    }])
    .factory('getBaseDataStatistics', ['$resource', function ($resource) {
        return $resource(public + 'getBaseDataStatistics');
    }])
    .factory('addBaseDataStatistics', ['$resource', function ($resource) {
        return $resource(public + 'addBaseDataStatistics');
    }]);

