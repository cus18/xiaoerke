define(['app','jquery'], function (app,$) {
    app
        .directive('bdfHead', ['$rootScope','$state','checkUserOrder',
            function ($rootScope,$state,checkUserOrder) {
                return {
                    restrict: 'EAC',
                    replace: true,
                    template: '<div class="head">' +
                    '<div class="left" ng-click="appointmentFirst()"><span class="city">首页</span> </div>' +
                    '<h1 class="title" >{{title}}</h1>' +
                    '<div class="right"><a class= my" ui-sref="myselfFirst">' +
                    '<img src="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fmy1.png" width="22" height="22" ng-if="!haveOrder">' +
                    '<img src="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fmy2.png" width="22" height="22" ng-if="haveOrder"> </a> ' +
                    '</div> </div>',
                    link: function(scope,ele,attrs) {
                        scope.appointmentFirst = function(){
                            window.location.href = "appoint";
                        }
                        scope.goBack = function(){
                            history.back();
                        }
                        scope.haveOrder = false;
                        checkUserOrder.save({unBindUserPhoneNum:$rootScope.unBindUserPhoneNum},function(data){
                            if(data.haveOrder){
                                scope.haveOrder = data.haveOrder
                            }
                        });
                    }
                }
            }])
        .directive('bdfFooter', [function () {
                return {
                    restrict: 'EAC',
                    replace: true,
                    template: '<div class="footer">' +
                    '<p><a ng-click="appointmentFirst()">首页</a>' +
                    '<a ui-sref="questions">常见问题</a></p>' +
                    '</p><a href="tel:4006237120">客服电话：400-623-7120</a></p></div>',
                    link: function(scope) {
                        scope.appointmentFirst = function(){
                            window.location.href = "appoint";
                        }
                    }
                }
            }])
})
