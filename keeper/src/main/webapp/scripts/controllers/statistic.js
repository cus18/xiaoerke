angular.module('controllers', [])
    .controller('statistic',
    function ($scope, GetStatisticData,GetOverallStatisticData,
              GetYYStatisticData,GetQDStatisticData,GetXWTJStatisticData,
              GetDoctorStatisticData,GetZHStatisticData,GetCSStatisticData,
              userPageStatistic,getUserFullToDoList,getBaseDataStatistics,addBaseDataStatistics){

        $scope.ysAnalyse = "false";
        $scope.yyAnalyse = "false";
        $scope.zxAnalyse = "false";
        $scope.sAnalyse = "true";
        $scope.xwtjAnalyse = "false";
        $scope.xwxxAnalyse = "false";
        $scope.jsAnalyse = "false";
        $scope.qdAnalyse = "false";
        $scope.userAnalyse = "false";
        $scope.userBaseAnalyse="false";
        $scope.info = {"startDate":moment().format('YYYY-MM-DD'),"endDate":moment().format('YYYY-MM-DD')}
        $scope.phoneList = "13501057565,18510634355," +
            "13601025662,13601025165,13552425102," +
            "13910933071,18911617317,18612481147," +
            "18510333067,18618458486,13910530692,123";

        $scope.select = function(item){
            if(item=="zhAnalyse"){
                $scope.zhAnalyse = "true";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "false";
                $scope.zxAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.sAnalyse = "false";
                $scope.xwxxAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "false";
                $scope.userAnalyse = "false";
                $scope.userBaseAnalyse="false";
            }
            else if(item=="ysAnalyse"){
                $scope.zhAnalyse = "false";
                $scope.ysAnalyse = "true";
                $scope.yyAnalyse = "false";
                $scope.sAnalyse = "false";
                $scope.zxAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.xwxxAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "false";
                $scope.userBaseAnalyse="false";
            }
            else if(item=="sAnalyse"){
                $scope.sAnalyse = "true";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "false";
                $scope.zxAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.zhAnalyse = "false";
                $scope.xwxxAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "false";
                $scope.userBaseAnalyse="false";
                $scope.userAnalyse = "false";
            }
            else if(item=="yyAnalyse"){
                $scope.zhAnalyse = "false";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "true";
                $scope.zxAnalyse = "false";
                $scope.sAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.xwxxAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "false";
                $scope.userAnalyse = "false";
                $scope.userBaseAnalyse="false";
            }
            else if(item=="zxAnalyse"){
                $scope.zhAnalyse = "false";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "false";
                $scope.zxAnalyse = "true";
                $scope.sAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.xwxxAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "false";
                $scope.userAnalyse = "false";
                $scope.userBaseAnalyse="false";
            }
            else if(item=="xwtjAnalyse"){
                $scope.zhAnalyse = "false";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "false";
                $scope.zxAnalyse = "false";
                $scope.sAnalyse = "false";
                $scope.xwtjAnalyse = "true";
                $scope.xwxxAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "false";
                $scope.userAnalyse = "false";
                $scope.userBaseAnalyse="false";
            }
            else if(item=="xwxxAnalyse"){
                $scope.zhAnalyse = "false";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "false";
                $scope.zxAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.xwxxAnalyse = "true";
                $scope.sAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "false";
                $scope.userAnalyse = "false";
                $scope.userBaseAnalyse="false";
            }
            else if(item=="jsAnalyse"){
                $scope.zhAnalyse = "false";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "false";
                $scope.zxAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.xwxxAnalyse = "false";
                $scope.sAnalyse = "false";
                $scope.jsAnalyse = "true";
                $scope.qdAnalyse = "false";
                $scope.userAnalyse = "false";
                $scope.userBaseAnalyse="false";
            }
            else if(item=="qdAnalyse"){
                $scope.zhAnalyse = "false";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "false";
                $scope.zxAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.xwxxAnalyse = "false";
                $scope.sAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "true";
                $scope.userAnalyse = "false";
                $scope.userBaseAnalyse="false";
            }
            else if(item=="userAnalyse"){
                $scope.zhAnalyse = "false";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "false";
                $scope.zxAnalyse = "false";
                $scope.sAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.xwxxAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "false";
                $scope.userAnalyse = "true";
                $scope.userBaseAnalyse="false";
            }
            else if(item=="userBaseAnalyse"){
                $scope.zhAnalyse = "false";
                $scope.ysAnalyse = "false";
                $scope.yyAnalyse = "false";
                $scope.zxAnalyse = "false";
                $scope.sAnalyse = "false";
                $scope.xwtjAnalyse = "false";
                $scope.xwxxAnalyse = "false";
                $scope.jsAnalyse = "false";
                $scope.qdAnalyse = "false";
                $scope.userAnalyse = "false";
                $scope.userBaseAnalyse="true";
            }
        }

        $scope.checkData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
                GetStatisticData.save({
                    startDate: $scope.info.startDate,
                    endDate: $scope.info.endDate
                }, function (data) {
                    $scope.statistic = data;
                });
            }
            else
            {
                alert("无权限查询");
            }
        }

        $scope.analyData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
            GetOverallStatisticData.save({startDate:$scope.info.startDate,endDate:$scope.info.endDate},function(data){

                $scope.statisticOverall = data;
            });
            }
            else
            {
                alert("无权限查询");
            }
        }
        //医生关注信息
        $scope.doctorAnalyData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
                GetDoctorStatisticData.save({startDate:$scope.info.startDate,endDate:$scope.info.endDate},function(data){

                    $scope.doctorStatistic = data.doctorInfo;
                });
            }
            else
            {
                alert("无权限查询");
            }
        }

        $scope.checkzhData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
                GetZHStatisticData.save({startDate:$scope.info.startDate,endDate:$scope.info.endDate},function(data){
                    $scope.statisticzhData = data;
                });
            }
            else
            {
                alert("无权限查询");
            }
        }

        $scope.checkUserFullTodoList = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
            	getUserFullToDoList.save({begin_time:$scope.info.startDate,end_time:$scope.info.endDate,openid:$scope.info.openid},function(data){
            		$scope.userFullTodoList = data.user;
                });
            }
            else
            {
                alert("无权限查询");
            }
        }

        $scope.checkjsData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
                GetYYStatisticData.save({begin_time:$scope.info.startDate,end_time:$scope.info.endDate,"searchFlag":"jiesuan"},function(data){
                    $scope.statisticjsData = data;
                });
            }
            else
            {
                alert("无权限查询");
            }
        }

        $scope.checkqdData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
                GetQDStatisticData.save({startDate:$scope.info.startDate,endDate:$scope.info.endDate},function(data){
                    $scope.statistictQdData = data.qdData;
                });
            }
            else
            {
                alert("无权限查询");
            }
        }

        $scope.checkxwtjData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
                GetXWTJStatisticData.save({startDate:$scope.info.startDate,endDate:$scope.info.endDate},function(data){
                    $scope.statistictXwtjData = data.xwtjData;
                    console.log($scope.statistictXwtjData);
                });
            }
            else
            {
                alert("无权限查询");
            }
        }
        //用户咨询统计信息 zdl
        $scope.consultData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
                GetCSStatisticData.save({startDate:$scope.info.startDate,endDate:$scope.info.endDate},function(data){
                    $scope.statisticConsultData = data.consult;
                    console.log($scope.statisticConsultData);
                });
            }
            else
            {
                alert("无权限查询");
            }
        }
        //用户分析
        $scope.userconsultData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
            	userPageStatistic.save({startDate:$scope.info.startDate,endDate:$scope.info.endDate},function(data){
            		$scope.userData = data;
                });
            }
            else
            {
                alert("无权限查询");
            }
        }
        $scope.change = function(){
            //alert($scope.info.startDate);
        }
        
        $scope.checkyyData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
            	GetYYStatisticData.save({begin_time:$scope.info.startDate,end_time:$scope.info.endDate,"searchFlag":"yuyue"},function(data){
            		$scope.statisticyyData = data.yuyueData;
                });
            }
            else
            {
                alert("无权限查询");
            }
        }
        
        $scope.getBaseData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
            	getBaseDataStatistics.save({begin_time:$scope.info.startDate,end_time:$scope.info.endDate},function(data){
            		$scope.statisticBaseData = data.user;
                });
            }
            else
            {
                alert("无权限查询");
            }
        }
        $scope.addBaseData = function(){
            if($scope.phoneList.indexOf($scope.info.userPhone)!=-1) {
            	addBaseDataStatistics.save({begin_time:$scope.info.startDate,end_time:$scope.info.endDate},function(data){
                });
            }
            else
            {
                alert("无权限查询");
            }
        }

    })