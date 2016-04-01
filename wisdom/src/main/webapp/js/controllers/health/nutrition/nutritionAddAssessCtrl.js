angular.module('controllers', ['ionic']).controller('nutritionAddAssessCtrl', [
    '$scope','$state','$stateParams','GetEvaluate',
    function ($scope,$state,$stateParams,GetEvaluate) {

        $scope.scrollNum=0;
        $scope.noNext = "true";


        $scope.banner =function(){

        };


        $scope.$on('$ionicView.enter', function(){
            var num = 0;
            if ($stateParams.type == "oilSalt") {
                $scope.title = "油类";
                if($stateParams.weight=="less"){
                    $scope.talk = "宝宝吃的不够！";
                    $scope.ping = "宝宝摄入的食用油偏少哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(-8deg)");
                }else if($stateParams.weight=="more"){
                    $scope.talk = "宝宝吃的过多！";
                    $scope.ping = "宝宝的食用油和盐摄入量偏多哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(8deg)");
                }else{
                    $scope.talk = "宝宝吃的正好！";
                    $scope.ping = "宝宝的食用油和盐摄入量适宜，为宝宝点个赞！";
                }

            }
            if ($stateParams.type == "milk") {
                $scope.title = "奶类";
                if($stateParams.weight=="less"){
                    $scope.talk = "宝宝吃的不够！";
                    $scope.ping = "宝宝奶类摄入量不足哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(-8deg)");
                }else if($stateParams.weight=="more"){
                    $scope.talk = "宝宝吃的过多！";
                    $scope.ping = "宝宝奶类摄入太多了哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(8deg)");
                }else{
                    $scope.talk = "宝宝吃的正好！";
                    $scope.ping = "宝宝奶类食物摄入量适宜，为宝宝点个赞！";
                }
            }
            if ($stateParams.type == "fishEggs") {
                $scope.title = "鱼禽肉蛋";
                if($stateParams.weight=="less"){
                    $scope.talk = "宝宝吃的不够！";
                    $scope.ping = "鱼禽肉蛋类食物摄入有点少哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(-8deg)");
                }else if($stateParams.weight=="more"){
                    $scope.talk = "宝宝吃的过多！";
                    $scope.ping = "宝宝鱼禽肉蛋类食物摄入有点多哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(8deg)");
                }else{
                    $scope.talk = "宝宝吃的正好！";
                    $scope.ping = "宝宝鱼禽肉蛋类食物摄入量适宜，为宝宝点个赞！";
                }
            }
            if ($stateParams.type == "meat") {
                $scope.title = "水果类";
                if($stateParams.weight=="less"){
                    $scope.talk = "宝宝吃的不够！";
                    $scope.ping = "宝宝水果类食物摄入不足哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(-8deg)");
                }else if($stateParams.weight=="more"){
                    $scope.talk = "宝宝吃的过多！";
                    $scope.ping = "宝宝水果吃的有点多哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(8deg)");
                }else{
                    $scope.talk = "宝宝吃的正好！";
                    $scope.ping = "宝宝水果类食物摄入量适宜，为宝宝点个赞！";
                }
            }
            if ($stateParams.type == "milletandpotato") {
                $scope.title = "谷薯类";
                if($stateParams.weight=="less"){
                    $scope.talk = "宝宝吃的不够！";
                    $scope.ping = "宝宝谷薯类食物摄入不足哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(-8deg)");
                }else if($stateParams.weight=="more"){
                    $scope.talk = "宝宝吃的过多！";
                    $scope.ping = "宝宝谷薯类食物吃的有点多哦，小心宝宝变成小胖墩哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(8deg)");
                }else{
                    $scope.talk = "宝宝吃的正好！";
                    $scope.ping = "宝宝谷薯类食物摄入量适宜，为宝宝点个赞！";
                }
            }
            if ($stateParams.type == "water") {
                $scope.title = "水类";
                if($stateParams.weight=="less"){
                    $scope.talk = "宝宝吃的不够！";
                    $scope.ping = "宝宝的饮水量摄入偏少哦。（如果宝宝喝奶或汤汁比较多的话，宝妈也不用太在意哦，只要宝宝全日总水量摄入充足即可。)";
                    $(".balance-pic .balance-pallet").css("transform","rotate(-8deg)");
                }else if($stateParams.weight=="more"){
                    $scope.talk = "宝宝吃的过多！";
                    $scope.ping = "宝宝的饮水量偏多哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(8deg)");
                }else{
                    $scope.talk = "宝宝吃的正好！";
                    $scope.ping = "宝宝的饮水量适宜，为宝宝点个赞！";
                }
            }
            if ($stateParams.type == "vegetables") {
                $scope.title = "蔬菜类";
                if($stateParams.weight=="less"){
                    $scope.talk = "宝宝吃的不够！";
                    $scope.ping = "宝宝蔬菜类食物摄入不足哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(-8deg)");
                }else if($stateParams.weight=="more"){
                    $scope.talk = "宝宝吃的过多！";
                    $scope.ping = "宝宝蔬菜吃得有点多哦。";
                    $(".balance-pic .balance-pallet").css("transform","rotate(8deg)");
                }else{
                    $scope.talk = "宝宝吃的正好！";
                    $scope.ping = "宝宝蔬菜类食物摄入量适宜，为宝宝点个赞！";
                }
            }
            GetEvaluate.get({"flag":"evaluate"}, function (data) {
                console.log("pingg",data);
                if(data.todayEvaluateMap.fishEggs!=""){
                    num++;
                }
                if(data.todayEvaluateMap.meat!=""){
                    num++;
                }
                if(data.todayEvaluateMap.milk!=""){
                    num++;
                }
                if(data.todayEvaluateMap.millet!=""){
                    num++;
                }
                if(data.todayEvaluateMap.oilSalt!=""){
                    num++;
                }
                if(data.todayEvaluateMap.potato!="") {
                    num++;
                }
                if(data.todayEvaluateMap.vegetables!="") {
                    num++;
                }
                if(data.todayEvaluateMap.water!=""){
                    num++;
                }

                if(num==8){
                    $scope.noNext = "false";
                }
            });

        });

        $scope.goJiangjie = function () {
            $state.go("nutritionEffect",{finish:"no",type:$stateParams.type,weight:$stateParams.weight});
        }

        $scope.goAssess = function () {
            //$state.go("nutritionAssess",{flag:"noagain"});
            window.location.href = "ap/ntr?value=251343#/nutritionAssess/noagain";
        }

        $scope.goAssessResult = function () {
            //$state.go("nutritionAssessResult");
            window.location.href = "ap/ntr?value=251345#/nutritionAssessResult";
        }
    }]);

