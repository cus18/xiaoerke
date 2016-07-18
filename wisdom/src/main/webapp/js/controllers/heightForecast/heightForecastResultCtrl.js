angular.module('controllers', ['ionic']).controller('heightForecastResultCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {
        /*
        孩子身高公式：
        女儿的身高=（父亲身高+母亲身高-13）/2，随机+1~3厘米
        儿子的身高=（父亲身高+母亲身高+13）/2，随机+1~5厘米

        结果如下
        男宝：
        低于170，文质彬彬
        低于175，气宇轩昂
        低于180，玉树临风
        低于185，长腿欧巴
        低于190，顶天立地
        高于190，篮球飞人


        例子：恭喜您的宝宝将来会成为身高172的维密超模！
        */
        //男宝宝http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fbiaoti_png_03.png

        $scope.heightImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fbiaoti_bb_png_03.png';
        $scope.babyImg = '';
        $scope.starImg = '';
        $scope.babyHeight = '';
        $scope.boyHeight = '';
        $scope.girlHeight = '';
        $scope.boyImg = '';
        $scope.girlImg = '';
        $scope.showDouble = false;
        $scope.showOne = false;
        $scope.$on('$ionicView.enter', function(){
            if($stateParams.resultGirl != 0 && $stateParams.resultBoy != 0){
                $scope.heightImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fbiaoti_bb_png_03.png';
                $scope.showDouble = true;
                $scope.boyHeight = $stateParams.resultBoy ;
                $scope.girlHeight = $stateParams.resultGirl;
                if($scope.boyHeight < 170){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimages_w_png_03.png';
                }else if($scope.boyHeight < 175){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_q_png_03.png';
                }else if($scope.boyHeight < 180){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_y_png_03.png';
                }else if($scope.boyHeight < 185){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_c_png_03.png';
                }else if($scope.boyHeight < 190){
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_d_png_03.png';
                }else{
                    $scope.boyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_l_png_03.png';
                }

                if($scope.girlHeight < 160){
                    $scope.girlImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_x_png_03.png';
                }else if($scope.girlHeight < 165){
                    $scope.girlImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_dajiaguixiu_png_03_03.png';
                }else if($scope.girlHeight < 170){
                    $scope.girlImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimages_png_03.png';
                }else{
                    $scope.girlImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_chaoji_03.png';
                }
            }
            if($stateParams.resultGirl == 0 && $stateParams.resultBoy != ''){
                $scope.heightImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fbiaoti_nan_png_03.png';
                $scope.showOne = true;
                $scope.babyHeight = $stateParams.resultBoy;
                if($scope.babyHeight < 170){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimages_w_png_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_hejiong_png_03.png';
                }else if($scope.babyHeight < 175){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_q_png_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_liangchaowei_png_03.png';
                }else if($scope.babyHeight < 180){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_y_png_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_songzhongji_png_03.png';
                }else if($scope.babyHeight < 185){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_c_png_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_wuyanzu_png_03.png';
                }else if($scope.babyHeight < 190){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_d_png_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_wuyifan_png_03.png';
                }else{
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_l_png_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_yijianlian_png_03.png';
                }
            }
            if($stateParams.resultGirl != '' && $stateParams.resultBoy == 0){
                $scope.heightImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fbwlsg_png_03.png';
                $scope.showOne = true;
                $scope.babyHeight = $stateParams.resultGirl;
                if($scope.babyHeight < 160){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_x_png_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_ciayilin_png_03.png';
                }else if($scope.babyHeight < 165){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_dajiaguixiu_png_03_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_sunli_png_03.png';
                }else if($scope.babyHeight < 170){
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimages_png_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_yangmi_png_03.png';
                }else{
                    $scope.babyImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fimage_chaoji_03.png';
                    $scope.starImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/height%2Fphoto_lin_png_03.png';
                }
            }
        });

        console.log($stateParams.resultGirl);
        console.log($stateParams.resultBoy);
        $scope.goUmbrella =function(){
            window.location.href="http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000005/a"
        };
    }]);

