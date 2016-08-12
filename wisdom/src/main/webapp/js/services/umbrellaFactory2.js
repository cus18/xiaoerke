/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var umbrella_app = "umbrella/thirdParty/";

define(['appUmbrella2'], function (app) {
    app
        //获取参加保护伞总人数
        .factory('GetJoinNumber', ['$resource', function ($resource) {
            return $resource(umbrella_app + 'firstPageMutualHelpCount');
        }])
        //根据用户输入的手机号判断该用户是否已经购买宝护伞以及是否关注平台
        .factory('CheckPhone', ['$resource', function ($resource) {
            return $resource(umbrella_app + 'checkIsBuyAndAttention');
        }])
        //添加宝宝及父母信息
        .factory('SaveMessage', ['$resource', function ($resource) {
            return $resource(umbrella_app + 'joinUs');
        }])
        //获取临时二维码
        .factory('GetUserQRCode', ['$resource', function ($resource) {
            return $resource(umbrella_app + 'getUserQRCode');
        }])
        //调用支付宝支付
        .factory('Goalipayment', ['$resource', function ($resource) {
            return $resource(umbrella_app + 'alipayment');
        }])

})

