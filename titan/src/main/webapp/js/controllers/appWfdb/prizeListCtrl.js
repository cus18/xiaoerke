angular.module('controllers', ['ionic']).controller('prizeListCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','$http','GetCardInfoList',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,$http,GetCardInfoList) {
       /*查看代金券*/
        GetCardInfoList.save({},function(res){
            $scope.list=res.rewardsVo;
            for(var i=0;i<$scope.list.length;i++){
                $scope.list[i].day=getDate( $scope.list[i].createTime,'-')
                $scope.list[i].hour=getHour( $scope.list[i].createTime,'-')
            }
            console.log($scope.list)
        })
        $scope.hongbao0='http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/prizeList/hongbao_a_png_03.png';
        $scope.quan='http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/appWfdb/prizeList/quan_a_png_03.png';


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