angular.module('controllers', ['ionic']).controller('memberServiceCtrl', [
        '$scope','GetUserLoginStatus','$state','$stateParams','ExtendMemberService','ListHospital','resolveUserLoginStatus',
        function ($scope,GetUserLoginStatus,$state,$stateParams,ExtendMemberService,ListHospital,resolveUserLoginStatus) {

            $scope.title = "会员服务"
            $scope.title0 = "宝大夫（400-623-7120）"
            $scope.routeInfo = {}
            $scope.memberType = $stateParams.memberType;
            $scope.$on('$ionicView.enter', function(){
                //送入type和action参数给后台接口，返回属性值
                if($stateParams.action=="extend") {
                    $scope.pageLoading = true;
                    var routePath = "/appointBBBBBB/memberService/" +
                        $stateParams.memberType + "," +
                        $stateParams.action + "," + "";
                    GetUserLoginStatus.save({routePath:routePath},function(data){
                        $scope.pageLoading = false;
                        if(data.status=="9") {
                            window.location.href = data.redirectURL;
                        } else if(data.status=="8"){
                            window.location.href = data.redirectURL+"?targeturl="+routePath;
                        }else {
                            ExtendMemberService.get({"memberType": $stateParams.memberType}, function (data) {
                                if (data.result == "true") {
                                    $scope.extendMember = "true";
                                    $scope.startDate = data.startDate;
                                    $scope.endDate = data.endDate;
                                    $scope.leftTimes = data.leftTimes;
                                }
                                else if (data.result == "false") {
                                    $scope.extendMember = "false";
                                    Showbo.Msg.alert("感谢您对宝大夫的长期支持与关注，您已体验过免费名医预约服务，" +
                                        "不再享受免费参加和赠送体验活动，点击“立即购买”可购买短期会员。");
                                    $scope.startDate = moment().format('YYYY/MM/DD');
                                    $scope.endDate = moment().add('days', 7).format('YYYY/MM/DD');
                                    $scope.leftTimes = 1;
                                }
                            })
                        }
                    })
                }
                else if($stateParams.action=="charge") {
                    $scope.extendMember = "false";
                } else if($stateParams.action=="showInfo") {
                    $scope.extendMember = "showInfo";
                }

                $scope.pageLoading = true;
                ListHospital.save({pageNo: "1", pageSize: "200",orderBy:"1"}, function (data) {
                    $scope.pageLoading = false;
                    $scope.hospitalData = data.hospitalData;
                });
            })

            $scope.buttonAction = function(action) {
                if (action == "IKnow") {
                    history.back();
                }
                else if (action == "charge") {
                    resolveUserLoginStatus.events("memberService",
                        $stateParams.memberType + "," + $stateParams.action + "," + "",
                        "","/titan/pay/patientPay.do?" +
                        "patient_register_service_id=" +
                        "noData" + "&chargePrice=15000","notGo");
                }
            }
    }])
