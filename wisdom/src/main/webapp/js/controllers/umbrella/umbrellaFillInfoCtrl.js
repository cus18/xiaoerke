angular.module('controllers', ['ionic']).controller('umbrellaFillInfoCtrl', [
        '$scope','$state','$stateParams','getBabyinfoList','getOpenidStatus','ifExistOrder','$filter','IdentifyUser',
        'saveBabyInfo','updateBabyInfo','updateInfo','GetUserLoginStatus','$location',"newJoinUs",
        function ($scope,$state,$stateParams,getBabyinfoList,getOpenidStatus,ifExistOrder,$filter,IdentifyUser
            ,saveBabyInfo,updateBabyInfo,updateInfo,GetUserLoginStatus,$location,newJoinUs) {
            $scope.title="宝护伞-宝大夫儿童家庭重疾互助计划";
            $scope.sexItem = "boy";
            $scope.parentItem = "mother";
            $scope.checkLock = true;
            $scope.fillLock = false;
            $scope.selectItem = "";
            $scope.info={};

            $scope.selectedBaby="";
            $scope.codeSecond="获取";
            $scope.openid="";
            $scope.babyInfoList={};
            $scope.umbrellaId=$stateParams.id;
            $scope.codeButton=false;
            $scope.protocolLock=false;
            $scope.ifUpedateBabyInfo=false;
            $scope.ifExist=false;
            $scope.buttonDis=false;

            /*点击输入框跳转到相应位置*/
            $scope.skip = function(item){
                $(".view,html,body").stop().animate({"scrollTop":$("#"+item).offset().top},0);
                if(item=="babyName"){
                    $scope.fillLock = true;
                }
            }

            /*填写宝宝姓名 获取焦点*/
            $scope.fillName = function(){
                $scope.fillLock = true;
            };
            /*填写宝宝姓名 失去焦点*/
            $scope.fillFinish = function(){
               $scope.fillLock = false;
            };
            /*选择性别*/
            $scope.selectSex = function(sex){
                $scope.sexItem=sex;
            };
            /*填写宝宝姓名 选择宝宝*/
            $scope.selectBaby = function(selectItem){
                if(selectItem.name!="添加") {
                    $scope.info.id=selectItem.id;
                    $scope.info.babyName = selectItem.name;
                    $scope.selectedBaby=selectItem;
                    $("#birthday").val($filter('date')(selectItem.birthday, 'yyyy-MM-dd'));
                    if (selectItem.sex == "1") {
                        $scope.selectSex('boy');
                    } else {
                        $scope.selectSex('girl');
                    }
                }else{
                    $scope.info.id=selectItem.id;
                    $scope.info.babyName = "";
                    $("#birthday").val("");
                }
            };

            /*选择父母*/
            $scope.selectParent = function(parent){
                $scope.parentItem=parent;
            };
            /*触摸滑动区域时响应*/
            $scope.onTouch = function(parent){
                $scope.fillLock = false;
            };

            /*查看宝大夫儿童家庭重疾互助计划公约*/
            $scope.lookProtocol = function(){
                $scope.protocolLock=true;
            };
            /*关闭 查看宝大夫儿童家庭重疾互助计划公约*/
            $scope.cancelProtocol = function(){
                $scope.protocolLock=false;
            };
            /*校验手机号*/
            $scope.checkPhone= function(){
                var phoneNumber = $scope.info.phoneNum+"";
                if (phoneNumber.match(/^1[43578]\d{9}$/)){
                    $scope.checkLock = true;
                }
                else{
                    $scope.checkLock = false;
                    $scope.tipsText="手机号有误";
                    return;
                }
            }
            
            /*校验身份证号*/
            $scope.checkCode= function(){
                var sId =$scope.info.IdCard;

                var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",
                    23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",
                    41:"河南",42:"湖北", 43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",
                    52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",
                    71:"台湾",81:"香港",82:"澳门",91:"国外"}
                var iSum=0
                if(!/^\d{17}(\d|x)$/i.test(sId)){
                    $scope.checkLock = false;
                    $scope.tipsText="身份证号码有误";
                    return false;
                }
                sId=sId.replace(/x$/i,"a");
                if(aCity[parseInt(sId.substr(0,2))]==null){
                    $scope.checkLock = false;
                    $scope.tipsText="身份证号码有误";
                    return false;
                }
                sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2));
                var d=new Date(sBirthday.replace(/-/g,"/"))
                if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate())){
                    $scope.checkLock = false;
                    $scope.tipsText="身份证号码有误";
                    return false;
                }
                for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11)
                if(iSum%11!=1){
                    $scope.checkLock = false;
                    $scope.tipsText="身份证号码有误";
                    return false;
                }
                if(true){
                    $scope.checkLock = true;
                    return true;
                }
            }
            $scope.immediateActive=function(){
                //宝宝信息是否填写完整
                var name=$scope.info.babyName;
                var birthday=$("#birthday").val();
                var sex=$scope.sexItem == "boy"?1:0;
                if(typeof(name) == "undefined"){
                    alert("姓名不能为空");
                    return;
                }
                if(birthday == ""){
                    alert("请选择宝宝生日");
                    return;
                }
                
                //验证保护伞信息是否填写完整
                if($scope.info.id==""){
                    alert("请选择或添加一个宝宝");
                    return;
                }
                if($scope.info.parentName==""){
                    alert("请输入父(母)的姓名");
                    return;
                }
                if($scope.info.phoneNum==""){
                    alert("请输入手机号");
                    return;
                }
                if($scope.info.code==""){
                    alert("请填写验证码");
                    return;
                }
                if($scope.info.IdCard==""){
                    alert("请输入身份证号");
                    return;
                }
                var id=$scope.checkCode();
                if(!id){
                    return;
                }
                if($scope.info.id!="add"||$scope.info.id!="undefined"||$scope.info.id!=""){
                    var sname=$scope.selectedBaby.name;
                    var sbirthday=$filter('date')($scope.selectedBaby.birthday, 'yyyy-MM-dd');
                    var ssex=$scope.selectedBaby.sex;
                    var ssid=$scope.selectedBaby.id;

                    var name=$scope.info.babyName;
                    var birthday=$("#birthday").val();
                    var sex=$scope.sexItem == "boy"?1:0;
                    var id=parseInt($scope.info.id);
                    if(id==ssid){
                        if(sname!=name||sbirthday!=birthday||ssex!=sex){
                            $scope.updateBabyInfo();
                        }else{
                            $scope.updateUmbrellaInfo();
                        }
                    }else{
                        $scope.saveBabyInfo();
                    }
                }else{
                    $scope.saveBabyInfo();
                }
            };


            /*填写宝宝姓名 选择宝宝*/
            $scope.getCode = function(){
                $scope.checkPhone();
                if($scope.checkLock==false){
                    return;
                }
                var phone=$scope.info.phoneNum
                if(typeof(phone) == "undefined"||phone==""){
                    alert("手机号不能为空");
                    return;
                }
                IdentifyUser.save({"userPhone":$scope.info.phoneNum},function (data){
                    if(data.status=="1") {
                        $scope.codeSecond = 60;
                        $scope.codeButton = true;
                        $scope.getCodeSecond();
                    }else{
                        $scope.codeSecond="重新发送";
                        $scope.codeButton=false;
                    }
                });
            };

             $scope.getCodeSecond=function () {
                 $scope.codeSecond=$scope.codeSecond-1;
                var t;
                if($scope.codeSecond>0) {
                    t = setTimeout(function(){$scope.getCodeSecond()}, 1000);
                    $scope.$digest();
                }else{
                    clearTimeout(t);
                    $scope.codeSecond="再次获取验证码";
                    $scope.codeButton=false;
                    $scope.$digest();
                }
            };
            //添加宝宝
            $scope.saveBabyInfo = function(){
                if($scope.ifUpedateBabyInfo==true){
                    $scope.updateUmbrellaInfo();
                    return;
                }
                var name=$scope.info.babyName;
                var birthday=$("#birthday").val();
                var sex=$scope.sexItem == "boy"?1:0;
                if(typeof(name) == "undefined"){
                    alert("姓名不能为空");
                    return;
                }
                if(birthday == ""){
                    alert("请选择宝宝生日");
                    return;
                }
                saveBabyInfo.get({"sex":sex,"name":encodeURI(name),"birthDay":birthday}, function (data){
                    if(data.resultCode=='1'){
                        $scope.ifUpedateBabyInfo=true;
                        $scope.info.id=data.autoId;
                        $scope.updateUmbrellaInfo();
                    }
                });
            };

            /*保存宝宝信息*/
            $scope.updateBabyInfo = function(){
                if($scope.ifUpedateBabyInfo==true){
                    $scope.updateUmbrellaInfo();
                    return;
                }
                var name=$scope.info.babyName;
                var birthday=$("#birthday").val();
                var sex=$scope.sexItem == "boy"?1:0;
                var id=parseInt($scope.info.id);
                if(typeof(name) == "undefined"){
                    alert("姓名不能为空");
                    return;
                }
                if(birthday == ""){
                    alert("请选择宝宝生日");
                    return;
                }
                updateBabyInfo.get({"id":id,"sex":sex,"name":encodeURI(name),"birthDay":birthday}, function (data){
                    if(data.resultCode=='1'){
                        $scope.ifUpedateBabyInfo=true;
                        $scope.updateUmbrellaInfo();
                    }
                });
            };

            //更新保障信息
            $scope.updateUmbrellaInfo = function(){
                compareDate();
                $scope.buttonDis=true;
                $scope.parentType=$scope.parentItem=="father"?2:3;
                if($scope.ifExist) {
                    updateInfo.save({
                        "phone": $scope.info.phoneNum, "code": $scope.info.code, "babyId": $scope.info.id,
                        "idCard": $scope.info.IdCard, "parentName": encodeURI($scope.info.parentName),
                        "parentType": $scope.parentType, "umbrellaId": $scope.umbrellaId
                    }, function (data) {
                        if (data.result == '1') {
                            recordLogs("BHS_TXXX_LJJH");
                            window.location.href = "../wisdom/umbrella?value=" + new Date().getTime() + "#/umbrellaMemberList/" + $stateParams.id + "/" + $stateParams.status;
                        } else if (data.result == '3') {
                            alert("验证码无效");
                            $scope.buttonDis=false;
                            return;
                        } else {
                            alert("更新保障信息失败");
                            $scope.buttonDis=false;
                            return;
                        }
                    });
                }else {
                    var bname = $scope.info.babyName;
                    var bbirthday = $("#birthday").val();
                    var bsex = $scope.sexItem == "boy" ? 1 : 0;
                    newJoinUs.save({
                        "phone": $scope.info.phoneNum, "code": $scope.info.code, "babyId": $scope.info.id,
                        "idCard": $scope.info.IdCard, "parentName": encodeURI($scope.info.parentName), "bname": bname,
                        "parentType": $scope.parentType, "bbirthDay": bbirthday, "bsex": bsex,"shareId":$stateParams.id
                    }, function (data) {
                        if (data.result == '1') {
                            recordLogs("BHS_TXXX_LJJH");
                            if ($scope.ifExist) {
                                window.location.href = "umbrella#/umbrellaJoin/" + new Date().getTime() + "/" + $stateParams.id;
                            } else {
                                window.location.href = "http://s201.xiaork.com/keeper/wxPay/patientPay.do?serviceType=umbrellaPay&shareId=" + $stateParams.id;
                            }
                        } else if (data.result == '3') {
                            alert("验证码无效");
                            $scope.buttonDis=false;
                            return;
                        } else {
                            alert("更新保障信息失败");
                            $scope.buttonDis=false;
                            return;
                        }
                    });
                }
            };

            var recordLogs = function(val){
                $.ajax({
                    url:"util/recordLogs",// 跳转到 action
                    async:true,
                    type:'get',
                    data:{logContent:encodeURI(val)},
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                    },
                    error : function() {
                    }
                });
            };
            
            function compareDate() {
                var last = new Date();
                var now = $("#birthday").val();
                now = new Date(Date.parse(now.replace(/-/g, "/")));
                var days = compareDate(new Date(now).Format("yyyy-MM-dd"), new Date(last).Format("yyyy-MM-dd"));
                var fourthDay = 14 * 365;
                if (days > fourthDay) {
                    alert("目前还只服务于0-14岁的宝宝哦~ ");
                    return;
                }
            }
            //计算两个日期的时间间隔
            function compareDate(start,end){
                if(start==null||start.length==0||end==null||end.length==0){
                    return 0;
                }

                var arr=start.split("-");
                var starttime=new Date(arr[0],parseInt(arr[1]-1),arr[2]);
                var starttimes=starttime.getTime();

                var arrs=end.split("-");
                var endtime=new Date(arrs[0],parseInt(arrs[1]-1),arrs[2]);
                var endtimes=endtime.getTime();

                var intervalTime = endtimes-starttimes;//两个日期相差的毫秒数 一天86400000毫秒
                var Inter_Days = ((intervalTime).toFixed(2)/86400000)+1;//加1，是让同一天的两个日期返回一天

                return Inter_Days;
            }
            $scope.$on('$ionicView.enter', function(){

                $.ajax({
                    url:"umbrella/getOpenid",// 跳转到 action
                    async:true,
                    type:'post',
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                        if(data.openid=="none"){
                            window.location.href = "http://s201.xiaork.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s201.xiaork.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
                            // window.location.href = "http://s2.xiaork.cn/keeper/wechatInfo/fieldwork/wechat/author?url=http://s2.xiaork.cn/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
                        }
                    },
                    error : function() {
                    }
                });
                

                  //根据Openid 判断用户是否领取过
                    ifExistOrder.save(function (data){
                        $scope.info.phoneNum=data.phone;
                         if(data.result=="3"){
                            window.location.href="../wisdom/firstPage/umbrella?id="+$stateParams.id;
                        }else{
                             if(data.result=="2" && data.umbrella.pay_result=="success"){
                                 $scope.ifExist=true;
                             }
                            getOpenidStatus.save(function (data){
                                $scope.openid=data.openid;
                                //获取用户下宝宝信息列表
                                getBabyinfoList.save({"openId":$scope.openid},function (data){
                                    if(data.babyInfoList!="") {
                                        $scope.babyInfoList = data.babyInfoList;
                                        // var addBaby=new Object();
                                        // addBaby.name="添加";
                                        // addBaby.id="add";
                                        // $scope.babyInfoList.unshift(addBaby);
                                    }
                                });
                            });
                             var timestamp;//时间戳
                             var nonceStr;//随机字符串
                             var signature;//得到的签名
                             var appid;//得到的签名
                             $.ajax({
                                 url:"wechatInfo/getConfig",// 跳转到 action
                                 async:true,
                                 type:'get',
                                 data:{url:location.href.split('#')[0]},//得到需要分享页面的url
                                 cache:false,
                                 dataType:'json',
                                 success:function(data) {
                                     if(data!=null ){
                                         timestamp = data.timestamp;//得到时间戳
                                         nonceStr = data.nonceStr;//得到随机字符串
                                         signature = data.signature;//得到签名
                                         appid = data.appid;//appid
                                         //微信配置
                                         wx.config({
                                             debug: false,
                                             appId: appid,
                                             timestamp:timestamp,
                                             nonceStr: nonceStr,
                                             signature: signature,
                                             jsApiList: [
                                                 'chooseWXPay'
                                             ] // 功能列表
                                         });
                                         wx.ready(function () {
                                             wx.hideOptionMenu();
                                             // config信息验证后会执行ready方法，
                                             // 所有接口调用都必须在config接口获得结果之后，
                                             // config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，
                                             // 则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，
                                             // 则可以直接调用，不需要放在ready函数中。
                                         });

                                     }else{
                                     }
                                 },
                                 error : function() {
                                 }
                             });

                        var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
                        $("#birthday").mobiscroll().date();
                        //初始化日期控件
                        var opt = {
                            preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                            theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
                            display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
                            mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
                            lang:'zh',
                            dateFormat: 'yyyy-mm-dd', // 日期格式
                            setText: '确定', //确认按钮名称
                            cancelText: '取消',//取消按钮名籍我
                            dateOrder: 'yyyymmdd', //面板中日期排列格式
                            dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
                            showNow: false,
                            nowText: "今",
                            //startYear:1980, //开始年份
                            //endYear:currYear //结束年份
                            minDate: new Date(2002,date.substring(5,7)-1,date.substring(8,10)),
                            maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10))
                            //endYear:2099 //结束年份
                        };
                        $("#birthday").mobiscroll(opt);
                    }
                });

            });


    }]);