angular.module('controllers', [])
    .controller('helpYouChooseFromYouZanCtrl', ['$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {

            $scope.consultContent = [];

            //点击查看医生团
            $scope.lookDoctorGroup=function(){
                window.location.href="https://baodaifu.kuaizhan.com/58/72/p401526039c1905?from=singlemessage&isappinstalled=0";
            };

            //点击咨询医生
            $scope.goConsultDoctor=function(){
                $state.go("patientConsultYouZan");
            };


            $scope.$on('$ionicView.enter', function(){

            });


        }])