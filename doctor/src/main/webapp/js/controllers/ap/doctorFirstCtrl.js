angular.module('controllers', ['ionic','ngAnimate']).controller('doctorFirstCtrl', [
    '$scope','$filter','$state','$stateParams','$ionicPopup', '$timeout','$ionicScrollDelegate',
    'DoctorRegister','GetDoctorInfo','DeleteRegister','DatesRegister','CheckBind',
    'DeleteRegisterCount','$location','GetUserLoginStatus',
        function ($scope,$filter,$state,$stateParams,$ionicPopup,$timeout,$ionicScrollDelegate,
                  DoctorRegister,GetDoctorInfo,DeleteRegister,DatesRegister,CheckBind,DeleteRegisterCount,$location,GetUserLoginStatus) {

            $scope.tile0="出诊安排";
            $scope.lock=false
            $scope.babyInfoLock="false";
            $scope.sTime="true";
            $scope.addLock="false";
            $scope.delLock="false";
            $scope.allLock="false";
            $scope.cancelDelLock="false";
            $scope.cancelSuccessLock="false";
            $scope.delSureLock="false";
            $scope.prevWeek="true";
            $scope.nextWeek="false";
            $scope.week=['日','一','二','三','四','五','六'];
            $scope.date=[];//存放日期
            $scope.dateArray=[];//存放年月日
            $scope.registerDateArray=[];//存放号源年月日
            $scope.Success="false";
            $scope.Fail="false";
            $scope.noAppointmentLock="false";//当前时间没有患者预约
            var timeArray=[];//存放删除时间
            $scope.deleteArray=[];//存放删除总数据
            var sunDate="";//每周周日时间
            var weekindex=0;//保存上周转下周时，最后点击日期
            $scope.num=0;//判断加载几周的数据
            var result="";//判断是否删除成功
            var deleteDate="";//获取当前日期
            var currdate="";
            $scope.i = [];//判断哪个日期有号源的日期下角标
            $scope.deleteLock = "false";
            $scope.pastDate = "true";
            var currentDate = "";
            var currDate2="";//保存系统当前时间

            /**
             * 宝宝就诊信息关闭按钮
             */
            $scope.cancelBabyInfo=function(){
                $scope.babyInfoLock="false";
            }

            /**
             *
             * 判断号源是否被约
             */
            $scope.delogBaby = function(status,name,birthday,illness,phone){
                if($scope.delLock=="false"){
                    if(status==1){
                        $scope.babyInfoLock="true";
                        $scope.babyname = name;
                        $scope.illness = illness;
                        $scope.phone = phone;
                        if(birthday==undefined||birthday==""){
                            $scope.noPhone = "false";
                        }else{
                            $scope.noPhone = "true";
                            $scope.birthday = birthday;
                        }
                    }else{
                      /*  Showbo.Msg.alert("当前时间没有患者预约！");*/
                        $scope.noAppointmentLock="true";
                        $timeout(function() {
                            $scope.noAppointmentLock="false";
                        }, 2000);

                    }
                }else{
                    $scope.babyInfoLock="false";
                }

            }

            $scope.selectWeek = function(item){
                if(item=="prev") {
                    $scope.prevWeek = "true";
                    $scope.nextWeek = "false";
                    var changeDate = moment($scope.dateArray[0]).subtract( 7, 'days').format('YYYY-MM-DD');
                    if(moment(changeDate).diff($scope.initialFixDate, "days")<=0 || moment(changeDate).diff($scope.initialFixDate, "days")>=21)
                    {
                        $scope.prevWeekDisable = "true"
                        $scope.nextWeekDisable = "false"
                    }
                    else
                    {
                        $scope.prevWeekDisable = "false"
                        $scope.nextWeekDisable = "false"
                    }
                    $scope.changeWeek(changeDate);
                }
                else if(item=="next"){
                    $scope.prevWeek = "false";
                    $scope.nextWeek = "true";
                    var changeDate=moment(sunDate).add(7,'days').format('YYYY-MM-DD');
                    if(moment(changeDate).diff($scope.initialFixDate, "days")<=0 || moment(changeDate).diff($scope.initialFixDate, "days")>=21)
                    {
                        $scope.nextWeekDisable = "true"
                        $scope.prevWeekDisable = "false"
                    }
                    else
                    {
                        $scope.nextWeekDisable = "false"
                        $scope.prevWeekDisable = "false"
                    }
                    $scope.changeWeek(changeDate);
                }
            }

            /**
             * 上、下周获取号源日期及号源方法
             */
            $scope.changeWeek = function(changeDate){
                $scope.initial(changeDate);
            }

            /**
             * 点击本周不同日期更新号源
             * @param index
             */
            $scope.selectTime=function(index){
                $scope.delLock="false";
                $scope.deleteArray=[];//清空删除数据
                timeArray=[];//清空删除号源
                weekindex=index;
                $scope.selectItem = $scope.date[index];
                //获取医生号源
                $scope.pageLoading = true;
                DoctorRegister.save({"doctorId":$scope.doctorId ,"date": $scope.dateArray[index]}, function (data) {
                    $scope.pageLoading = false;
                    $scope.char(data.date);
                    deleteDate=$filter('limitTo')(data.date, 10);
                    $scope.addDate=angular.copy(data.date);
                    if(data.appointmentList.length==0){
                        $scope.noRegister= 'true';
                    }else{
                        $scope.noRegister= 'false';
                        $scope.hospitalList=data.appointmentList;
                    }
                    if(Date.parse(deleteDate)>Date.parse(currDate2)||moment(deleteDate).format('YYYY-MM-DD')==currDate2){
                        $scope.pastDate = "true";
                    }else{
                        $scope.pastDate = "false";
                    }
                });
            }

            /**
             * 添加号源
             */
            $scope.addTime=function(){
                $scope.addLock="true";
                $state.go("addTime",{"doctorId":$scope.doctorId,"date":$scope.addDate,"fixDate":$scope.fixDate});
            }

            /**
             * 显示删除种类
             */
            $scope.delTime=function(){
                $scope.delLock="true";
            }

            /**
             * 删除全部号源
             */
            $scope.selectAll=function(){
                if($scope.allLock=="false"){
                    $scope.allLock="true";
                    $scope.deleteArray=[];
                    timeArray=[];
                    for(var i=0;i<$scope.hospitalList.length;i++){
                        for(var j=0;j<$scope.hospitalList[i].available_time.length;j++){
                            timeArray.push($scope.hospitalList[i].available_time[j].begin_time);
                        }
                        $scope.deleteArray.push({"doctorId":$scope.doctorId,
                            "hospitalId":$scope.hospitalList[i].hospitalId,
                            "locationId":$scope.hospitalList[i].locationId,
                            "date":moment(deleteDate).format('YYYY-MM-DD'),
                            "repeated":false,
                            "times":timeArray
                        });
                        if($scope.deleteArray.length!=0){
                            $scope.deleteLock='true';
                        }
                        timeArray=[];
                    }
                }
                else{
                    $scope.allLock="false";
                    $scope.deleteLock='false';
                    $scope.deleteArray=[];
                    timeArray=[];
                }
            }

            /**
             * 删除号源
             */
            $scope.delete = function(){
                $scope.delLock = "true";
                $scope.cancelDelLock = "true";
            }

            /**
             * 取消删除号源
             */
            $scope.cancel=function(){
                $scope.delLock="false";
                $scope.deleteLock='false';
                $scope.deleteArray=[];
                timeArray=[];
            }

            /**
             * 取消返回
             */
            $scope.cancelDel=function(){
                $scope.cancelDelLock="false";
                $scope.delSureLock="false";
            }

            /**
             * 确认删除
             */
            $scope.cancelSuccess=function(data){
                $scope.cancelDelLock="false";//删除重复还是今天隐藏
                //仅删除今天
                if(data==0){
                    $scope.deleteAll();
                }
                //删除所有重复
                 if(data==1){
                    for(var i=0;i<$scope.deleteArray.length;i++){
                        $scope.deleteArray[i].repeated=true;
                    }
                     $scope.deleteAll();
                }
            }

            /**
             *删除选择好的号源
             */
            $scope.deleteAll = function () {
                $scope.pageLoading = true;
                DeleteRegisterCount.save($scope.deleteArray,function(data){
                    $scope.pageLoading = false;
                    $scope.count=data.count;
                    if($scope.count==0){
                        $scope.delSureLock="false";
                        $scope.pageLoading = true;
                        DeleteRegister.save($scope.deleteArray,function(data){
                            $scope.pageLoading = false;
                            result=data.status;
                            $scope.deleteResult();
                        });
                    }else{
                        $scope.delSureLock="true";
                    }
                });
            }

            /**
             * 确认最终删除
             */
            $scope.deleteSuccess = function(){
               $scope.delSureLock="false";
               $scope.pageLoading = true;
               DeleteRegister.save($scope.deleteArray,function(data){
                   $scope.pageLoading = false;
                   result=data.status;
                   $scope.deleteResult();
               });
           }

            /**
             * 删除结果
             */
            $scope.deleteResult = function () {
                if(result=="OK"){
                    $scope.cancelSuccessLock="true";
                    $scope.Success="true";//删除成功提示
                    $timeout(function() {
                        $scope.Success="false";
                        $scope.cancelSuccessLock="false";
                        $scope.delLock="false";//删除选择框隐藏
                        $scope.delSureLock="false";
                        $scope.deleteLock='false';
                        $scope.allLock="false";
                        $ionicScrollDelegate.scrollTop();
                    }, 2000);

                    $scope.pageLoading = true;
                    DatesRegister.get({"doctorId": $scope.doctorId,"days": 7,"date": sunDate}, function (data) {
                        $scope.pageLoading = false;
                        $scope.registerDateArray = angular.copy(data.dates);
                        $scope.i=[];

                        for(var i=0;i<$scope.dateArray.length;i++){
                            var lock=true;
                            for(var j=0;j<$scope.registerDateArray.length;j++){
                                if($scope.dateArray[i]==$scope.registerDateArray[j].date){
                                    $scope.i.push(i);
                                    lock=false;
                                }
                            }
                            if(lock){
                                $scope.i.push("");
                            }

                        }
                    });
                    //获取医生号源
                    $scope.pageLoading = true;
                    DoctorRegister.save({"doctorId":$scope.doctorId ,"date": $scope.deleteArray[0].date}, function (data) {
                        $scope.pageLoading = false;
                        $scope.char(data.date);
                        deleteDate=$filter('limitTo')(angular.copy(data.date), 10);
                        if(data.appointmentList.length==0){
                            $scope.noRegister= 'true';
                        }else{
                            $scope.noRegister= 'false';
                            $scope.hospitalList=data.appointmentList;
                        }
                        $scope.deleteArray=[];
                        timeArray=[];
                    });
                }else{
                    $scope.cancelSuccessLock="true";
                    $scope.Fail="true";//删除成功提示
                    $timeout(function() {
                        $scope.Fail="false";
                        $scope.cancelSuccessLock="false";
                        $scope.delLock="false";//删除选择框隐藏
                        $scope.delSureLock="false";
                        $scope.deleteLock='false';
                        $scope.allLock="false";
                        $ionicScrollDelegate.scrollTop();
                    }, 2000);

                }
            }

            /**
             * 删除选中的号源
             *$parent.$index
             */
            $scope.deleteRegister = function (locationid, index,delLock) {
                var parentindex=0;
                for(var i=0;i<$scope.hospitalList.length;i++){
                    if($scope.hospitalList[i].locationId==locationid){
                        parentindex=i;
                    }
                }
                var lock=true;//判断删除号源是否存在,true增加号源
                var lock2=true;//判断删除locationid是否存在，true增加删除数组
                //判断删除数组是否为空，如果为空，就进行付初始值
                if($scope.deleteArray.length==0){
                    timeArray.push($scope.hospitalList[parentindex].available_time[index].begin_time);
                    $scope.deleteArray.push({"doctorId":$scope.doctorId,
                        "hospitalId":$scope.hospitalList[parentindex].hospitalId,
                        "locationId":$scope.hospitalList[parentindex].locationId,
                        "date":moment(deleteDate).format('YYYY-MM-DD'),
                        "repeated":false,
                        "times":timeArray});
                }else
                {
                    for(var i=0;i<$scope.deleteArray.length;i++){
                        //判断点击删除的locationId是否与deleteArray中存放的locationId相等
                        if($scope.deleteArray[i].locationId==$scope.hospitalList[parentindex].locationId){
                            //判断删除的号源是否已在删除时间数组内，如果存在则删掉，没有则添加
                            for(var j=0; j<$scope.deleteArray[i].times.length;j++)
                            {
                                if($scope.deleteArray[i].times[j]==$scope.hospitalList[parentindex].available_time[index].begin_time){

                                    $scope.deleteArray[i].times.splice(j,1);
                                    lock=false;
                                }
                            }
                            //判断删除号源是否为空，为空将删除信息从删除数组中删去
                            if($scope.deleteArray[i].times.length==0){
                                $scope.deleteArray.splice(i,1);
                            }

                            if(lock)
                            {
                                $scope.deleteArray[i].times.push($scope.hospitalList[parentindex].available_time[index].begin_time);
                            }
                            lock2=false;
                        }
                    }
                    if(lock2)
                    {
                        timeArray=[];
                        timeArray.push($scope.hospitalList[parentindex].available_time[index].begin_time);
                        $scope.deleteArray.push({  "doctorId":$scope.doctorId,
                            "hospitalId":$scope.hospitalList[parentindex].hospitalId,
                            "locationId":$scope.hospitalList[parentindex].locationId,
                            "date":moment(deleteDate).format('YYYY-MM-DD'),
                            "repeated":false,
                            "times":timeArray});
                    }
                }
                if($scope.deleteArray.length!=0){
                    $scope.deleteLock='true';
                }else{
                    $scope.deleteLock='false';
                }
            }

            /**
             * 对时间进行拆分
             */
            $scope.char = function(date){
                var mo=$filter('limitTo')(date, 7);
                var da=$filter('limitTo')(date, 10);
                $scope.newWeek=$filter('limitTo')(date, -2);
                $scope.newYear=$filter('limitTo')(date, 4);
                $scope.newMon=$filter('limitTo')(mo, -2);
                $scope.newDa=$filter('limitTo')(da, -2);
            }

            $scope.initial = function(currentTime){
                $scope.i = [];
                var longDate = currentTime;
                 currentDate = $filter('date')(currentTime, 'yyyy-MM-dd');
                $scope.newDate(longDate);//调用显示日期方法
                //获取有号源的日期
                DatesRegister.get({"doctorId":$scope.doctorId ,"days":7,"date":sunDate},function(data){
                    $scope.pageLoading = false;
                    $scope.registerDateArray=angular.copy(data.dates);
                    for(var i=0;i<$scope.dateArray.length;i++)
                    {
                        var lock = "false"
                        for (var j = 0; j < $scope.registerDateArray.length; j++)
                        {
                            if ($scope.dateArray[i] == $scope.registerDateArray[j].date)
                            {
                                $scope.i.push(i);
                                lock = "true"
                            }
                        }
                        if(lock=="false")
                        {
                            $scope.i.push("");
                        }
                    }
                });
                //获取医生号源
                DoctorRegister.save({"doctorId":$scope.doctorId ,"date":currentDate}, function (data) {
                    $scope.char(data.date);
                    deleteDate=$filter('limitTo')(data.date, 10);
                    $scope.addDate=angular.copy(data.date);
                    if(data.appointmentList.length==0){
                        $scope.noRegister= 'true';
                    }else{
                        $scope.noRegister= 'false';
                        $scope.hospitalList=data.appointmentList;
                    }
                    if(Date.parse(deleteDate)>Date.parse(currDate2)||moment(deleteDate).format('YYYY-MM-DD')==currDate2){
                        $scope.pastDate = "true";
                    }else{
                        $scope.pastDate = "false";
                    }
                });
            }

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/ap/doctorBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    }else{
                        $scope.prevWeekDisable = "true"
                        if($stateParams.fixDate=="")
                        {
                            $scope.fixDate = moment().format('YYYY-MM-DD');
                            var weekNum = moment().format('E');
                            $scope.initialFixDate = moment().add('days',0-weekNum).format('YYYY-MM-DD');
                        }
                        else
                        {
                            $scope.fixDate = $stateParams.fixDate
                            var weekNum = moment($stateParams.fixDate).format('E');
                            $scope.initialFixDate = moment($stateParams.fixDate).add('days',0-weekNum).format('YYYY-MM-DD');
                        }

                        if($stateParams.addDate!="") {
                            if (moment($stateParams.addDate).diff($scope.initialFixDate, "days") <= 0) {
                                $scope.prevWeekDisable = "true"
                            }
                            else {
                                $scope.prevWeekDisable = "false"
                            }
                            if (moment($stateParams.addDate).diff($scope.initialFixDate, "days") >= 21) {
                                $scope.nextWeekDisable = "true"
                            }
                            else {
                                $scope.nextWeekDisable = "false"
                            }
                        }

                        /**
                         * 判断医生是否绑定
                         */
                        $scope.pageLoading = true;
                        GetDoctorInfo.save({},function(data){
                            $scope.pageLoading = false;
                            if(data.doctorId!=undefined)
                            {
                                $scope.doctorId = data.doctorId;
                                currDate2 = $filter('date')(data.currentTime, 'yyyy-MM-dd');
                                $scope.pageLoading = true;
                                if($stateParams.addDate!="") {
                                    $scope.initial((new Date($stateParams.addDate)).getTime());
                                }
                                else {
                                    $scope.initial(data.currentTime);
                                }
                            }
                            else if(data.doctorId==undefined)
                            {
                                $state.go("bindDoctorPhone",{"redirect":"doctorFirst"});
                            }
                        });
                    }
                })
            })

            /**
             * 日期显示
             */
            $scope.newDate = function (date) {
                var week="";
                var weeknum=moment(date).format('E');//根据服务器时间获取当前时间周期数
                currdate=moment(date).format('YYYY-MM-DD');
                if(weeknum==1){week="一"};
                if(weeknum==2){week="二"};
                if(weeknum==3){week="三"};
                if(weeknum==4){week="四"};
                if(weeknum==5){week="五"};
                if(weeknum==6){week="六"};
                if(weeknum==7){week="日"};

                for(var i=0;i<$scope.week.length;i++){
                    if($scope.week[i]==week){
                        $scope.date[i]=currdate;//找到当天周期数对应的日期位置
                        if(i==0){
                            //显示本周日期
                            $scope.date[0]=moment(currdate).format("DD");
                            $scope.date[1]=moment(currdate).add(1, 'days').format('DD');
                            $scope.date[2]=moment(currdate).add(2, 'days').format('DD');
                            $scope.date[3]=moment(currdate).add(3, 'days').format('DD');
                            $scope.date[4]=moment(currdate).add(4, 'days').format('DD');
                            $scope.date[5]=moment(currdate).add(5, 'days').format('DD');
                            $scope.date[6]=moment(currdate).add(6, 'days').format('DD');
                            //获得本周日期-年月日格式
                            $scope.dateArray[0]=moment(currdate).format('YYYY-MM-DD');
                            $scope.dateArray[1]=moment(currdate).add(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[2]=moment(currdate).add(2, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[3]=moment(currdate).add(3, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[4]=moment(currdate).add(4, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[5]=moment(currdate).add(5, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[6]=moment(currdate).add(6, 'days').format('YYYY-MM-DD');

                            sunDate=moment(currdate).format('YYYY-MM-DD');//获取本周周日的日期
                            $scope.selectItem=$scope.date[0];//显示初始日期
                        }
                        if(i==1){
                            $scope.date[0]=moment(currdate).subtract(1, 'days').format('DD');
                            $scope.date[1]=moment(currdate).format("DD");
                            $scope.date[2]=moment(currdate).add(1, 'days').format('DD');
                            $scope.date[3]=moment(currdate).add(2, 'days').format('DD');
                            $scope.date[4]=moment(currdate).add(3, 'days').format('DD');
                            $scope.date[5]=moment(currdate).add(4, 'days').format('DD');
                            $scope.date[6]=moment(currdate).add(5, 'days').format('DD');

                            $scope.dateArray[0]=moment(currdate).subtract(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[1]=moment(currdate).format('YYYY-MM-DD');
                            $scope.dateArray[2]=moment(currdate).add(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[3]=moment(currdate).add(2, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[4]=moment(currdate).add(3, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[5]=moment(currdate).add(4, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[6]=moment(currdate).add(5, 'days').format('YYYY-MM-DD');

                            sunDate=moment(currdate).subtract(1, 'days').format('YYYY-MM-DD');
                            $scope.selectItem=$scope.date[1];
                        }
                        if(i==2){
                            $scope.date[0]=moment(currdate).subtract(2, 'days').format('DD');
                            $scope.date[1]=moment(currdate).subtract(1, 'days').format('DD');
                            $scope.date[2]=moment(currdate).format("DD");
                            $scope.date[3]=moment(currdate).add(1, 'days').format('DD');
                            $scope.date[4]=moment(currdate).add(2, 'days').format('DD');
                            $scope.date[5]=moment(currdate).add(3, 'days').format('DD');
                            $scope.date[6]=moment(currdate).add(4, 'days').format('DD');

                            $scope.dateArray[0]=moment(currdate).subtract(2, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[1]=moment(currdate).subtract(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[2]=moment(currdate).format('YYYY-MM-DD');
                            $scope.dateArray[3]=moment(currdate).add(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[4]=moment(currdate).add(2, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[5]=moment(currdate).add(3, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[6]=moment(currdate).add(4, 'days').format('YYYY-MM-DD');

                            sunDate=moment(currdate).subtract(2, 'days').format('YYYY-MM-DD');
                            $scope.selectItem=$scope.date[2];
                        }
                        if(i==3){
                            $scope.date[0]=moment(currdate).subtract(3, 'days').format('DD');
                            $scope.date[1]=moment(currdate).subtract(2, 'days').format('DD');
                            $scope.date[2]=moment(currdate).subtract(1, 'days').format('DD');
                            $scope.date[3]=moment(currdate).format("DD");
                            $scope.date[4]=moment(currdate).add(1, 'days').format('DD');
                            $scope.date[5]=moment(currdate).add(2, 'days').format('DD');
                            $scope.date[6]=moment(currdate).add(3, 'days').format('DD');

                            $scope.dateArray[0]=moment(currdate).subtract(3, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[1]=moment(currdate).subtract(2, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[2]=moment(currdate).subtract(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[3]=moment(currdate).format('YYYY-MM-DD');
                            $scope.dateArray[4]=moment(currdate).add(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[5]=moment(currdate).add(2, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[6]=moment(currdate).add(3, 'days').format('YYYY-MM-DD');

                            sunDate=moment(currdate).subtract(3, 'days').format('YYYY-MM-DD');
                            $scope.selectItem=$scope.date[3];
                        }
                        if(i==4){
                            $scope.date[0]=moment(currdate).subtract(4, 'days').format('DD');
                            $scope.date[1]=moment(currdate).subtract(3, 'days').format('DD');
                            $scope.date[2]=moment(currdate).subtract(2, 'days').format('DD');
                            $scope.date[3]=moment(currdate).subtract(1, 'days').format('DD');
                            $scope.date[4]=moment(currdate).format("DD");
                            $scope.date[5]=moment(currdate).add(1, 'days').format('DD');
                            $scope.date[6]=moment(currdate).add(2, 'days').format('DD');

                            $scope.dateArray[0]=moment(currdate).subtract(4, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[1]=moment(currdate).subtract(3, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[2]=moment(currdate).subtract(2, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[3]=moment(currdate).subtract(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[4]=moment(currdate).format('YYYY-MM-DD');
                            $scope.dateArray[5]=moment(currdate).add(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[6]=moment(currdate).add(2, 'days').format('YYYY-MM-DD');

                            sunDate=moment(currdate).subtract(4, 'days').format('YYYY-MM-DD');
                            $scope.selectItem=$scope.date[4];
                        }
                        if(i==5){
                            $scope.date[0]=moment(currdate).subtract(5, 'days').format('DD');
                            $scope.date[1]=moment(currdate).subtract(4, 'days').format('DD');
                            $scope.date[2]=moment(currdate).subtract(3, 'days').format('DD');
                            $scope.date[3]=moment(currdate).subtract(2, 'days').format('DD');
                            $scope.date[4]=moment(currdate).subtract(1, 'days').format('DD');
                            $scope.date[5]=moment(currdate).format("DD");
                            $scope.date[6]=moment(currdate).add(1, 'days').format('DD');

                            $scope.dateArray[0]=moment(currdate).subtract(5, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[1]=moment(currdate).subtract(4, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[2]=moment(currdate).subtract(3, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[3]=moment(currdate).subtract(2, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[4]=moment(currdate).subtract(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[5]=moment(currdate).format('YYYY-MM-DD');
                            $scope.dateArray[6]=moment(currdate).add(1, 'days').format('YYYY-MM-DD');

                            sunDate=moment(currdate).subtract(5, 'days').format('YYYY-MM-DD');
                            $scope.selectItem=$scope.date[5];
                        }
                        if(i==6){
                            $scope.date[0]=moment(currdate).subtract(6, 'days').format('DD');
                            $scope.date[1]=moment(currdate).subtract(5, 'days').format('DD');
                            $scope.date[2]=moment(currdate).subtract(4, 'days').format('DD');
                            $scope.date[3]=moment(currdate).subtract(3, 'days').format('DD');
                            $scope.date[4]=moment(currdate).subtract(2, 'days').format('DD');
                            $scope.date[5]=moment(currdate).subtract(1, 'days').format('DD');
                            $scope.date[6]=moment(currdate).format("DD");

                            $scope.dateArray[0]=moment(currdate).subtract(6, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[1]=moment(currdate).subtract(5, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[2]=moment(currdate).subtract(4, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[3]=moment(currdate).subtract(3, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[4]=moment(currdate).subtract(2, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[5]=moment(currdate).subtract(1, 'days').format('YYYY-MM-DD');
                            $scope.dateArray[6]=moment(currdate).format('YYYY-MM-DD');

                            sunDate=moment(currdate).subtract(6, 'days').format('YYYY-MM-DD');
                            $scope.selectItem=$scope.date[6];
                        }
                    }
                }
            }
    }])
