angular.module('controllers', ['ionic']).controller('constipationQuestionsCtrl', [
    '$scope','$state','$stateParams','GetQuestion','GetUserLoginStatus','$location','$http',
    function ($scope,$state,$stateParams,GetQuestion,GetUserLoginStatus,$location,$http) {

       /* $scope.pageLoading =true;*/
        $scope.img1 ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir1.png" ;
        $scope.img2 ="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Fcir1_cur.png" ;

        var pData = {logContent:encodeURI("BMGL_41")};
        $http({method:'post',url:'util/recordLogs',params:pData});

        $scope.questions = [
            { text: "大便有点干", checked: false },
            { text: "连续三天未排便", checked: false },
            { text: "有严重肛裂/排便疼痛", checked: false }
        ];
        $scope.text=$scope.questions[0].text;
        $scope.constipationSelect=function(index,text){
            $scope.text=text;
            if(index==0){
                getQuestion("轻");
                $state.go('constipationLight');
            }
            else if(index==1){
                getQuestion("中");
                $state.go('constipationMedium');
            }
            else if(index==2){
                getQuestion("重");
                $state.go('constipationWeight');
            }
        }

        function getQuestion(type){
            GetQuestion.save({"type":type},function(data){
            })
        }

        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/ap/ctpBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }
            })
        })
    }]);

