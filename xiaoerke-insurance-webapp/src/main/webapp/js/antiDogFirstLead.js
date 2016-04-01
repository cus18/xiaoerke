var webpath = "/xiaoerke-insurance-webapp";
document.write('<scr'+'ipt src="' + webpath + '/js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery-2.1.3.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-resource.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-sanitize.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-route.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.touchSlider.js"></scr'+'ipt>');

var doRefresh = function(){

    $.ajax({
        url:"ap/util/user/findHealthPlanAddItem",// 跳转到 action
        async:true,
        type:'post',
        data:{},
        cache:false,
        dataType:'json',
        success:function(data) {
            $.each(data,function(index,value){
                if(value.addValue=="FQB_YDY"){
                    window.location.href="ap/firstPage/antiDogFirst";
                }
            })

            var param = '{"addValue":"' + "FQB_YDY" + '"}';
            $.ajaxSetup({
                contentType : 'application/json'
            });
            $.post('ap/util/user/recordHealthPlanAddItem',param,
                function(data) {
                }, 'json');
        },
        error : function() {
        }
    });

}
var startEnter = function(){
    window.location.href="ap/firstPage/antiDogFirst";
};
