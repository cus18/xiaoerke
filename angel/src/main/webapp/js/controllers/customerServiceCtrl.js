angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('customerServiceCtrl',['$scope','ConsultCustomOnly',
        function ($scope,ConsultCustomOnly) {
            $scope.customerServiceInit = function () {
            };
            $scope.doctorConsultPay = function () {
                window.location.href="http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=doctorConsultPay"
            };
            $scope.closeCustomerService = function () {

                ConsultCustomOnly.get({},function () {
                    wx.closeWindow();
                })
            };

            var recordLogs = function(val){
                $.ajax({
                    url:"util/recordLogs",// 跳转到 action
                    async:true,
                    type:'get',
                    data:{logContent:encodeURI(val)},
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                    },
                    error : function() {
                    }
                });
            };

            recordLogs("consult_chargetest_once_information_cs");
        }])

