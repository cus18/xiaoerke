angular.module('controllers', ['ionic']).controller('DoctorAppointmentCtrl', ['$rootScope',
    '$scope','$state','$filter','$stateParams','DoctorDetail',
    'DoctorAppointmentInfo','$ionicPopup', '$timeout','MsgAppointment',
    'AttentionDoctor','CheckAttentionDoctor','GetUserEvaluate',
    'GetDoctorVisitInfoByLocation','GetDoctorVisitInfo','FindDoctorLocationByDoctorId',
    'MsgAppointmentStatus','CheckIfAppScanDoctor','$location','resolveUserLoginStatus',
    function ($rootScope,$scope,$state,$filter,$stateParams,DoctorDetail,
              DoctorAppointmentInfo,$ionicPopup, $timeout,MsgAppointment,
              AttentionDoctor,CheckAttentionDoctor,GetUserEvaluate,
              GetDoctorVisitInfoByLocation,GetDoctorVisitInfo,FindDoctorLocationByDoctorId,
              MsgAppointmentStatus,CheckIfAppScanDoctor,$location,resolveUserLoginStatus) {
        $scope.title="医生详情";
        $scope.title0 = "宝大夫（400-623-7120）";
        $scope.orderOpen = "预约开放提醒"
        $scope.info = {}
        $scope.isAttention =false;
        $scope.userPhone = ''
        $scope.doc = true;
        $scope.user =false;
        $scope.wangQiang =false;
        $scope.choiceDate = ''
        $scope.timeListStatus=[];
        $scope.availableTime = [];
        $scope.appointmentInfo = [];
        $scope.timeList = [];
        $scope.weekList = [];
        $scope.timeListWithMonth = [];
        $scope.specialPrompt = "";
        $scope.du = "1";
        $scope.order = {};
        $scope.bookRemindLock =false;
        var routePath = encodeURI(encodeURI("/appointBBBBBB" + $location.path()));

        //默认的医生出诊地点位置
        $scope.doctorId = $stateParams.doctorId;
        $scope.available_time = $stateParams.available_time;
        $scope.hospitalName = $stateParams.hospitalName;
        $scope.location = $stateParams.location;
        $scope.position = $stateParams.position;
        $scope.mark = $stateParams.mark;
        $scope.location_id = $stateParams.location_id;

        //每个案例对应的背景色
        $scope.caseColor=["#f08664","#f39618","#f5c61e","#efea3a","#ba81b6","#eb96be","#f6c3d9",
            "#94ceeb","#5abceb","#8cd2f3","#c1e5f7","#3baf36","#7ebe30","#bdd535","#f5d120","#f7f131",
            "#3891cf","#69b3e4","#8b70b0","#ba81b6","#e179a8","#f0a7b3","#c8e6df","#c9e7f9","#f9d7e6",
            "#bdddf4","#e0cae2","#e0d5e9","#d5ece4","#fbe0e5","#fae4ee",];

        $scope.caseList=[
            {
                caseName:"湿疹",
                caseNum:"141"
            },
            {
                caseName:"皮肤过敏皮肤过敏",
                caseNum:"37"
            },
            {
                caseName:"特应性皮炎",
                caseNum:"16"
            },
            {
                caseName:"疖",
                caseNum:"11"
            },
            {
                caseName:"儿童荨麻疹",
                caseNum:"9"
            },
            {
                caseName:"过敏",
                caseNum:"5"
            },
            {
                caseName:"水痘",
                caseNum:"5"
            },{
                caseName:"婴儿血管瘤",
                caseNum:"5"
            },
            {
                caseName:"其他",
                caseNum:"81"
            }
        ];

        $scope.cancelRemind=function(){
            $scope.bookRemindLock =false;
        }

        //根据输入的available_time，获取该日期的预约信息
        $scope.getAppointmentInfoByAvailableTime = function(date,location_id){
            //默认日期的预约信息
            $scope.pageLoading = true;
            DoctorAppointmentInfo.save({"doctorId":$scope.doctorId,
                    "date":date,location_id:location_id},
                function(data){
                    $scope.pageLoading = false;
                    $scope.appointmentInfo.choiceDate = date;
                    var boolean = moment(date).isAfter(moment().format('YYYY-MM-DD HH:mm'));
                    if(!boolean){
                        for(var i=0;i<data.appointmentList.length;i++){
                            for(var j=0;j<data.appointmentList[i].available_time.length;j++){
                                var time = data.appointmentList[i].available_time[j].begin_time;
                                var da =  moment( moment(moment().add($stateParams.available_time,'days')).format('YYYY/MM/DD')+" "+time);
                                var boolean = moment(moment().format('YYYY-MM-DD HH:mm')).isAfter(da);
                                if(boolean){
                                    data.appointmentList[i].available_time[j].status = '1'
                                }
                            }
                        }
                    }

                    $scope.appointmentDate = data.date;
                    $scope.choiceDate = moment(moment().add($scope.available_time,'days')).format('YYYY/MM/DD');
                    $scope.appointmentList = angular.copy(data.appointmentList);
                    _.each($scope.appointmentList,function(val){
                        val.available_time =  _.chunk(val.available_time,3);
                    })
                });
        }

        //根据输入的地点信息，获医生一周内出诊的出诊日期
        $scope.GetDoctorVisitInfoByLocationInWeek = function(hospitalName,location_id){
            $scope.pageLoading = true;
            GetDoctorVisitInfoByLocation.save({doctorId:$scope.doctorId,
                hospitalName:hospitalName,
                location_id:location_id},function(data){

                $scope.specialPrompt = data.kindlyReminder;
                $scope.pageLoading = false;
                var dateList = new Array();
                _.each(data.dateList,function(val){
                    dateList.push(moment(val.date).format('YYYY/MM/DD'));
                })
                $scope.minAvailableDate = dateList[0];
                var dateValue = dateList.join("-");
                for(var i=0;i<7;i++) {
                    $scope.timeListStatus[i] = dateValue.indexOf(moment().add(i, 'days').format('YYYY/MM/DD'));
                }
            });
        }

        //实现点击最近可预约时间的按钮后，跳转到1周内，最近的可约时间处
        $scope.doRefresh = function(){
            $scope.choiceItem = angular.copy($scope.initChoiceItem);
            $scope.available_time = angular.copy($scope.initAvailableTime);
            $scope.getAppointmentInfoByAvailableTime(moment(moment().add($scope.available_time,'days')).
                format('YYYY/MM/DD'),$scope.location_id);
        }

        $scope.choiceDoctorLocation = function(value)
        {
            $scope.choiceItem = 0;
            $scope.initChoiceItem = 0;
            $scope.initAvailableTime = 0;

            $scope.infoItem = $scope.infoItem + value;
            if ($scope.infoItem <= 0) {
                $scope.infoItem = angular.copy($scope.maxItem);
            }
            else if ($scope.infoItem > $scope.maxItem) {
                $scope.infoItem = 1
            }
            $scope.doctorLocation = angular.copy($scope.doctorLocationArray[$scope.infoItem-1]);

            $scope.getAppointmentInfoByAvailableTime(moment().format('YYYY/MM/DD'),$scope.doctorLocationIdArray[$scope.infoItem-1]);

            $scope.GetDoctorVisitInfoByLocationInWeek($scope.doctorHospitalNameArray[$scope.infoItem-1],$scope.doctorLocationIdArray[$scope.infoItem-1]);

            $scope.location_id = $scope.doctorLocationIdArray[$scope.infoItem-1];

            $scope.getMsgAppointmentStatus($scope.doctorLocation);
        }

        //用户进行不同出诊地点的选择
        $scope.choiceLocation = function(value){
            $scope.infoItem = $scope.infoItem + value;
            if($scope.infoItem<=0)
            {
                $scope.infoItem = angular.copy($scope.maxItem);
            }
            else if($scope.infoItem>$scope.maxItem)
            {
                $scope.infoItem=1
            }
            _.each($scope.doctorVisitInfo,function(val){
                if(val.infoItem==$scope.infoItem)
                {
                    //选择的医生出诊地点位置
                    $scope.sys_hospital_id = val.sys_hospital_id;
                    $scope.available_time = val.available_time;
                    $scope.hospitalName = val.hospitalName;
                    $scope.location = val.location;
                    $scope.position = val.position;
                    $scope.location_id = val.location_id;
                    $scope.initChoiceItem = angular.copy($filter('number')($scope.available_time));
                    $scope.choiceItem = angular.copy($scope.initChoiceItem);
                    $scope.initAvailableTime = angular.copy($scope.available_time);
                    $scope.minAvailableDate = moment(val.date).format('YYYY/MM/DD');
                    $scope.getAppointmentInfoByAvailableTime(moment(val.date).format('YYYY/MM/DD'),$scope.location_id);
                }
            })
            $scope.appointmentInfo.choiceDate = $scope.choiceDate;
            $scope.GetDoctorVisitInfoByLocationInWeek($scope.hospitalName,$scope.location_id);
        }

        //关注功能
        $scope.attentionDoctor= function(){
            $scope.pageLoading = true;
            AttentionDoctor.save({doctorId:$scope.doctorId,routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }
                else{
                    $scope.focus = "已关注"
                    $scope.isAttention = true
                    $scope.doctorDetail.fans_number = (typeof($scope.doctorDetail.fans_number) == undefined)?1:
                        ($scope.doctorDetail.fans_number+1);
                }
            })
        }

        //预约开放提醒按钮
        $scope.showPopup = function() {
            $scope.pageLoading = true;
            // An elaborate, custom popup
            $ionicPopup.show({
                template: '手机号：'+ data.userPhone,
                title: '预约开放通知',
                subTitle: '一旦医生有预约时间开放，您将收到手机短信提醒!',
                scope: $scope,
                buttons: [
                    {
                        text: '<b>取消</b>',
                        type: 'button-calm'
                    },
                    {
                        text: '<b>确定</b>',
                        type: 'button-calm',
                        onTap: function(e) {
                            var doctorLocation = $scope.doctorLocation.split(" ");
                            $scope.pageLoading = true;
                            MsgAppointment.save({phoneNum:data.userPhone,doctorId:$stateParams.doctorId,
                                hospitalName:doctorLocation[0],position:doctorLocation[1],location:doctorLocation[2],routePath:routePath},function(data){
                                $scope.pageLoading = false;
                                if(data.status=="9") {
                                    window.location.href = data.redirectURL;
                                }
                                else if(data.status>0){
                                    if($scope.latestAppointmentDate!=null){
                                        $scope.order[$scope.latestAppointmentDate] = "1"
                                    }else{
                                        $scope.order[0] = "1"
                                    }
                                }
                            })
                        }
                    },
                ]
            });
        };

        $scope.getMsgAppointmentStatus = function(doctorLocation){
            var doctorLocation = doctorLocation.split(" ");
            $scope.pageLoading = true;
            MsgAppointmentStatus.save({phoneNum:$scope.userPhone,doctorId:$stateParams.doctorId,
                hospitalName:doctorLocation[0],position:doctorLocation[1],location:doctorLocation[2]},function(data){
                $scope.pageLoading = false;
                if(data[0]=='1'){
                    if($scope.latestAppointmentDate!=null){
                        $scope.order[$scope.latestAppointmentDate] = "1"
                    }else{
                        $scope.order[0] = "1"
                    }
                }
                else if(data[0]=='0'){
                    if($scope.latestAppointmentDate!=null){
                        $scope.order[$scope.latestAppointmentDate] = "0"
                    }else{
                        $scope.order[0] = "0"
                    }
                }
            })
        }

        //点击日期获取相应预约详情 医生id，日期、出诊地址
        $scope.selectDate = function(date,item){
            $scope.getAppointmentInfoByAvailableTime(date,$scope.location_id);
            $scope.choiceItem = item;
            $scope.choiceDate = date;
            $scope.appointmentInfo.choiceDate = date;
        }

        $scope.change = function(item){
            if(item=="doc"){
                $scope.du="1";
                $scope.doc = true;
                $scope.user = false;
                $("#doc").addClass("cur")
                $("#user").removeClass("cur");
            }
            else{
                $scope.du="0";
                $scope.user = true;
                $scope.doc = false;
                $("#user").addClass("cur")
                $("#doc").removeClass("cur");
            }
        }

        $scope.checkIfAppScanDoctor = function(register_service_id,doctorId,choiceDate,begin_time,end_time){

            /*王强预约提示信息*/
            choiceDate = choiceDate.replace("/","-").replace("/","-");
            if(doctorId=='8c97ccb3b9c34279b807e4a03c46133d'){
                $scope.wangQiang =true;
                $scope.begin_time_am = parseInt(begin_time.substr(0, 2));
            }else if(doctorId=='1aea1e059598442fb2d363fdd52119a0'){
                var boolean = moment(choiceDate+" "+"06:00","YYYY/MM/DD HH:mm").isAfter(moment().format('YYYY-MM-DD HH:mm'));
                if(!boolean){
                    alert("王继英主任不接受当天预约，您可预约其他医生或预约王主任7天内的其他出诊时间。如需帮助，请拨打客服热线 400-623-7120")
                    return;
                }
            }

            $scope.pageLoading = true;
            CheckIfAppScanDoctor.save({register_service_id:register_service_id,attentionDoctorId:$stateParams.attentionDoctorId},
                function(data){
                    $scope.chargePrice = 0;
                    //if(data.status=="5"){
                    //    $scope.pageLoading = false;
                    //    $scope.bookRemindLock =true;
                    //    return;
                    //}

                    //检测如果是黄牛则跳转到前一个页面
                    if(data.isBlack=="true"){
                        history.back();
                    }

                    var boolean = "true"
                    if($rootScope.memberFunction=="true"){
                        if(data.needPay=="false"){
                            boolean = "false";
                        }
                        else if(data.needPay=="true"){
                            boolean = "true";
                            $scope.chargePrice = 15000;
                        }
                        else if(data.needPay=="tehui") {
                            boolean = "tehui";
                            $scope.chargePrice = 15000;
                        }
                    }else{
                        boolean = "false";
                    }

                    resolveUserLoginStatus.events("appointmentConfirm",
                        register_service_id + "," + doctorId+ "," +choiceDate+ "," +
                        begin_time+ "," +end_time+ "," +
                        boolean+ "," +$scope.chargePrice,
                        {register_service_id:register_service_id, doctorId:doctorId,date:choiceDate,
                            begin_time:begin_time,end_time:end_time,needPay:boolean,chargePrice:$scope.chargePrice},"","go");
                })
        }

        $scope.doctorGroup = function(){
            $state.go("doctorGroupList",{groupId: $scope.doctorDetail.doctorGroupId})
        }

        $scope.$on('$ionicView.enter', function(){
            $scope.currentDateTime = moment().format('YYYY/MM/DD HH:MM');
            for(var i=0;i<7;i++)
            {
                $scope.timeListWithMonth[i] = moment().add(i, 'days').format('YYYY/MM/DD');
                $scope.timeList[i] = moment().add(i, 'days').format('DD');
                $scope.weekList[i] = moment(moment().add(i, 'days')).format("E");
                if($scope.weekList[i]==1){$scope.weekList[i]="一"}
                if($scope.weekList[i]==2){$scope.weekList[i]="二"}
                if($scope.weekList[i]==3){$scope.weekList[i]="三"}
                if($scope.weekList[i]==4){$scope.weekList[i]="四"}
                if($scope.weekList[i]==5){$scope.weekList[i]="五"}
                if($scope.weekList[i]==6){$scope.weekList[i]="六"}
                if($scope.weekList[i]==7){$scope.weekList[i]="日"}
            }

            //检测用户是否已关注
            $scope.pageLoading = true;
            CheckAttentionDoctor.save({doctorId:$scope.doctorId},function(data){
                $scope.pageLoading = false;
                if(data.isconcerned){
                    $scope.focus = "已关注"
                    $scope.isAttention = true
                }
            })

            //获取用户评价
            $scope.pageLoading = true;
            GetUserEvaluate.save({doctorId:$scope.doctorId},function(data){
                $scope.pageLoading = false;
                $scope.userEvaluate = data.userEvaluate;
            })

            //医生的基本信息
            $scope.pageLoading = true;
            DoctorDetail.save({"doctorId":$scope.doctorId},function(data){
                $scope.pageLoading = false;
                $scope.doctorDetail = data;
                $scope.doctorId = data.doctorId;
                $scope.appointmentInfo.doctorDetail = $scope.doctorDetail;
                $scope.avgMajorStar = data.doctorScore.avgMajorStar == null?"5": data.doctorScore.avgMajorStar;
                $scope.avgStar =  data.doctorScore.avgStar == null?"5":data.doctorScore.avgStar;
            });

            if($scope.mark=="dateAvailable")
            {
                $scope.choiceItem = $filter('number')($scope.available_time);
                $scope.initChoiceItem = angular.copy($filter('number')($scope.available_time));
                $scope.initAvailableTime = angular.copy($scope.available_time);

                //根据医生的出诊地点，获取医生本周将出诊的日期
                $scope.GetDoctorVisitInfoByLocationInWeek($scope.hospitalName,$scope.location_id);

                //获取医生的7天内的出诊地点和位置信息
                $scope.pageLoading = true;
                GetDoctorVisitInfo.save({doctorId:$scope.doctorId},function(data){
                    $scope.pageLoading = false;
                    $scope.doctorVisitInfo = data.visitInfo;
                    $scope.maxItem = data.maxItem;
                    _.each($scope.doctorVisitInfo, function(val){
                        if(val.hospitalName==$scope.hospitalName&&val.location==$scope.location)
                        {
                            $scope.infoItem = val.infoItem;
                            $scope.sys_hospital_id = val.sys_hospital_id;
                        }
                    })
                })

                //默认日期的预约信息
                $scope.getAppointmentInfoByAvailableTime(moment(moment().add($scope.available_time,'days')).format('YYYY/MM/DD'),
                    $scope.location_id);
            }
            else if($scope.mark=="dateNoAvailable")
            {
                $scope.choiceItem = 0;
                $scope.initChoiceItem = 0;
                $scope.initAvailableTime = 0;

                if($scope.available_time>6)
                {
                    $scope.latestAppointmentDate = moment(moment().add($scope.available_time,'days')).format('YYYY/MM/DD');
                }
                $scope.pageLoading = true;
                FindDoctorLocationByDoctorId.save({sys_doctor_id:$scope.doctorId},function(data){
                    $scope.pageLoading = false;
                    var doctorLocation = new Array();
                    var doctorLocationId = new Array();
                    var doctorHospitalName = new Array();
                    $scope.infoItem = 1;
                    $scope.maxItem = 0;
                    _.each(data.location,function(val){
                        $scope.maxItem++;
                        doctorLocation.push(val.hospitalName+" " + val.position + " " + val.location);
                        doctorLocationId.push(val.id);
                        doctorHospitalName.push(val.hospitalName);
                    })
                    $scope.doctorLocation = doctorLocation[0];
                    $scope.doctorLocationArray = doctorLocation;
                    $scope.doctorLocationIdArray = doctorLocationId;
                    $scope.doctorHospitalNameArray = doctorHospitalName;
                    $scope.getAppointmentInfoByAvailableTime(moment().format('YYYY/MM/DD'),data.location[0].id);
                    $scope.GetDoctorVisitInfoByLocationInWeek(data.location[0].hospitalName,data.location[0].id);
                    $scope.location_id = data.location[0].id;
                    $scope.getMsgAppointmentStatus($scope.doctorLocation);
                })
            }
        })
    }])