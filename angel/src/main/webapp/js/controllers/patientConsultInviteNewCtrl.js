angular.module('controllers', [])
    .controller('patientConsultInviteNewCtrl', ['$scope', 'CreateInviteCard',
        function ($scope, CreateInviteCard) {

            var reg = new RegExp("(^|&)" + "oldOpenId" + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            var oldOpenId = unescape(r[2])
            alert(oldOpenId);
            //var marketer = GetQueryString("marketer");
            alert(marketer);
            CreateInviteCard.save({}, function (data) {
                $scope.headImgUrl = {
                    'background': 'url(' + data.headImgUrl + ')',
                    'background-size': '100% 100%'
                };
                $scope.headImgNickName = data.babyCoinVo.nickName;
                $scope.userQRCode = {
                    'background': 'url(' + data.userQRCode + ')',
                    'background-size': '100% 100%'
                };
            });
        }])