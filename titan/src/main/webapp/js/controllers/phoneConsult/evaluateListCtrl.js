angular.module('controllers', ['ionic']).controller('evaluateListCtrl', [
        '$scope','$state','$stateParams','GetUserEvaluate','DoctorDetail',
        function ($scope,$state,$stateParams,GetUserEvaluate,DoctorDetail) {
            $scope.title = "评价";
            $scope.pageLoading =false;
            $scope.starImg1 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar.png";
            $scope.starImg2 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_part.png";
            $scope.starImg3 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_gray.png";
            $scope.classifyItem="all";
            $scope.userStarNum=[];
            /*假数据*/
            /* $scope.evaluateList=[
             {
             name:"宝粉1",
             phone:"15811111111",
             date:"03/12",
             week:"周二",
             time:"03:12",
             star:3,
             impression:"不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。"
             },
             {
             name:"宝粉2",
             phone:"15811111111",
             date:"02/12",
             week:"周二",
             time:"03:12",
             star:4,
             impression:"不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。"
             },
             {
             name:"宝粉3",
             phone:"15811111111",
             date:"03/14",
             week:"周二",
             time:"03:12",
             star:4.5,
             impression:"不错，挺好。不错，挺好。不错，挺好。不错，挺好。"
             },
             {
             name:"宝粉4",
             phone:"15811111111",
             date:"03/12",
             week:"周二",
             time:"03:12",
             star:5,
             impression:"不错，挺好。"
             },

             ];*/
            $scope.evaluateClassify = function(item){
                $scope.classifyItem =item;
            };
            DoctorDetail.get({"doctorId":$stateParams.doctorId},function(data){
                $scope.doctorDetail = data;
                $scope.avgMajorStar = data.doctorScore.avgMajorStar == null?"5": data.doctorScore.avgMajorStar;
                $scope.avgStar =  data.doctorScore.avgStar == null?"5":data.doctorScore.avgStar;
                $scope.doctorStar();
            });

            // 医生星级评价 星星个数
            $scope.doctorStar = function () {
                $scope.starNum=((parseFloat($scope.avgMajorStar)+parseFloat($scope.avgStar))/2).toFixed(1);;
                $scope.starNum= $scope.starNum.toString();
                console.log("star "+ $scope.starNum);
                $scope.starNumInt = parseInt($scope.starNum.substr(0, 1));
                $scope.starNumFloat=  parseInt($scope.starNum.substr(2, 1));

            }
            // 用户星级评价 星星个数
            $scope.userStar = function () {
                for(var i=0;i<$scope.evaluateList.length;i++){
                    if($scope.evaluateList[i].star==""){
                        $scope.evaluateList[i].star="5"
                    }
                    console.log($scope.evaluateList[i].star);
                    $scope.userStarNum[i] = parseInt( $scope.evaluateList[i].star);
                }
            }
            $scope.$on('$ionicView.enter', function(){
                GetUserEvaluate.save({"doctorId":$stateParams.doctorId,evaluateType:"1",pageNo:"1",pageSize:"1000"},function(data){
                    $scope.evaluateList = data.evaluateList;
                    $scope.userStar();
                });

            })
    }])
