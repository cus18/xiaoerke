
angular.module('controllers', ['ionic']).controller('healthRecordUpdateBabyCtrl', [
    '$scope','$state','$stateParams','$ionicPopup','getBabyinfoList',
    'GetUserLoginStatus','$location','updateBabyInfo','deleteBabyInfo',
    function ($scope,$state,$stateParams,$ionicPopup,getBabyinfoList
    		,GetUserLoginStatus,$location,updateBabyInfo,deleteBabyInfo) {
        $scope.title = "修改宝宝信息";
        $scope.title0 = "修改宝宝信息";
        $scope.boyLock = true;
        $scope.girlLock = false;
        var listIndex="";
        $scope.babyInfo={};
        $scope.baby={};

       /* 选择性别*/
        $scope.selectSex = function(sex){
            if(sex=="boy"){
                $scope.boyLock = true;
                $scope.girlLock = false;
            }
            else{
                $scope.boyLock = false;
                $scope.girlLock = true;
            }
        };
        /*保存宝宝信息*/
        $scope.saveBabyInfo = function(){
        	var name=$scope.baby.name;
        	var birthday=$("#birthday").val();
			var sex=$scope.boyLock == true?1:0;
        	var id=parseInt($scope.baby.id);
        	if(typeof(name) == "undefined"){
        		alert("姓名不能为空");
        		return;
        	}
        	if(birthday == ""){
        		alert("请选择宝宝生日");
        		return;
        	}
        	updateBabyInfo.get({"id":id,"sex":sex,"name":encodeURI(name),"birthDay":birthday}, function (data){
                if(data.resultCode=='1'){
                	if($stateParams.pageIndex=="1"){
                	window.location.href="/xiaoerke-wxapp/ap/health#/consultFillInfo/"+$stateParams.index+","+$scope.conid;
                }else  if($stateParams.pageIndex=='2'){
                	$state.go("healthRecordIndex",{index:$stateParams.index});
                }else  {
                	$state.go("healthRecordSelectBaby",{index:$stateParams.index});
                }
                }
            });
        };
        $scope.deleteBabyInfo = function() {
            var confirmPopup = $ionicPopup.confirm({
                title: '<br><br>是否确认删除宝宝信息？<br><br>',
                buttons: [
                    { text: '<b class="confirm-btn">是</b>',
                        type: 'button-calm ',
                        onTap: function(e) {
                        	var id=parseInt($scope.baby.id);
                        	deleteBabyInfo.get({"id":id}, function (data){
                                if(data.resultCode=='1'){
                                	$state.go("healthRecordSelectBaby",{index:0})
                                }
                            });
                        }
                    },
                    { text: '<b class="confirm-btn">否</b>',
                        type: 'button-calm',
                    }
                ]
            });
        }
        $scope.$on('$ionicView.enter',function() {
        	var routePath = "/appointBBBBBB" + $location.path();
        	GetUserLoginStatus.save({routePath:routePath},function(data){
	            if(data.status=="9") {
	                window.location.href = data.redirectURL;
	            } else if(data.status=="8"){
					window.location.href = data.redirectURL+"?targeturl="+routePath;
				}else {
					var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
	            	$("#birthday").mobiscroll().date();
	                //var currYear = (new Date()).getFullYear();
	                //初始化日期控件
	                var opt = {
	                    preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
	                    theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
	                    display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
	                    mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
	                    lang:'zh',
	                    dateFormat: 'yyyy-mm-dd', // 日期格式
	                    setText: '确定', //确认按钮名称
	                    cancelText: '取消',//取消按钮名籍我
	                    dateOrder: 'yyyymmdd', //面板中日期排列格式
	                    dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
	                    showNow: false,
	                    nowText: "今",
	                    //startYear:1980, //开始年份
	                    //endYear:currYear //结束年份
						minDate: new Date(1980,0,1),
						maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10))
	                    //endYear:2099 //结束年份
	                };
	                $("#birthday").mobiscroll(opt);
	                listIndex=$stateParams.index;
	                getBabyinfoList.save({"openId":""}, function (data){
	                	$scope.babyInfo=data.babyInfoList[listIndex];
	                	$scope.baby.name=data.babyInfoList[listIndex].name;
	                	$scope.baby.id=data.babyInfoList[listIndex].id;
	        			if(data.babyInfoList[listIndex].sex=='1'){
	        				$scope.boyLock = true;
	                        $scope.girlLock = false;
	        			}else{
	        				$scope.boyLock = false;
	                        $scope.girlLock = true;
	        			}
//	    				$("#birthday").val(data.babyInfoList[listIndex].birthday);
	                    });
	            }
        	});
        });
    }])
