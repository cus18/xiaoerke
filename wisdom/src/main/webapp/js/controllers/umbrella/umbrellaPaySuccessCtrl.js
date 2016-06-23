angular.module('controllers', ['ionic']).controller('umbrellaPaySuccessCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="宝大夫儿童家庭重疾互助计划";

            $scope.$on('$ionicView.enter', function() {
               /* $('.wp-inner').fullpage({
                    page: '.page',
                    dir: 'v',
                    drag: true,
                    start: 0,
                    duration: 100,
                    der: 0.1,
                    change: function (e) {
                        console.log('change', e.next, e.cur);
                        $('.indicator li').removeClass('cur').eq(e.cur).addClass('cur');
                    },
                    beforeChange: function (e) {
                        console.log('beforeChange', e.next, e.cur);
                    },
                    afterChange: function (e) {
                        console.log('afterChange', e.next, e.cur);
                    }
                });*/
            })

            
    }]);