angular.module('controllers', ['ionic']).controller('phoneConEvaluateCtrl',[
    '$scope','$state','$location','$stateParams','$filter','OrderPraiseOperation','FindDoctorCaseEvaluation','GetUserLoginStatus',
    function ($scope,$state,$location,$stateParams,$filter,OrderPraiseOperation,FindDoctorCaseEvaluation,GetUserLoginStatus) {

        $scope.title="评价医生";
        $scope.title0 = "宝大夫（400-623-7120）";
        $scope.date =  $stateParams.date;
        $scope.beginTime =  $stateParams.beginTime;
        $scope.endTime =  $stateParams.endTime;
        $scope.treatTime =  $stateParams.beginTime;
        $scope.submit = "提交"
        $scope.lock = "1"
        $scope.info = {};
        $scope.starNum = 3;
        $scope.major_star = 3;
        $scope.illnessLock = "false";
        $scope.otherLock = "false";
        $scope.serviceLock = "true";
        $scope.professionLock = "true";
        $scope.doctorCaseId = "";
        $scope.illnessList=["急慢性咳嗽","肺炎","小儿高热","支气管炎","营养不良","鼻窦炎","扁桃体炎","腹泻","胃炎","脑积水","其他"];
        $scope.itemIllness = "请选择疾病";
        $scope.starImg1 = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star_select.png";
        $scope.starImg2 = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star.png";
        $scope.imgs1=[$scope.starImg1,$scope.starImg1,$scope.starImg1,$scope.starImg2,$scope.starImg2];
        $scope.imgs2=[$scope.starImg1,$scope.starImg1,$scope.starImg1,$scope.starImg2,$scope.starImg2];

     /*   $scope.getImpress = function(impression){
            if(impression!=""&&$scope.ImpressionShow.length<3){
                $scope.ImpressionShow.push(impression)
            }
            $scope.info.desc = ""
        };*/
        /*点击 请选择疾病*/
        $scope.toggleIllness = function(){
            if( $scope.illnessLock == "false"){
                $scope.illnessLock = "true";
            }
            else{
                $scope.illnessLock = "false";
            }

        };
        /*取消  请选择疾病*/
        $scope.toggleIllnessCancel = function(){
            $scope.illnessLock = "false";
        };

        /* 选择 对应的 医生案列*/
        $scope.selectIllness = function(item){
            $scope.illnessLock = "false";
            $scope.itemIllness = item.doctor_case_name;
            $scope.doctorCaseId= item.id;
            if($scope.itemIllness=="其他"){
                $scope.otherLock = "true";
                $scope.doctorCaseId = "";
            }else{
                $scope.otherLock = "false";
            }
        }
        $scope.skip = function(){
            $('html,body').animate({scrollTop:$('#eva-con').offset().top}, 0);
        };
       /* 服务态度评价 星级显示*/
        $scope.selectStar1 = function(num){
            $scope.serviceLock = "true";
            $scope.starNum = num+1;
            for(var i=0;i<num;i++){
                $scope.imgs1[i]= $scope.starImg1;
            }
            for(var i=num+1;i<5;i++){
                $scope.imgs1[i]=$scope.starImg2;
            }
            if($scope.imgs1[num]==$scope.starImg1){
                $scope.imgs1[num]=$scope.starImg2;
                $scope.starNum=$scope.starNum-1;
            }
            else{
                $scope.imgs1[num]=$scope.starImg1;
            }
        }

       /* 专业水平评价 星级显示*/
        $scope.selectStar2 = function(num){
            $scope.major_star = num+1;
            $scope.professionLock = "true";
            for(var i=0;i<num;i++){
                $scope.imgs2[i]=$scope.starImg1;
            }
            for(var i=num+1;i<5;i++){
                $scope.imgs2[i]=$scope.starImg2;
            }
            if($scope.imgs2[num]==$scope.starImg1){
                $scope.imgs2[num]=$scope.starImg2;
                $scope.major_star=  $scope.major_star-1;
            }
            else{
                $scope.imgs2[num]=$scope.starImg1;
            }
        }

        /*提交评价*/
        $scope.evaluateUser = function(){
            var initialTime = angular.copy($scope.beginTime);
            var visit_endTime = $("#treatTime").val();
            if(visit_endTime!="")
            {
                visit_endTime = visit_endTime.replace("PM", "").replace("AM", "");
            }
            else
            {
                visit_endTime = initialTime;
            }
            $scope.pageLoading=true;
            OrderPraiseOperation.save({"patient_register_service_id":$stateParams.patient_register_service_id,
                "action":{"status":"3","star":$scope.starNum,"major_star":$scope.major_star,
                    "otherCase":$scope.info.symptom,"impression":$scope.info.context,"zan":$scope.lock,
                    "visit_endTime":visit_endTime,'doctorCaseId':$scope.doctorCaseId}},function(data){
                $scope.pageLoading=false;
                $state.go("sharedDetail",{patient_register_service_id:$stateParams.patient_register_service_id,status:'true',"returnpay":data.amount});
            })
        }


        $scope.$on('$ionicView.enter',function() {
            var currYear = (new Date()).getFullYear();
            $("#treatTime").mobiscroll().time();
            //初始化日期控件
            var opt = {
                preset: 'time',
                theme: 'android-ics light',
                display: 'bubble',
                mode: 'scroller',
                lang:'zh',
                timeFormat: 'HH:ii',//24小时显示
                timeWheels: 'HHii',
                setText: '确定',
                cancelText: '取消',
            };
            $("#treatTime").mobiscroll(opt);

            $scope.pageLoading = true;
            var routePath = "/appointBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                } else {
                    //初始化信息
                    FindDoctorCaseEvaluation.save({"patient_register_service_id":$stateParams.patient_register_service_id},function(data){
                       /* $scope.illnessList = data.doctorCaseList*/
                    });
                }
            });
        });
    }])
