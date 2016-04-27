
var resultList=["","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fbumanyi_weixuanzhong.png",//不满意
    "","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fmanyiweixuanzhong.png",//满意
    "","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Ffeichangmanyiweixuanzhong.png"]//非常满意

var moneyNum;

$(function(){
    $('.evalhavemoney').hide();//收到心意钱
    $('.evalsharebut').hide();//分享按钮
    $('#ping').hide();
})

//判断输入心意钱
var moreMoney = function () {
   // recordLogs("ZXPJSXY_JE");
    if($('#getMoney').val()>0){
        $('.inputmoney img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_xuanzhong.png");
    }else{
        $('.inputmoney img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_bukedian.png");
    }
}

//心意钱数
var setMoney = function (index) {
    moneyNum = $('#getMoney').val();
    if(index==0){
        if(moneyNum<=0){
            $('.inputmoney img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_bukedian.png");
        }else{
            $('.inputmoney img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_xuanzhong.png");
            moneyNum--;
            if(moneyNum==0){
                $('.inputmoney img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_bukedian.png");
            }
            $('#getMoney').val(moneyNum);
        }
    }else if(index==1){
        moneyNum++;
        $('#getMoney').val(moneyNum);
        $('.inputmoney img').eq(1).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjia_xuanzhong.png");
        $('.inputmoney img').eq(0).attr("src","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fjian_xuanzhong.png");
    }
}

var doRefresh = function () {
    
}