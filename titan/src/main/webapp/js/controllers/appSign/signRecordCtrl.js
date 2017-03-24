function getDay(time){
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
        return  dMonth + '月' + dDate+'日';
    }
}

function getHour(time){
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
        return dHours + ':' + dMinutes
    }
}

angular.module('controllers', ['ionic','ngDialog']).controller('signRecordCtrl', [
    '$scope','$state','$stateParams', 'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog','FindPunchCardBySelf',
    function ($scope,$state,$stateParams,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog,FindPunchCardBySelf) {
        $scope.noRecordStatus=false;
        FindPunchCardBySelf.save({openId:$stateParams.openId},function(res){
            if(res.headImgUrl==''||res.headImgUrl==null){
                $scope.headImgUrl='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf/consult/yonghumoren.png';
            }else{
                $scope.headImgUrl=res.headImgUrl;
            }
            $scope.total=res.rewardsInfo;
            if(res.dataList==null||res.dataList==''){
                $scope.noRecordStatus=true;
                $scope.nickname=res.key;
            }else{
                $scope.list=res.dataList;
                $scope.nickname=$scope.list[0].nickname;
                for(var i= 0;i<$scope.list.length;i++){
                    $scope.list[i].day=getDay( $scope.list[i].createTime);
                    $scope.list[i].hour=getHour( $scope.list[i].updateTime)
                }
            }
        })
    }])