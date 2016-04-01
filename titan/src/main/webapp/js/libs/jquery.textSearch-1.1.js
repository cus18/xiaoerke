
// by zhangxixnu 2010-06-21  welcome to visit my personal website http://www.zhangxinxu.com/
// textSearch.js v1.0 ���֣��ؼ��ֵ�ҳ�洿�ͻ����ѽH
// 2010-06-23 �޸�����ĸ������ǩ���������
// 2010-06-29 �޸�ҳ��ע����ʾ�����c
// 2013-05-07 �޸��������عؼ��ְ���֮ǰ�����ؼ���û�н�������c
// ���ۺ����������ر�����������? 


(function($){
	$.fn.textSearch = function(str,options){
		var defaults = {
			divFlag: true,
			divStr: " ",
			markClass: "",
			markColor: "red",
			nullReport: true,
			callback: function(){
				return false;
			}
		};
		var sets = $.extend({}, defaults, options || {}), clStr;
		if(sets.markClass){
			clStr = "class='"+sets.markClass+"'";
		}else{
			clStr = "style='color:"+sets.markColor+";'";
		}

		//��ǰһ�θ�����������ֻ�ԭ		
		$("span[rel='mark']").each(function() {
			var text = document.createTextNode($(this).text());
			$(this).replaceWith($(text));
		});


		//�ַ���������ʽ�ؼ���ת��
		$.regTrim = function(s){
			var imp = /[\^\.\\\|\(\)\*\+\-\$\[\]\?]/g;
			var imp_c = {};
			imp_c["^"] = "\\^";
			imp_c["."] = "\\.";
			imp_c["\\"] = "\\\\";
			imp_c["|"] = "\\|";
			imp_c["("] = "\\(";
			imp_c[")"] = "\\)";
			imp_c["*"] = "\\*";
			imp_c["+"] = "\\+";
			imp_c["-"] = "\\-";
			imp_c["$"] = "\$";
			imp_c["["] = "\\[";
			imp_c["]"] = "\\]";
			imp_c["?"] = "\\?";
			s = s.replace(imp,function(o){
				return imp_c[o];
			});
			return s;
		};
		$(this).each(function(){
			var t = $(this);
			str = $.trim(str);
			if(str === ""){
				alert("�ؼ���Ϊ��");
				return false;
			}else{
				//���ؼ���push������֮د
				var arr = [];
				if(sets.divFlag){
					arr = str.split(sets.divStr);
				}else{
					arr.push(str);
				}
			}
			var v_html = t.html();
			//ɾ��ע��
			v_html = v_html.replace(/<!--(?:.*)\-->/g,"");

			//��HTML����֧��ΪHTMLƬ�κ�����Ƭ�Σ���������Ƭ�����������滻������HTMLƬ����֮����
			var tags = /[^<>]+|<(\/?)([A-Za-z]+)([^<>]*)>/g;
			var a = v_html.match(tags), test = 0;
			$.each(a, function(i, c){
				if(!/<(?:.|\s)*?>/.test(c)){//�Ǳ�q
					//��ʼִ�����
					$.each(arr,function(index, con){
						if(con === ""){return;}
						var reg = new RegExp($.regTrim(con), "g");
						if(reg.test(c)){
							//�����滻
							c = c.replace(reg,"?"+con+"��");
							test = 1;
						}
					});
					c = c.replace(/?/g,"<span rel='mark' "+clStr+">").replace(/��/g,"</span>");
					a[i] = c;
				}
			});
			//��֧��������������ַ���
			var new_html = a.join("");

			$(this).html(new_html);

			if(test === 0 && sets.nullReport){
				alert("û���������");
				return false;
			}

			//ִ�лص�����
			sets.callback();
		});
	};
})(jQuery);// JavaScript Document