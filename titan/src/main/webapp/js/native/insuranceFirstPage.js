
var doRefresh = function () {
    setLog("FWLB");
}


//手足口
var goHandfootmouth = function () {
    window.location.href = "insurance#/handfootmouthIndex";
}
//防犬宝
var goantiDog = function () {
    window.location.href = "firstPage/antiDogFirst";
}
//购买保险列表
var goPay = function () {
    setLog("FWLB_PEI_");
    window.location.href = "insurance#/insuranceOrderList";
}

var setLog = function (item) {
    $.ajax({
        url:"util/recordLogs",// 跳转到 action
        async:true,
        type:'get',
        data:{logContent:encodeURI(item)},
        cache:false,
        dataType:'json',
        success:function(data) {
        },
        error : function() {
        }
    });
}