angular.module('controllers', ['ionic']).controller('umbrellaMemberListCtrl', [
        '$scope','$state','$stateParams','getFamilyList',
        function ($scope,$state,$stateParams,getFamilyList) {
            $scope.title="宝护伞";
            //$scope.id = $stateParams.id;


            getFamilyList.save({"id":$stateParams.id},function(data){
                console.log(data);
                $scope.familyList =data.familyList;
                for(var i=0;i<data.familyList.length;i++){
                    $scope.familyList[i].birthday = moment(data.familyList[i].birthday).format("YYYY-MM-DD");
                    var birth = moment(data.familyList[i].birthday);
                    var nowTime = moment();
                    var age = nowTime.diff(birth, 'years');       // 1
                    if(age>18){
                      if($scope.familyList[i].sex==0){
                          $scope.familyList[i].sex = "宝爸"
                      }else{
                          $scope.familyList[i].sex = "宝妈"
                      }
                    }else{
                        if($scope.familyList[i].sex==0){
                            $scope.familyList[i].sex = "男宝"
                        }else{
                            $scope.familyList[i].sex = "女宝"
                        }
                    }
                }
            })

            $scope.addMember=function(){
                $state.go("umbrellaMemberAdd",{id:$stateParams.id});
            }

            $scope.goActive=function(){
                $state.go("umbrellaFillInfo",{id:$scope.umbrellaId});
            };
            $scope.goShare=function(){
                $scope.shareLock=true;
            };

            $scope.$on('$ionicView.enter', function(){

            });

            
    }]);