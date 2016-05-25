angular.module('controllers', ['ionic']).controller('umbrellaFillInfoCtrl', [
        '$scope','$state','$stateParams','getBabyinfoList','getOpenidStatus',
        function ($scope,$state,$stateParams,getBabyinfoList,getOpenidStatus) {
            $scope.title="宝护伞";
            $scope.sexItem = "boy";
            $scope.parentItem = "mother";
            $scope.checkLock = true;
            $scope.fillLock = false;
            $scope.selectItem = "";
            $scope.info={};

            /*填写宝宝姓名 获取焦点*/
            $scope.fillName = function(){
                $scope.fillLock = true;
            };
            /*填写宝宝姓名 失去焦点*/
            $scope.fillFinish = function(){
                $scope.fillLock = false;
            };
            /*填写宝宝姓名 失去焦点*/
            $scope.selectBaby = function(selectItem){
                $scope.info.babyName = selectItem;
                console.log("babyName"+ $scope.selectItem);
            };
            /*选择性别*/
            $scope.selectSex = function(sex){
                $scope.sexItem=sex;
            };
            /*选择父母*/
            $scope.selectParent = function(parent){
                $scope.parentItem=parent;
            };
            /*校验手机号*/
            $scope.checkPhone= function(){
                var phoneNumber = $scope.info.phoneNum+"";
                if (phoneNumber.match(/^1[3578]\d{9}$/)){
                    $scope.checkLock = true;
                }
                else{
                    $scope.checkLock = false;
                    $scope.tipsText="手机号有误";
                }
            }
            /*校验验证码*/
            $scope.checkCode= function(){

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
                $state.go("umbrellaJoin");
            };
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
                    //startYear:1980, //开始年份
                    //endYear:currYear //结束年份
                    minDate: new Date(2002,date.substring(5,7)-1,date.substring(8,10)),
                    maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10))
                    //endYear:2099 //结束年份
                };
                $("#birthday").mobiscroll(opt);
            });

            getOpenidStatus.save({"openId":""},function (data){
                $scope.babyInfoList=data.babyInfoList;
            });
            //获取用户下宝宝信息列表
            getBabyinfoList.save({"openId":"o3_NPwrrWyKRi8O_Hk8WrkOvvNOk"},function (data){
                $scope.babyInfoList=data.babyInfoList;
            });
    }]);