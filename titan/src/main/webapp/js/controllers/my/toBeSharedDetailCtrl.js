angular.module('controllers', ['ionic']).controller('ToBeSharedDetailCtrl', [
        '$scope','$stateParams','MyselfInfoAppointmentDetail','GetUserLoginStatus','$location',
        function ($scope,$stateParams,MyselfInfoAppointmentDetail,GetUserLoginStatus,$location) {

            $scope.title = "预约详情"
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.workTime = "出诊时间："
            $scope.workLocation = "出诊地点："
            $scope.appointmentNumber = "订单号："
            $scope.appointmentStatus = "待分享"
            $scope.department = "科室："
            $scope.tips = "温馨提示"
            $scope.tips1 = "1、见医生须知，这是医生牺牲个人休息时间提供的服务，医院护士或工作人员有可能不知道，如需帮助请联系乖宝宝客服。"
            $scope.tips2 = "2. 咨询结束后，登录宝大夫完成评价，即可再次获得新的邀请码。"
            $scope.tips3 = "3. 如错误预约，则“取消预约”后，此邀请码可以继续使用。 "
            $scope.tips4 = "4.如医生不能履约，宝大夫会提前为您协调其他时间或其他医生，如医生爽约，请联系宝大夫客服：400-6237-120。"
            $scope.addDetail = "北京市朝阳区首都儿科研究所2楼北侧专家诊区11诊室"
            $scope.appointmentSuccess = "预约成功：";
            $scope.cancelAppointment = "取消预约";
            $scope.registerId = $stateParams.patient_register_service_id;

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/ap/appointBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    } else {
                        //根据用户的业务订购ID，获取医生信息，和预约详情信息
                        $scope.pageLoading = true;
                        MyselfInfoAppointmentDetail.save({patient_register_service_id:$stateParams.patient_register_service_id},
                            function(data){
                                $scope.pageLoading = false;
                                $scope.appointmentDataDetail = data;
                            })
                    }
                })
            });
        }])
