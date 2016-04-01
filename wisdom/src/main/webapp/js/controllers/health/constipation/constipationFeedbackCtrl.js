angular.module('controllers', ['ionic']).controller('constipationFeedbackCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        $scope.feedback = [
            { text: "有好转，继续坚持", checked: true },
            { text: "未见好转，没有变化", checked: false },
            { text: "更加严重了", checked: false }
        ];
        $scope.feedbackSelect=function(index){
            if(index==0){
                $state.go('constipationIndex');
            }
            else if(index==1){
                $state.go('constipationEmergency');
            }
            else if(index==2){
                $state.go('constipationEmergency');
            }

        }


    }]);

