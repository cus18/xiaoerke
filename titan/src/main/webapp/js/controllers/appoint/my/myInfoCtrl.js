angular.module('controllers', ['ionic']).controller('MyInfoCtrl', [
    '$scope','$state','$stateParams','$http','$ionicPopup',
    '$location','MyselfInfo','GetUserLoginStatus',
    function ($scope,$state,$stateParams,$http,$ionicPopup,
              $location,MyselfInfo,GetUserLoginStatus) {
        $scope.title = "我的资料";
        $scope.title0 = "宝大夫（400-623-7120）";
        $scope.userPhoneNum =$stateParams.userPhoneNum;
        $scope.message = {}

        $scope.showConfirm = function() {
            var confirmPopup = $ionicPopup.confirm({
                title: '确定要退出登录吗？',
                buttons: [
                    { text: '<b>确定</b>',
                        type: 'button-calm ',
                        onTap: function(e) {
                            $scope.pageLoading = true;
                            window.location.href = "logout";
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
            var routePath = "/appointBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                } else {
                    $scope.userName = data.userName;
                    $scope.userPhoneNum = data.userPhone;
                }
            })
        });
    }])
