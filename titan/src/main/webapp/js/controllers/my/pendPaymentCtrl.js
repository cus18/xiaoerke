angular.module('controllers', ['ionic']).controller('PendPaymentCtrl', [
    '$scope','MyselfInfoAppointment','$state','$ionicPopup',
    'OrderCancelOperation','$rootScope','MyselfInfoAppointmentByPhone',
    'GetUserMemberService','GetUserLoginStatus','$location',
        function ($scope,MyselfInfoAppointment,$state,$ionicPopup,
                  OrderCancelOperation,$rootScope,MyselfInfoAppointmentByPhone,
                  GetUserMemberService,GetUserLoginStatus,$location) {

            $scope.title = "待支付"
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.goPay = "去支付";
            $scope.doctorInfo = "朱春梅 主任医师 教授";
            $scope.waitPay = "待支付";
            $scope.orderSucccess = "预约成功";
            $scope.isBlank = false;

            $scope.toPayAppointment = function(patient_register_service_id)
            {
                GetUserMemberService.save({},function(data){
                    $scope.memberServiceItem = "";
                    for(var i=0;i<data.memberServiceList.length;i++){
                        if(data.memberServiceList[i].leftTimes>0){
                                $scope.memberServiceItem = data.memberServiceList[i]
                        }
                    }
                        if($scope.memberServiceItem != ""){
                            $state.go("appointmentUseMember",{patient_register_service_id:patient_register_service_id,
                                doctorName:"",
                                date:"",
                                begin_time:"",
                                end_time:"",
                                week:"",
                                hospitalName:"",
                                position:""})
                        }else{
                            window.location.href="/xiaoerke-appoint/pay/patientPay.do?patient_register_service_id="
                                + patient_register_service_id + "&chargePrice=15000";
                        }


                })
            }

            $scope.showPopup = function(index) {
                $scope.data = {}
                // An elaborate, custom popup
                var myPopup = $ionicPopup.show({
                    template: '',
                    title: '确定要删除吗？',
                    subTitle: '',
                    scope: $scope,
                    buttons: [
                        {
                            text: '取消',
                            type: 'button-calm'
                        },
                        {
                            text: '<b>确定</b>',
                            type: 'button-calm',
                            onTap: function (e) {
                                //取消订单
                                $scope.pageLoading = true;
                                OrderCancelOperation.save({"patient_register_service_id":$scope.appointmentData[index].patient_register_service_id,
                                    "action":{"status":"2","appointmentNo": $scope.appointmentData[index].appointmentNo,"flag":"1"}},function(data){
                                    $scope.pageLoading = true;
                                    MyselfInfoAppointment.save({"pageNo": "1", "pageSize": "10", "status": "0"}, function (data) {
                                        $scope.pageLoading = false;
                                        if (data.failure == "0") {
                                            $state.go("bindUserPhone");
                                        }
                                        $scope.appointmentData = data.appointmentData;
                                        if( data.appointmentData.length==0){
                                            $scope.care1 = "医生正在热情的等待哦"
                                            $scope.care2 = "赶紧为宝宝预订私人医生吧"
                                            $scope.isBlank = true
                                        }
                                    });
                                })
                            }
                        },
                    ]
                });
            }

            $scope.$on('$ionicView.enter', function() {
                $scope.pageLoading = true;
                var routePath = "/ap/appointBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath: routePath}, function (data) {
                    $scope.pageLoading = false;
                    if (data.status == "9") {
                        window.location.href = data.redirectURL;
                    }
                    else {
                        $scope.pageLoading = true;
                        MyselfInfoAppointment.save({"pageNo": "1", "pageSize": "10", "status": "0"}, function (data) {
                            $scope.pageLoading = false;
                            console.log(data)
                            $scope.appointmentData = data.appointmentData;
                            if (data.appointmentData.length == 0) {
                                $scope.care1 = "医生正在热情的等待哦"
                                $scope.care2 = "赶紧为宝宝预约医生吧"
                                $scope.isBlank = true
                            }
                        })
                    }
                })
            })
        }])
