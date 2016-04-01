angular.module('controllers', ['ionic']).controller('myCardCtrl', [
        '$scope','$state','$stateParams','CheckBind','GetDoctorInfo','$location','GetUserLoginStatus',
        function ($scope,$state,$stateParams,CheckBind,GetDoctorInfo,$location,GetUserLoginStatus) {

            CheckBind.save({},function(data){
                $scope.pageLoading = false;
                if(data.status=="0")
                {
                    $state.go("bindDoctorPhone",{"redirect":"myselfFirst"});
                }
            })

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/ap/doctorBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    }
                })

                $scope.pageLoading = true;
                GetDoctorInfo.save({},function(data){
                    $scope.pageLoading = false;
                    if(data.doctorId!=""){
                        $scope.doctorId = data.doctorId;
                        $scope.doctorName = data.doctorName;
                        $scope.position1 = data.position1;
                        $scope.position2 = data.position2;
                        $scope.hospitalName = data.hospitalName;
                        $scope.department = data.department;
                        $scope.imgUrl = !data.extensionQrcode?("http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/images%2Fdoctor_code.png"):
                            ("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+data.extensionQrcode);
                    }else{
                        $state.go("bindDoctorPhone",{"redirect":"myselfFirst"});
                    }
                });
            });
    }])
