angular.module('controllers', ['ionic']).controller('antiDogHospitalCtrl', [
    '$scope','$state','$stateParams','getInsuranceHospitalListByInfo','$ionicScrollDelegate',
    function ($scope,$state,$stateParams,getInsuranceHospitalListByInfo,$ionicScrollDelegate) {

        $scope.num = "";
        $scope.openImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farrow_blue_down.png";
        $scope.openImg1 = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farrow_blue_up.png";
        $scope.openImg2 = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Farrow_blue_down.png";
        $scope.telList ={};
       /* $scope.hospitalList =[
            {
                district:"朝阳区",
                hospital:[
                    {
                        name:"北京市朝阳区常营社区卫生服务中心",
                        position:"朝阳区常营乡柏林爱乐小区西门",
                        tel:"65435960-8019 65435904"

                    },
                    {
                        name:"北京市朝阳区中医医院",
                        position:"朝阳区工体南路6号",
                        tel:"65531155-2101/2121"
                    },
                    {
                        name:"北京和睦家妇婴医疗保健中心",
                        position:"朝阳区将台路2号",
                        tel:"59277120 59277000"
                    }

                ]
            },
            {
                district:"海淀区",
                hospital:[
                    {
                        name:"海军总医院",
                        position:"海淀区阜城路6号",
                        tel:"66951344"

                    },
                    {
                        name:"北京四季青医院",
                        position:"海淀区远大路32号",
                        tel:"88446071-8025"
                    },
                    {
                        name:"中国人民解放军第三一六医院",
                        position:"海淀区香山路娘娘府甲2号",
                        tel:"66320242"
                    }

                ]
            },
            {
                district:"东城区",
                hospital:[
                    {
                        name:"东城区第一人民医院",
                        position:"东城区永定门外大街130号",
                        tel:"67221756、67212233-3051"

                    },
                    {
                        name:"北京市和平里医院",
                        position:"东城区和平里北街18号",
                        tel:"58043127"
                    },
                    {
                        name:"北京市第六医院",
                        position:"东城区交道口北二条36号",
                        tel:"64035566-3219"
                    }

                ]
            },
            {
                district:"西城区",
                hospital:[
                    {
                        name:"北京大学人民医院",
                        position:"西城区西直门南大街11号",
                        tel:"67221756、67212233-3051"

                    },
                    {
                        name:"北京市回民医院",
                        position:"西城区右安门内大街11号",
                        tel:"83912537"
                    }

                ]
            },
            {
                district:"丰台区",
                hospital:[
                    {
                        name:"丰台医院（北院）",
                        position:"丰台区丰台镇西安街头条1号",
                        tel:"63811115-2462"

                    },
                    {
                        name:"丰台医院（南院）",
                        position:"丰台区丰台南路99号",
                        tel:"63811115-2461"
                    },
                    {
                        name:"南苑医院",
                        position:"丰台区南苑街道公所胡同3号",
                        tel:"67991313-80006"
                    }

                ]
            },
        ];*/

        $scope.districtList =[
            {
                district:"海淀"
            },
            {
                district:"朝阳"
            },
            {
                district:"东城"
            },
            {
                district:"西城"
            },
            {
                district:"石景山"
            },
            {
                district:"丰台"
            },
            {
                district:"门头沟"
            },
            {
                district:"房山"
            },
            {
                district:"通州"
            },
            {
                district:"顺义"
            },
            {
                district:"大兴"
            },
            {
                district:"昌平"
            },
            {
                district:"平谷"
            },
            {
                district:"怀柔"
            },
            {
                district:"密云"
            },
            {
                district:"延庆"
            }
        ];

        $scope.openMore = function(index){
          /* var divScroll= document.getElementById(index).offsetTop;*/
            getInsuranceHospitalListByInfo.save({"district":index+''}, function (data){
                if(data.insurance!=''||data.insurance!=null){
                    $scope.hospitalList=data.insurance;
                   /* for(var i=0;i< $scope.hospitalList.length;i++){
                        $scope.hospitalList[i].tel=$scope.hospitalList[i].phone.split(";");
                       console.log("phone "+$scope.hospitalList[i].phone);
                       console.log("tel "+$scope.hospitalList[i].tel);
                     }*/
                }

            });
           if( $(".hospital dt img").eq(index).attr("src")== $scope.openImg){
               $scope.selectItem=index;
               $(".hospital dt img").attr("src",$scope.openImg2);
               $(".hospital dt img").eq(index).attr("src",$scope.openImg1);

              /* $("body,html").stop().animate({"scrollTop":divScroll},0);*/
           }
            else{
               $scope.selectItem=-1;
               $(".hospital dt img").eq(index).attr("src",$scope.openImg2);
           }

        };



        $scope.$on('$ionicView.enter', function(){

        })
    }]);

