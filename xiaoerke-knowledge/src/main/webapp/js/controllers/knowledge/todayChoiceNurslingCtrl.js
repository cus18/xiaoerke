angular.module('controllers', ['ionic']).controller('todayChoiceNurslingCtrl', [
        '$scope','$state','$stateParams','findByParentId',
        function ($scope,$state,$stateParams,findByParentId) {
            $scope.info = {};
            $scope.lock='false';
            $scope.showMoreLock=false;
            $scope.no = "true";

            $scope.showMore=function(){
                $scope.showMoreLock=true;
            }

            $scope.$on('$ionicView.enter', function(){
                var contentid = $stateParams.id;
                $scope.title = $stateParams.title;
                $scope.pageLoading=true;
                findByParentId.save({"categoryId":contentid},function(data){
                    $scope.pageLoading=false;
                    $scope.ageGroupList = data.categoryList;
                });

            });

            /**
             * 搜索
             */
            $scope.search = function(){
                if($scope.info.content!=undefined){
                    $state.go('knowledgeSearch',{content:$scope.info.content,generalize:"WX"});
                }else{
                    alert("请输入搜索内容！");
                }
            }
    }]);