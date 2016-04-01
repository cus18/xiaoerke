angular.module('controllers', ['ionic']).controller('todayChoiceNewbornCtrl', [
        '$scope','$state','$stateParams','GetArticleList','findByParentId',
        function ($scope,$state,$stateParams,GetArticleList,findByParentId) {
            $scope.info = {};
            $scope.lock='false';
            $scope.itemIndex='0';

            $scope.$on('$ionicView.enter', function(){
                var contentid = $stateParams.id;
                $scope.title = $stateParams.title;
                $scope.pageLoading=true;
                findByParentId.save({"categoryId":contentid},function(data){
                    $scope.pageLoading=false;
                    $scope.ageGroupList = data.categoryList;
                    contentList(data.categoryList[0].categoryId);
                });
            });

            var contentList = function(id){
                $scope.pageLoading=true;
                GetArticleList.save({"id":id},function(data){
                    $scope.pageLoading=false;
                    $scope.articleList = data.articleList;
                });
            }

            /**
             * 获取不同菜单列表
             * @param id
             */
            $scope.getNext = function(id,index){
                $scope.itemIndex=index;
                contentList(id);
            }

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