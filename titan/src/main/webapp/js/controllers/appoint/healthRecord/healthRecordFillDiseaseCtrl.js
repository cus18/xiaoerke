angular.module('controllers', ['ionic']).controller('healthRecordFillDiseaseCtrl', [
    '$scope','$state','$stateParams','addDoctorCase','GetUserLoginStatus'
	,'$location',
    function ($scope,$state,$stateParams,addDoctorCase,GetUserLoginStatus
    		,$location) {
        $scope.title = "填写病情诊断";
        $scope.title0 = "填写病情诊断";
        var id=$stateParams.id;
        $scope.addDoctorCase = function (){
        	var name=$("#parise").val();
        	if(name==""){
        		alert("病情诊断不能为空");
        	}
        	addDoctorCase.get({"PatientRegisterId":id,"caseName":encodeURI(name)}, function (data){
                if(data.state=="true"){
                	$state.go("healthRecordIndex");

					$state.go('healthRecordIndex',{"index":$stateParams.index});
                }
            });
        }

        $scope.$on('$ionicView.enter',function() {
        	 var routePath = "/appointBBBBBB" + $location.path();
        	GetUserLoginStatus.save({routePath:routePath},function(data){
	            if(data.status=="9") {
	                window.location.href = data.redirectURL;
	            }else if(data.status=="8"){
					window.location.href = data.redirectURL+"?targeturl="+routePath;
				}
        	});
        });
    }])
