angular.module('controllers', ['ionic']).controller('umbrellaPublicizeCtrl', [
        '$scope','$state','$stateParams','getNickNameAndRanking',
        function ($scope,$state,$stateParams,getNickNameAndRanking) {
            $scope.title="宝大夫儿童家庭重疾互助计划";
            $scope.openid="111";
            var id;//路由传过来的参数
            $scope.goFillInfo=function(){
                //window.location.href = "http://s165.baodf.com/wisdom/umbrella#/umbrellaFillInfo/130300000/a";
                window.location.href = "http://s165.baodf.com/wisdom/umbrella#/umbrellaFillInfo/"+id+"/a";
            };
            $scope.goUmbrellaFirst=function(){
                //window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa_130300000";
                window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa_"+id;
            };


            $scope.$on('$ionicView.enter', function(){
                id=$stateParams.id;
                //初始化 加入宝护伞人数

                $.ajax({
                    type: 'POST',
                    url: "umbrella/firstPageDataCount",
                    contentType: "application/json; charset=utf-8",
                    success: function(result){
                        var count=result.count;;
                        $("#count").html(count);
                    },
                    dataType: "json"
                });

                //获取用户openid
              /*  $.ajax({
                    url:"umbrella/getOpenid",// 跳转到 action
                    async:true,
                    type:'post',
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                        console.log('data.openid',data.openid)
                        if(data.openid=="none"){
                            window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
                        }
                        else{
                            $scope.openid=data.openid;
                            getNickNameAndRanking.save({"openid":"o3_NPwlfeHYBUk3oFOuvhyrfKwDQ"},function (data) {
                                if(data.nickName!=""){
                                    $scope.nickName=data.nickName;
                                }
                                console.log(" $scope.nickName", $scope.nickName);

                            });
                        }
                    },
                    error : function() {
                    }
                });*/
            });

            
    }]);