angular.module('controllers2', [])
    .controller('indexCtrl',['$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {

            $(function(){
                $('#mov_doctor').movingBoxes({
                    width: 1000,
                    reducedSize : 0.5,
                    startPanel : 2,
                    currentPanel : 'svccurrent',
                    hashTags: false,
                    wrap: false,
                    initChange: function(e, slider, tar){
                        console.log("1",e);
                        console.log("2",slider);
                        console.log("3",tar);
                        slider.curWidth = 300;
                    }

                });
                $('#mov_hosp').movingBoxes({
                    width: 1000,
                    reducedSize : 0.5,
                    startPanel : 2,
                    currentPanel : 'svccurrent',
                    hashTags: false,
                    wrap: false,
                    initChange: function(e, slider, tar){
                        console.log("1",e);
                        console.log("2",slider);
                        console.log("3",tar);
                        slider.curWidth = 300;
                    }

                });
            })


        }]);
