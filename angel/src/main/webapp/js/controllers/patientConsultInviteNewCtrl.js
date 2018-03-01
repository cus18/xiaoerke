angular.module('controllers', [])
    .controller('patientConsultInviteNewCtrl', ['$scope', 'CreateInviteCard','$stateParams',
        function ($scope, CreateInviteCard,$stateParams) {

            $scope.marketer = $stateParams.marketer;
            $scope.oldOpenId = $stateParams.oldOpenId;
            CreateInviteCard.save({"marketer":$scope.marketer,"oldOpenId":$scope.oldOpenId}, function (data) {
                $scope.headImgUrl = {
                    'background': 'url(' + data.headImgUrl + ')',
                    'background-size': '100% 100%'
                };
                $scope.headImgNickName = data.babyCoinVo.nickName;
                /*$scope.userQRCode = {
                    'background': 'url(' + data.userQRCode + ')',
                    'background-size': '100% 100%'
                };*/
                $scope.userQRCode = data.userQRCode;
            });
        }])