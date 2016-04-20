angular.module('controllers', ['ionic']).controller('phoneConDoctorListCtrl', ['$rootScope',
    '$scope','$state','$stateParams',
    'ListHospitalDoctor','ListAppointmentTimeDoctor',
    'ListHospital','ListAppointmentTimeHospital',
    'ListHospitalDepartment','ListHospitalDepartmentDoctor','ListSecondIllnessDoctor',
    'ListHospitalByIllnessSecond','SearchDoctors',
    function ($rootScope,$scope,$state,$stateParams,
              ListHospitalDoctor,ListAppointmentTimeDoctor,
              ListHospital,ListAppointmentTimeHospital,
              ListHospitalDepartment,ListHospitalDepartmentDoctor,ListSecondIllnessDoctor,
              ListHospitalByIllnessSecond,SearchDoctors) {

        $scope.title = $stateParams.titleName.length > 8 ? $stateParams.titleName.substr(0,8)+"...":$stateParams.titleName;
        $scope.title0 = "宝大夫（400-623-7120）";
        $scope.rankData = '0';
        $scope.reloadMoreDataMark="";
        $scope.isBlank = false;
        $scope.week = ['今天','明天','后天','3天后','4天后','5天后','6天后'];
        $scope.departmentSelectName="全部科室";
        $scope.hospitalSelectName="全部医院";
        $scope.rankTitle = "排序";
        $scope.allItemLock = false;//选择列表中全部科室 或 全部医院 是否显示
        //获取更多列表信息
        $scope.infoPage = {};
        var infoParam = [];
        $scope.info = {};
        $scope.departmentData = [];
        $scope.starImg1 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar.png";
        $scope.starImg2 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_part.png";
        $scope.starImg3 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_gray.png";
        $scope.starNumInt=[];
        $scope.starNumFloat=[];
        $scope.shadowLock1=false;// 全部医院或科室列表
        $scope.shadowLock2=false;// 排序列表

        // 医生星级评价 星星个数
        $scope.doctorStar = function () {
            for(var i=0;i<$scope.doctorData.length;i++){
                if($scope.doctorData[i].star==""){
                    $scope.doctorData[i].star="5.0"
                }
                $scope.starNumInt[i] =  parseInt($scope.doctorData[i].star.substr(0, 1));
                $scope.starNumFloat[i] =  parseInt($scope.doctorData[i].star.substr(2, 1));
                if($scope.starNumInt[i]>=5){
                    $scope.starNumInt[i]=5;
                }


            }
        }
        $scope.init = function () {
            $scope.dataLoading = true;
            $scope.reloadMoreDataMark="";
            $scope.shadowLock = false;
            $scope.orderBy = "0";
            $scope.departmentSelectName="全部科室";
            $scope.hospitalSelectName="全部医院";
            $scope.doRefresh($scope.orderBy,1,'');
            $scope.allItemLock = false;//选择列表中全部科室 或 全部医院 是否显示
        };

        /*点击 全部医院或全部科室*/
        $scope.selectAllDepartmentOrHospital = function(){
            $scope.selectType = $scope.remark;
            $scope.shadowLock2 = false;
            if( $scope.shadowLock1){
                $scope.shadowLock1 = false;
            }
            else{
                $scope.shadowLock1 = true;
            }

        };

        /*点击 排序*/
        $scope.selectAllRank = function(){
            $scope.shadowLock1 = false;
            if( $scope.shadowLock2){
                $scope.shadowLock2 = false;
            }
            else{
                $scope.selectType = "rank";
                $scope.shadowLock2 = true;
            }
        };
        /*加载 某科室下 的医生列表*/
        $scope.reloadDoctorInDepartment = function(hospitalId,departmentLevel1Name){
            $scope.pageLoading = true;
            $scope.reloadMoreDataMark = "reloadDoctorInDepartment";
            infoParam.hospitalId = hospitalId;
            infoParam.departmentLevel1Name = departmentLevel1Name;
            $scope.shadowLock1 = false;
            $scope.orderBy= "0";
            $scope.departmentSelectName=departmentLevel1Name;
            $scope.doRefresh($scope.orderBy,1,infoParam);
            $scope.allItemLock = true;//选择列表中全部科室 或 全部医院 是否显示
        };

        /*加载 某医院下 的医生列表*/
        $scope.reloadDoctorInHospital = function(hospitalId,hospitalName){
            $scope.pageLoading = true;
            $scope.reloadMoreDataMark = "reloadDoctorInHospital";
            infoParam.hospitalId = hospitalId;
            $scope.orderBy = "0";
            $scope.shadowLock1 = false;
            $scope.hospitalSelectName=hospitalName;
            $scope.doRefresh($scope.orderBy,1,infoParam);
            $scope.allItemLock = true;//选择列表中全部科室 或 全部医院 是否显示
        };

        /*按时间最近 排序*/
        $scope.timeRank = function(){
            $scope.pageLoading = true;
            $scope.selectType="rank";
            $scope.orderBy = "0";
            $scope.pageCurrent = 1;
            $scope.reloadMoreDataMark="";
            $scope.doctorData="";
            $scope.shadowLock2 = false;
            $scope.rankTitle = "时间最近";
            $scope.doRefresh($scope.orderBy,1,infoParam);
            $scope.shadowLock2 = false;
        };
        /*按粉丝数 排序*/
        $scope.fansRank = function(){
            $scope.pageLoading = true;
            $scope.selectType="rank";
            $scope.pageCurrent = 1;
            $scope.reloadMoreDataMark="";
            $scope.doctorData="";
            $scope.shadowLock2 = false;
            $scope.orderBy = "1";
            $scope.rankTitle = "粉丝最多";
            $scope.doRefresh($scope.orderBy,1,infoParam);
            $scope.shadowLock2 = false;
        }
        /*按工作年限 排序*/
        $scope.workTimeRank = function(){
            $scope.pageLoading = true;
            $scope.selectType="rank";
            $scope.pageCurrent = 1;
            $scope.reloadMoreDataMark="";
            $scope.doctorData="";
            $scope.shadowLock = false;
            $scope.orderBy = "2";
            $scope.rankTitle = "从业时间";
            $scope.doRefresh($scope.orderBy,1,infoParam);
            $scope.shadowLock2 = false;
        }

        /*下拉时加载更多医生 排序*/
        $scope.loadMore = function () {
            if ($scope.scrollLoading) {
                $scope.pageLoading = true;
                $scope.doRefresh($scope.orderBy,$scope.infoPage.page + 1,$scope.info);
            }
        };

        $scope.doRefresh = function(orderBy,page,infoParam){
            $scope.info = infoParam;
            $scope.orderBy = orderBy;
            if($scope.reloadMoreDataMark=="reloadDoctorInDepartment"&&$stateParams.action==null)
            {
                $scope.pageLoading = true;
                ListHospitalDepartmentDoctor.save({"pageNo":page+"","pageSize":"10","orderBy":orderBy,
                    "hospitalId":infoParam.hospitalId,"departmentLevel1Name":infoParam.departmentLevel1Name}, function (data) {
                    $scope.pageLoading = false;
                    $scope.scrollLoading = page < data.pageTotal;
                    $scope.infoPage.page = page;
                    $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                    $scope.doctorData = $scope.doctorData.concat(data.doctorDataVo || []);
                    $scope.doctorStar();
                    for(var i=0;i<$scope.doctorData.length;i++){
                        console.log($scope.doctorData[i].star);
                        /*     $(".doctor-info .pic").eq(i).children("img")*/
                    }
                    $scope.allHospitalOrDepartment="hospital";
                    $scope.$broadcast('scroll.refreshComplete');
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                });
            }
            else if($scope.reloadMoreDataMark=="reloadDoctorInHospital"&&$stateParams.action==null)
            {
                $scope.pageLoading = true;
                ListHospitalDoctor.save({pageNo: page+"", pageSize: "10",orderBy:orderBy,hospitalId:infoParam.hospitalId}, function (data) {
                    $scope.pageLoading = false;
                    $scope.scrollLoading = page < data.pageTotal;
                    $scope.infoPage.page = page;
                    $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                    $scope.doctorData = $scope.doctorData.concat(data.doctorDataVo || []);
                    $scope.doctorStar();
                    /* console.log($scope.doctorData );*/
                    for(var i=0;i<$scope.doctorData.length;i++){
                        console.log($scope.doctorData[i].star);
                        /*     $(".doctor-info .pic").eq(i).children("img")*/
                    }
                    if( data.doctorDataVo.length==0){
                        $scope.care1 = "医生正在赶来的路上哦，请耐心等待！"
                        $scope.isBlank = true
                    }
                    $scope.allHospitalOrDepartment="department";
                    $scope.$broadcast('scroll.refreshComplete');
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                });
            }
            else
            {
                if ($stateParams.action == "searchDoctorByHospitalName") {
                    if($scope.reloadMoreDataMark=="reloadDoctorInDepartment")
                    {
                        $scope.pageLoading = true;
                        ListHospitalDoctor.save({
                                "pageNo":page+"",
                                "pageSize":"10",
                                "orderBy":orderBy,
                                "hospitalId":infoParam.hospitalId,
                                "departmentLevel1Name":infoParam.departmentLevel1Name},
                            function (data) {
                                $scope.pageLoading = false;
                                $scope.scrollLoading = page < data.pageTotal;
                                $scope.infoPage.page = page;
                                $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                                $scope.doctorData = $scope.doctorData.concat(data.doctorDataVo || []);
                                $scope.doctorStar();
                                $scope.$broadcast('scroll.refreshComplete');
                                $scope.$broadcast('scroll.infiniteScrollComplete');
                            });
                    }
                    else
                    {
                        ListHospitalDoctor.save({
                            pageNo: page + "",
                            pageSize: "10",
                            orderBy: orderBy,
                            hospitalId: $stateParams.searchName,
                            department_level1:infoParam.departmentLevel1Name
                        }, function (data) {
                            $scope.pageLoading = false;
                            $scope.scrollLoading = page < data.pageTotal;
                            $scope.infoPage.page = page;
                            $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                            $scope.doctorData = $scope.doctorData.concat(data.doctorDataVo || []);
                            $scope.doctorStar();
                            $scope.$broadcast('scroll.refreshComplete');
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            if(data.doctorDataVo.length==0){
                                $scope.title = $stateParams.titleName.length>8? $stateParams.titleName.substr(0,8)+"...": $stateParams.titleName
                                $scope.care1 = "医生正在赶来的路上哦，请耐心等待！";
                                $scope.isBlank = true
                            }
                        });
                    }
                    $scope.pageLoading = true;
                    ListHospitalDepartment.save({
                        pageNo: "1",
                        pageSize: "100",
                        orderBy: orderBy,
                        "consultPhone": "1",
                        hospitalId: $stateParams.searchName
                    }, function (data) {
                        $scope.pageLoading = false;
                        $scope.departmentData = data.departmentData;
                    });
                    $scope.remark = "department";
                }
                else if ($stateParams.action == "searchDoctorByIllnessSecondId") {
                    if($scope.reloadMoreDataMark=="reloadDoctorInHospital")
                    {
                        $scope.pageLoading = true;
                        ListSecondIllnessDoctor.save({
                            "pageNo": page + "",
                            "pageSize": "10",
                            "orderBy": orderBy,
                            "illnessSecondId": $stateParams.searchName,
                            hospitalId:infoParam.hospitalId
                        }, function (data) {
                            $scope.pageLoading = false;
                            $scope.scrollLoading = page < data.pageTotal;
                            $scope.infoPage.page = page;
                            $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                            $scope.doctorData = $scope.doctorData.concat(data.doctorDataVo || []);
                            $scope.doctorStar();
                            $scope.$broadcast('scroll.refreshComplete');
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            if( data.doctorDataVo.length==0){
                                $scope.title = $stateParams.titleName.length>8? $stateParams.titleName.substr(0,8)+"...": $stateParams.titleName
                                $scope.care1 = "没有可约的医生哦，去看看其他医生吧！"
                                $scope.isBlank = true
                            }
                        });
                    }
                    else
                    {
                        $scope.pageLoading = true;
                        ListSecondIllnessDoctor.save({
                            "pageNo": page + "",
                            "pageSize": "10",
                            "orderBy": orderBy,
                            "illnessSecondId": $stateParams.searchName,
                            hospitalId:infoParam.hospitalId
                        }, function (data) {
                            $scope.pageLoading = false;
                            $scope.scrollLoading = page < data.pageTotal;
                            $scope.infoPage.page = page;
                            $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                            $scope.doctorData = $scope.doctorData.concat(data.doctorDataVo || []);
                            $scope.doctorStar();
                            $scope.$broadcast('scroll.refreshComplete');
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            if( data.doctorDataVo.length==0){
                                $scope.title = $stateParams.titleName.length>8? $stateParams.titleName.substr(0,8)+"...": $stateParams.titleName
                                $scope.care1 = "没有可约的医生哦，去看看其他医生吧！"
                                $scope.isBlank = true
                            }
                        });
                    }
                    $scope.pageLoading = true;
                    ListHospitalByIllnessSecond.save({
                        "pageNo": "1",
                        "pageSize": "100",
                        "orderBy": orderBy,
                        "illnessSecondId": $stateParams.searchName,
                        "consultPhone":"1"
                    }, function (data) {
                        $scope.pageLoading = false;
                        $scope.hospitalData = data['hospitalData'];
                        console.log($scope.hospitalData);
                    });

                    $scope.remark = "hospital";
                }
                else if ($stateParams.action == "searchDoctorByDate") {
                    if($scope.reloadMoreDataMark=="reloadDoctorInHospital") {
                        $scope.pageLoading = true;
                        ListAppointmentTimeDoctor.save({
                            "pageNo": page + "",
                            "pageSize": "10",
                            "orderBy": orderBy,

                            "date": moment($stateParams.searchName,"MM/DD").set('year', moment().format('YYYY')).format('YYYY/MM/DD'),
                            hospitalId: infoParam.hospitalId
                        }, function (data) {
                            $scope.pageLoading = false;
                            $scope.scrollLoading = page < data.pageTotal;
                            $scope.infoPage.page = page;
                            $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                            $scope.doctorData = $scope.doctorData.concat(data.doctorDataVo || []);
                            $scope.doctorStar();
                            $scope.$broadcast('scroll.refreshComplete');
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            $scope.title = $stateParams.titleName

                            if( data.doctorDataVo.length==0){
                                $scpoe.care1 = "没有可约的医生哦，去看看其他医生吧!"
                                $scope.isBlank = true
                            }
                        });
                    }
                    else{
                        $scope.pageLoading = true;
                        ListAppointmentTimeDoctor.save({
                            "pageNo": page + "",
                            "pageSize": "10",
                            "orderBy": orderBy,
                            "date":  moment($stateParams.searchName,"MM/DD").set('year', moment().format('YYYY')).format('YYYY/MM/DD'),
                            hospitalId: infoParam.hospitalId
                        }, function (data) {
                            $scope.pageLoading = false;
                            $scope.scrollLoading = page < data.pageTotal;
                            $scope.infoPage.page = page;
                            $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                            $scope.doctorData = $scope.doctorData.concat(data.doctorDataVo || []);
                            $scope.doctorStar();
                            $scope.$broadcast('scroll.refreshComplete');
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            $scope.title = $stateParams.titleName
                            if( data.doctorDataVo.length==0){
                                $scope.care1 = "没有可约的医生哦，去看看其他医生吧!"
                                $scope.isBlank = true
                            }
                        });
                    }

                    $scope.pageLoading = true;
                    ListAppointmentTimeHospital.save({
                        "pageNo": "1",
                        "pageSize": "100",
                        "orderBy": orderBy,
                        "consultPhone":"1",
                        "date":  moment($stateParams.searchName,"MM/DD").set('year', moment().format('YYYY')).format('YYYY/MM/DD')
                    }, function (data) {
                        $scope.pageLoading = false;
                        $scope.hospitalData = data.hospitalData;
                    });

                    $scope.remark = "hospital";
                }
                else if($stateParams.action == "searchDoctorByOpenSearch"){
                    if($scope.reloadMoreDataMark=="reloadDoctorInHospital")
                    {
                        $scope.pageLoading =true;
                        SearchDoctors.save({
                            "pageNo": page , "pageSize": 10, "queryStr":$stateParams.searchName,"hospitalId":
                                infoParam.hospitalId,"orderBy":orderBy
                        }, function (data) {
                            $scope.pageLoading = false;
                            $scope.scrollLoading = page < data.pageTotal;
                            $scope.infoPage.page = page;
                            $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                            $scope.doctorData = $scope.doctorData.concat(data.doctorData || []);
                            $scope.doctorStar();
                            $scope.$broadcast('scroll.refreshComplete');
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            $scope.hospitalData = data.hospitals;
                        });
                    }else{
                        SearchDoctors.save({"queryStr":$stateParams.searchName,"pageSize":10,"pageNo":page,
                            "orderBy":orderBy},function(data){
                            $scope.pageLoading = false;
                            $scope.scrollLoading = page < data.pageTotal;
                            $scope.infoPage.page = page;
                            $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                            $scope.doctorData = $scope.doctorData.concat(data.doctorData || []);
                            $scope.doctorStar();
                            $scope.$broadcast('scroll.refreshComplete');
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            var teamHash = {}
                            var hospitalList = {}
                            for(var i= 0;i < data.doctorData.length;i++){
                                teamHash[data.doctorData[i].hospitalId] = data.doctorData[i].hospitalName;
                            }
                            var j = 0
                            for(var key in teamHash){
                                var hospitalInfo = {"hospitalId":key,"hospitalName":teamHash[key]}
                                hospitalList[j] = hospitalInfo
                                j++
                            }
                            $scope.hospitalData = hospitalList;
                            if( data.doctorData.length==0){
                                $scope.title = $stateParams.titleName.length>8? $stateParams.titleName.substr(0,8)+"...":
                                    $stateParams.titleName
                                $scope.care1 = "医生正在赶来的路上哦，请耐心等待！"
                                $scope.isBlank = true
                            }
                        });
                    }
                    $scope.pageLoading = true;
                    $scope.remark = "hospital";

                }
            }
        }

        /*点击可预约的时间信息*/
        $scope.doctorAppointment = function(doctorId,available_time,hospitalName,location,position,location_id){
            if(available_time>=0&&available_time<7)
            {
                $state.go("phoneConDoctorDetail", {doctorId: doctorId,choiceItem:available_time });
            }
            else
            {
                $state.go("phoneConDoctorDetail", {
                    doctorId: doctorId,choiceItem:available_time });
            }
        }

        $scope.init();
    }])