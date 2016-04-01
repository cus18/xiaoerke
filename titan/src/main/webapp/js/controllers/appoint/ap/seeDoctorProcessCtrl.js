angular.module('controllers', ['ionic']).controller('seeDoctorProcessCtrl', [
        '$scope','$state','$stateParams','GetHospitalInfoById',
        function ($scope,$state,$stateParams,GetHospitalInfoById) {
            $scope.info = {};
            $scope.title = "见医生流程";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.Text= ["请预先到医院办卡处办理医院诊疗卡；",
                "直接找医生告知您从宝大夫预约及宝宝姓名（请提前15分钟到达诊室），听从医生安排就诊；",
                "这是医生无偿提供的额外服务，请珍惜并尊重医生；",
                "医院护士不知道该服务，如需帮助或投诉请致电400-623-7120。"];
            $scope.$on('$ionicView.enter', function(){
                GetHospitalInfoById.save({id:$stateParams.hospitalId},function(data){
                    $scope.processText = data.medicalProcess.split("；");
                });
            });

    }])
