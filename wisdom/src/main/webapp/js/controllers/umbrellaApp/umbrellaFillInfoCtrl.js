angular.module('controllers', ['ionic']).controller('umbrellaFillInfoCtrl', [
    '$scope','$state','CheckPhone','SaveMessage',
    function ($scope,$state,CheckPhone,SaveMessage) {
        $scope.title="宝护伞-宝大夫儿童家庭重疾互助计划";
        $scope.sexItem = 1;//男孩
        $scope.parentItem = 3;//母亲
        $scope.checkLock = true;
        $scope.info={};
        $scope.codeSecond="获取";
        $scope.babyInfoList={};
        $scope.codeButton = false;
        $scope.protocolLock=false;
        $scope.buttonDis = true;
        $scope.pgLock = false;//判断手机号是否购买过保护伞
        $scope.pgbut = false;//关注宝大夫按钮
        $scope.pgbut2 = false;//确定按钮
        $scope.lgLock = false;//关注宝大夫

        $scope.$on('$ionicView.enter', function(){
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
                minDate: new Date(2002,date.substring(5,7)-1,date.substring(8,10)),
                maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10)),
                onSelect: function (valueText) {
                    var day = compareDate(valueText,moment().format("YYYY-MM-DD"));
                    if(day>365*14){
                        alert("目前还只服务于0-14岁的宝宝哦~");
                        $("#birthday").val("");
                    }
                }
            };
            $("#birthday").mobiscroll(opt);

        });


        //点击输入框跳转到相应位置
        $scope.skip = function(item){
            $(".view,html,body").stop().animate({"scrollTop":$("#"+item).offset().top},0);

        }

        //判断宝宝名字，家长名字，宝宝生日
        $scope.checkName = function (index) {
            if(index==0){
                if($scope.info.babyName==""||$scope.info.babyName==undefined){
                    alert("宝宝姓名不能为空！");
                }
            }
            if(index==1){
                if($("#birthday").val()==""||$("#birthday").val()==undefined){
                    alert("宝宝生日不能为空！");
                }
            }
            if(index==2){
                if($scope.info.parentName==""||$scope.info.parentName==undefined){
                 alert("家长姓名不能为空！");
                }
            }
        }
        
        //选择孩子性别
        $scope.selectSex = function(sex){
            $scope.sexItem=sex;
        };

        //选择父母性别
        $scope.selectParent = function(parent){
            $scope.parentItem=parent;
        };

        //校验手机号
        $scope.checkPhone= function(){
            if($scope.info.phoneNum==""||$scope.info.phoneNum==undefined){
                alert("手机号不能为空！");
            }else{
                if (String($scope.info.phoneNum).match(/^1[3578]\d{9}$/)){
                    $scope.checkLock = true;
                    $scope.codeButton = false;
                }
                else{
                    $scope.checkLock = false;
                    $scope.tipsText="手机号有误";
                    $scope.codeButton = true;
                }
            }
        }

        //获取手机验证码
        $scope.getCode = function(){
            if($scope.checkLock==true&&$scope.info.phoneNum!=""&&$scope.info.phoneNum!=undefined){
                CheckPhone.save({"userPhone":$scope.info.phoneNum.toString()},function (data) {
                    console.log("data",data);
                    if(data.result=="0") {//该用户没有购买过保护伞
                        $scope.codeSecond = 60;
                        $scope.codeButton = true;
                        getCodeSecond();
                    }else if(data.result=="1"){//该用户已经购买过保护伞，但是没有关注宝大夫
                        $scope.pgLock = true;
                        $scope.pgbut = true;
                        $scope.pgcontent = "您已经加入保护伞";
                        $scope.pgcontent2 = "可关注宝大夫查看";
                        $scope.pgbutton = "关注宝大夫";
                    }else if(data.result=="2"){//该用户已经购买过保护伞且关注宝大夫
                        $scope.pgLock = true;
                        $scope.pgbut2 = true;
                        $scope.pgcontent = "您已经加入保护伞";
                        $scope.pgcontent2 = "可登陆宝大夫平台查看";
                        $scope.pgbutton = "确定";
                    }
                });
            }else{
                alert("手机号有误！");
            }
        }

        var getCodeSecond=function () {
            $scope.codeSecond=$scope.codeSecond-1;
            var timeID;
            if($scope.codeSecond>0) {
                timeID = setTimeout(function(){getCodeSecond()}, 1000);
                $scope.$digest();
            }else{
                clearTimeout(timeID);
                $scope.codeSecond="再次获取验证码";
                $scope.codeButton=false;
                $scope.$digest();
            }
        }

        //查看我的保障
        $scope.lookSafeguard = function () {
            $scope.pgbut = false;
            $scope.pgLock = false;
            $scope.lgLock = true;
        }
        
        //校验身份证号
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
                $scope.buttonDis = false;
                return true;
            }
        }

        //查看宝大夫儿童家庭重疾互助计划公约
        $scope.lookProtocol = function(){
            $scope.protocolLock=true;
        }

        //关闭 查看宝大夫儿童家庭重疾互助计划公约
        $scope.cancelProtocol = function(){
            $scope.protocolLock=false;
            $scope.pgLock = false;
            $scope.pgbut = false;
            $scope.pgbut2 = false;
            $scope.lgLock = false
        }

        //激活保存宝宝信息
        $scope.immediateActive=function() {
            if($scope.info.babyName==""||$scope.info.babyName==undefined||$("#birthday").val()==""||$("#birthday").val()==undefined||
                $scope.info.parentName==""||$scope.info.parentName==undefined||$scope.info.phoneNum==""||$scope.info.phoneNum==undefined
                ||$scope.info.code==""||$scope.info.code==undefined){
                alert("信息不能为空！");
            }else{
                SaveMessage.save({"phone":$scope.info.phoneNum,"code":$scope.info.code,"sex":$scope.sexItem,"birthDay":$("#birthday").val(),
                                "name":$scope.info.babyName,"idCard":$scope.info.IdCard,"parentName":$scope.info.parentName,"parentType":$scope.parentItem},
                                function (data) {
                                    console.log("data",data);
                                    if(data.result=="0"){
                                        alert("验证码有误，请重新输入！");
                                    }else if(data.status==1&&data.result=="1"){
                                        $state.go("umbrellaPay");
                                    }
                                })

            }
        }

        //手机号、身份证号重新获取焦点
        $scope.backCheck = function () {
            $scope.checkLock = true;
            $scope.tipsText="";
        }

        var recordLogs = function(val){

        }

        //计算两个日期的时间间隔
        var compareDate = function (start,end){
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



    }]);
