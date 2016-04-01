angular.module('controllers', ['ionic']).controller('otherDiseaseCtrl', [
        '$scope','$state','$stateParams','findByParentId','GetArticleList',
        function ($scope,$state,$stateParams,findByParentId,GetArticleList) {
            $scope.info = {};
            $scope.lock='false';
            $scope.itemIndex="0";

            $scope.$on('$ionicView.enter', function(){
                var secondMenuId = $stateParams.id;
                $scope.title = $stateParams.title;
                $scope.pageLoading=true;
                findByParentId.save({"categoryId":secondMenuId},function(data){
                    $scope.pageLoading=false;
                    $scope.ageGroupList = data.categoryList;
                    contentList(data.categoryList[0].categoryId);
                });
            });


            /**
             * 获取横向菜单内容
             */
            $scope.getNext = function(id,index){
                $scope.itemIndex=index;
                contentList(id);
            }

            var contentList = function(id){
                $scope.pageLoading=true;
                GetArticleList.save({"id":id},function(data){
                    $scope.pageLoading=false;
                    $scope.articleList = data.articleList;

                });
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

    }])