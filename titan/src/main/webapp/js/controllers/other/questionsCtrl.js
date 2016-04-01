angular.module('controllers', ['ionic']).controller('QuestionsCtrl', [
        '$scope','$state',
        function ($scope,$state) {

            $scope.title = "常见问题"
            $scope.show =function(index){
                $(".item .content").eq(index).toggle();
            }
            $scope.select2 =function(){
                if( $scope.lock2 == "1"){
                    $scope.lock1 = "1"
                    $scope.lock2 = "0"
                }
                else{
                    $scope.lock1 = "0"
                    $scope.lock2 = "1"
                }
            }

            $scope.appointmentFirst = function(){
                window.location.href="ap/firstPage/appoint";
            }

    }])