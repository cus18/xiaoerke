define(['appNonRealTimeConsult','jquery'], function (app,$) {
    app
        .directive('bdfHead', ['$rootScope','$state','checkUserOrder',
            function ($rootScope,$state,checkUserOrder) {
                return {
                    restrict: 'EAC',
                    replace: true,
                    template: '<div class="head bg1 tc f8 border2">' +
                    '<div class="left fl f6 c3" ng-click="appointmentFirst()"><span class="city">首页</span> </div>' +
                    '<div class="title fl c3" >{{title}}</div>' +
                    '<div class="right fr"><a class= my" ng-click="myself()">' +
                    '<img class="my-img1" src="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fmy1.png" width="22" height="22" ng-if="!haveOrder">' +
                    '<img class="my-img1" src="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fmy2.png" width="22" height="22" ng-if="haveOrder"> </a> ' +
                    '</div> </div>',
                    link: function(scope,ele,attrs) {
                        scope.appointmentFirst = function(){
                            window.location.href = "http://s68.baodf.com/titan/firstPage/phoneConsult";
                        };
                        scope.myself = function(){
                            window.location.href = "http://s68.baodf.com/titan/phoneConsult#/selfCenter";
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
                template: '<div class="footer tc">' +
                '<p><a class="c1 f2 home" ng-click="appointmentFirst()">首页</a>' +
                '<a class="c1 f2" ng-click="commonQuestion()">常见问题</a></p>' +
                '</p><a class="c1 f2" href="tel:4006237120">客服电话：400-623-7120</a></p></div>',
                link: function(scope) {
                    scope.appointmentFirst = function(){
                        window.location.href = "firstPage/phoneConsult";
                    }
                    scope.commonQuestion = function(){
                        window.location.href = "baoFansCamp#/questions";
                    }
                }
            }
        }])
        .directive('errSrc', function() {
            return {
                link: function(scope, element, attrs) {
                    element.bind('error', function() {
                        if (attrs.src != attrs.errSrc) {
                            attrs.$set('src', attrs.errSrc);
                        }
                    });
                }
            }
        })
    //.directive('wxImg', ['$scope',function($scope) {
    //    return {
    //        restrict:'E',
    //        replace :true,
    //        template:'<img src="">',
    //        link: function(scope, elem, attr) {
    //            $scope.$watch('per',function(nowVal){
    //                //elem.attr('src',nowVal);
    //            })
    //        }
    //    };
    //}]);

        .directive('wxImg', [
            function (scope) {
                return {
                    restrict: 'E',
                    replace: true,
                    template: '<img class="pos" width="100%" src=""/>',
                    link: function(scope,ele,attrs) {
                        scope.$watch('per',function(nowVal){
                                            ele.attr('src',nowVal);
                                        })
                    }
                }
            }])

})
