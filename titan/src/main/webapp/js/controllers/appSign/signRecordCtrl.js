angular.module('controllers', ['ionic','ngDialog']).controller('signRecordCtrl', [
    '$scope','$state','$stateParams', 'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog','FindPunchCardBySelf',
    function ($scope,$state,$stateParams,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog,FindPunchCardBySelf) {
        FindPunchCardBySelf.save({openId:''},function(res){
            console.log(res)
        })

    }])