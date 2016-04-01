define(['appConstipation','jquery'], function (app,$) {
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
                    '<img src="images/me1.png" width="22" height="22" ng-if="!haveOrder">' +
                    '<img src="images/me2.png" width="22" height="22" ng-if="haveOrder"> </a> ' +
                    '</div> </div>',
                    link: function(scope,ele,attrs) {
                        scope.appointmentFirst = function(){
                            window.location.href = "ap";
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
})
