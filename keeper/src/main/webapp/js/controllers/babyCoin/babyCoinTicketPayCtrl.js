angular.module('controllers', ['ionic']).controller('babyCoinTicketPayCtrl', [
    '$scope','$state','$stateParams','exchangeCoupon','GetBabyCoinInfo',
    function ($scope,$state,$stateParams,exchangeCoupon,GetBabyCoinInfo) {

        $scope.surePayLock=true; // 是否点击使用
        $scope.imgUrl = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/sendMindCoupon"+$stateParams.id;
        $scope.needCoin = parseInt($stateParams.name*10)


        //点击 赚取宝宝币
        $scope.earnBabyCoin = function () {
            $state.go("babyCoinInvitePage");
        };
        //点击 使用
        $scope.selectUse = function () {
            $scope.surePayLock=!$scope.surePayLock;

        };
        //点击 确认兑换
        $scope.sureExchange = function () {
            recordLogs("WSC_LJDH");
            exchangeCoupon.save({"id":parseInt($stateParams.id)},function (date) {
                if("success" == date.status){
                    window.location.href = date.link;
                }else if("failure" == date.status){
                    alert("宝宝币不足");
                }else{
                    alert("请重新打开页面兑换");
                }
            })
        };

        $scope.$on('$ionicView.enter', function(){
            GetBabyCoinInfo.save({},function (date) {
                $scope.babyCoinCash=date.babyCoinCash;
                if($scope.needCoin>$scope.babyCoinCash){
                    $scope.surePayLock=false;
                }
            })
        });

        function recordLogs(val){
            $.ajax({
                url:"util/recordLogs",// 跳转到 action
                async:true,
                type:'get',
                data:{logContent:encodeURI(val)},
                cache:false,
                dataType:'json',
                success:function(data) {
                },
                error : function() {
                }
            });
        };


    }]);
