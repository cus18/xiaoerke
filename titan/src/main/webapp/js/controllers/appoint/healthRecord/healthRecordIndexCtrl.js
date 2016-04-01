
angular.module('controllers', ['ionic']).controller('healthRecordIndexCtrl', [
    '$scope','$state','$stateParams','getBabyinfoList',
    '$location','getAppointmentInfo','getCustomerLogByOpenID','GetUserLoginStatus',
    function ($scope,$state,$stateParams,getBabyinfoList
    		,$location,getAppointmentInfo,getCustomerLogByOpenID,GetUserLoginStatus) {
        $scope.title0 = "健康档案";
        $scope.title = "健康档案"; 
        $scope.babyOrderInfo=true;
        $scope.orderInfoListLock=false; //预约记录List
        $scope.orderInfoListNullLock=true; //预约记录为空
        $scope.recordAppointLock = true;//预约记录开关
        $scope.recordConsultLock = false;//咨询记录开关
        $scope.fillInfoLock = false;//完善信息开关
        $scope.nothingBabyInfoLock=false;//没有宝宝信息
        $scope.orderList=[];
        $scope.nothingCustomerInfoLock=true;//没有咨询记录
        $scope.CustomerInfoLock=false;//有咨询记录
        $scope.nothingBabyInfoLock=true;
        $scope.logList = [];
        var babyListIndex=$stateParams.index;
        var index=0;

       /* 取消宝宝信息完善*/
        $scope.cancel = function(){
            $scope.fillInfoLock = false;
        };
        //立即完善
        $scope.updateBaby = function(){
        	var id=index;
        	window.location.href ="ap/appoint?value=251335#/healthRecordUpdateBaby/"+id+",0,0";
        };

        /*选择宝宝*/
        $scope.selectBaby = function(){
            $state.go("healthRecordSelectBaby");
        };
        /*选择预约记录和咨询记录*/
        $scope.selectRecord = function(select){
            if(select=="appoint"){
                $scope.recordAppointLock = true;
                $scope.recordConsultLock = false;

            }
            else{
                $scope.recordAppointLock = false;
                $scope.recordConsultLock = true;

            }
        };
       /* 预约记录详情*/
        $scope.appointDetail = function(id){
        	var babyName=$scope.babyId;
            $state.go("healthRecordAppoint",{index:id,babyName:babyName});
        };
        /*咨询记录详情*/
        $scope.consultDetail = function(){
            $state.go("healthRecordConsult");
        };
        /*立即咨询*/
        $scope.consultDoc = function (data) {
          //SendWechatMessageToUser.save({}, function (data) {
          //});
            WeixinJSBridge.call('closeWindow');
        };
        /*添加宝宝*/
        $scope.addBaby = function(){
            //$state.go("healthRecordAddBaby");
            window.location.href ="ap/appoint?value=251333#/healthRecordAddBaby/,";
        };
        /*修改宝宝*/
        $scope.updateBirthday = function(){
            //$state.go("healthRecordAddBaby");
            window.location.href ="ap/appoint?value=251335#/healthRecordUpdateBaby/"+babyListIndex+",2,0";
        };
       /* 填写病情诊断*/
        $scope.fillDisease = function(id){
            $state.go("healthRecordFillDisease",{id:id,index:babyListIndex});
        }

        $scope.appointmentFirst = function(){
            $location.href = "ap/firstPage/appoint";
        }

        $scope.$on('$ionicView.enter',function() {
        	var routePath = "/ap/appointBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                } else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else {
                    //获取用户下宝宝信息列表
                    getBabyinfoList.save({"openId":""},function (data){
                        if(data.babyInfoList==""){
                        	$scope.recordAppointLock = false;
                        	$scope.nothingBabyInfoLock=false;
                            $scope.fillInfoLock = false;//完善信息开关
                            $scope.nothingBabyInfoPageLock=true;//没有宝宝信息
                            $scope.BabyInfoLock=false;//没有咨询记录
                            if(!$scope.BabyInfoLock){
                            $scope.orderInfoListLock=true; //预约记录List
                            }
                            $scope.orderInfoListNullLock=false; //预约记录为空
                        }else{
                        	$scope.fillInfoLock = false;
                        	if(babyListIndex!=""){
                        		index=babyListIndex;
                        	}
                            var name=data.babyInfoList[index].name;
                            $scope.babyName=name;
                            $scope.babySex=data.babyInfoList[index].sex=='1'?'男':'女';
                            $scope.babyId=data.babyInfoList[index].id;
                            var babyName=name;
                            //计算宝宝年龄
                            var date=(new Date().getTime()-new Date(data.babyInfoList[index].birthday).getTime());
                            var days = ((date).toFixed(2)/86400000)+1;//加1，是让同一天的两个日期返回一天
                            var month=0;
                            var years=0;
                            if(days>31){
                                month=parseInt(days/31);
                                years=parseInt(month/12);
                                if(month>12){
                                    month=month-(years*12);
                                    days=days-(month*31)-(years*365);
                                }
                                if(days>31){
                                    days=days-(month*31);
                                }
                            }
                            var age="";
                            if(years>=1){
                                age+=years+"岁";
                            }
                            if(month>=1){
                                age+=month+"月";
                            }
                            if(days>=0){
                                if(days==0){
                                    days=1;
                                }
                                age+=parseInt(days)+"天";
                            }
                            if(age==""){
                                //return ;
                            }else{
                                $scope.babyBirthday=age;
                            }
                            //根据宝宝信息获取
                            getAppointmentInfo.get({'babyId':$scope.babyId},function (data){
                            	if(data.orderInfoList!=""){
                                    $scope.orderList=data.orderInfoList;
                                    $scope.orderInfoListLock=true; //预约记录List
                                    $scope.orderInfoListNullLock=false; //预约记录为空
                                }else{
                                	$scope.orderInfoListLock=false; //预约记录List
                                    $scope.orderInfoListNullLock=true; //预约记录为空
                                }
                            });
                            //根据宝宝信息获取
                            getCustomerLogByOpenID.save({},function (data){
                                if(data.logList!=""){
                                    $scope.CustomerInfoLock=true;//有咨询记录
                                    $scope.logList = data.logList;
                                }else{
                                     $scope.CustomerInfoLock=false;//有咨询记录
                                     $scope.orderInfoListLock=true;
                                }
                            });
                        }
                    });
                }});
        });
    }])
