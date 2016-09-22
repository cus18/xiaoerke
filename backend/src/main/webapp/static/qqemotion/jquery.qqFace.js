// QQ表情插件
(function($){  
	$.fn.qqFace = function(options){
		var defaults = {
			id : 'facebox',
			path : 'face/',
			assign : 'content',
			tip : 'em_'
		};
		var option = $.extend(defaults, options);
		var id = option.id;
		var path = option.path;
		var tip = option.tip;
		/*
		if(assign.length<=0){
			alert('缺少表情赋值对象。');
			return false;
		}*/
		
		$(this).click(function(e){
			var strFace, labFace;
			if($('#'+id).length<=0){
				strFace = '<div id="'+id+'" style="position:absolute;display:none;z-index:1000;" class="qqFace">' +
							  '<table border="0" cellspacing="0" cellpadding="0"><tr>';
				for(var i=1; i<=75; i++){
					labFace = '['+tip+i+']';
					labFace = emotionSendFilter(labFace);
					strFace += '<td><img src="'+path+i+'.gif" onclick="$(\'#'+option.assign+'\').setCaret();$(\'#'+option.assign+'\').insertAtCaret(\'' + labFace + '\');" /></td>';
					if( i % 15 == 0 ) strFace += '</tr><tr>';
				}
				strFace += '</tr></table></div>';
			}
			$(this).parent().append(strFace);
			var offset = $(this).position();
			var top = offset.top + $(this).outerHeight();
			$('#'+id).css('top',top);
			$('#'+id).css('left',offset.left);
			$('#'+id).show();
			e.stopPropagation();
		});

		$(document).click(function(){
			$('#'+id).hide();
			$('#'+id).remove();
		});
	};

})(jQuery);

jQuery.extend({ 
unselectContents: function(){ 
	if(window.getSelection) 
		window.getSelection().removeAllRanges(); 
	else if(document.selection) 
		document.selection.empty(); 
	} 
}); 
jQuery.fn.extend({ 
	selectContents: function(){ 
		$(this).each(function(i){ 
			var node = this; 
			var selection, range, doc, win; 
			if ((doc = node.ownerDocument) && (win = doc.defaultView) && typeof win.getSelection != 'undefined' && typeof doc.createRange != 'undefined' && (selection = window.getSelection()) && typeof selection.removeAllRanges != 'undefined'){ 
				range = doc.createRange(); 
				range.selectNode(node); 
				if(i == 0){ 
					selection.removeAllRanges(); 
				} 
				selection.addRange(range); 
			} else if (document.body && typeof document.body.createTextRange != 'undefined' && (range = document.body.createTextRange())){ 
				range.moveToElementText(node); 
				range.select(); 
			} 
		}); 
	}, 

	setCaret: function(){
		if(!$.browser.msie) return;
		var initSetCaret = function(){ 
			var textObj = $(this).get(0);
			textObj.caretPos = document.selection.createRange().duplicate();
		}; 
		$(this).click(initSetCaret).select(initSetCaret).keyup(initSetCaret);
	},

	insertAtCaret: function(textFeildValue){
		var textObj = $(this).get(0);
		if(document.all && textObj.createTextRange && textObj.caretPos){
			var caretPos=textObj.caretPos; 
			caretPos.text = caretPos.text.charAt(caretPos.text.length-1) == '' ? 
			textFeildValue+'' : textFeildValue; 
		} else if(textObj.setSelectionRange){ 
			var rangeStart=textObj.selectionStart; 
			var rangeEnd=textObj.selectionEnd; 
			var tempStr1=textObj.value.substring(0,rangeStart); 
			var tempStr2=textObj.value.substring(rangeEnd); 
			textObj.value=tempStr1+textFeildValue+tempStr2; 
			textObj.focus(); 
			var len=textFeildValue.length; 
			textObj.setSelectionRange(rangeStart+len,rangeStart+len); 
			textObj.blur();
		}else{
			textObj.value+=textFeildValue;
		} 
	} 
});

var emotionSendFilter = function (val) {
	val = val.replace(/\[em_1\]/g, '/::)');
	val = val.replace(/\[em_2\]/g, '/::~');
	val = val.replace(/\[em_3\]/g, '/::B');
	val = val.replace(/\[em_4\]/g, '/::|');
	val = val.replace(/\[em_5\]/g, '/:8-)');
	val = val.replace(/\[em_6\]/g, '/::<');
	val = val.replace(/\[em_7\]/g, '/::X');
	val = val.replace(/\[em_8\]/g, '/::Z');
	val = val.replace(/\[em_9\]/g, '/::<');
	val = val.replace(/\[em_10\]/g, '/::-|');
	val = val.replace(/\[em_11\]/g, '/::@');
	val = val.replace(/\[em_12\]/g, '/::P');
	val = val.replace(/\[em_13\]/g, '/::D');
	val = val.replace(/\[em_14\]/g, '/::O');
	val = val.replace(/\[em_15\]/g, '/::(');
	val = val.replace(/\[em_16\]/g, '/:--b');
	val = val.replace(/\[em_17\]/g, '/::Q');
	val = val.replace(/\[em_18\]/g, '/::T');
	val = val.replace(/\[em_19\]/g, '/:,@P');
	val = val.replace(/\[em_20\]/g, '/:,@-D');
	val = val.replace(/\[em_21\]/g, '/::d');
	val = val.replace(/\[em_22\]/g, '/:,@-o');
	val = val.replace(/\[em_23\]/g, '/::g');
	val = val.replace(/\[em_24\]/g, '/:|-');
	val = val.replace(/\[em_25\]/g, '/::!');
	val = val.replace(/\[em_26\]/g, '/::L');
	val = val.replace(/\[em_27\]/g, '/::>');
	val = val.replace(/\[em_28\]/g, '/::,@');
	val = val.replace(/\[em_29\]/g, '/:,@f');
	val = val.replace(/\[em_30\]/g, '/::-S');
	val = val.replace(/\[em_31\]/g, '/:?');
	val = val.replace(/\[em_32\]/g, '/:,@x');
	val = val.replace(/\[em_33\]/g, '/:,@@');
	val = val.replace(/\[em_34\]/g, '/::8');
	val = val.replace(/\[em_35\]/g, '/:,@!');
	val = val.replace(/\[em_36\]/g, '/:xx');
	val = val.replace(/\[em_37\]/g, '/:bye');
	val = val.replace(/\[em_38\]/g, '/:wipe');
	val = val.replace(/\[em_39\]/g, '/:dig');
	val = val.replace(/\[em_40\]/g, '/:&-(');
	val = val.replace(/\[em_41\]/g, '/:B-)');
	val = val.replace(/\[em_42\]/g, '/:<@');
	val = val.replace(/\[em_43\]/g, '/:@>');
	val = val.replace(/\[em_44\]/g, '/::-O');
	val = val.replace(/\[em_45\]/g, '/:<-|');
	val = val.replace(/\[em_46\]/g, '/:P-(');
	val = val.replace(/\[em_47\]/g, '/::"|');
	val = val.replace(/\[em_48\]/g, '/:X-)');
	val = val.replace(/\[em_49\]/g, '/::*');
	val = val.replace(/\[em_50\]/g, '/:@x');
	val = val.replace(/\[em_51\]/g, '/:8*');
	val = val.replace(/\[em_52\]/g, '/:hug');
	val = val.replace(/\[em_53\]/g, '/:moon');
	val = val.replace(/\[em_54\]/g, '/:sun');
	val = val.replace(/\[em_55\]/g, '/:bome');
	val = val.replace(/\[em_56\]/g, '/:!!!');
	val = val.replace(/\[em_57\]/g, '/:pd');
	val = val.replace(/\[em_58\]/g, '/:pig');
	val = val.replace(/\[em_59\]/g, '/:<W>');
	val = val.replace(/\[em_60\]/g, '/:coffee');
	val = val.replace(/\[em_61\]/g, '/:eat');
	val = val.replace(/\[em_62\]/g, '/:heart');
	val = val.replace(/\[em_63\]/g, '/:strong');
	val = val.replace(/\[em_64\]/g, '/:weak');
	val = val.replace(/\[em_65\]/g, '/:share');
	val = val.replace(/\[em_66\]/g, '/:v');
	val = val.replace(/\[em_67\]/g, '/:@)');
	val = val.replace(/\[em_68\]/g, '/:jj');
	val = val.replace(/\[em_69\]/g, '/:ok');
	val = val.replace(/\[em_70\]/g, '/:no');
	val = val.replace(/\[em_71\]/g, '/:rose');
	val = val.replace(/\[em_72\]/g, '/:fade');
	val = val.replace(/\[em_73\]/g, '/:showlove');
	val = val.replace(/\[em_74\]/g, '/:love');
	val = val.replace(/\[em_75\]/g, '/<L>');
	return val;
};