angular.module('controllers', ['ionic']).controller('AppointmentFirstCtrl', [
    '$scope', '$state', 'ListHospital', 'ListHospitalByIllnessSecond',
    'ListAppointmentTime', 'resolveUserLoginStatus', 'ListFirstIllness', 'ListSecondIllness', 'PatientSearchList',
    'RemoveAllSearchHistory', 'SendWechatMessageToUser', '$rootScope', 'RecordLogs', 'checkUserOrder', "AutoPrompting",
    function ($scope, $state, ListHospital, ListHospitalByIllnessSecond,
              ListAppointmentTime, resolveUserLoginStatus, ListFirstIllness, ListSecondIllness, PatientSearchList,
              RemoveAllSearchHistory, SendWechatMessageToUser, $rootScope, RecordLogs, checkUserOrder, AutoPrompting) {

        $scope.hospitalList = "true";
        $scope.illnessList = "false";
        $scope.timeList = "false";
        $scope.lightLock = 'false';
        $scope.his = "false";

        $scope.haveOrder = false;
        $scope.pageLoading = true;
        $scope.myActiveSlide = 0;//轮播图初始值
        $scope.info = {};
        $scope.patientSearchList = [];
        $scope.searchObj = "";

        $scope.lightImg = 'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fa_fastEntry_light1.png';
        $scope.choiceItem = "hospitalList";

        $scope.patientList = ['湿疹', '咳嗽', '发烧', '过敏', '感冒', '鼻炎', '眼科', '腹泻', '呕吐'];

        $scope.appointmentFirst = function () {
            window.location.href = "/xiaoerke-appoint/ap";
        };
        //取消特惠医院浮层
        $scope.cancelPreference = function () {
            $scope.PreferenceLock = false;
        }

        $scope.onSwipe = function () {
            $scope.lightLock = 'false';
            $scope.lightImg = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fa_fastEntry_light1.png";
        }

        $scope.searchInPatientList = function (index) {
            if ($scope.patientList[index]) {
                $state.go("searchList", ({
                    "searchName": $scope.patientList[index],
                    "action": 'searchDoctorByOpenSearch', "titleName": $scope.patientList[index]
                }));
            }
        }

        $scope.searchInPatientSearchList = function (index) {
            if ($scope.patientSearchList[index]) {
                $state.go("searchList", ({
                    "searchName": $scope.patientSearchList[index].idurlname,
                    "action": 'searchDoctorByOpenSearch', "titleName": $scope.patientSearchList[index].idurlname
                }));
            }
        }

        $scope.seaHis = function () {
            $scope.his = "true";
            PatientSearchList.save({}, function (data) {
                $scope.patientSearchList = data.patientSearchList;
            })
        }

        $scope.cancel = function () {
            $scope.his = "false";
        }

        $scope.selectIllnessDetail = function (illnessName) {
            $scope.pageLoading = true;
            ListSecondIllness.save({illnessName: illnessName}, function (data) {
                $scope.pageLoading = false;
                $scope.illnessSecondData = data.illnessListData;
            })
            $scope.selectItemName = illnessName;
            $scope.myStyle = {'background-color': '#ccc'};
        }

        $scope.choiceList = function (item) {
            $scope.choiceItem = item;
            if (item == "hospitalList") {
                $scope.hospitalList = "true";
                $scope.illnessList = "false";
                $scope.timeList = "false";
            }
            else if (item == "illnessList") {
                $scope.pageLoading = true;
                ListFirstIllness.save({"pageNo": "1", "pageSize": "100", "orderBy": "0"}, function (data) {
                    $scope.pageLoading = false;
                    $scope.illnessData = data.illnessData;
                    $scope.selectIllnessDetail(data.illnessData[0].illnessName);
                });
                $scope.hospitalList = "false";
                $scope.illnessList = "true";
                $scope.timeList = "false";
            }
            else if (item == "timeList") {
                $scope.pageLoading = true;
                ListAppointmentTime.save({pageNo: "1", pageSize: "7", orderBy: "1"}, function (data) {
                    $scope.pageLoading = false;
                    $scope.timeData = data.timeData;
                });
                $scope.hospitalList = "false";
                $scope.illnessList = "false";
                $scope.timeList = "true";
            }
        }

        $scope.appointmentFirst = function () {
            window.location.href = "/xiaoerke-appoint/ap";
        }

        $scope.searchDoc = function () {
            if ($scope.searchObj) {
                $state.go("searchList", ({
                    "searchName": $scope.searchObj, "action": 'searchDoctorByOpenSearch',
                    "titleName": $scope.searchObj
                }));
            }
        }

        $scope.removeAllSearchHistory = function () {
            RemoveAllSearchHistory.save({}, function (data) {
            })
            $scope.patientSearchList = []
        }

        $scope.light = function () {
            if ($scope.lightLock == 'true') {
                $scope.lightImg = 'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fa_fastEntry_light1.png';
                $scope.lightLock = 'false';
            }
            else {
                $scope.lightLock = 'true';
                $scope.lightImg = 'http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fa_fastEntry_light2.png';
            }
        }

        $scope.myAppointment = function () {
            RecordLogs.save({logContent: "快速入口-我的预约"}, function () {
            })
            resolveUserLoginStatus.events("myAppointment","","","","go");
        }

        $scope.consultDoc = function (data) {
            if (data == "home") {
                RecordLogs.save({logContent: "首页快速入口-咨询医生"})
            } else {
                RecordLogs.save({logContent: "快速入口-咨询医生"})
            }

            SendWechatMessageToUser.save({}, function (data) {
            });
            WeixinJSBridge.call('closeWindow');
        }

        $scope.goToMemberService = function () {
            RecordLogs.save({logContent: "首页banner-购会员买"})
            $state.go("memberService", {memberType: "week", action: "charge", register_service_id: ""});
        }

        $scope.goToCooperationHospital = function (hospitalId) {
            $state.go("cooperationHospital", {hospitalId: hospitalId});
            RecordLogs.save({logContent: "特惠医院轮询图:" + hospitalId})
        }


        $scope.toKnowledgeIndex = function (data) {
            RecordLogs.save({logContent: "首页郑玉巧育儿经"})
            $state.go("knowledgeIndex");

        }

        $scope.opinionFeedback = function () {
            $state.go("feedback");
            RecordLogs.save({logContent: "快速入口-意见反馈"}, function () {
            })
        }

        $scope.contactCustomer = function () {
            RecordLogs.save({logContent: "快速入口-联系客服"}, function () {
            })
        }

        ListHospital.save({pageNo: "1", pageSize: "200", orderBy: "1"}, function (data) {
            $scope.pageLoading = false;
            $scope.hospitalData = data.hospitalData;
        });

        checkUserOrder.save({unBindUserPhoneNum: $rootScope.unBindUserPhoneNum}, function (data) {
            if (data.haveOrder) {
                $scope.haveOrder = data.haveOrder
            }
        });

        $scope.get_cookie = function (Name) {
            var search = Name + "="
            var returnvalue = ""
            if (document.cookie.length > 0) {
                offset = document.cookie.indexOf(search)
                if (offset != -1) {
                    offset += search.length
                    end = document.cookie.indexOf(";", offset)
                    if (end == -1)
                        end = document.cookie.length;
                    returnvalue = unescape(document.cookie.substring(offset, end))
                }
            }
            return returnvalue;
        }

        $scope.doRefresh = function () {
            if (get_cookie("droppedin") == "") {
                $scope.PreferenceLock = true;//特惠医院
                var oDate = new Date();
                oDate.setDate(oDate.getDate() + 30 * 60 * 1000);
                document.cookie = 'droppedin=yes;expires=' + oDate.toGMTString();
            } else {
                var oDate = new Date();
                oDate.setDate(oDate.getDate() + 30 * 60 * 1000);
                document.cookie = 'droppedin=yes;expires=' + oDate.toGMTString();
            }

        }

        $scope.goToHospitalList = function (searchName, action, titleName) {
            if (searchName == '626487c34f954abd90e188bb40341995' || searchName == 'faa875abfe9748438111aa208151f1ed') {
                $state.go("cooperationHospital", {hospitalId: searchName});
                RecordLogs.save({logContent: "特惠医院列表:" + searchName})
            } else {
                $state.go("searchList", {searchName: searchName, action: action, titleName: titleName});
            }
        }

        $scope.autoPrompt = function () {
            AutoPrompting.save({queryStr: $scope.info.search}, function (data) {
            });
        }
    }])


function get_cookie(Name) {
    var search = Name + "="
    var returnvalue = ""
    if (document.cookie.length > 0) {
        offset = document.cookie.indexOf(search)
        if (offset != -1) {
            offset += search.length
            end = document.cookie.indexOf(";", offset)
            if (end == -1)
                end = document.cookie.length;
            returnvalue = unescape(document.cookie.substring(offset, end))
        }
    }
    return returnvalue;
}