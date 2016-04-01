define(['appPatientConsult','jquery'], function (app,$) {
    app
        .directive('bdfFooter', [function () {
                return {
                    restrict: 'EAC',
                    replace: true,
                    template: '<div class="footer">' +
                    '<p><a ng-click="appointmentFirst()">首页</a>' +
                    '<a ui-sref="questions">常见问题</a></p>' +
                    '<a href="tel:4006237120">客服电话：400-623-7120</a></p></div>',
                    link: function(scope) {
                        scope.appointmentFirst = function(){
                            window.location.href = "ap/knowledge";
                        }
                    }
                }
            }])
})
