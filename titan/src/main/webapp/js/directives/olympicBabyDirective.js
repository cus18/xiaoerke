define(['appOlympicBaby','jquery'], function (app,$) {
    app
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

})
