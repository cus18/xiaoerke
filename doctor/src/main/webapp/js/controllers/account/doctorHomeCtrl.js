angular.module('controllers', ['ionic']).controller('doctorHomeCtrl', [
    '$scope','$ionicPopup','$state','$stateParams','$location','GetUserLoginStatus','GetDoctorDetail','$filter','GetDoctorEvaluate',
    function ($scope,$ionicPopup,$state,$stateParams,$location,GetUserLoginStatus,GetDoctorDetail,$filter,GetDoctorEvaluate) {
        $scope.title = "医生信息";
        $scope.commentList =["全部评论","电话咨询","预约挂号"];
        $scope.commentTitle ="全部评论";
        $scope.haveComment = true;
        $scope.noComment = false;
        $scope.StayPart = false;//半颗星
        $scope.noEval = true;
        var num;
        var evalType;

        $scope.$on('$ionicView.enter', function(){
           $scope.moreComment = false;
           $scope.pageLoading = true;
           var routePath = "/doctorBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    $scope.starList = [];
                    $scope.evaluateCon = "点击加载更多";
                    GetDoctorDetail.save({"doctorId":$stateParams.id},function(data){
                        $scope.doctorId = data.doctorId;
                        $scope.doctorName = data.doctorName;
                        $scope.doctorHospitalName = data.hospitalName;
                        $scope.doctorDepartmentName = data.departmentName;
                        $scope.fansNum = data.fans_number;
                        $scope.doctorScore = data.doctorScore;
                        var synthesizeStar = (data.doctorScore.avgMajorStar+data.doctorScore.avgStar)/2;
                        var fl = fomatFloat(synthesizeStar,1);//保留1位小数
                        if(fl.toString().indexOf(".")!=-1){
                            for(var i=0;i<fl.toString().split(".")[0];i++){
                                $scope.starList.push(i);
                            }
                            $scope.StayPart = true;
                        }else{
                            for(var i=0;i<fl;i++){
                                $scope.starList.push(i);
                            }
                            $scope.StayPart = false;
                        }
                        num = 5;
                        evalType = "0";//全部评论
                        getEvaluate(evalType,num);

                    });

                }
            })

        });
        //关闭评论选择框
        $scope.close = function(){
            $scope.moreComment = false;
        }
        //去粉丝页面
        $scope.goFans = function(){
            $state.go("doctorFans",{id:$scope.doctorId});
        }

        //评论类型选择
        $scope.getComment = function(){
            $scope.moreComment = true;
        }
        //选择具体评论类型
        $scope.setComment = function (index) {
            num = 5;
            $scope.moreComment = false;
            $scope.commentTitle = $scope.commentList[index];
            if($scope.commentTitle=="全部评论"){
                evalType = "0";
                $scope.commentList = ["全部评论","电话咨询","预约挂号"];
            }
            if($scope.commentTitle=="电话咨询"){
                evalType = "1";
                $scope.commentList = ["电话咨询","全部评论","预约挂号"];
            }
            if($scope.commentTitle=="预约挂号"){
                evalType = "2";
                $scope.commentList = ["预约挂号","全部评论","电话咨询"];
            }
            getEvaluate(evalType,num);
        }
        //获取更多评论
        $scope.getMoreEval = function () {
            num = num+5;
            getEvaluate(evalType,num);
            $scope.moreComment = false;
        }

        var getEvaluate = function(type,size){
            $scope.pageLoading = true;
            GetDoctorEvaluate.save({"doctorId":$scope.doctorId,"evaluateType":type,"pageNo":"1","pageSize":size},function(data){
                $scope.pageLoading = false;
                if(data.evaluateList.length==0){
                    $scope.haveComment = false;
                    $scope.noComment = true;
                }
                if(data.evaluateList.length<size){
                    $scope.haveComment = true;
                    $scope.noComment = false;
                    $scope.evaluateList = data.evaluateList;
                    $scope.noEval = false;
                    $scope.evaluateCon = "已全部显示";
                }else{
                    $scope.haveComment = true;
                    $scope.noComment = false;
                    $scope.evaluateList = data.evaluateList;
                    $scope.noEval = true;
                    $scope.evaluateCon = "点击加载更多";
                }

            });
        }


        function fomatFloat(src,pos){
            return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);
        }

    }]);




