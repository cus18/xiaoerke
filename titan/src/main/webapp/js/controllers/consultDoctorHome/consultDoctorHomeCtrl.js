angular.module('controllers', ['ionic']).controller('consultDoctorHomeCtrl', [
    '$scope','$state','$stateParams','GetConsultDoctorHomepageInfo',
    function ($scope,$state,$stateParams,GetConsultDoctorHomepageInfo) {

        $scope.evaluationList = [];
        $scope.commentShow = false;//显示评论列表
        $scope.commentList = [];//保存评论列表
        $scope.goDetails = false;//评论详情页

        $scope.$on('$ionicView.beforeEnter',function() {
            //"00034ads0d764sdsa66a2d6esd0e8ddf";
            $scope.evaluationList = [];
            $scope.commentList = [];//保存评论列表
            GetConsultDoctorHomepageInfo.get({"userId":$stateParams.id},function (data) {
                $scope.doctorName = data.doctorName;//医生名字
                $scope.department = data.department;//医生科室
                $scope.title = data.title;//医生职位
                $scope.practitionerCertificateNo = data.practitionerCertificateNo;//医生注册号
                $scope.personNum = data.personNum;//医生问诊量
                $scope.evaluationList = data.evaluationList;//医生评论
                $scope.allEvaluationNum = data.allEvaluationNum;//评论条数
                $scope.allEvaluationNum == '0' ? $scope.allEvaluationNum = '1':$scope.allEvaluationNum = $scope.allEvaluationNum;
                $scope.description = data.description.split(" ");//医生介绍
                $scope.rate = data.rate*100;//医生有用百分比
                $scope.gender = data.gender;
                for (i = $scope.description.length - 1;  i >= 0; i--) {
                    if ($scope.description[i] === '') {
                        $scope.description.splice(i, 1);
                    }
                }
                //判断医生头像是否存在
                var doctorImg = "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/"+$stateParams.id;
                if(doctorImg==null||doctorImg==undefined||doctorImg==""){
                    if(data.gender == "1"){
                        $scope.doctorImg = "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/consultDoctor/home_docman.png";
                    }else{
                        $scope.doctorImg = "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/consultDoctor/home_docwoman.png";
                    }
                }else{
                    $scope.doctorImg = doctorImg;
                }

                /*BabyCoinInit.save({},function(data){
                    $scope.babyMoney = data.babyCoinVo.cash;
                    $scope.babyCoinRecordVos = data.babyCoinRecordVos;
                });*/

                var recordLogs = function(val){
                    $.ajax({
                        url:"util/recordLogs",// 跳转到 action
                        async:true,
                        type:'get',
                        data:{logContent:encodeURI(val)},
                        cache:false,
                        dataType:'json',
                        success:function(data) {
                        },
                        error : function() {
                        }
                    });
                };

                recordLogs("clickDetails");

                //判断医生评论条数
                if(data.evaluationList.length==0){
                    $scope.commentNum = 1;
                    var comment = {
                        "headimgurl":"",
                        "nickname":"大白",
                        "redPacket":"18",
                        "createtime":new Date().getTime(),
                        "serviceAttitude":"5",
                        "content":"医生很耐心很专业哦，而且凌晨还有医生值班，给大家的帮助可真大。"
                    }
                    $scope.evaluationList.push(comment);
                    $scope.commentShow = false;
                    $scope.goDetails = false
                }else if(data.evaluationList.length==1){
                    $scope.commentNum = data.evaluationList.length;
                    $scope.commentShow = false;
                    $scope.goDetails = false
                }else{
                    $scope.commentNum = data.evaluationList.length;
                    $scope.commentShow = true;
                    if($scope.commentNum>=3){
                        $scope.goDetails = true;
                    }else{
                        $scope.goDetails = false;
                    }
                    for(var i = 1;i<data.evaluationList.length;i++){
                        $scope.commentList.push(data.evaluationList[i]);
                    }
                }
            });

        });

        //跳转评价详情页
        $scope.goCommentDetails = function () {
            /*if($scope.goDetails){
                $state.go("consultDoctorCommentDetails",{"id":$stateParams.id,"name":$scope.doctorName,"gender":$scope.gender});
            }*/
            $state.go("consultDoctorCommentDetails",{"id":$stateParams.id,"name":$scope.doctorName,"gender":$scope.gender});
        }


    }]);

