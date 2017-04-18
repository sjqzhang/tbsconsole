String.prototype.startWith=function(str){     
      var reg=new RegExp("^"+str);     
      return reg.test(this);        
}  


String.prototype.endWith=function(str){     
      var reg=new RegExp(str+"$");     
      return reg.test(this);        
}  


function fileloadon() {
     
    
    $("#_fileForm").submit();
}



function bindParam(map, div) {


	var name = "";
	var value = "";
	var type = "";
	div = typeof (div) == 'object' ? div : $(div);
	for ( var key in map) {
		$("input,select,textarea", div).each(function() {
			var elem = $(this);
			name = elem.attr("name");
			type = elem.attr("type");
			if(type==undefined)type='text';
			if (key == name) {
				if (type.toLowerCase() == "checkbox") {
					if (map[key] == 1) {
						elem.attr("checked", "checked");
					} else {
						elem.attr("checked", "");
					}
				} else if (type.toLowerCase() == "radio"){
					if($(this,div).val()==map[key]) {
						$(this).attr("checked","checked");
					}
				} else if (type.toLowerCase() == "select-one") {
					var options = $(this).get(0).options;
					for ( var i = 0; i < options.length; i++) {
						if (options[i].value == map[key]) {
							elem.get(0).options[i].selected = true;
							break;
						} else if (options[i].text == map[key]) {
							elem.get(0).options[i].selected = true;
							break;
						}
					}
				} else {
					elem.val(map[key]);
				}
			}
			//break;
		})
	}
}

function buildPara(div, where) { //说明div 是放置输入框的容器
	var q = "";
	var name = "";
	var value = "";
	var w = "";

	if ((where == null) || (where == "") || (where == "undefined")) {
		where = "";
	}
	where = where.split(/,/);
	$("input,select,textarea", $(div)).each(
			function(i) { // debugger; 
				var type = $(this).attr("type")
				if (type != 'button') {
					var name = $(this).attr("name");
					if (type == "checkbox") {
						if ($(this).attr("checked")) {
							value = 1;
						} else {
							value = 0;
						}
					} else {
						value = $(this).val();
					}
					for ( var wi in where) {
						if ($.trim(name) != "" && name == where[wi]) {
							w += "\"" + name + "\":\""
									+ encodeURIComponent(value) + "\",";
						}
					}
					q += "\"" + name + "\":\"" + encodeURIComponent(value)
							+ "\",";
				}
			})
	if (q != "") {
		if (w != "") {
			w = w.substring(0, w.length - 1);
			w = '"where":{' + w + '}';
			q = q + w;
		} else {
			q = q.substring(0, q.length - 1);
		}
		q = '{' + q + '}';
	}
	return q;
}

function isInteger( str ){  
var regu = /^[-]{0,1}[0-9]{1,}$/; 
return regu.test(str); 
} 

function isDecimal(str) {
	if (isInteger(str))
		return true;
	var re = /^[-]{0,1}(\d+)[\.]+(\d+)$/;
	if (re.test(str)) {
		if (RegExp.$1 == 0 && RegExp.$2 == 0)
			return false;
		return true;
	} else {
		return false;
	}
}




function buildJsonPara(container) {
	var p = [];
	$(container).find('div').each(function() {
		var key = $('input[name=key]', this).val();
		var val = $('input[name=val]', this).val();
		if (key != 'Key') {
			if (val == 'Value') {
				val = '';
			}
//			if((val.startWith('\\[')&&val.endWith('\\]'))||(val.startWith('\\{')&&val.endWith('\\}'))) {
//				p.push('"' + key + '":' + encodeURIComponent(val) );
//			} else {
//				p.push('"' + key + '":"' + encodeURIComponent(val) + '"');
//			}

			if(isDecimal(val)){
				p.push('"' + key + '":"' + encodeURIComponent(val.replace(/\"/ig,'\\"')) + '"');
			} else if(JSON.parse(val)) {
				p.push('"' + key + '":' + JSON.stringify(JSON.parse(val)));
			} else {
				p.push('"' + key + '":"' + encodeURIComponent(val.replace(/\"/ig,'\\"')) + '"');
			}

		}
	});
	return '{' + p.join(',') + '}';
}

function bindJsonPara(container, json) {
	var tpl = $('#tpl_param').html();
	$(container).html('');
	var tpl = $('#tpl_param').html();
	var p = eval('(' + (json.replace(/\\/ig,'\\\\')) + ')');
	for ( var key in p) {
		console.log(key);
		var tmp = tpl;
		tmp = tmp.replace(/value\=["]?Key["]?/, 'value="' + key + '"');
		if (typeof (p[key]) == 'object') {
//			tmp = tmp.replace(/value\=["]?Value["]?/, 'value="'
//					+ obj2str(p[key]).replace(/\"/ig, "'") + '"');
				tmp = tmp.replace(/value\=["]?Value["]?/, "value='"+ JSON.stringify(p[key]) + "'");
		} else {
			tmp = tmp.replace(/value\=["]?Value["]?/, "value='" + p[key] + "'");
		}
		$(container).append(tmp);
		$('.del').click(function() {
			$(this).parent().remove();
		})//end click;
	}

}

function obj2str(o,flag,replace){
	var arr_start = "ARRAY_S";
	var arr_end = "ARRAY_E";
	if(flag==null){
		flag = "\"";//默认是双引号
	}
	if(replace==null){
		replace = true;
	}
	var r = [];
	if(typeof o == "string" || o == null) {
		return o;
	}
	//alert(typeof(o));
	if(typeof o == "object"){
		//alert(o.sort);
		if(!o.sort){
			//alert("in if");
			r[0]="{";
			for(var i in o){
				//alert(i+"="+o[i]);
				r[r.length]=flag;
				r[r.length]=i;
				r[r.length]=flag;
				r[r.length]=":";
				r[r.length]=flag;
				r[r.length]=obj2str(o[i],flag,false);
				r[r.length]=flag;
				r[r.length]=",";
			}
			r[r.length-1]="}";
		}else{//数组元素
			r[0]= arr_start;
			for(var i =0;i<o.length;i++){
				r[r.length]=flag;
				r[r.length]=obj2str(o[i],flag,false);
				r[r.length]=flag;
				r[r.length]=",";
			}
			r[r.length-1]=arr_end;
		}
	 
		var str = r.join("");
		//alert("结果:"+str);
		//针对{} 就是没有属性的对象，会返回单个 },把它补齐
		if(str=="}"){
			str="{}";
		}
		//针对[] 就是长度为0的数组，会返回单个 ],把它补齐
		if(str==arr_end){
			str=arr_start+arr_end;
		}

		if(replace){//在递归子循环中不替换,到最后统一替换
			//替换掉 "{ }" "[ ]"
			var reg=new RegExp(flag+"{","g"); // 包含字符 "{
			str = str.replace(reg,"{");

			reg=new RegExp("}"+flag,"g");// 包含字符 }"
			str = str.replace(reg,"}");
			
			reg=new RegExp(flag+arr_start,"g"); // 包含字符 "[
			str = str.replace(reg,"[");

			reg=new RegExp(arr_end+flag,"g"); // 包含字符 ]"
			str = str.replace(reg,"]");

			//alert(str);

			if(str.indexOf(arr_start+"{")>-1){
				reg=new RegExp(arr_start+"{","g"); 
				str = str.replace(reg,"[{");
			}
			if(str.indexOf("}"+arr_end)>-1){
				reg=new RegExp("}"+arr_end,"g"); 
				str = str.replace(reg,"}]");
			}
		}
		//alert("--"+str);
		return str;
	}
	return o.toString();
}

function str2obj(json){ 
	return eval("("+json+")"); 
}





function str2obj(str) {
	return eval('(' + str + ')')
}

function request(paras) {
	var url = location.href;
	var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
	var paraObj = {};
	for (i = 0; j = paraString[i]; i++) {
		paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j
				.indexOf("=") + 1, j.length);
	}
	var returnValue = paraObj[paras.toLowerCase()];
	if (typeof (returnValue) == "undefined") {
		return "";
	} else {
		return returnValue;
	}
}

	 function addTab(title,url){
		  if ($('#tabs').tabs('exists', title)){
		        $('#tabs').tabs('select', title);
		        $("#optip").hide();
		  }
		  else
		  {	
			  $("#optip").hide();	
			$('#tabs').tabs('add',{
				title:title,
				content:"	<div title=\"信息\" >"
				+"			<iframe frameborder=\"0\" src=\""+url+"\" style=\"width:100%;\"></iframe>"
				+"		</div>",
				//href:'http://172.16.10.110:8084/flexigrid/view/country.html',
				iconCls:'icon-save',
				closable:true
			}).resize();//end tabs
		  }//end if 
	}
	 
	

		function encode(str){
			return encodeURIComponent(str);
		}
		function decode(str){
			return decodeURIComponent(str);
		}
	
function log(obj){
	console.log(obj);
}
		


$.Post=function(url,param,callback){
	$.post(_gwebContext+url,param,callback);
}

$.PostSync=function(url,param,callback){
	$.ajax({url : _gwebContext+url,
    data:param,
     cache : false,
     async : false,
     type : "POST",
     dataType : 'json/xml/html',
     success : callback
 });
}

 


function getProjectName()
{
var pn=	request('project');
if(pn) {
	return pn;
}
	if(!_gproject){
		alert('请选择项目');
	} else {
		return _gproject;
	}
}


function getContext(){
	
	return _gwebContext;
}
