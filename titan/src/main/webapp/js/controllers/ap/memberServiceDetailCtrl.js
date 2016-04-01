angular.module('controllers', ['ionic']).controller('memberServiceDetailCtrl', [
        '$scope','$stateParams','MemberServiceDetail','$state',
        function ($scope,$stateParams,MemberServiceDetail,$state) {

            $scope.title = "会员服务"
            $scope.title0 = "宝大夫（400-623-7120）"

            MemberServiceDetail.get({"memberServiceId":$stateParams.memberServiceId},function(data){
             $scope.memberVo = data.memberVo

             $scope.startDate = moment(data.memberVo.createDate).format('YYYY/MM/DD');
             $scope.endDate = moment(data.memberVo.endDate).format('YYYY/MM/DD');
                console.log(data)
            });

            $scope.buttonAction = function(){
                window.location.href="ap/firstPage/appoint";
            }

    }])
