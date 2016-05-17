/**
 * 取得实际位置
 * 调用方式：geoFactory.getGeo();
 */
var user_h5 = ''
var wxChat = '/wechatInfo/'

define([appName], function (app) {
    app
        //获取用户登陆状态
        .factory('GetUserLoginStatus', ['$resource', function ($resource) {
            return $resource(user_h5 + 'auth/info/loginStatus');
        }])
        //发送验证码
        .factory('IdentifyUser', ['$resource', function ($resource) {
            return $resource(user_h5 + 'util/user/getCode');
        }])
        .factory('RecordLogs',['$resource',function ($resource){
            return $resource(user_h5 + 'util/recordLogs');
        }])
        //send WechatMessage To User
        .factory('SendWechatMessageToUser',['$resource',function ($resource){
            return $resource(wxChat + 'postInfoToWechat');
        }])
        .factory('SendWechatMessageToUserOnline',['$resource',function ($resource){
            return $resource(wxChat + 'postInfoToWechatOnline');
        }])
        //根据用户的登陆状态，进行相应的操作
        .factory('resolveUserLoginStatus', ['GetUserLoginStatus','$state',
            function(GetUserLoginStatus,$state) {
                var runFirstRequest = function(redirectParam,routeParam,goParam,hrefParam,action) {
                    var routePath = "";
                    if(action=="go"){
                        routePath = "/appointBBBBBB/"+redirectParam+"/"+routeParam;
                    } else if(action=="notGo"){
                        routePath = "/appointBBBBBB/"+redirectParam;
                    }
                    GetUserLoginStatus.save({routePath:routePath},function(data){
                        if(data.status=="9") {
                            window.location.href = data.redirectURL;
                        } else if(data.status=="8"){
                            window.location.href = data.redirectURL+"?targeturl="+routePath;
                        }else {
                            if(action=="notGo"){
                                if(hrefParam==""){
                                    $state.go(redirectParam);
                                }else{
                                    window.location.href = hrefParam;
                                }
                            }else if(action=="go"){
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
