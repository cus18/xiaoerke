angular.module('controllers', ['ionic']).controller('todayChoiceNurslingListCtrl', [
        '$scope','$state','$stateParams','GetArticleList','$ionicScrollDelegate',
        function ($scope,$state,$stateParams,GetArticleList,$ionicScrollDelegate) {
            $scope.info = {};
            $scope.lock='false';
            $scope.lockScroll="true";
            var id = "";
            var num = 10;

            $scope.$on('$ionicView.enter', function(){
                $scope.zhutitle = $stateParams.zhutitle;
                $scope.houtitle = $stateParams.houtitle;
                id = $stateParams.id;
                GetArticleList.save({"id":id,"pageNo":1,"pageSize":10},function(data){
                    if(data.articleList.length==0){
                        $scope.lockScroll="false";
                        alert("妈妈莫着急，部分内容正在拼命建设中！");
                    }else{
                        $scope.articleList = data.articleList;
                    }
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                });
            });

            /**
             * 上拉刷新
             */
            $scope.loadMoreWithDrawlsList = function(){
                num=num+10;
                //从后台获取菜单内容
                GetArticleList.save({"pageNo":1,"pageSize":num,"id":id},function(data){
                    if($scope.articleList.length==data.articleList.length){
                        $scope.lockScroll="false";
                        $ionicScrollDelegate.scrollBottom();
                    }else{
                        $scope.articleList=data.articleList;
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    }
                });
            }
    }]);