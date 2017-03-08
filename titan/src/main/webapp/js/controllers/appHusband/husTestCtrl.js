angular.module('controllers', ['ionic','ngDialog']).controller('husTestCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog) {
       $scope.data=[{
           question:'1,情人节你老公会',
           answer:['A、忘记了','B、从来不过','C、送花送礼物','D、为你准备了惊喜'],
           mark:0
       },{
           question:'2,当孩子大哭大闹时，他会',
           answer:['A、当做没听见','B、直接丢给你','C、吓唬孩子不许哭','D、主动哄孩子'],
           mark:0
        },{
           question:'3,当你和婆婆闹矛盾的时候',
           answer:['A、不闻不问','B、和婆婆统一战线','C、和自己统一战线','D、两边说和'],
           mark:0
        },{
           question:'4,当你生病了，你老公怎么会照顾你？',
           answer:['A、让你多喝热水','B、告诉你记得吃药','C、端水喂药伺候你','D、帮你出去买药'],
           mark:0
        },{
           question:'5,当你生孩子的时候，你老公在',
           answer:['A、不知道在哪','B、产房门口玩手机','C、手术室门口团团转','D、主动要求陪产'],
           mark:0
        },{
           question:'6,当他发工资的时候',
           answer:['A、全都自己留着','B、偷偷留点再给你','C、如数上交','D、问你工资到账没'],
           mark:0
        },{
           question:'7,你们一家三口出门，你老公会',
           answer:['A、从来没一起出过门','B、打电话看手机','C、只顾自己扮酷','D、大包小包+抱孩子'],
           mark:0
        }]

        $scope.choose=function(i,item){
            item.mark=(i+1)*5;
        }
        setTimeout(function(){
            $('.answer').find('span').click(function(){
                $(this).addClass('cur').siblings().removeClass('cur');
            })
        },1000);
        $scope.sum=0;
        $scope.Submit=function(){
            for(var i=0;i<$scope.data.length;i++){
                if($scope.data[i].mark==0){
                    $scope.sum=0;
                    alert('亲，你还没有完题呢，赶紧把题答完，看结果欧～')
                    return;
                }else{
                    $scope.sum+=$scope.data[i].mark;
                }
            }
            alert($scope.sum);
            $scope.sum=0;
        }


    }])