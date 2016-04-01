angular.module('controllers', ['ionic']).controller('AppointmentCancelCtrl', [
    '$scope','GetUserLoginStatus','$state','$location',,'MyselfInfoAppointmentDetail',
    'OrderCancelOperation','$stateParams',
        function ($scope,GetUserLoginStatus,$state,$location,MyselfInfoAppointmentDetail,
                  OrderCancelOperation,$stateParams) {
            $scope.title="取消预约";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.lock=false;
            $scope.info = {};
            $scope.info.buttonLock = false;

           $scope.skip = function(){
                $('html,body').animate({scrollTop:$('#reason').offset().top}, 0);
            }

            $scope.select = function(a){
                if(a=="1"){
                    $scope.lock=false;
                    $scope.cancelReason = "临时有事，无法按时就诊";
                }
                else if(a=="2"){
                    $scope.lock = false;
                    $scope.cancelReason = "预约有误，重新预约";
                }
                else if(a=="3"){
                    $scope.lock=true;
                    $scope.cancelReason=$scope.info.reason;
                    $("#reason").click(function(){
                        $('html,body').animate({scrollTop:$('#reason').offset().top}, 0);
                    });
               }
            }

            //修改订单状态，表明完成支付，等待就诊
            $scope.appointmentCancel = function(){
                if($scope.info.reason!=undefined)
                {
                    $scope.cancelReason = $scope.info.reason;
                }
                $scope.info.buttonLock = true;
                $scope.pageLoading = true;
                OrderCancelOperation.save({"patient_register_service_id":$stateParams.patient_register_service_id,
                    "appointmentNo": $scope.appointmentDataDetail.appointmentNo,"flag":"0","reason":$scope.cancelReason
                },function(data){
                    if(data.status=="20")
                    {
                        alert("很抱歉，就诊前3小时无法取消预约；\n如需帮助，请联系客服：400-623-7120。");
                        $scope.pageLoading = false;
                        history.back();
                        return;
                    }
                    $scope.pageLoading = false;
                    $scope.info.buttonLock = false;
                    $state.go('appointmentCancelSuccess',{"amount":data.amount,
                        payType:data.memberServiceId,
                        relationType:data.relationType});
                })
            }

            //获取个人的订单信息
            $scope.pageLoading = true;
            MyselfInfoAppointmentDetail.save({
                patient_register_service_id : $stateParams.patient_register_service_id
            }, function(data) {
                $scope.pageLoading = false;
                $scope.appointmentDataDetail = data;
                var title = ((data.status == '1') ? "取消成功" : "取消失败");
                $scope.info2 = title
                $scope.info1 = data.doctorName + " - " + data.date
                $scope.workLocation = data.location
                $scope.appointmentNumber = data.appointmentNo
                $scope.endTime = data.endTime
                $scope.isPayOrder = data.isPayOrder
            })

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
                        $scope.info = {};
                        $scope.cancelReason = "临时有事，无法按时就诊";
                    }
                })
            });
    }])
