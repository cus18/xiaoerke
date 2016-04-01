angular.module('controllers', ['ionic']).controller('todayReadMoreCtrl', [
        '$scope','$state','$stateParams','GetArticleList','findByParentId','getBabyEmrList',
        function ($scope,$state,$stateParams,GetArticleList,findByParentId,getBabyEmrList) {
            $scope.info = {};
            $scope.lock='false';
            $scope.noContent = "false";
            $scope.ageList = ["0-28天","1-3月","4-6月","7-9月","10-12月","13-18月","19-24月","25-36月"];
            $scope.ageDaysList = [28,90,180,270,365,540,730,1096];
            var img = document.getElementById('imghead');

            var transferBirthdayToDays = function Computation(sDate1, sDate2){   //sDate1和sDate2是2008-12-13格式
                var  aDate,  oDate1,  oDate2,  iDays
                aDate  =  sDate1.split("-")
                oDate1  =  new  Date(aDate[0]  +  '-'  +  aDate[1]  +  '-'  +  aDate[2])    //转换为12-18-2006格式
                aDate  =  sDate2.split("-")
                oDate2  =  new  Date(aDate[0]  +  '-'  +  aDate[1]  +  '-'  +  aDate[2])
                iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24)    //把相差的毫秒数转换为天数
                return  iDays + 1
            }

            var contentList = function(id){
                $scope.pageLoading=true;
                GetArticleList.save({"id":id,"pageNo":1,"pageSize":20},function(data){
                    $scope.pageLoading=false;
                    if(data.articleList.length==0){
                        $scope.noContent = "true";
                    }else{
                        $scope.noContent = "false";
                        $scope.articleList = data.articleList;
                    }
                });
            }

            $scope.$on('$ionicView.enter', function(){
                img = document.getElementById('imghead');
                var secondMenuId = $stateParams.secondMenuId;
                $scope.title = $stateParams.title;
                $scope.pageLoading=true;
                findByParentId.save({"categoryId":secondMenuId},function(data){
                    $scope.pageLoading=false;
                    $scope.ageGroupList = data.categoryList;
                    getBabyEmrList.get(function(data){
                        if(data.status=="0"){
                            img.src ="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2FtodayReadMore_defaultPic.png";
                        }else{
                            img.src ="http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/"+data.id;
                        }

                        var birthday = data.birthday;
                        var ageDay = transferBirthdayToDays(String(birthday),String(moment().format('YYYY-MM-DD')));
                        var initIndex = 1;
                        var index = 0;
                        $scope.ageNewList = angular.copy($scope.ageGroupList);
                        _.each($scope.ageDaysList,function(val){
                            if(parseInt(val) < parseInt(ageDay)){
                                index++;
                            }
                        });
                        var j = 0;
                        var init = index - initIndex;
                        for(var i = 0;i< 8-index;i++)
                        {
                            $scope.ageNewList[i+initIndex] = angular.copy($scope.ageGroupList[index+i]);
                            j++;
                        }
                        for(var i=0;i< 8-j;i++)
                        {
                            var newIndex = j + i + initIndex;
                            console.log(newIndex);
                            if(newIndex > 7)
                            {
                                newIndex = 0;
                            }
                            $scope.ageNewList[newIndex] = angular.copy($scope.ageGroupList[i]);
                        }
                        if(index==0)
                        {
                            $scope.ageNewList[index] = angular.copy($scope.ageGroupList[7]);
                        }
                        contentList($scope.ageNewList[initIndex].categoryId);
                        $scope.choiceItem = initIndex;
                    })
                });
            });

            /**
             * 获取横向菜单内容
             */
            $scope.getNext = function(id,index){
                contentList(id);
                $scope.choiceItem = index;
            }
    }]);