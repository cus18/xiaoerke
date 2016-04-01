angular.module('controllers', ['ionic']).controller('evaluateListCtrl', [
        '$scope','$state',
        function ($scope,$state) {
            $scope.title = "评价";
            $scope.pageLoading =false;
            $scope.starImg1 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar.png";
            $scope.starImg2 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_part.png";
            $scope.starImg3 ="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fstar_gray.png";
            $scope.classifyItem="all";
            $scope.evaluateList=[
                {
                    name:"宝粉1",
                    phone:"15811111111",
                    date:"03/12",
                    week:"周二",
                    time:"03:12",
                    star:3,
                    impression:"不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。"
                },
                {
                    name:"宝粉2",
                    phone:"15811111111",
                    date:"02/12",
                    week:"周二",
                    time:"03:12",
                    star:4,
                    impression:"不错，挺好。不错，挺好。不错，挺好。不错，挺好。不错，挺好。"
                },
                {
                    name:"宝粉3",
                    phone:"15811111111",
                    date:"03/14",
                    week:"周二",
                    time:"03:12",
                    star:4.5,
                    impression:"不错，挺好。不错，挺好。不错，挺好。不错，挺好。"
                },
                {
                    name:"宝粉4",
                    phone:"15811111111",
                    date:"03/12",
                    week:"周二",
                    time:"03:12",
                    star:5,
                    impression:"不错，挺好。"
                },

            ];
            $scope.evaluateClassify = function(item){
                $scope.classifyItem =item;
            };
            $scope.$on('$ionicView.enter', function(){


            })
    }])
