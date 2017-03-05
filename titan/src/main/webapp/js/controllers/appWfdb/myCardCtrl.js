angular.module('controllers', ['ionic']).controller('myCardCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','GetCardInfoList',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,GetCardInfoList) {
        var swiper = new Swiper('.swiper-container', {
            pagination: '.swiper-pagination',
            slidesPerView: 5,
            paginationClickable: true,
            spaceBetween: 15
        });
       //获取基本信息接口
        GetCardInfoList.save('',function(res){
            console.log(res)
        })
    }])
