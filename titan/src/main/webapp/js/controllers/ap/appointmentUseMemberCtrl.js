angular.module('controllers', ['ionic']).controller('appointmentUseMemberCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location) {
        $scope.title = "预约";
        $scope.date = $stateParams.date;
        $scope.begin_time = $stateParams.begin_time;
        $scope.end_time = $stateParams.end_time;
        $scope.patient_register_service_id = $stateParams.patient_register_service_id;
        $scope.info = {}
        $scope.info.buttonLock = false

        $scope.appoint = function(){
            $scope.pageLoading = true;
            $scope.info.buttonLock = true
            OrderPayMemberServiceOperation.get({patient_register_service_id:$scope.patient_register_service_id,
                memSrsItemSrsRelId:$scope.memberServiceItem.memSrsItemSrsRelId},function(data){
                $scope.pageLoading = false;
                $scope.info.buttonLock = false
                if(data.status=="1") {
                    $state.go('appointmentSuccess', {patient_register_service_id:$scope.patient_register_service_id});
                }
                else if(data.status=="2"){
                    alert("亲,您已经为此订单使用过预约券了,谢谢!");
                    $state.go("appointmentFirst");
                }
                else{
                    window.location.href="ap/firstPage/appoint";
                }
            })
        }

        $scope.$on('$ionicView.enter',function() {
            $scope.pageLoading = true;
            var routePath = "/ap/appointBBBBBB" + $location.path();
            MyselfInfoAppointmentDetail.save({patient_register_service_id :
                $stateParams.patient_register_service_id,
                routePath:routePath
            }, function(data) {
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                } else{
                    $scope.pageLoading = false;
                    $scope.appointmentDataDetail = data;
                    GetUserMemberService.save({},function(data){
                        for(var i=0;i<data.memberServiceList.length;i++){
                            if(data.memberServiceList[i].leftTimes>0){
                                $scope.memberServiceItem = data.memberServiceList[i]
                                return;
                            }
                        }
                    })
                }
            })
        })

    }])
