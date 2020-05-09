// ---------------------------  自定义方法函数   ----------------------------------------
var tjfsType={'0':'按年推荐','1':'按学期推荐','2':'按月推荐','3':'按周推进','4':'按天推进'};
var bzlxType={'0':'学院','1':'专业','2':'课程','3':'教师','4':'学生','5':'全部'};
//为Date原型添加如下的方法
Date.prototype.format = function(fmt) { 
     var o = { 
        "M+" : this.getMonth()+1,                 //月份 
        "d+" : this.getDate(),                    //日 
        "h+" : this.getHours(),                   //小时 
        "m+" : this.getMinutes(),                 //分 
        "s+" : this.getSeconds(),                 //秒 
        "q+" : Math.floor((this.getMonth()+3)/3), //季度 
        "S"  : this.getMilliseconds()             //毫秒 
    }; 
    if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
     for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
         }
     }
    return fmt; 
}
// String replaceAll
String.prototype.replaceAll = function(s1,s2){
	return this.replace(new RegExp(s1,"gm"),s2);
}
// 日期转换星期（中文）
function getWeek(dateString){
    var date;
    if(isNull(dateString)){
        date = new Date();
    }else{
        var dateArray = dateString.split("-");
        date = new Date(dateArray[0], parseInt(dateArray[1] - 1), dateArray[2]);
    }
    //var weeks = new Array("日", "一", "二", "三", "四", "五", "六");
    //return "星期" + weeks[date.getDay()];
    return "星期" + "日一二三四五六".charAt(date.getDay());
}
// 限制文本输入指定长度字符
function maxInputLen(inputObj , num){
	if(inputObj.value.length > num) inputObj.value = inputObj.value.substr(0,num);
}
//html转换为pdf  2018/11/21 kl add  参数说明  HtmlToPdf（导出位置（暂以class取值）,导出文件名称）
function HtmlToPdf(dcwz,wjmc){
	html2canvas($('.'+dcwz), {
          onrendered:function(canvas) {

              var contentWidth = canvas.width;
              var contentHeight = canvas.height;

              //一页pdf显示html页面生成的canvas高度;
              var pageHeight = contentWidth / 595.28 * 841.89;
              //未生成pdf的html页面高度
              var leftHeight = contentHeight;
              //pdf页面偏移
              var position = 0;
              //a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
              var imgWidth = 555.28;
              var imgHeight = 555.28/contentWidth * contentHeight;

              var pageData = canvas.toDataURL('image/jpeg', 1.0);

              var pdf = new jsPDF('', 'pt', 'a4');
              //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
              //当内容未超过pdf一页显示的范围，无需分页
              if (leftHeight < pageHeight) {
                  pdf.addImage(pageData, 'JPEG', 20, 0, imgWidth, imgHeight );
              } else {
                  while(leftHeight > 0) {
                      pdf.addImage(pageData, 'JPEG', 20, position, imgWidth, imgHeight)
                      leftHeight -= pageHeight;
                      position -= 841.89;
                      //避免添加空白页
                      if(leftHeight > 0) {
                          pdf.addPage();
                      }
                  }
              }

              pdf.save(wjmc+'.pdf');
          }
      })  
}
//form表单回填
var util = {  
    serializeObject: function(form) {  
        var formEL = $(form);  
        var o = {};  
        var a = formEL.serializeArray();  
        $.each(a, function() {  
            if(o[this.name]) {  
                if(!o[this.name].push) {  
                    o[this.name] = [o[this.name]];  
                }  
                o[this.name].push(this.value || '');  
            } else {  
                o[this.name] = this.value || '';  
            }  
        });  
        return o;  
    },  
  
    fillFormData: function(form, obj, isStatus) {  
        var formEL = $(form);  
        $.each(obj, function(index, item) {  
           formEL.find("[name=" + index.toLowerCase() + "]").val(item);  
        });  
    },  
    empty: function(data) {  
        if(null == data || "" == data) return true;  
        else return false;  
    }  
};  
//清空文本框内容
function clearForm(form) {
    // input清空
    $(':input', form).each(function () {
        var type = this.type;
        var tag = this.tagName.toLowerCase(); // normalize case
        if (type == 'text' || type == 'password' || tag == 'textarea' ){
            this.value = "";
            // 多选checkboxes清空
            // select 下拉框清空
        }else if (tag == 'select'){
            this.selectedIndex = 0;            
        }
    });
};
/**
 * 格式化日期
 * @param {} date
 * @returns {}
 */
function formatDate(date) {
    if(!date) return "";
    var ts = arguments[0] || 0;
    var t, y, m, d, h, i, s;
    t = ts ? new Date(ts) : new Date();
    y = t.getFullYear();
    m = t.getMonth() + 1;
    d = t.getDate();
    return y + '-' + (m < 10 ? '0' + m : m) + '-' + (d < 10 ? '0' + d : d);
}
// 构造对象
var commonJs = {
		//产生随机数函数
		rndNum: function(n) {
			var rnd = "";
		    for(var i=0;i<n;i++)
		        rnd+=Math.floor(Math.random()*10);
		    return rnd;
		},
		//自动生成编号（YYYYMMDD + 四位随机数字）
		getNumber: function() {
			var d = new Date();
			var yuar = d.getFullYear();
			var month = ((d.getMonth()+1) < 10) ? ('0' + (d.getMonth()+1)): (d.getMonth()+1);
			var day = d.getDate();
			var str = yuar + '' + month + '' + day + '' + commonJs.rndNum(4);
			return str;
		},
		// 页面引入公用html代码
		include: function(id,type,url){
			var xmlhttp = new XMLHttpRequest();
			xmlhttp.onreadystatechange = function() {
			    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			        document.getElementById(id).innerHTML = xmlhttp.responseText;
			    }
			}
			xmlhttp.open(type, url, true);
			xmlhttp.send();
		},
		// 格式化日期
		formatDate: function( dateStr, format){
			var str = dateStr.toString();
			str = str.replace("/-/g", "/");
			var oDate1 = new Date(str);
			var time1 = oDate1.format(format);
			return time1;
		},
		// 获取之后几天日期
		getDateStr: function(AddDayCount){
		     var dd = new Date(); 
		     dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期 
		     var y = dd.getFullYear(); 
		     var m = dd.getMonth()+1;//获取当前月份的日期 
		     var d = dd.getDate(); 
		     return y + "-" + m + "-" + d; 
		},
		/**
		 * 是否为Null
		 * @param object
		 * @returns {Boolean}
		 */ 
		isNull: function(object){
		    if(object == null || object == '' || typeof object == "undefined"){ 
		        return true; 
		    } 
		    return false; 
		},
		/**
		 * 根据日期字符串获取星期几
		 * @param dateString 日期字符串（如：2016-12-29），为空时为用户电脑当前日期
		 * @returns {String}
		 */
		getWeek: function(dateString){
		    var date;
		    if(commonJs.isNull(dateString)){
		        date = new Date();
		    }else{
		        var dateArray = dateString.split("-");
		        date = new Date(dateArray[0], parseInt(dateArray[1] - 1), dateArray[2]);
		    }
		    //var weeks = new Array("日", "一", "二", "三", "四", "五", "六");
		    //return "星期" + weeks[date.getDay()];
		    return "星期" + "日一二三四五六".charAt(date.getDay());
		},
		/**
		 * form表单数据转为json
		 * @param {} formId
		 * @returns {} 
		 */
		serializeToJSON:function(formId){
			var arr = jQuery('#'+formId).serializeArray();
			var resultJSON = {};
			if(arr && arr.length>0){
				jQuery.each(arr,function(i,v){
					if(!resultJSON[v['name']]){
						resultJSON[v['name']]=v['value'];
					}else{
						resultJSON[v['name']]=resultJSON[v['name']]+","+v['value'];
					}
				});
			}
			return resultJSON;
		},
		joinArray:function(arr,property){
			var ids=[];
			jQuery.each(arr,function(i,v){
				ids.push(v[property]);
			});
			return ids;
	    },
	    covertNode:function(rows,idField,treeField,iconClsField,parentField,parentNode){
			function exists(rows, parentId){
				for(var i=0; i<rows.length; i++){
					if (rows[i][idField] == parentId) {
						return true;
					}
				}
				return false;
			}
			var nodes = [];
			var toDo = [];
			//创建根目录
			if(parentNode && parentNode['id']){
				nodes = [parentNode];
				for(var i=0; i<rows.length; i++){
	    			var row = rows[i];
	    			//console.log(row);
	    			if (!exists(rows, row[parentField])){
	    				var child = {
	    					id:row[idField],
	    					text:row[treeField],
	    					//iconCls:row[iconClsField],
	    					state:{
							    checked:(row['CHECKSTATUS'] && row['CHECKSTATUS']=='1')
						    }
	    				};
	    				toDo.push(child);
	    				if(parentNode){
							if (parentNode['nodes']){
								parentNode['nodes'].push(child);
							} else {
								parentNode['nodes'] = [child];
							}
						}
	    			}
	    		}
			}else{
				// get the top level nodes
	    		for(var i=0; i<rows.length; i++){
	    			var row = rows[i];
	    			if (!exists(rows, row[parentField])){
	    				var child = {
	    					id:row[idField],
	    					text:row[treeField],
	    					//iconCls:row[iconClsField]
	    				};
	    				if(row['CHECKSTATUS']){
	    					child['state'] = {checked: (row['CHECKSTATUS'] && row['CHECKSTATUS']=='1')};
	    				}
	    				//console.log(child);
	    				nodes.push(child);
	    			}
	    		}
	    		for(var i=0; i<nodes.length; i++){
	    			toDo.push(nodes[i]);
	    		}
			}
			while(toDo.length > 0){
		//	console.log(toDo);
				var node = toDo.shift();	// the parent node
				// get the children nodes
				for(var i=0; i<rows.length; i++){
					var row = rows[i];
					if (row[parentField] == node["id"]){
						var child = {
							id:row[idField],
	    					text:row[treeField],
	    					//iconCls:row[iconClsField]
						};
						if(row['CHECKSTATUS']){
	    					child['state'] = {checked:  (row['CHECKSTATUS'] && row['CHECKSTATUS']=='1')};
	    				}
						if (node['nodes']){
							node['nodes'].push(child);
						} else {
							node['nodes'] = [child];
						}
						if(node['nodes'] && node['nodes'].length > 0){
							//node['nodes']['state'] = {checked: false};
						}
						toDo.push(child);
					}
				}
			}
			return nodes;
	   },  
		/**
		 * form表单数据回填
		 * @param {} form
		 * @param {} obj
		 * @returns {} 
		 */
	    fillFormData: function(form, obj) {  
	        var formEL = $('#' + form);
	        $.each(obj, function(index, item) {
	           formEL.find("[name=" + index.toLowerCase() + "]").val(item);  
	        });  
	    }
}
	//提示框、选择框
$(function() {
    $.MsgBox = {
        Alert: function(title, msg) {
            GenerateHtml("alert", title, msg);
            btnOk(); //alert只是弹出消息，因此没必要用到回调函数callback
            btnNo();
        },
        Alert2: function(title, msg,callback) {
            GenerateHtml("alert", title, msg);
            btnOk(callback); //alert只是弹出消息，因此没必要用到回调函数callback
            btnNo();
        },
        Confirm: function(title, msg, callback) {
            GenerateHtml("confirm", title, msg);
            btnOk(callback);
            btnNo();
        }
    }
    //生成Html
    var GenerateHtml = function(type, title, msg) {
        var _html = "";
        _html += '<div id="mb_box"></div><div id="mb_con"><span id="mb_tit">' + title + '</span>';
        _html += '<a id="mb_ico">x</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
        if (type == "alert") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        if (type == "confirm") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
            _html += '<input id="mb_btn_no" type="button" value="取消" />';
        }
        _html += '</div></div>';
        //必须先将_html添加到body，再设置Css样式
        $("body").append(_html);
        //生成Css
        GenerateCss();
    }

    //生成Css
    var GenerateCss = function() {
        $("#mb_box").css({
            width: '100%',
            height: '100%',
            zIndex: '99999',
            position: 'fixed',
            filter: 'Alpha(opacity=60)',
            backgroundColor: 'black',
            top: '0',
            left: '0',
            opacity: '0.6'
        });
        $("#mb_con").css({
            zIndex: '999999',
            width: '400px',
            position: 'fixed',
            backgroundColor: 'White',
            borderRadius: '15px'
        });
        $("#mb_tit").css({
            display: 'block',
            fontSize: '14px',
            color: '#444',
            padding: '10px 15px',
            backgroundColor: '#FFF',
            borderRadius: '15px 15px 0 0',
            borderBottom: '3px solid #009BFE',
            fontWeight: 'bold'
        });
        $("#mb_msg").css({
            padding: '20px',
            lineHeight: '20px',
            borderBottom: '1px dashed #FFF',
            fontSize: '13px'
        });
        $("#mb_ico").css({
            display: 'block',
            position: 'absolute',
            right: '10px',
            top: '9px',
            border: '1px solid Gray',
            width: '18px',
            height: '18px',
            textAlign: 'center',
            lineHeight: '16px',
            cursor: 'pointer',
            borderRadius: '12px',
            fontFamily: '微软雅黑'
        });
        $("#mb_btnbox").css({
            margin: '15px 0 10px 0',
            textAlign: 'center'
        });
        $("#mb_btn_ok,#mb_btn_no").css({
            width: '85px',
            height: '30px',
            color: 'white',
            border: 'none'
        });
        $("#mb_btn_ok").css({
            backgroundColor: '#428bca'
        });
        $("#mb_btn_no").css({
            backgroundColor: '#abbac3',
            marginLeft: '20px'
        });
        //右上角关闭按钮hover样式
        $("#mb_ico").hover(function() {
            $(this).css({
                backgroundColor: 'Red',
                color: 'White'
            });
        }, function() {
            $(this).css({
                backgroundColor: '#FFF',
                color: 'black'
            });
        });
        var _widht = document.documentElement.clientWidth; //屏幕宽
        var _height = document.documentElement.clientHeight; //屏幕高
        var boxWidth = $("#mb_con").width();
        var boxHeight = $("#mb_con").height();
        //让提示框居中
        $("#mb_con").css({
            top: (_height - boxHeight) / 2 + "px",
            left: (_widht - boxWidth) / 2 + "px"
        });
    }
    //确定按钮事件
    var btnOk = function(callback) {
        $("#mb_btn_ok").click(function() {
            $("#mb_box,#mb_con").remove();
            if (typeof(callback) == 'function') {
                callback();
            }
        });
    }
    //取消按钮事件
    var btnNo = function() {
        $("#mb_btn_no,#mb_ico").click(function() {
            $("#mb_box,#mb_con").remove();
        });
    }
});




//导出方法调用 kl  2020-03-24 新加
function exportwj(aparamObj,bparamObj,cparamObj,path){
	var downloadframe = jQuery('#downloadIframe');
		if(downloadframe.length==0){
			jQuery('body').append('<iframe id="downloadIframe" name="downloadIframe" style="border:none;height:300px;width:0px;"></iframe>');
		}
		//构建下载form表单
		var downloadFormSin = jQuery('#downloadFormSin');
		if(downloadFormSin.length==0){
			jQuery('body').append('<form id="downloadFormSin" name="downloadFormSin" method="POST" target="downloadIframe"></form>');
		}else{
			jQuery('#downloadFormSin').html("");
		}
		//构建参数对象
		var indexVal = 0;
		var inithtml='';
		jQuery.each(aparamObj,function(i,v){
			inithtml+='<input type="hidden" name="'+i+'" id="'+i+'" value="'+v+'"/>';
		});
		jQuery.each(bparamObj,function(i,v){
			//连接符
			var ljf="=";
			//判断连接符
			if(cparamObj.length>0){
				ljf=cparamObj[indexVal];
			}
			inithtml+='<input type="hidden" name="opValue['+indexVal+']" id="opValue['+indexVal+']" value="'+ljf+'"/>';
			inithtml+='<input type="hidden" name="fieldName['+indexVal+']" id="fieldName['+indexVal+']" value="'+i+'"/>';
			inithtml+='<input type="hidden" name="fieldValue['+indexVal+']" id="fieldValue['+indexVal+']" value="'+v+'"/>';
			indexVal++;
		});
		jQuery('#downloadFormSin').html(inithtml);
		if(path){
			jQuery('#downloadFormSin').attr('action',rootPath + '/basic/fileUpload/'+path);
		}else{
			jQuery('#downloadFormSin').attr('action',rootPath + '/basic/fileUpload/exportFileExecl.do');
		}
		jQuery('#downloadFormSin').submit();
}
