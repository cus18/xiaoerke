angular.module('controllers', [])
    .controller('patientConsultInviteNewCtrl',['$scope','CreateInviteCard',
        function ($scope,CreateInviteCard) {
            $scope.inviteNewInit = function(){
                CreateInviteCard.save({},function(data){
                    $scope.headImgUrl = {
                        'background' : 'url('+data.headImgUrl+')',
                        'background-size' : '100% 100%'
                    };
                    $scope.userQRCode = {
                        'background' : 'url('+data.userQRCode+')',
                        'background-size' : '100% 100%'
                    };
                });
            }
        }])