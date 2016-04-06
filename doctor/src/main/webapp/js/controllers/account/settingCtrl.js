angular.module('controllers', ['ionic']).controller('settingCtrl', [
        '$scope','$ionicPopup','$state','$stateParams','CheckBind','GetDoctorInfo','OutOfBind','$location','GetUserLoginStatus',
        function ($scope,$ionicPopup,$state,$stateParams,CheckBind,GetDoctorInfo,OutOfBind,$location,GetUserLoginStatus) {

            $scope.aboutUs = function(){
                $state.go("aboutUs");
            }

            $scope.outOfBind = function(){
                $scope.pageLoading = true;
                var confirmPopup = $ionicPopup.confirm({
                    title: '确定要退出登录吗？',
                    //template: '您真的确定?' ,
                    buttons: [
                        { text: '<b>确定</b>',
                            type: 'button-calm ',
                            onTap: function(e) {
                                $scope.pageLoading = true;
                                OutOfBind.save(function(data){
                                    $scope.pageLoading = false;
                                    //$state.go("bindDoctorPhone",{"redirect":"doctorFirst"});
                                    WeixinJSBridge.call('closeWindow');
                                })
                            }
                        },
                        { text: '取消',
                            type: 'button-calm',
                        }
                    ]
                });
            }

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/doctorBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    }
                })

                $scope.scrollLoading = "加载更多";
                $scope.pageLoading = true;
                GetDoctorInfo.save({},function(data){
                    $scope.pageLoading = false;
                    if(data.doctorId!=""){
                        $scope.doctorId = data.doctorId;
                        $scope.choice = "everyDayList";
                    }
                });
            });
    }])
