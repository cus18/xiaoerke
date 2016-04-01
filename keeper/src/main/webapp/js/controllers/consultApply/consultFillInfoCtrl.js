angular.module('controllers', ['ionic']).controller('consultFillInfoCtrl', [
    '$scope','$state','$stateParams','$ionicModal','GetUserLoginStatus','$location',
    'modifyBabyIndfo','getBabyinfoList',
    function ($scope,$state,$stateParams,$ionicModal,GetUserLoginStatus,$location,
    		modifyBabyIndfo,getBabyinfoList) {
        $scope.title0 = "填写资料";
        $scope.title = "填写资料";
        $scope.selectClassifyLock = false; //选择图片的分类开关
        $scope.img={};
        $scope.babyListIndex=$stateParams.index;
        $scope.img1Type = "";
        $scope.img2Type = "";
        $scope.img3Type = "";
        $scope.img4Type = "";

      /* 选择图片分类*/
        $scope.selectClassify = function(){
            $scope.selectClassifyLock = true;
        };
        //取消上传图片
        $scope.cancel = function(){
            $scope.selectClassifyLock = false;
        };
        //选择宝宝
        $scope.selectBaby= function(){
        	var index=$scope.babyListIndex;
            window.location.href = "/xiaoerke-appoint/appoint#/healthRecordSelectBaby/"+index+",1,"+$stateParams.conid;
        };

        $scope.consultHistory = function(){
            $state.go("consultHistory");
        };
        
        var caseImg = [];
        var resultImg= [];
        var positionImg = [];
        var otherImg = [];
        var all = [];
        /**
         * 上传病历图像
         */
        $scope.chooseImage = function(sort){
//            $scope.photoLock="false";
        	if(all.length>=4){
        		alert("最多上传4张图片");
        		return;
        	}
        	$scope.selectClassifyLock = false; //选择图片的分类开关
            wx.chooseImage({
                count: 1, // 默认9
                sizeType: ['original','compressed'], // 可以指定是原图还是压缩图，默认二者都有
                sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                success: function (res) {
                    //img.src = res.localIds[0]; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                    //var img=new Image();
                    //img.src=res.localIds;
                    //if(img.fileSize>0){
                    //    if(img.fileSize>5*1024){
                    //        alert("图片不大于5M。");
                    //         return;
                    //     }
                    //    return;
                    //}
                    var imgId = res.localIds[0];

                switch(all.length+1)
               	 {
               	 case 1:
               	   	$("#img1").attr("src",imgId);
               	   	$scope.img1Type = sort;
               	   	all.push(imgId);
                    $scope.upLoadImg(sort,imgId);
               	   break;
               	 case 2:
               		 $("#img2").attr("src",imgId);
               		 $scope.img2Type = sort;
               		 all.push(imgId);
                     $scope.upLoadImg(sort,imgId);
               	   break;
               	 case 3:
               		 $("#img3").attr("src",imgId);
               		$scope.img3Type = sort;
               		all.push(imgId);
                     $scope.upLoadImg(sort,imgId);
                 	   break;
               	 case 4:
               		 $("#img4").attr("src",imgId);
               		$scope.img4Type = sort;
               		all.push(imgId);
                     $scope.upLoadImg(sort,imgId);
                 	   break;
               	 }
                }
            });

            //if(sort =="caseImg"){
            //    setchooseImag(document.getElementById('img1'),sort);
            //}
            //if(sort =="resultImg"){
            //    setchooseImag(document.getElementById('img2'),sort);
            //}
            //if(sort =="positionImg"){
            //    setchooseImag(document.getElementById('img3'),sort);
            //}
            //if(sort =="otherImg"){
            //    setchooseImag(document.getElementById('img4'),sort);
            //}


        }

        function setchooseImag(imageId,sort){
            wx.chooseImage({
                    count: 1, // 默认9
                    sizeType: ['original','compressed'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: function (res) {
                        imageId.src = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        //alert(sort);
                        if(sort =="caseImg"){
                            caseImg.push(res.localIds[0]);
                            all.push(res.localIds[0]);

                        }
                        if(sort =="resultImg"){
                            //all.resultImg = res.localIds[0];
                            resultImg.push(res.localIds[0]);
                            all.push(res.localIds[0]);
                        }
                        if(sort =="positionImg"){
                            //all.positionImg = res.localIds[0];
                            positionImg.push(res.localIds[0]);
                            all.push(res.localIds[0]);
                        }
                        if(sort =="otherImg"){
                            //all.otherImg = res.localIds[0];
                            otherImg.push(res.localIds[0]);
                            all.push(res.localIds[0]);
                        }

                    }
            });

        }
        
        $scope.cancelUpLoad = function(index){
        	switch(index)
          	 {
          	 case 1:
          		$("#img1").attr("src","images/aa.png");
                $("#img1ServiceID").val("");
          	   	$scope.img1Type = "";
                 leftImgInfo(2);
          	   	all.splice(index-1,1);
          	   break;
          	 case 2:
          		 $("#img2").attr("src","images/aa.png");
                 $("#img2ServiceID").val("");
          		 $scope.img2Type = "";
                 leftImgInfo(3);
          		all.splice(index-1,1);
          	   break;
          	 case 3:
          		 $("#img3").attr("src","images/aa.png");
          		$scope.img3Type = "";
                 leftImgInfo(3);
          		all.splice(index-1,1);
            	   break;
          	 case 4:
          		 $("#img4").attr("src","images/aa.png");
          		$scope.img4Type = "";
                 leftImgInfo(4);
          		all.splice(index-1,1);
            	   break;
          	 }
        };
        function leftImgInfo(i){
            for(var int=i;int<=all.length;int++){

                switch(int)
                {
	                case 1:
	                    $("#img1").attr("src",all[1]);
	                    $scope.img1Type = $scope.img2Type;
	                    $("#img1ServiceID").val($("#img2ServiceID").val());
                        if(int==all.length){
                            $("#img2").attr("src","images/aa.png");
                        }
	                    break;
	                case 2:
	                    $("#img2").attr("src",all[2]);
	                    $scope.img2Type = $scope.img3Type;
	                    $("#img2ServiceID").val($("#img3ServiceID").val());
                        if(int==all.length){
                            $("#img3").attr("src","images/aa.png");
                        }
	                    break;
	                case 3:
	                    $("#img3").attr("src",all[3]);
	                    $scope.img3Type = $scope.img4Type;
	                    $("#img3ServiceID").val($("#img4ServiceID").val());
                        if(int==all.length){
                            $("#img4").attr("src","images/aa.png");
                        }
	                    break;
                }
            }
        }


        $scope.doRefresh = function(){
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
                        timestamp=data.timestamp;//得到时间戳
                        nonceStr=data.nonceStr;//得到随机字符串
                        signature=data.signature;//得到签名
                        appid=data.appid;//appid
                        //微信配置
                        wx.config({
                            debug: false,
                            appId: appid,
                            timestamp:timestamp,
                            nonceStr: nonceStr,
                            signature: signature,
                            jsApiList: [
                                'onMenuShareTimeline',
                                'onMenuShareAppMessage',
                                'onMenuShareWeibo',
                                'chooseImage',
                                'uploadImage',
                                'downloadImage'
                            ] // 功能列表
                        });
                    }else{
                    }
                },
                error : function() {
                }
            });
        }
        
        $scope.upLoadImg = function (type,imgId){
        	//for (var int = 1; int <= all.length; int++) {
        	//	var imgId=$("#img"+int)[0].src;
        	//	var type="";
        	//	switch(int)
             //	 {
             //	 case 1:
             //		type=$scope.img1Type;
             //	   break;
             //	 case 2:
             //		type=$scope.img2Type;
             //	   break;
             //	 case 3:
             //		type=$scope.img3Type;
             //  	   break;
             //	 case 4:
             //		type=$scope.img4Type;
             //  	   break;
             //	 }
        		//上传图片接口
                wx.uploadImage({
                    localId: imgId, // 需要上传的图片的本地ID，由chooseImage接口获得
                    isShowProgressTips: 1, // 默认为1，显示进度提示
                    success: function (res) {
                    	 var serverId = res.serverId; // 返回图片的服务器端ID
                         var index=all.length;
                         $("#img"+index+"ServiceID").val(serverId);
                         $("#img"+index+"ServiceType").val(type);
                    	 //if("caseImg"==type){
                	     //   //病例图片集合
                          //   //alert("if   "+serverId);
                    		// caseImg.push(serverId);
                	     // }else if("resultImg"==type){
                	     //   //患病部位图集合
                	    	//  resultImg.push(serverId);
                	     // }else if("positionImg"==type){
                    	 //   //诊断结果图片集合
                	    	//  positionImg.push(serverId);
                	     // }else if("otherImg"==type){
                    	 //       //其他图片合集
                	    	//  otherImg.push(serverId);
                	     // }

                    }
                 });

			//}

       };
        
        /**
         * 添加病情描述
         */
        $scope.modifyBabyIndfo = function(){
            //$scope.upLoadImg();
        	var id=$scope.babyId;
        	var illnessDescribe=$("#illnessDescribe").val();
        	if(illnessDescribe.length<10||typeof(illnessDescribe) == "undefined"||illnessDescribe==""){
        		alert("填写资料尽可能详细描述病情，治疗情况以及想要获得的帮助（至少10个字）");
        		return;
        	}
            for(var  a=0;a<all.length;a++){
                var b=a+1;

                var type=$("#img"+b+"ServiceType").val();

                if(type=="caseImg"){
                    caseImg.push($("#img"+b+"ServiceID").val());
                }
                if(type=="resultImg"){
                	resultImg.push($("#img"+b+"ServiceID").val());
                }
                if(type=="positionImg"){
                	positionImg.push($("#img"+b+"ServiceID").val());
                }
                if(type=="otherImg"){
                	otherImg.push($("#img"+b+"ServiceID").val());
                }
            }

            var json="{\"babyId\":"+id+",\"illnessDescribe\":"+illnessDescribe+"";
            if(caseImg.length!=0) {
                var caseImgs=caseImg.toString();
                json += ",\"caseImg\":" + caseImgs + "";
            }
            if(resultImg.length!=0) {
                json += ",\"resultImg\":" + resultImg.toString() + "";
            }
            if(positionImg!=0) {
                json += ",\"positionImg\":" + positionImg.toString() + "";
            }
            if(otherImg.length!=0) {
                json += ",\"otherImg\":" + otherImg.toString() + "";
            }
            json+="}";
            //alert(json);
            var conversationid=$stateParams.conid;
            modifyBabyIndfo.save({babyId:id,illnessDescribe:illnessDescribe,caseImg:caseImg.toString(),resultImg:resultImg.toString(),positionImg:positionImg.toString(),otherImg:otherImg.toString(),conversation_id:conversationid}, function (data){
                if(data.state=='1'){
//                	window.location.href = "ap/logout";
                    WeixinJSBridge.call('closeWindow');
                }
            });
        }
        
        $scope.$on('$ionicView.enter',function() {
            var routePath = "/ap/healthBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                	//获取用户下宝宝信息列表
                    getBabyinfoList.save({"openId":""},function (data){
                        if(data.babyInfoList==""){
                        }else{
                        	var index=0;
                        	if(typeof($scope.babyListIndex) != "undefined"){
                        		index=$scope.babyListIndex;
                        	}
                            var name=data.babyInfoList[index].name;
                            $scope.babyName=name;
                            $scope.babyId=data.babyInfoList[index].id;
                        }
                    });
                }
            })
        });
    }])
