angular.module('controllers', ['ionic']).controller('prizeListCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','$http','GetCardInfoList',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,$http,GetCardInfoList) {
       /*查看代金券*/
        $scope.prize_status=false;

        GetCardInfoList.save({},function(res){
            $scope.list=res.rewardsVo;
            console.log($scope.list)
            if($scope.list==undefined){
                $scope.prize_status=true;
                return;
            }
            if($scope.list.length>0){
                $scope.prize_status=false;
            }else{
                $scope.prize_status=true;
            }
            for(var i=0;i<$scope.list.length;i++){
                $scope.list[i].day=getDate( $scope.list[i].createTime,'-')
                $scope.list[i].hour=getHour( $scope.list[i].createTime,'-')
                if($scope.list[i].type==0){
                    switch ($scope.list[i].moneyCount){
                        case 5:$scope.list[i].urlData='https://h5.youzan.com/v2/ump/promocard/fetch?alias=qmohxwgt';break;
                        case 10:$scope.list[i].urlData='https://h5.youzan.com/v2/ump/promocard/fetch?alias=pv3jvvzn';break;
                        case 20:$scope.list[i].urlData='https://h5.youzan.com/v2/ump/promocard/fetch?alias=1h8v2p8hk';break;
                        case 30:$scope.list[i].urlData='https://h5.youzan.com/v2/ump/promocard/fetch?alias=1a5ufjzzs';break;
                        case 40:$scope.list[i].urlData='https://h5.youzan.com/v2/ump/promocard/fetch?alias=v6vjlswx';break;
                    }
                }else{
                    $scope.list[i].urlData=''
                }
            }
            console.log($scope.list)
        })


        //点击查看
        $scope.lookVoucher=function(Url){
            window.location.href=Url;
        }
        $scope.hongbao='http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/prizeList/hongbao_a_png_03.png';
        $scope.quan='http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/prizeList/quan_a_png_03.png';
        /* 5元
         https://h5.youzan.com/v2/ump/promocard/fetch?alias=qmohxwgt

         10元
         https://h5.youzan.com/v2/ump/promocard/fetch?alias=pv3jvvzn
         20元
         https://h5.youzan.com/v2/ump/promocard/fetch?alias=1h8v2p8hk

         30元
         https://h5.youzan.com/v2/ump/promocard/fetch?alias=1a5ufjzzs

         40元
         https://h5.youzan.com/v2/ump/promocard/fetch?alias=v6vjlswx*/

    }])


function getDate(time,mark){
    if(time==null||time==''){
        return '';
    }else{
        var date=new Date(parseInt(time));
        var dYear = date.getFullYear();
        var dMonth = date.getMonth()+1;
        var dDate = date.getDate();
        var dHours = date.getHours();
        var dMinutes = date.getMinutes();
        var dSeconds = date.getSeconds();
        if(dMonth<10){
            dMonth='0'+dMonth
        };
        if(dDate<10){
            dDate='0'+dDate
        };
        if(dHours<10){
            dHours='0'+dHours
        };
        if(dMinutes<10){
            dMinutes='0'+dMinutes
        };
        if(dSeconds<10){
            dSeconds='0'+dSeconds
        };
        if(mark){
            return dMonth + mark + dDate
        }else{
            return dMonth+'/'+dDate
        }
    }
}


function getHour(time,mark){
    if(time==null||time==''){
        return '';
    }else{
        var date=new Date(parseInt(time));
        var dYear = date.getFullYear();
        var dMonth = date.getMonth()+1;
        var dDate = date.getDate();
        var dHours = date.getHours();
        var dMinutes = date.getMinutes();
        var dSeconds = date.getSeconds();
        if(dMonth<10){
            dMonth='0'+dMonth
        };
        if(dDate<10){
            dDate='0'+dDate
        };
        if(dHours<10){
            dHours='0'+dHours
        };
        if(dMinutes<10){
            dMinutes='0'+dMinutes
        };
        if(dSeconds<10){
            dSeconds='0'+dSeconds
        };
        if(mark){
            return dHours + ':' + dMinutes
        }else{
            return dHours+':'+dMinutes
        }
    }
}