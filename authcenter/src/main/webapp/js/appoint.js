//页面初始化执行,用户初始化页面参数信息以及微信的支付接口
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

