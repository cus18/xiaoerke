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
        $scope.lock1 = "0"
        $scope.lock2 = "0"
        $scope.lock3 = "0"
        $scope.isBlank = false;
        $scope.week = ['今天','明天','后天','3天后','4天后','5天后','6天后'];
        $scope.departmentSelectName="全部科室";
        $scope.hospitalSelectName="全部医院";
        $scope.rankTitle = "排序";
        //获取更多列表信息
        $scope.infoPage = {};
        var infoParam = [];
        $scope.info = {};
        $scope.departmentData = [];
        $scope.starImg1 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar.png";
        $scope.starImg2 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_part.png";
        $scope.starImg3 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_gray.png";

        $scope.selectAllDepartment = function(){
            $scope.selectType = $scope.remark;
            if($scope.lock1 == "0")
            {
                $scope.lock1 = "1"
                $scope.lock2 = "0"
                $scope.arrowImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Farr_blue_up.png";
                $scope.rankImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fs_doc_rank.png";
                $scope.shadow = "1";
                $scope.mySeek = "1"
                $scope.department = "1"
                $scope.rank = "0";
            }
            else
            {
                $scope.lock1 = "0"
                $scope.lock2 = "0"
                $scope.arrowImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Farr_blue_d.png";
                $scope.rankImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fs_doc_rank.png";
                $scope.shadow = "0";
                $scope.mySeek = "0";
                $scope.department="0";
                $scope.rank = "0";
            }
        }

        $scope.selectAllRank = function(){
            $scope.selectType = "rank";
            if($scope.lock2 == "0")
            {
                $scope.lock2 = "1"
                $scope.lock1 = "0"
                $scope.rankImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fs_doc_rank_cur.png";
                $scope.arrowImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Farr_blue_d.png";
                $scope.rank = "1";
                $scope.department="0"
                $scope.mySeek="1"
                $scope.shadow = '1';
            }
            else
            {
                $scope.lock2 = "0"
                $scope.lock1 = "0"
                $scope.rankImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fs_doc_rank.png";
                $scope.arrowImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Farr_blue_d.png";
                $scope.rank = "0";
                $scope.department="0"
                $scope.mySeek="0"
                $scope.shadow = '0';
            }
        }

        $scope.init = function () {
            $scope.dataLoading = true;
            $scope.reloadMoreDataMark="";
            $scope.lock1 = "0";
            $scope.lock2 = "0";
            $scope.lock3 = "0";
            $scope.shadow = '0';
            $scope.orderBy = "0";
            $scope.departmentSelectName="全部科室";
            $scope.hospitalSelectName="全部医院";
            $scope.arrowImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Farr_blue_d.png";
            $scope.rankImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fs_doc_rank.png";
            $scope.mySeek="0"
            $scope.doRefresh($scope.orderBy,1,'');
        };

        $scope.reloadDoctorInDepartment = function(hospitalId,departmentLevel1Name){
            $scope.pageLoading = true;
            $scope.selectType="";
            $scope.reloadMoreDataMark = "reloadDoctorInDepartment";
            infoParam.hospitalId = hospitalId;
            infoParam.departmentLevel1Name = departmentLevel1Name;
            $scope.lock1=0;
            $scope.lock2=0;
            $scope.lock3 = "1";
            $scope.shadow = '0';
            $scope.arrowImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Farr_blue_d.png";
            $scope.orderBy= "0";
            $scope.departmentSelectName=departmentLevel1Name;
            $scope.doRefresh($scope.orderBy,1,infoParam);
        }

        $scope.reloadDoctorInHospital = function(hospitalId,hospitalName){
            $scope.pageLoading = true;
            $scope.selectType="";
            $scope.reloadMoreDataMark = "reloadDoctorInHospital";
            infoParam.hospitalId = hospitalId;
            $scope.orderBy = "0";
            $scope.doRefresh($scope.orderBy,1,infoParam);
            $scope.shadow = '0';
            $scope.arrowImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Farr_blue_d.png";
            $scope.lock1=0;
            $scope.lock2=0;
            $scope.lock3 = "1";
            $scope.hospitalSelectName=hospitalName;
        }

        $scope.timeRank = function(){
            $scope.pageLoading = true;
            $scope.selectType="";
            $scope.orderBy = "0";
            $scope.pageCurrent = 1;
            $scope.reloadMoreDataMark="";
            $scope.lock1=0;
            $scope.lock2=0;
            $scope.doctorData="";
            $scope.shadow = '0';
            $scope.rankTitle = "时间最近";
            $scope.doRefresh($scope.orderBy,1,infoParam);
        }

        $scope.fansRank = function(){
            $scope.pageLoading = true;
            $scope.selectType="";
            $scope.pageCurrent = 1;
            $scope.reloadMoreDataMark="";
            $scope.lock1=0;
            $scope.lock2=0;
            $scope.doctorData="";
            $scope.shadow = '0';
            $scope.orderBy = "1";
            $scope.rankTitle = "粉丝最多";
            $scope.doRefresh($scope.orderBy,1,infoParam);
        }

        $scope.workTimeRank = function(){
            $scope.pageLoading = true;
            $scope.selectType="";
            $scope.pageCurrent = 1;
            $scope.reloadMoreDataMark="";
            $scope.lock1=0;
            $scope.lock2=0;
            $scope.doctorData="";
            $scope.shadow = '0';
            $scope.orderBy = "2";
            $scope.rankTitle = "从业时间";
            $scope.doRefresh($scope.orderBy,1,infoParam);
        }

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
                    console.log($scope.doctorData );
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
                        ListHospitalDoctor.save({"pageNo":page+"","pageSize":"10","orderBy":orderBy,
                            "hospitalId":infoParam.hospitalId,"departmentLevel1Name":infoParam.departmentLevel1Name}, function (data) {
                            $scope.pageLoading = false;
                            $scope.scrollLoading = page < data.pageTotal;
                            $scope.infoPage.page = page;
                            $scope.doctorData = page == 1 ? [] : $scope.doctorData;
                            $scope.doctorData = $scope.doctorData.concat(data.doctorDataVo || []);
                            console.log(  $scope.doctorData)
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
                            console.log(  $scope.doctorData)
                            $scope.$broadcast('scroll.refreshComplete');
                            $scope.$broadcast('scroll.infiniteScrollComplete');
                            if(data.doctorDataVo.length==0){
                                $scope.title = $stateParams.titleName.length>8? $stateParams.titleName.substr(0,8)+"...": $stateParams.titleName
                                $scope.care1 = "医生正在赶来的路上哦，请耐心等待！"
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