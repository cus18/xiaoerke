
var getValidateCode = function(){
    if(countdown<60)
    {
        return;
    }
    else{
        var partner = /^1[3578]\d{9}$/;
        if(!partner.exec($('#username').val())) {
            $('#info').html("手机号格式不对");
            return;
        }
        else{
            var mydata = '{"userPhone":"'
                + $('#username').val() + '"}';
            $.ajax({
                url:"util/user/getCode",
                async:true,
                type:'post',
                data:mydata,
                cache:false,
                contentType:"application/json; charset=utf-8",
                dataType:'json',
                success:function(data) {

                },
                error : function() {
                }
            });
            lockValidateCode();
        }
    }
}

var buttonValue = true;

var countdown=60;
var lockValidateCode = function()
{
    if (countdown == 0) {
        $('#validateCode').html("获取验证码");
        countdown = 60;
        return;
    } else {
        $('#validateCode').html("重新发送(" + countdown + ")");
        countdown--;
    }
    setTimeout(function() {
            lockValidateCode() }
        ,1000)
}

var deal = function(){
    if(buttonValue==false){
        buttonValue = true;
        $("#dealImg").attr("src","http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/images%2Fdeal_y.png");
    }
    else if(buttonValue==true){
        buttonValue = false;
        $("#dealImg").attr("src","http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/images%2Fdeal_n.png");
    }
    $("#doctorButton").attr('disabled',buttonValue);
}

/*var userDeal = function(){
    window.location.href = "/xiaoerke-doctor/ap/doctor#/userDeal";
}*/



