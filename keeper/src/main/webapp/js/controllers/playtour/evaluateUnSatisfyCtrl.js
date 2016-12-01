angular.module('controllers', ['ionic']).controller('evaluateUnSatisfyCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        var starNum1=1;//对医生的评价，0无评价 1不满意 3满意 5非常满意
        var redPacket;//支付前述
        var noManYi=[];
        $scope.info={};
        $scope.customerId="";
        $scope.sessionId="";
        $scope.commitLock=true;


        //不满意的选项
        $scope.setNo=function(index){
            var flag = 0;
            if(noManYi.length==0){
                noManYi.push(index);
                $('.noSatisfy-item span').eq(index).addClass("select");
            }
            else{
                $scope.commitLock=false;
                $.each(noManYi, function (num,value) {
                    if(value==index){
                        $('.noSatisfy-item  span').eq(index).removeClass("select");
                        noManYi.splice(num,1);
                        flag = 1;
                        return;
                    }
                });
                if(flag!=1){
                    noManYi.push(index);
                    $('.noSatisfy-item  span').eq(index).addClass("select");
                }
            }
            if(noManYi.length!=0){
                $scope.commitLock=false;
            }
            else{
                $scope.commitLock=true;
            }
        };

        // 根据页面参数获取医生信息
        function getCustomerEvaluationInfo(){
            $.ajax({
                url:"interaction/user/findCustomerEvaluation",// 跳转到 action
                async:false,
                type:'POST',
                data:"{'id':'"+$scope.customerId+"'}",
                contentType: "application/json; charset=utf-8",
                dataType:'json',
                success:function(data) {
                    console.log("用户评价医生信息",data);
                    $scope.doctorName=data.doctorHeadImage.doctor_name;
                    $scope.doctorAvatar=data.doctorHeadImage.doctor_pic_url;
                    $scope.redPacket=data.starInfo.redPacket;
                    $scope.serverNum = data.serverNum;
                    $scope.startNum=parseFloat(data.starInfo.startNum)*100;

                },
                error : function() {
                }
            }, 'json');
        }
       /* $scope.commitEvaluateUnSatisfy = function () {
            var content=$("#content").val();
            $.ajax({
                url:"interaction/user/updateCustomerEvaluation",// 跳转到 action
                async:false,
                type:'POST',
                data:"{'id':'"+$scope.customerId+"','starNum1':'"+starNum1+"','content':'"+content+"','dissatisfied':'"+noManYi+"','redPacket':'"+redPacket+"','sessionId':'"+$scope.sessionId+"','consultStatus':'"+$stateParams.consultStatus+"'}",
                contentType: "application/json; charset=utf-8",
                dataType:'json',
                success:function(data) {
                    console.log("提交评价",data);
                    if(data=="1"){
                        recordLogs("ZXPJSXY_PJ");
                        window.location.href = "playtour#/playtourShare/"+3;
                    }
                    if(data=="2"){
                        window.location.href = "playtour#/evaluateSuccess";
                    }
                },
                error : function() {
                }
            }, 'json');

        };*/
        //提交评价
        $(".commit").click(function(){
            var content=$("#content").val();
            $scope.commitLock=true;
            $.ajax({
                url:"interaction/user/updateCustomerEvaluation",// 跳转到 action
                async:false,
                type:'POST',
                data:"{'id':'"+$scope.customerId+"','starNum1':'"+starNum1+"','content':'"+content+"','dissatisfied':'"+noManYi+"','redPacket':'"+redPacket+"','sessionId':'"+$scope.sessionId+"','consultStatus':'"+$stateParams.consultStatus+"'}",
                contentType: "application/json; charset=utf-8",
                dataType:'json',
                success:function(data) {
                    console.log("提交评价",data);
                    if(data=="1"){
                        recordLogs("ZXPJSXY_PJ");
                        window.location.href = "playtour#/playtourShare/"+3;
                    }
                    if(data=="2"){
                        window.location.href = "playtour#/evaluateSuccess";
                    }
                },
                error : function() {
                }
            }, 'json');
        });
        $scope.$on('$ionicView.enter', function(){
            $scope.customerId=$stateParams.customerId;
            $scope.sessionId=$stateParams.sessionId;
            getCustomerEvaluationInfo();


        });

        function recordLogs(val){
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


    }]);
