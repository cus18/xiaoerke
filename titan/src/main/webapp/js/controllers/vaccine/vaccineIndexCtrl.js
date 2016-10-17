
angular.module('controllers', ['ionic']).controller('vaccineIndexCtrl', [
    '$scope','$http','$stateParams','SaveBabyVaccine','GetVaccineStation',
    function ($scope,$http,$stateParams,SaveBabyVaccine,GetVaccineStation) {
        $scope.info = {
            babyName: "",
            babyNum: ""
        };
        $scope.openId = $stateParams.openId;
        $scope.QRCode = $stateParams.QRCode;
        $scope.sexItem = "";
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
        //获取我的回复列表
        GetVaccineStation.save({}, function (data) {
            $scope.vaccineStationType = data.vaccineStationInfo;
            console.log("$scope.vaccineStationType", $scope.vaccineStationType)
        });

        $scope.isSelectedG = true;
        $scope.isSelectedB = false;
        //选择孩子性别
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

        $(".select-area .select-value").each(function() {
            if ($(this).next("select").find("option:selected").length != 0) {
                $(this).text($(this).next("select").find("option:selected").text());
            }
        });
        $(".select-area select").change(function() {
            var value = $(this).find("option:selected").text();
            $(this).parent(".select-area").find(".select-value").text(value);
        });
        $scope.submit = function() {
            if ($scope.info.babyName == "") {
                alert("宝宝姓名不能为空！");
                return;
            }
            if ($scope.sexItem == null) {
                alert("宝宝性别不能为空！");
                return;
            }
            if ($("#babyBirthday").val() == "") {
                alert("宝宝生日不能为空！");
                return;
            }
            if ($scope.info.babyNum == "") {
                alert("宝宝接种编号不能为空！");
                return;
            }
            if ($scope.info.vaccineStation == undefined) {
                alert("宝宝疫苗站不能为空！");
                return;
            }
            var information = {
                "openId": $scope.openId,
                "birthday": $("#babyBirthday").val(),
                "name": encodeURI(encodeURI($scope.info.babyName)),
                "sex": $scope.sexItem,
                "QRCode": $scope.QRCode,
                "babySeedNumber": $scope.info.babyNum,
                "vaccineStationId": $scope.info.vaccineStation.vaccineStationId,
                "vaccineStationName":encodeURI(encodeURI($scope.info.vaccineStation.vaccineStationName))
            };
            SaveBabyVaccine.save({
                openId:$scope.openId,
                name: encodeURI(encodeURI($scope.info.babyName)),
                sex: $scope.sexItem,
                QRCode: $scope.QRCode,
                babySeedNumber: $scope.info.babyNum,
                vaccineStationId: $scope.info.vaccineStation.vaccineStationId,
                vaccineStationName:encodeURI(encodeURI($scope.info.vaccineStation.vaccineStationName))
            }, function (data) {
                console.log(data);
                if (data.status == "success") {
                    wx.closeWindow();
                } else if (data.status == "failure") {
                    alert("宝宝信息保存失败！")
                } else if (data.status == "UserInfoAlready") {
                    alert("宝宝信息已存在！")
                }
            });
        };

    }])

