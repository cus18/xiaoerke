
angular.module('controllers', ['ionic']).controller('consultHistoryCtrl', [
    '$scope','$state','$stateParams','$ionicPopup','getCustomerLogByOpenID',
    'GetUserLoginStatus','$location','updateConversationInfo',
    function ($scope,$state,$stateParams,$ionicPopup,getCustomerLogByOpenID,
    		GetUserLoginStatus,$location,updateConversationInfo) {
        $scope.title0 = "历史咨询";
        $scope.title = "历史咨询";
        $scope.logList=[];
        

       /* 取消宝宝信息完善*/
        $scope.cancel = function(){
            $scope.fillInfoLock = false;
        };
        //是否开启历史咨询
        $scope.startConsultHistory = function(id) {
            var confirmPopup = $ionicPopup.confirm({
                title: '<br><br>确定针对本次病情继续咨询吗？<br><br>',
                buttons: [
                    { text: '<b class="confirm-btn">确定</b>',
                        type: 'button-calm ',
                        onTap: function(e) {
                        	updateConversationInfo.get({"id":id},function (data){
                                if(data.state!=""){
                                	WeixinJSBridge.call('closeWindow');
                                }
                            });
//                            window.location.href = "ap/logout";
                        	
                        }
                    },
                    { text: '<b class="confirm-btn">取消</b>',
                        type: 'button-calm',
                    }
                ]
            });
        }

        $scope.$on('$ionicView.enter',function() {
        	 var routePath = "/healthBBBBBB" + $location.path();
             GetUserLoginStatus.save({routePath:routePath},function(data){
                 $scope.pageLoading = false;
                 if(data.status=="9") {
                     window.location.href = data.redirectURL;
                 }else{
                    //根据宝宝信息获取
                    getCustomerLogByOpenID.save({},function (data){
                        if(data.logList!=""){
                            $scope.logList = data.logList;
                        }
                    });
                 }
            });
        });
    }])
