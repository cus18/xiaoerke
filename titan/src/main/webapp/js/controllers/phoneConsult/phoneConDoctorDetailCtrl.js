angular.module('controllers', ['ionic']).controller('phoneConDoctorDetailCtrl', [
    '$scope','$state','$stateParams','DoctorDetail','DoctorVisitInfoByLocationInWeek','DoctorAppointmentInfoDetail','$location','CheckAttentionDoctor','GetUserLoginStatus','EarliestVisiteInfo','GetUserEvaluate','AttentionDoctor',
    function ($scope,$state,$stateParams,DoctorDetail,DoctorVisitInfoByLocationInWeek,DoctorAppointmentInfoDetail,$location,CheckAttentionDoctor,GetUserLoginStatus,EarliestVisiteInfo,GetUserEvaluate,AttentionDoctor) {
        $scope.title = "医生详情页";
        $scope.pageLoading =false;
        $scope.toggleItem="phone";//默认的底部选择
        $scope.doctorRank =4;/*医生星级评价*/
        $scope.starImg1 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar.png";
        $scope.starImg2 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_part.png";
        $scope.starImg3 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_gray.png";
        $scope.selectApImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Ficon_ap1.png";
        $scope.selectPhoneImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Ficon_phone2.png";
        $scope.attentionImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Ficon_attention_n.png";
        $scope.serviceImg ="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2FblueW_down.png";
        $scope.serviceLock =false;
        $scope.timeListWithMonth = [];
        $scope.timeList = [];
        $scope.weekList = [];
        $scope.timeListStatus = [];
        $scope.choiceItem = $stateParams.choiceItem;

        //每个案例对应的背景色
        $scope.caseColor=["#f08664","#f39618","#f5c61e","#efea3a","#ba81b6","#eb96be","#f6c3d9",
            "#94ceeb","#5abceb","#8cd2f3","#c1e5f7","#3baf36","#7ebe30","#bdd535","#f5d120","#f7f131",
            "#3891cf","#69b3e4","#8b70b0","#ba81b6","#e179a8","#f0a7b3","#c8e6df","#c9e7f9","#f9d7e6",
            "#bdddf4","#e0cae2","#e0d5e9","#d5ece4","#fbe0e5","#fae4ee"];
        $scope.evaluateList = [];
        $scope.userStarNum=[];

        var routePath = encodeURI(encodeURI("/appointBBBBBB" + $location.path()));

        $scope.showService=function(){
            if($scope.serviceLock){
                $scope.serviceLock =false;
                $scope.serviceImg ="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2FblueW_down.png";
            }
            else{
                $scope.serviceLock =true;
                $scope.serviceImg ="http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2FblueW_up.png";
            }
        }

        //获取医生的信息
        DoctorDetail.get({"doctorId":$stateParams.doctorId},function(data){
            console.log(data)
            $scope.pageLoading = false;
            $scope.doctorDetail = data;
            $scope.position = (data.position1==''?'':data.position1+"、")+data.position2
            $scope.evaluateList = $scope.doctorDetail.evaluaMap.evaluateList;
            $scope.userStar();
            console.log($scope.doctorDetail.doctorCaseList);
            //计算总的案列数
            $scope.doctorDetail.sumcase=0;
            for(var i=0;i<$scope.doctorDetail.doctorCaseList.length;i++){
                $scope.doctorDetail.sumcase+=$scope.doctorDetail.doctorCaseList[i].doctor_case_number;
                console.log($scope.doctorDetail.doctorCaseList[i].doctor_case_number)
            };
           /* $scope.doctorDetail.sumcase+=$scope.doctorDetail.otherCase;*/

            $scope.avgMajorStar = data.doctorScore.avgMajorStar == null?"5": data.doctorScore.avgMajorStar;
            $scope.avgStar =  data.doctorScore.avgStar == null?"5":data.doctorScore.avgStar;
            $scope.doctorStar();
        });

        // 医生星级评价 星星个数
        $scope.doctorStar = function () {
            $scope.starNum=(parseFloat($scope.avgMajorStar)+parseFloat($scope.avgStar))/2;
            $scope.starNum= $scope.starNum.toString();
            console.log("star "+ $scope.starNum);
            $scope.starNumInt = parseInt($scope.starNum.substr(0, 1));
            $scope.starNumFloat=  parseInt($scope.starNum.substr(2, 1));
        };
        // 用户星级评价 星星个数
        $scope.userStar = function () {
            for(var i=0;i<$scope.evaluateList.length;i++){
                if($scope.evaluateList[i].star==""){
                    $scope.evaluateList[i].star="5"
                }
                console.log($scope.evaluateList[i].star);
                $scope.userStarNum[i] = parseInt( $scope.evaluateList[i].star);
            }
        }

        //根据输入的地点信息，获医生一周内出诊的出诊日期
        DoctorVisitInfoByLocationInWeek.save({"doctorId":$stateParams.doctorId,"state":"0"},function(data){
            var dateList = new Array();
            _.each(data.dateList,function(val){
                dateList.push(moment(val.date).format('YYYY/MM/DD'));
                $scope.minAvailableDate = dateList[0];
                var dateValue = dateList.join("-");
                for(var i=0;i<7;i++) {
                    $scope.timeListStatus[i] = dateValue.indexOf(moment().add(i, 'days').format('YYYY/MM/DD'));
                }
            })
            $scope.getAppointmentInfoByAvailableTime($scope.minAvailableDate);
        });

        /*查看某一天的号源情况*/
        $scope.getAppointmentInfoByAvailableTime = function(date) {
            DoctorAppointmentInfoDetail.save({
                doctorId: $stateParams.doctorId,
                date: date
            }, function (data) {
                for(var i=0;i<data.consultPhoneTimeList.length;i++){
                    var boolean = moment(moment(data.consultPhoneTimeList[i].data).format('YYYY/MM/DD')+" "+data.consultPhoneTimeList[i].begin_time).isAfter(moment().add(5, 'm'));
                    if(!boolean){
                        data.consultPhoneTimeList[i].state = '1'
                    }
                }
                $scope.appointmentList= data.consultPhoneTimeList;
                console.log(data.consultPhoneTimeList)
            });
        }
        $scope.chooseTime = function(item){
            if(item.state == "1")return
            var routePath = "http://xiaork.cn/keeper/wxPay/patientPay.do?serviceType=phoneConsultAAAAAAphoneConDoctorDetail="
                + item.id+"AAAAAAdoctorId="+$stateParams.doctorId;
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    $scope.nowdate = moment().format('YYYY/MM/DD HH:MM');
                    var boolean = moment(moment(item.data).format('YYYY/MM/DD')+" "+item.begin_time).isAfter(moment().add(5, 'm'));
                    if(boolean){
                        window.location.href = "http://xiaork.cn/keeper/wxPay/patientPay.do?serviceType=phoneConsult&phoneConDoctorDetail="
                            + item.id+"&doctorId="+$stateParams.doctorId;
                    }else{
                        alert("预约时间间隔过短")
                        retrun
                    }

                }})
        }

        /*选择某一天的号源*/
        $scope.selectDay = function(index){
            $scope.choiceItem=index;
            $scope.getAppointmentInfoByAvailableTime($scope.timeListWithMonth[index]);
        };

        /*切换底部的选择*/
        $scope.toggleSelect = function(index){
            $scope.toggleItem=index;
            if(index=="ap"){
                $scope.selectApImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Ficon_ap2.png";
                $scope.selectPhoneImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Ficon_phone1.png";
                window.location.href = "appoint#/doctorAppointment/04b17d3ed0374609ad65s2f3b68b9b32,,%E5%8C%97%E4%BA%AC%E4%B8%AD%E6%97%A5%E5%8F%8B%E5%A5%BD%E5%8C%BB%E9%99%A2,,,dateNoAvailable,,";
            }
            else{
                $scope.selectApImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Ficon_ap1.png";
                $scope.selectPhoneImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Ficon_phone2.png";
                $state.go("phoneConDoctorDetail", {doctorId: $stateParams.doctorId,choiceItem:$stateParams.choiceItem});
            }
        };

        //关注功能
        $scope.attentionDoctor= function(){
            var routePath = "/phoneConsultBBBBBB"+$location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    if($scope.isAttention)return;
                    $scope.pageLoading = true;
                    AttentionDoctor.save({doctorId:$stateParams.doctorId,routePath:routePath},function(data){
                        $scope.pageLoading = false;
                        if(data.status=="9") {
                            window.location.href = data.redirectURL;
                        }
                        else{
                            $scope.attentionImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Ficon_attention_y.png";
                            $scope.isAttention = true;
                            $scope.doctorDetail.fans_number = (typeof($scope.doctorDetail.fans_number) == undefined)?1:
                                ($scope.doctorDetail.fans_number+1);
                        }
                    })
                }
            })
        }

        // 点击查看全部评价 跳转到评价列表页
        $scope.goEvaluateList = function(){
          $state.go("evaluateList",{"doctorId":$stateParams.doctorId})
        }
        //获取全部评价（电话咨询）
        $scope.getConsultEvaluate = function(){
            $scope.pageLoading = false;
            GetUserEvaluate.save({"doctorId":$stateParams.doctorId,evaluateType:"1",pageNo:"1",pageSize:"1000"},function(data){
                $scope.evaluateList = data.evaluateList;
                $scope.userStar();
            });
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
            //EarliestVisiteInfo.get({doctorId:$stateParams.doctorId},function(data){
            //  console.log(data)
            //})

            $scope.isAttention = false;
            //检测用户是否已关注
            CheckAttentionDoctor.save({doctorId:$stateParams.doctorId},function(data){
                $scope.pageLoading = false;
                if(data.isConcerned){
                    $scope.attentionImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Ficon_attention_y.png";
                    $scope.isAttention = true;
                }
            })
        })
    }])
