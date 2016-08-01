angular.module('controllers', []).controller('olympicBabyInvitationCardCtrl', [
    '$scope','$state','$timeout',
    function ($scope,$state,$timeout) {

        var imgList = [     "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao1.png",
                            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao2.png",
                            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao3.png",
                            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao4.png",
                            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao5.png",
                            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_yao6.png"];

        $scope.imgIndex = imgList[5];
        $scope.imgEwm = "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/olympicBaby/invitationCard/olympic_erweima.png";
        
        
    }]);
