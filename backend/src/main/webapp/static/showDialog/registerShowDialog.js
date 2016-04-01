function showWinSelf(mode, msg, t, sd_width) {
	var sd_width = sd_width ? sd_width : 400;
	var mode = in_array(mode, ['confirm', 'window', 'info', 'loading']) ? mode : 'alert';
	var t = t ? t : "提示信息";
	var msg = msg ? msg : "";
	var confirmtxt = confirmtxt ? confirmtxt : "确定";
	var canceltxt = canceltxt ? canceltxt : "取消";
	sd_remove();
	try {
		if(typeof document.body.style.maxHeight === "undefined") {
			$("body","html").css({height: "100%", width: "100%"});
			if(document.getElementById("SD_HideSelect") === null) {
				$("body").append("<iframe id='SD_HideSelect'></iframe><div id='SD_overlay'></div>");
			}
		} else {
			if(document.getElementById("SD_overlay") === null) {
				$("body").append("<div id='SD_overlay'></div>");
			}
		}
		if(mode == "alert") {
			if(detectMacXFF()) {
				$("#SD_overlay").addClass("SD_overlayMacFFBGHack");
			} else {
				$("#SD_overlay").addClass("SD_overlayBG");
			}
		} else {
			if(detectMacXFF()) {
				$("#SD_overlay").addClass("SD_overlayMacFFBGHack2");
			} else {
				$("#SD_overlay").addClass("SD_overlayBG2");
			}
		}
		$("body").append("<div id='SD_window'></div>");
		var SD_html;
		SD_html = "";
		SD_html += "<table cellspacing='0' cellpadding='0'><tbody><tr><td class='SD_bg'></td><td class='SD_bg'></td><td class='SD_bg'></td></tr>";
		SD_html += "<tr><td class='SD_bg'></td>";
		SD_html += "<td id='SD_container'>";
		SD_html += "<h3 id='SD_title'>" + t + "</h3>";
		SD_html += "<div id='SD_body'><div id='SD_content'>" + msg + "</div></div>";
		SD_html += "<div id='SD_button'><div class='SD_button'>";
		SD_html += "<a id='SD_confirm'>" + confirmtxt + "</a>";
		SD_html += "<a id='SD_cancel'>" + canceltxt + "</a>";
		SD_html += "</div></div>";
		SD_html += "<a href='javascript:;' id='SD_close' title='关闭'></a>";
		SD_html += "</td>";
		SD_html += "<td class='SD_bg'></td></tr>";
		SD_html += "<tr><td class='SD_bg'></td><td class='SD_bg'></td><td class='SD_bg'></td></tr></tbody></table>";
		$("#SD_window").append(SD_html);
		$("#SD_confirm").bind("click", function(){
			if($("#operType").val()=="add"){
				var intervalFlag = "no";
				if(document.getElementById("operCopy").checked==true){
					  if(document.getElementById("operInterval1").checked==true){
						  intervalFlag="0";
					  }else if(document.getElementById("operInterval2").checked==true){
						  intervalFlag="1";
					  }
				}
				addRegister($("#operPrice").val(),$("#operServerType").val(),$("#time").val(),intervalFlag);
			}
			var operCopy = "";
			if(document.getElementById("operCopy").checked==true){
				operCopy = "yes";
			}
			if($("#operType").val()=="update"){
				updateRegister($("#operPrice").val(),$("#operServerType").val(),$("#time").val(),$("#registerId").val(),operCopy);
			}
			if($("#operType").val()=="del"){
				deleteRegister($("#time").val(),$("#registerId").val(),operCopy);
			}
		});
		$("#SD_cancel,#SD_close").bind("click", function(){
			sd_remove();
		});
		if(mode == "info" || mode == "alert") {
			$("#SD_cancel").hide();
			$("#SD_button").show();
		}
		if(mode == "window") {
			$("#SD_button").show();
			$("#SD_close").show();
		}
		if(mode == "confirm") {
			$("#SD_button").show();
		}
		var sd_move = false;
		var sd_x, sd_y;
		$("#SD_container > h3").click(function(){}).mousedown(function(e){
			sd_move = true;
			sd_x = e.pageX - parseInt($("#SD_window").css("left"));
			sd_y = e.pageY - parseInt($("#SD_window").css("top"));
		});
		$(document).mousemove(function(e){
			if(sd_move){
				var x = e.pageX - sd_x;
				var y = e.pageY - sd_y;
				$("#SD_window").css({left:x, top:y});
			}
		}).mouseup(function(){
			sd_move = false;
		});
		$("#SD_body").width(sd_width - 50);
		sd_load(sd_width);
		$("#SD_window").show();
		$("#SD_window").focus();
	} catch(e) {
		alert("System Error !");
	}
}
function registerShowWindow(title, the_url, sd_width){
	var sd_width = sd_width ? sd_width : 400;
	$.ajax({
		type		: "GET",
		dataType	: "html",
		cache		: false,
		timeout		: 10000,
		url			: the_url,
		data		: "isajax=1",
		success		: function(data){
			showWinSelf("window", data, title, sd_width);
		},
		error		: function(data){
			showDialog("alert", "读取数据失败");
		},
		beforeSend	: function(data){
			showDialog("loading", "正在读取数据...");
		}
	});
}