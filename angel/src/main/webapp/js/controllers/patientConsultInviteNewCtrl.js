angular.module('controllers', [])
    .controller('patientConsultInviteNewCtrl',['$scope','CreateInviteCard',
        function ($scope,CreateInviteCard) {
            $scope.inviteNewInit = function(){
                CreateInviteCard.save({},function(data){
                    $scope.headImgUrl = {
                        'background' : 'url('+data.headImgUrl+')'
                    };
                    $scope.userQRCode = {
                        'background' : 'url('+data.userQRCode+')'
                    };
                });
            }
        }])