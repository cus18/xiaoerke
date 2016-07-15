angular.module('controllers', ['ionic']).controller('heightForecastResultCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        /*
        孩子身高公式：
        女儿的身高=（父亲身高+母亲身高-13）/2，随机+1~3厘米
        儿子的身高=（父亲身高+母亲身高+13）/2，随机+1~5厘米

        结果如下
        男宝：
        低于170，文质彬彬
        低于175，气宇轩昂
        低于180，玉树临风
        低于185，长腿欧巴
        低于190，顶天立地
        高于190，篮球飞人

        女宝：
        低于160，小家碧玉
        低于165，大家闺秀
        低于170，窈窕淑女
        高于170，维密超模

        例子：恭喜您的宝宝将来会成为身高172的维密超模！
        */

        $scope.boyHeight = $stateParams.resultBoy;
        console.log($stateParams.resultGirl);
        console.log($stateParams.resultBoy);
        $scope.goUmbrella =function(){
            window.location.href="http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000005/a"
        };
    }]);

