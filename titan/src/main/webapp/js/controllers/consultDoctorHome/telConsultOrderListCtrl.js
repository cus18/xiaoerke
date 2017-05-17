angular.module('controllers', ['ionic']).controller('telConsultOrderListCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.itemClassify ="waitingCall";
        // 假数据
        $scope.WaitingCallList =[
            {
                doctorAvatar:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png',
                doctorName:'王波',
                doctorTitle:'主任医师',
                price:'30',
                userName:'王某某',
                userPhone:'13200000000'
            },
            {
                doctorAvatar:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png',
                doctorName:'李波波',
                doctorTitle:'副主任医师',
                price:'30',
                userName:'张某某',
                userPhone:'13500000000'
            },
            {
                doctorAvatar:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png',
                doctorName:'王医生',
                doctorTitle:'主任医师',
                price:'30',
                userName:'陈某某',
                userPhone:'13800000000'
            }

        ];
        $scope.CalledList =[
            {
                doctorAvatar:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png',
                doctorName:'王波波',
                doctorTitle:'主任医师',
                price:'30',
                userName:'许某某',
                userPhone:'15200000000'
            },
            {
                doctorAvatar:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png',
                doctorName:'李医生',
                doctorTitle:'副主任医师',
                price:'30',
                userName:'张某某',
                userPhone:'18500000000'
            },
            {
                doctorAvatar:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyishengmoren.png',
                doctorName:'王医生',
                doctorTitle:'主任医师',
                price:'30',
                userName:'陈某某',
                userPhone:'13800000000'
            }

        ];

        //点击 顶部的 待接通 已接通
        $scope.selectClassify=function(item){
            $scope.itemClassify =item;

        };



        $scope.$on('$ionicView.beforeEnter',function() {

        })

    }]);

