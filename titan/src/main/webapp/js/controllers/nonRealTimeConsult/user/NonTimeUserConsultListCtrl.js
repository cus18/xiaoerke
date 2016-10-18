angular.module('controllers', []).controller('NonTimeUserConsultListCtrl', [
        '$scope','$state','$http',
        function ($scope,$state,$http) {
            $scope.selectItem = "cur";
            $scope.curMessageList = [
                {
                    pic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                    date:"10月8日",
                    name:"梁平",
                    department:"妇科",
                    position:"主任",
                    message:"这是因为肠胃问题引起的",
                    state:"2"

                },
                {
                    pic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                    date:"10月12日",
                    name:"王医生",
                    department:"儿内科",
                    position:"副主任",
                    message:"肠胃问题",
                    state:"1"
                },
                {
                    pic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                    date:"15:12",
                    name:"王医生",
                    department:"眼科",
                    position:"副主任",
                    message:"这属于正常问题",
                    state:"2"
                },
            ];
            $scope.allMessageList = [
                {
                    pic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                    date:"10月10日",
                    name:"李医生",
                    department:"儿外科",
                    position:"副主任",
                    message:"肠胃问题",
                    state:"0"

                },
                {
                    pic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                    date:"10月10日",
                    name:"王医生",
                    department:"耳鼻喉科",
                    position:"副主任",
                    message:"视力问题",
                    state:"2"
                },
                {
                    pic:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg",
                    date:"10月10日",
                    name:"张医生",
                    department:"皮肤科",
                    position:"主任",
                    message:"肠胃问题",
                    state:"1"
                }
            ];
            $scope.selectService=function(item){
                $scope.selectItem = item;
            };
            $scope.NonTimeUserConsultListInit = function(){

            }
    }]);
