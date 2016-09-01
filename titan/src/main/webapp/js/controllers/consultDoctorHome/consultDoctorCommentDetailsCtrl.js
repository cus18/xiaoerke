angular.module('controllers', ['ionic']).controller('consultDoctorCommentDetailsCtrl', [
    '$scope','$state','$stateParams','FindDoctorAllEvaluation',
    function ($scope,$state,$stateParams,FindDoctorAllEvaluation) {

        $scope.showMore = true;
        var num = 0;
        $scope.$on('$ionicView.beforeEnter',function() {
            $scope.doctorName = $stateParams.name;
            $scope.commentList = [];

            //判断医生头像是否存在
            var doctorImg = "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/"+$stateParams.id;
            if(doctorImg==null||doctorImg==undefined||doctorImg==""){
                if($stateParams.gender == "1"){
                    $scope.doctorImg = "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/consultDoctor/home_docman.png";
                }else{
                    $scope.doctorImg = "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/consultDoctor/home_dowocman.png";
                }
            }else{
                $scope.doctorImg = doctorImg;
            }

            FindDoctorAllEvaluation.get({"userId":$stateParams.id,"pageNo":num,"pageSize":10},function (data) {
                console.log("data",data);
                $scope.allEvaluationList = data.allEvaluationList[0];

                for(var i = 1;i<data.allEvaluationList.length;i++){
                    $scope.commentList.push(data.allEvaluationList[i]);
                }

            });

        });

        //查看更多
        $scope.goMore = function () {
            num++;
            FindDoctorAllEvaluation.get({"userId":$stateParams.id,"pageNo":num,"pageSize":10},function (data) {
                console.log("data",data);
                if(data.allEvaluationList.length<10){
                    $scope.showMore = false;
                }else{
                    $scope.allEvaluationList = data.allEvaluationList[0];

                    for(var i = 1;i<data.allEvaluationList.length;i++){
                        $scope.commentList.push(data.allEvaluationList[i]);
                    }
                }
            });

        }


    }]);


