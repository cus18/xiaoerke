
angular.module('controllers', ['ionic']).controller('healthRecordSelectBabyCtrl', [
    '$scope','$state','$stateParams','getBabyinfoList','GetUserLoginStatus',
    '$location',
    function ($scope,$state,$stateParams,getBabyinfoList,GetUserLoginStatus,
    		$location) {
        $scope.title = "选择宝宝";
        $scope.title0 = "选择宝宝";
        $scope.selectBabyLock = true;//当前宝宝
        $scope.babyInfoList=[];//宝宝信息列表
        $scope.notChangeLock=true;
      /*  选择宝宝*/
        $scope.selectBaby = function(index){
            $scope.babyNum=index;
            var page=$stateParams.page;
            if(page==1){
            	window.location.href="/xiaoerke-wxapp/ap/health#/consultFillInfo/"+index+","+$stateParams.conid;
            }else{
            	$state.go("healthRecordIndex",{index:index});
            }
        };
        //修改宝宝信息
        $scope.updateBaby = function(id){
            //$state.go("healthRecordUpdateBaby",{index:id});
            window.location.href ="ap/appoint?value=251335#/healthRecordUpdateBaby/"+id+","+$stateParams.page+","+$scope.conid;
        };
        $scope.addBaby = function(){
            //$state.go("healthRecordAddBaby");
        	if($stateParams.page=="1"){
            window.location.href ="ap/appoint?value=251334#/healthRecordAddBaby/"+$stateParams.page+","+$stateParams.conid;
        	}else{
        		window.location.href ="ap/appoint?value=251334#/healthRecordAddBaby/"+$stateParams.page+",0";
        	}
        	};
        
        $scope.$on('$ionicView.enter',function() {
        	var routePath = "/ap/appointBBBBBB" + $location.path();
        	GetUserLoginStatus.save({routePath:routePath},function(data){
	            if(data.status=="9") {
	                window.location.href = data.redirectURL;
	            }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    $scope.babyNum=$stateParams.index;
                    var pageIndex=$stateParams.page;
                    if(pageIndex=="1"){
                        $scope.notChangeLock=false;
                    }
                    //获取用户下宝宝信息列表
                    getBabyinfoList.save({"openId":""},function (data){
                        $scope.babyInfoList=data.babyInfoList;
                    });
                }
        	});
        
        });
    }])
