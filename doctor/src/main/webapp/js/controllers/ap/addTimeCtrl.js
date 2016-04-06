angular.module('controllers', ['ionic']).controller('addTimeCtrl', [
    '$scope','$state','$stateParams','$filter','$ionicScrollDelegate','GetDoctorTimeInfo','SaveDoctorAppointmentInfo','SaveDoctorArrange',
    function ($scope,$state,$stateParams,$filter,$ionicScrollDelegate,GetDoctorTimeInfo,SaveDoctorAppointmentInfo,SaveDoctorArrange) {
        $scope.tile0="出诊安排";
        $scope.lock=false;
        $scope.hospitalLock="false";//选择医院
        $scope.clinicLock="false";//选择科室
        $scope.startTimeLock="false";
        $scope.TimeLock="false";
        $scope.endTimeLock="false";
        $scope.startOrEnd="1";
        $scope.hospital=[];
        $scope.appointmentRepeat = "false";
        $scope.clinic=["门诊楼南侧2层4诊室","门诊楼北侧2层4诊室","门诊楼西侧2层5诊室"];
        $scope.appointmentTime=[
            "05:00","05:15","05:30","05:45","06:00","06:15","06:30","06:45",
            "07:00","07:15","07:30","07:45","08:00","08:15","08:30","08:45",
            "09:00", "09:15", "09:30","09:45","10:00","10:15","10:30","10:45",
            "11:00","11:15","11:30","11:45","12:00","12:15","12:30","12:45","13:00",
            "13:15","13:30","13:45","14:00","14:15","14:30","14:45","15:00","15:15",
            "15:30","15:45","16:00","16:15","16:30", "16:45","17:00","17:15","17:30",
            "17:45","18:00","18:15","18:30","18:45","19:00","19:15","19:30","19:45",
            "20:00","20:15","20:30","20:45","21:00"];
        $scope.startTime=["08","09","10","11","12","13","14","15","16","17"];
        $scope.startTimeM=["08:00","08:15","08:30","08:45","09:00","09:15","09:30","09:45","10:00","10:15","10:30"];
        $scope.endTime=["11:00","12:00","17:00","18:00"];
        $scope.begin_time="07:00";
        $scope.end_time="07:15";
        var doctorId="";//医生Id
        var addDate="";//添加号源日期   http://localhost:8080/xiaoerke-doctor/dc#/addTime
        var mo="";
        var repeatType="no";

        //初始化数据
        doctorId=$stateParams.doctorId;
        mo=$filter('limitTo')($stateParams.date, 7);
        addDate = moment($filter('limitTo')($stateParams.date, 10)).format('YYYY-MM-DD');
        $scope.newWeek=$filter('limitTo')($stateParams.date, -2);//星期
        $scope.newYear=$filter('limitTo')($stateParams.date, 4);//年
        $scope.newMon=$filter('limitTo')(mo, -2);//月
        $scope.newDa=$filter('limitTo')(addDate, -2);//日

        $ionicScrollDelegate.scrollTop();

        Array.prototype.remove = function(val) {
            var index = this.indexOf(val);
            if (index > -1) {
                this.splice(index, 1);
            }
        };

        /**
         * 选择医院
         */
        $scope.selectHospital=function(){
            if( $scope.hospitalLock=="false"){
                $scope.hospitalLock="true";
            }
            else{
                $scope.hospitalLock="false";
            }
        }

        $scope.selectRepeat = function(){
            if( $scope.appointmentRepeat == "false"){
                $scope.appointmentRepeat = "true";
                $scope.repeatItem = ["仅今天","重复每周","隔周重复"];
            }
            else{
                $scope.appointmentRepeat = "false";
            }

        }

        $scope.selectRepeatItem = function(item){
            $scope.appointmentRepeat = "false";
            $scope.repeatFlag = item;
            if(item=="仅今天"){
                repeatType="no";
            }
            if(item=="重复每周"){
                repeatType="0";
            }
            if(item=="隔周重复"){
                repeatType="1";
            }
        }

        $scope.selectHospitalName = function(hospitalId){
            $scope.appointmentResultTime = angular.copy($scope.appointmentTime);
            _.each($scope.dataValue,function(data) {
                _.each(data.availableInfo,function(value){
                    $scope.availableTimes = angular.copy(value.times);
                    _.each($scope.appointmentTime,function(var1){
                        _.each($scope.availableTimes,function(var2){
                            if($filter('limitTo')(var2.begin_time, 5)==var1)
                            {
                                $scope.appointmentResultTime.remove(var1);
                            }
                        })
                    });
                })
            })
            _.each($scope.dataValue,function(data) {
                if(data.hospitalId==hospitalId)
                {
                    $scope.hospitalName = angular.copy(data.hospitalName);
                    $scope.hospitalId = angular.copy(data.hospitalId);
                    $scope.location = angular.copy(data.availableInfo[0].location);
                    $scope.locationId = angular.copy(data.availableInfo[0].locationId);
                    $scope.info.price = angular.copy(data.availableInfo[0].price);
                    $scope.serviceType = angular.copy(data.availableInfo[0].serviceType);
                    $scope.availableTimes = angular.copy(data.availableInfo[0].times);
                    $scope.begin_time = angular.copy($scope.appointmentResultTime[0]);
                    $scope.end_time = moment(moment().format('YYYY-MM-DD') + " " +
                        $scope.begin_time).add(0.25, 'hours').format('HH:mm');
                    $scope.clinic = angular.copy(data.availableInfo);
                }
            })
            if( $scope.hospitalLock=="false"){
                $scope.hospitalLock="true";
            }
            else{
                $scope.hospitalLock="false";
            }
        }

        $scope.selectClinic=function(){
            if($scope.clinicLock=="false"){
                $scope.clinicLock="true"
            }
            else{
                $scope.clinicLock="false";
            }
        }

        $scope.selectClinicName=function(locationId){
            $scope.appointmentResultTime = angular.copy($scope.appointmentTime);
            _.each($scope.dataValue,function(data) {
                _.each(data.availableInfo,function(value){
                    $scope.availableTimes = angular.copy(value.times);
                    _.each($scope.appointmentTime,function(var1){
                        _.each($scope.availableTimes,function(var2){
                            if($filter('limitTo')(var2.begin_time, 5)==var1)
                            {
                                $scope.appointmentResultTime.remove(var1);
                            }
                        })
                    });
                })
            })
            _.each($scope.dataValue,function(data){
                $scope.availableInfo = angular.copy(data.availableInfo);
                _.each($scope.availableInfo,function(var1){
                    if(var1.locationId==locationId)
                    {
                        $scope.location = angular.copy(var1.location);
                        $scope.locationId = angular.copy(var1.locationId);
                        $scope.info.price = angular.copy(var1.price);
                        $scope.serviceType = angular.copy(var1.serviceType);
                        $scope.availableTimes = angular.copy(var1.times);
                    }
                })
            })
            $scope.begin_time = angular.copy($scope.appointmentResultTime[0]);
            $scope.end_time = moment(moment().format('YYYY-MM-DD')+" "+$scope.begin_time).add(0.25,'hours').format('HH:mm');
            if($scope.clinicLock=="false"){
                $scope.clinicLock="true"
            }
            else{
                $scope.clinicLock="false";
            }
        }

        $scope.selectHM=function(item){
            if( $scope.startOrEnd=="1"){
                $scope.startTimeLock="false";
                $scope.TimeLock="false";
                $scope.begin_time=item;
                $scope.end_time = moment(moment().format('YYYY-MM-DD')+" "+
                    $scope.begin_time).add(0.25,'hours').format('HH:mm');
            }
            if( $scope.startOrEnd=="2"){
                $scope.endTimeLock="false";
                $scope.TimeLock="false";
                $scope.end_time=item;
                if($scope.end_time<=$scope.begin_time){
                    Showbo.Msg.alert("结束时间不能小于开始时间！");
                }
            }
        }

        $scope.selectClose=function(){
            $scope.startTimeLock="false";
            $scope.endTimeLock="false";
            $scope.TimeLock="false";
            $scope.appointmentRepeat="false"
        }

        $scope.selectTime=function(item){
            if(item=='start'){
                $scope.startTimeLock="true";
                $scope.TimeLock="true";
                $scope.startOrEnd="1";
            }
            else{
                $scope.endTimeLock="true";
                $scope.TimeLock="true";
                $scope.startOrEnd="2";
            }
        }

        $scope.save = function(){
            if($scope.info.price<=0){
                Showbo.Msg.alert("请正确添加挂号费，谢谢！");
            }else{
                SaveDoctorArrange.save({
                    "doctorId":doctorId,
                    "hospitalId":$scope.hospitalId,
                    "locationId":$scope.locationId,
                    "serviceType":$scope.serviceType,
                    "price":$scope.info.price,
                    "date":addDate,
                    "fromTime":$scope.begin_time,
                    "toTime":$scope.end_time,
                    "repeatType":repeatType
                },function(data){
                    if(data.reason!=""){
                        //alert(data.reason);
                        Showbo.Msg.alert(data.reason);
                        repeatType="no";
                        $scope.hospital=[];
                        $state.go("doctorFirst",{"addDate":addDate,"fixDate":$stateParams.fixDate});
                    }else{
                        repeatType="no";
                        $scope.hospital=[];
                        $state.go("doctorFirst",{"addDate":addDate,"fixDate":$stateParams.fixDate});
                    }

                })
            }

        }

        $scope.$on('$ionicView.enter', function() {
            $scope.repeatFlag = "仅今天"
            $scope.info = [];
            GetDoctorTimeInfo.save({"doctorId":doctorId,"date":addDate},function(data){
                $scope.dataValue = data.dataValue;
                $scope.appointmentResultTime = angular.copy($scope.appointmentTime);
                _.each($scope.dataValue,function(data){
                    _.each(data.availableInfo,function(value){
                        $scope.availableTimes = angular.copy(value.times);

                        _.each($scope.appointmentTime,function(var1){
                            _.each($scope.availableTimes,function(var2){
                                if($filter('limitTo')(var2.begin_time, 5)==var1)
                                {
                                    $scope.appointmentResultTime.remove(var1);
                                }
                            })
                        });
                    })
                })
                _.each($scope.dataValue,function(data){
                    if(data.relationType=="1")
                    {
                        $scope.hospitalName = angular.copy(data.hospitalName);
                        $scope.hospitalId = angular.copy(data.hospitalId);
                        $scope.location = angular.copy(data.availableInfo[0].location);
                        $scope.locationId = angular.copy(data.availableInfo[0].locationId);
                        $scope.info.price = angular.copy(data.availableInfo[0].price);
                        $scope.serviceType = angular.copy(data.availableInfo[0].serviceType);
                        $scope.availableTimes = angular.copy(data.availableInfo[0].times);
                        $scope.begin_time = angular.copy($scope.appointmentResultTime[0]);
                        $scope.end_time = moment(moment().format('YYYY-MM-DD')+" "+$scope.begin_time).add(0.25,'hours').format('HH:mm');
                        $scope.clinic = angular.copy(data.availableInfo);
                    }
                    $scope.hospital.push({"hospitalName":angular.copy(data.hospitalName),
                        "hospitalId":angular.copy(data.hospitalId),"relationType":data.relationType});

                })
            })
        })
    }])
