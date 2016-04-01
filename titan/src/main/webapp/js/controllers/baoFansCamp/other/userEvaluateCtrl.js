angular.module('controllers', ['ionic']).controller('userEvaluateCtrl', [
        '$scope','$state','$stateParams','$filter','CustomerEvaluation','GetUserLoginStatus','$location',
        function ($scope,$state,$stateParams,$filter,CustomerEvaluation,GetUserLoginStatus,$location) {

            $scope.date =  $stateParams.date;
            $scope.parseInt = parseInt;
            $scope.starNum1="5"
            $scope.starNum2="5"
            $scope.starNum3="5"
            $scope.info = {};

            $scope.skip = function(){
                $('html,body').animate({scrollTop:$('#pp').offset().top}, 0);
            }
            $scope.selectStar1 = function(starIndex){
                $scope.starNum1 = parseInt(starIndex) + parseInt(1);
                $(".star-list1 img").eq(starIndex).attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star_select.png");
                $(".star-list1  img").eq(starIndex).siblings().attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star.png");
                $(".star-list1  img").eq(starIndex).prevAll().attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star_select.png");
            }
            $scope.selectStar2 = function(starIndex){
                $scope.starNum2 = parseInt(starIndex) + parseInt(1);
                $(".star-list2 img").eq(starIndex).attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star_select.png");
                $(".star-list2  img").eq(starIndex).siblings().attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star.png");
                $(".star-list2  img").eq(starIndex).prevAll().attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star_select.png");
            }
            $scope.selectStar3 = function(starIndex){

                $scope.starNum3 = parseInt(starIndex) + parseInt(1);
                $(".star-list3 img").eq(starIndex).attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star_select.png");
                $(".star-list3  img").eq(starIndex).siblings().attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star.png");
                $(".star-list3  img").eq(starIndex).prevAll().attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Frank_star_select.png");
            }
        $scope.submitEvaluation = function(){
          CustomerEvaluation.save({starNum1:$scope.starNum1,starNum2:$scope.starNum2,starNum3:$scope.starNum3,doctorId:$stateParams.doctorId,content:$scope.info.content},function(data){
              if(data =="true"){
                alert("感谢评价");
              }
              WeixinJSBridge.call('closeWindow');
         });
       }

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/appointBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    }
                })
            })
    }])