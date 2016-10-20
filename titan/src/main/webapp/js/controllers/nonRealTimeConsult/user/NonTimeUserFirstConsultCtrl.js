angular.module('controllers', ['ngFileUpload']).controller('NonTimeUserFirstConsultCtrl', [
        '$scope','$upload','$state','$stateParams','$timeout','$http','BabyBaseInfo','CreateSession',
        function ($scope,$upload,$state,$stateParams,$timeout,$http,BabyBaseInfo,CreateSession) {
            $scope.info = {
                describeIllness:""
            };
            $scope.photoList = [];
            $scope.sexItem = 0;
            $scope.isSelectedB = true;
            $scope.isSelectedG = false;
            $scope.NonTimeUserFirstConsultInit = function(){
                // 获取宝宝基本信息
                BabyBaseInfo.save({},function (data) {
                    $scope.sexItem = data.babySex;
                    if ($scope.sexItem == 0) {
                        $scope.isSelectedB = true;
                        $scope.isSelectedG = false;
                    }
                    if ($scope.sexItem == 1) {
                        $scope.isSelectedG = true;
                        $scope.isSelectedB = false;
                    }
                    if(data.babyBirthDay != ""){
                        $("#babyBirthday").val(data.babyBirthDay)
                    }
                    })
            };
            $scope.deletePhoto = function(index){
                $scope.photoList.splice(index, 1);
            };
            $scope.selectSex = function(sex) {
                $scope.sexItem = sex;
                if ($scope.sexItem == 0) {
                    $scope.isSelectedB = true;
                    $scope.isSelectedG = false;
                }
                if ($scope.sexItem == 1) {
                    $scope.isSelectedG = true;
                    $scope.isSelectedB = false;
                }
            };
            $scope.showInput = function() {
                var date = new Date(+new Date() + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '');
                $("#babyBirthday").mobiscroll().date();
                //初始化日期控件
                var opt = {
                    preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                    theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
                    display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
                    mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
                    lang: 'zh',
                    dateFormat: 'yyyy-mm-dd', // 日期格式
                    setText: '确定', //确认按钮名称
                    cancelText: '取消', //取消按钮名籍我
                    dateOrder: 'yyyymmdd', //面板中日期排列格式
                    dayText: '日',
                    monthText: '月',
                    yearText: '年', //面板中年月日文字
                    showNow: false,
                    nowText: "今",
                    startYear: 1960, //开始年份
                    //endYear:currYear //结束年份
                    /*minDate: new Date(2002,date.substring(5,7)-1,date.substring(8,10)),*/
                    maxDate: new Date(date.substring(0, 4), date.substring(5, 7) - 1, date.substring(8, 10))
                    //endYear:2099 //结束年份
                };
                $("#babyBirthday").mobiscroll(opt);
                $("#babyBirthday").mobiscroll("show");
            };
            //提交图片
            $scope.uploadFiles = function($files,fileType) {
                console.log('dataJsonValue');
                for (var i = 0; i < $files.length; i++) {
                    var file = $files[i];
                    $scope.upload = $upload.upload({
                        url: 'nonRealTimeConsultUser/uploadMediaFile',
                        file: file
                    }).progress(function(evt) {
                        console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
                    }).success(function(data, status, headers, config){
                        $scope.photoList.push(data.imgPath)
                    });
                }
            };
            $scope.submit = function(){
                var information = {
                    "csUserId":$stateParams.doctorId,
                    "sex": $scope.sexItem+"",
                    "birthday": $("#babyBirthday").val(),
                    "describeIllness": encodeURI(encodeURI($scope.info.describeIllness)),
                    "imgList":$scope.photoList
                };
                CreateSession.save(information,function (data) {
                    console.log(data)
                })
            };
    }]);
