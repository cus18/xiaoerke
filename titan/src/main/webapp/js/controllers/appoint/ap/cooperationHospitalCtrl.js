angular.module('controllers', ['ionic']).controller('CooperationHospitalCtrl', [
    '$scope','$state','$stateParams','GetCooperationHospitalInfo','$http','$ionicSlideBoxDelegate',
        function ($scope,$state,$stateParams,GetCooperationHospitalInfo,$http, $ionicSlideBoxDelegate) {

            $scope.haveOrder = false;
            $scope.pageLoading = false;
            $scope.hospitalMoreLock = false;
            $scope.projectMoreLock = false;
            $scope.hospitalMoreText = "查看详情";
            $scope.hospitalMoreImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_down.png";
            $scope.projectMorImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_down.png";
            $scope.week = ['今天','明天','后天','3天后','4天后','5天后','6天后'];

            $scope.hospitalBannerText=[
                "1",
                "2",
                "3"
            ]

            $scope.hospitalMore = function(){
                if(  $scope.hospitalMoreLock == false){
                    $scope.hospitalMoreLock = true;
                    $scope.hospitalMoreText = "收起";
                    $("dl .dd2").addClass("detail");
                    $scope.hospitalMoreImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_up.png";
                }
                else{
                    $scope.hospitalMoreLock = false;
                    $scope.hospitalMoreText = "查看详情";
                    $("dl .dd2").removeClass("detail");
                    $scope.hospitalMoreImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_down.png";
                }
            };
            $scope.projectMore = function(){
                if(  $scope.projectMoreLock == false){
                    $scope.projectMoreLock = true;
                    $scope.projectMorImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_up.png";
                }
                else{
                    $scope.projectMoreLock = false;
                    $scope.projectMoreText = "查看详情";
                    $scope.projectMorImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farr_blue_down.png";
                }
            };

            $scope.doctorAppointment = function(doctorId,available_time,hospitalName,location,position,location_id){
                if(available_time>=0&&available_time<7)
                {
                    $state.go("doctorAppointment", {
                        doctorId: doctorId, available_time: available_time,
                        hospitalName: hospitalName, location: location, position: position,
                        mark:"dateAvailable",location_id:location_id,attentionDoctorId:""
                    });
                }
                else
                {
                    $state.go("doctorAppointment", {
                        doctorId: doctorId, available_time: available_time,
                        hospitalName: hospitalName, location: location, position: position,
                        mark:"dateNoAvailable",location_id:location_id,attentionDoctorId:""
                    });
                }
            }

            $scope.$on('$ionicView.enter',function() {
                $scope.hospitalId = $stateParams.hospitalId;
                GetCooperationHospitalInfo.get({hospitalId:$stateParams.hospitalId},function(data){
                    $scope.hospitalInfo = data.hospitalInfo;
                    $scope.doctorList = data.doctorDataVo;
                    if( $scope.hospitalId=="faa875abfe9748438111aa208151f1ed"){
                        $scope.hospitalBannerImg=[
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/jialisan01.png",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/jialisan02.png",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/jialisan03.png"
                        ]
                    }
                    else if( $scope.hospitalId=="626487c34f954abd90e188bb40341995"){
                        $scope.hospitalBannerImg=[
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/dingfuzhuang01.png",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/dingfuzhuang02.png",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/dingfuzhuang03.png"

                        ]
                    }
                    else if( $scope.hospitalId=="508c345c3ddb4d25b1551cf7ff55031f"){
                        $scope.hospitalBannerImg=[
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fbalizhuang1.jpg",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fbalizhuang2.jpg",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fbalizhuang3.jpg"


                        ]
                    }
                    else if( $scope.hospitalId=="dda909f2fd79403b9a47b63ce11fd5a4"){
                        $scope.hospitalBannerImg=[
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fdongqu1.jpg",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fdongqu2.jpg",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fdongqu3.jpg"

                        ]
                    }

                    else if( $scope.hospitalId=="c1e4456b874d44208b58f1fa72e24e96"){
                        $scope.hospitalBannerImg=[
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fqingmiao1.jpg",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fqingmiao2.jpg",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fqingmiao3.jpg"


                        ]
                    }
                    else if( $scope.hospitalId=="e6845738a532467d8257a0f69a18903c"){
                        $scope.hospitalBannerImg=[
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fjimei1.jpg",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fjimei2.jpg",
                            "http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/coHospital%2Fjimei3.jpg"


                        ]
                    }
                    $ionicSlideBoxDelegate.update();

                })

                var pData = {logContent:encodeURI("我的test")};
                $http({method:'post',url:'util/recordLogs',params:pData});
            })
    }])