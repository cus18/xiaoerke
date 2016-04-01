angular.module('controllers', ['ionic']).controller('knowledgeSearchCtrl', [
        '$scope','$state','$stateParams','GetArticleList','$ionicScrollDelegate',
        function ($scope,$state,$stateParams,GetArticleList,$ionicScrollDelegate) {
            $scope.info = {};
            var num = 10;

            $scope.$on('$ionicView.enter', function(){
                $scope.lockScroll="true";
                $scope.noContent = "true";
                $scope.info.content = $stateParams.content;
                $scope.generalize = $stateParams.generalize;//推广字段
                getArticleList($scope.info.content,$scope.generalize);
            });

            /**
             * 根据搜索内容显示文章列表
             * @param title
             */
            getArticleList = function(title){
                $scope.pageLoading=true;
                GetArticleList.save({"title":title,"pageNo":1,"pageSize":20,"generalize":$scope.generalize},function(data){
                    $scope.pageLoading=false;
                    if(data.articleList.length==0){
                        $scope.noContent = "false";
                        alert("您输入内容不存在，请重新输入！");
                    }else{
                        $scope.noContent = "true";
                        $scope.articleList = data.articleList;
                    }
                });
            }

            /**
             * 搜索方法
             */
            $scope.searchTitle = function(){
                if($scope.info.content!=undefined){
                    $stateParams.content = $scope.info.content;

                    getArticleList($scope.info.content);
                }else{
                    alert("请输入搜索内容！");
                }
            }
    }]);