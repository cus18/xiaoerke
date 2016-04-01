angular.module('controllers', ['ionic']).controller('withDrawlsCtrl', [
        '$scope','$state','$stateParams','CheckBind','GetDoctorInfo','WithDrawMoney','$location','GetUserLoginStatus',
        function ($scope,$state,$stateParams,CheckBind,GetDoctorInfo,WithDrawMoney,$location,GetUserLoginStatus) {

            $scope.weeks = ["星期一","星期二","星期三","星期四","星期五","星期六","星期日"]

            $scope.withDrawlsConfirm = function(){
                if($scope.totalPrice<$scope.info.money)
                {
                    alert('亲，你提取的金额，大于你的账户余额啦');
                }
                else {
                    WithDrawMoney.get({"money": parseFloat($scope.info.money)}, function (data) {
                        if(data.result=="success"){
                            $state.go("withDrawlsConfirm",{"arriveDate":moment().add('days',3).format('YYYY-MM-DD')+"  "
                            +$scope.weeks[moment().add('days',3).format('d')-1]+"  "+moment().add('days',3).format('HH:mm:ss'),
                                "money":$scope.info.money});
                        }
                        else
                        {
                            alert('亲，提现出问题了，请稍后再试');
                        }
                    })
                }
            }

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/ap/doctorBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    }else{
                        $scope.info=[];
                        $scope.pageLoading = true;
                        GetDoctorInfo.save({},function(data){
                            $scope.pageLoading = false;
                            if(data.doctorId!=""){
                                $scope.doctorId = data.doctorId;
                                $scope.balance = data.balance;
                            }else{
                                $state.go("bindDoctorPhone",{"redirect":"doctorFirst"});
                            }
                        });
                    }
                })
            });
    }])
