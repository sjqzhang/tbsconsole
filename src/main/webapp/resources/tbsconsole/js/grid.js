
function GridOp()
{
	var that=this;
	
	this.editContainer='#editContainer';
	this.gridSelector='#gridTable';
	
	
	this.getGridOptions=function(){
		var options={
				'selector':that.gridSelector,
				'urlSelect':'',
				'urlDelete':'',
				'urlUpdate':'',
				'addHandle':function(){
					$(that.editContainer).dialog('open');
				}
				
		}
		return options;
	}
	
	this.toGetUrl=function(map){
		var url='';
		for(var key in map){
		  url+=key+'='+encode(map[key])+'&'
		}
		if(url.length>0)
		url=url.substring(0, url.length-1);
		return '?'+url
	}
	
	this.buildParam=function(){
		return  buildPara(that.editContainer);
	}

	this.addHandle=function(){
		$(that.editContainer).dialog('open');
	}
	
	this.getDeleteParam=function(val,data,i){
		return {}
	}
	
	this.delHandle=function(val,data,i){
		if(confirm('你确定要删除吗？')){
			$.Post(that.getGridOptions().urlDelete,this.getDeleteParam(val,data,i),function(data){
				if(data&&data.code==0){
					$(that.gridSelector).datagrid('reload');
				}
			})
		}
	}
	
	this.editHandle=function(val,data,i){
		bindParam(data,that.editContainer);
		that.addHandle();
	}
	
	this.getUpdateParam=function(){
		return {}
	}
	
	this.getSelectParam=function()
	{
		return {}
	}
	
	this.updateHandle=function(){
		var c = buildPara(that.editContainer);
		$.Post(that.getGridOptions().urlUpdate,this.getUpdateParam(), function(data) {
			if(data&&data.code==0){
				$(that.gridSelector).datagrid('reload');
			}
		});
		
	}
	
	this.operation=function(str,callback){
		var data= eval('('+ decode(str)+')');
		that[callback].call(that,data.val,data.data,data.index);
	}
	
	this.getIntanceName=function(){
		try {
			var name=that.constructor.toString().replace(/\{[\s\S]*\}/, '').replace(/\([\s\S]*\)/, '').replace(/function\s*/, '');
			name=name.substring(0, 1).toLocaleLowerCase()+name.substring(1, name.length);
			return name;
			
		}catch(e){
			
		}	
	}

	this.formatter=function(val,data,i){		
		var name=that.getIntanceName();
		//log(name)
		var str=encode( JSON.stringify({'val':val?val:'','data':data,'index':i}))
		var ret= '<a href="#" onclick="'+name+'.operation(\''+str+'\',\'delHandle\')">删除</a>&nbsp;';
		ret+= '<a href="#" onclick="'+name+'.operation(\''+str+'\',\'editHandle\')">修改</a>&nbsp;';
		return ret;
	
	
	}
}


function Grid() {
	
	var that=this;
	GridOp.apply(this);

	/*
	
	{selector,url,columns}
	
	 */
	this.gridEdit = function(options) {
		var grid = $(options.selector).datagrid({
			title : '应用系统列表',
			iconCls : 'icon-edit',// 图标
			width : 700,
			height : 'auto',
			nowrap : false,
			striped : true,
			border : true,
			collapsible : false,// 是否可折叠的
			fit : true,// 自动大小
			url : options.url,
			// sortName: 'code',
			// sortOrder: 'desc',
			remoteSort : false,
			idField : 'fldId',
			singleSelect : false,// 是否单选
			pagination : true,// 分页控件
			rownumbers : true,// 行号
			/*
			frozenColumns : [ [ {
				field : 'ck',
				checkbox : true
			} ] ],
			 */
			loadFilter : function(d) {
				console.log(d);
				//return {'rows':d.reply,'total':d.reply.length}
				if (d.reply) {
					return {
						'rows' : d.reply,
						'total' : d.reply.length
					}
				} else {
					return d;
				}

			},
			columns : [ options.columns ],
			onLoadSuccess : function(data) {
				console.log(data)
			},
			toolbar : [ {
				text : '添加',
				iconCls : 'icon-add',
				handler : function() {
					openDialog("add_dialog", "add");
				}
			}, '-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					openDialog("add_dialog", "edit");
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					delAppInfo();
				}
			} ],
		});
		// 设置分页控件
		var p = $(options.selector).datagrid('getPager');
		$(p).pagination({
			pageSize : 10,// 每页显示的记录条数，默认为10
			pageList : [ 5, 10, 15 ],// 可以设置每页记录条数的列表
			beforePageText : '第',// 页数文本框前显示的汉字
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
		/*
		 * onBeforeRefresh:function(){ $(this).pagination('loading');
		 * alert('before refresh'); $(this).pagination('loaded'); }
		 */
		});

		return grid;
	}

	this.gridSimple = function(options) {
		var grid = $(options.selector).datagrid({
			url :getContext()+ options.urlSelect+ that.toGetUrl(that.getSelectParam()),
			title : options.title,
			height : 'auto',
			fitColumns : true,
			nowrap : true,
			striped : true,
			border : true,
			singleSelect : true,// 是否单选
			collapsible : false,//是否可折叠的 
			rownumbers : true,//行号 
			//fit: true,//自动大小 
			loadFilter : function(d) {
				if (d.reply) {
					return {
						'rows' : d.reply,
						'total' : d.reply.length
					}
				} else {
					return d;
				}

			}
		});
		return grid;

	}

	this.gridSimple2 = function(options) {
		that._onSelect=function(rowindex,rowdata){
			if(that.onSelect){
				that.onSelect(rowindex,rowdata)
			}
		}
		that._onLoadSuccess=function(data){
			if(that.onLoadSuccess){
				that.onLoadSuccess(data);
			}
		}
		var grid = $(options.selector).datagrid({
			url :getContext()+ options.urlSelect,
			title : options.title,
			height : 'auto',
			fitColumns : true,
			nowrap : false,
			striped : true,
			border : true,
			singleSelect : true,// 是否单选
			collapsible : false,//是否可折叠的 
			rownumbers : true,//行号 
			onSelect:that._onSelect,
			onLoadSuccess:that._onLoadSuccess,
			//fit: true,//自动大小 
			loadFilter : function(d) {
				if (d.reply) {
					return {
						'rows' : d.reply,
						'total' : d.reply.length
					}
				} else {
					return d;
				}

			},
			toolbar : [ {
				text : '添加',
				iconCls : 'icon-add',
				handler : function() {
					if(!options.addHandle){
						that.addHandle();
					} else {
						options.addHandle();
					}
					
				}
			}]
		});
//		if(that.initEvent){
//			that.initEvent();
//		}
		return grid;

	}


}
