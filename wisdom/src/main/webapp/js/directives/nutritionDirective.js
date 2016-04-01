define(['appNutrition','jquery'], function (app,$) {
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
        .directive('nutritionFooter', [function () {
                return {
                    restrict: 'EAC',
                    replace: true,
                    template:
                    '<div class="my-footer tabs tabs-icon-top">' +
                    '<a class="tab-item"  ng-click="menuSelect(0)"><i class="icon "><img width="25" height="24.5" ng-src="{{home}}"> </i> 首页</a>' +
                    '<a class="tab-item" ng-click="menuSelect(1)"><i class="icon "><img width="25" height="24.5"  ng-src="{{assess}}"></i> 评估</a>' +
                    '<a class="tab-item" ng-click="menuSelect(4)"><i class="icon "><img width="25" height="24.5"  ng-src="{{assess}}"></i> 提问</a>' +
                    '<a class="tab-item" ng-click="menuSelect(2)"><i class="icon "><img width="26.5" height="24"  ng-src="{{report}}"></i> 报告</a>' +
                    '<a class="tab-item" ng-click="menuSelect(3)"><i class="icon "><img width="26.5" height="24.5"  ng-src="{{necessary}}"></i> 必备</a>' +
                    '</div>',
                    link: function(scope,state) {
                        scope.footerNum=0;
                        scope.home="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer1_select.png";
                        scope.assess="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer2.png";
                        scope.report="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer3.png";
                        scope.necessary="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer4.png";
                        scope.menuSelect = function(index){
                            if(index==0){
                                window.location.href = "ap/healthPlan#/nutritionIndex";
                                scope.home="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer1_select.png";
                                scope.assess="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer2.png";
                                scope.report="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer3.png";
                                scope.necessary="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer4.png";

                            }
                            else if(index==1){
                                window.location.href = "ap/healthPlan#/nutritionAssess";
                                scope.home="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer1.png";
                                scope.assess="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer2_select.png";
                                scope.report="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer3.png";
                                scope.necessary="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer4.png";
                            }
                            else if(index==2){
                                window.location.href = "ap/healthPlan#/nutritionReport";
                                scope.home="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer1.png";
                                scope.assess="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer2.png";
                                scope.report="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer3_select.png";
                                scope.necessary="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer4.png";
                            }
                            else {
                                window.location.href = "ap/healthPlan#/nutritionNecessary";
                                scope.home="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer1.png";
                                scope.assess="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer2.png";
                                scope.report="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer3.png";
                                scope.necessary="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer4_select.png";

                            }
                        }
                    }
                }
            }])
})
