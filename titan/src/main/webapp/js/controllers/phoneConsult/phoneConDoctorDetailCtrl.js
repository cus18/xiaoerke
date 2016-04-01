angular.module('controllers', ['ionic']).controller('phoneConDoctorDetailCtrl', [
    '$scope','$state','$stateParams','DoctorDetail','DoctorVisitInfoByLocationInWeek','DoctorAppointmentInfoDetail',
    function ($scope,$state,$stateParams,DoctorDetail,DoctorVisitInfoByLocationInWeek,DoctorAppointmentInfoDetail) {
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

        var routePath = encodeURI(encodeURI("/appointBBBBBB" + $location.path()));

        //获取医生的信息
        DoctorDetail.get({"doctorId":$stateParams.doctorId},function(data){
            $scope.pageLoading = false;
            $scope.doctorDetail = data;
            console.log($scope.doctorDetail.doctorCaseList);
            //计算总的案列数
            $scope.doctorDetail.sumcase=0;
            for(var i=0;i<$scope.doctorDetail.doctorCaseList.length;i++){
                $scope.doctorDetail.sumcase+=$scope.doctorDetail.doctorCaseList[i].doctor_case_number;
                console.log($scope.doctorDetail.doctorCaseList[i].doctor_case_number)
            };
            $scope.doctorDetail.sumcase+=$scope.doctorDetail.otherCase;

            $scope.avgMajorStar = data.doctorScore.avgMajorStar == null?"5": data.doctorScore.avgMajorStar;
            $scope.avgStar =  data.doctorScore.avgStar == null?"5":data.doctorScore.avgStar;
        });


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
                $scope.appointmentList= data.consultPhoneTimeList;
            });
        }
        $scope.chooseTime = function(sysConsultServiceId){
            window.location.href = "/xiaoerke-appoint/phoneConsultPay/patientPay.do?phoneConDoctorDetail="
                + sysConsultServiceId;
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

            }
            else{
                $scope.selectApImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Ficon_ap1.png";
                $scope.selectPhoneImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Ficon_phone2.png";
            }
        };

        //关注功能
        $scope.attentionDoctor= function(){
            $scope.pageLoading = true;
            AttentionDoctor.save({doctorId:$scope.doctorId,routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }
                else{
                    $scope.attentionImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Ficon_attention_y%.png";
                    $scope.isAttention = true;
                    $scope.doctorDetail.fans_number = (typeof($scope.doctorDetail.fans_number) == undefined)?1:
                        ($scope.doctorDetail.fans_number+1);
                }
            })
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
            CheckAttentionDoctor.save({doctorId:$scope.doctorId},function(data){
                $scope.pageLoading = false;
                if(data.isconcerned){
                    $scope.attentionImg ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Ficon_attention_y%.png";
                    $scope.isAttention = true;
                }
            })
        })
    }])
