$(function () {
    $("#jqGrid").jqGrid({
        url: '../like/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '点赞人openid', name: 'openid', index: 'openid', width: 80 },
			{ label: '点赞时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '数据ID', name: 'dataId', index: 'data_id', width: 80 },
			{ label: '点赞类型  1：资讯 2：评论 3：活动', name: 'likeType', index: 'like_type', width: 80 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>修改</span></button>&nbsp;";
                	dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='logic_del("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>删除</span></button>&nbsp;";
                	return dom;
                }
            }
        ],
		viewrecords: true,
		height: $(window).height() - 250,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

//修改
function edit(id){
	if(id == null){
		return ;
	}
//	vm.showList = false;
	vm.showModal = true;
    vm.title = "修改";
    vm.getInfo(id)
}

//逻辑删除
function logic_del(id){
	if(id == null){
		return ;
	}
	
	vm.%Modal.confirm({
        title: '提示',
        content: '确定要删除吗？',
        onOk:() => {
        	$.ajax({
    			type: "GET",
    			url: "../like/logic_del/" + id,
    		    success: function(r){
    		    	if(r.code == 0){
    					$("#jqGrid").trigger("reloadGrid");
    		    		vm.%Message.success('操作成功!');
    				}else{
    					vm.%Message.error(r.msg);
    				}
    			}
    		});
        }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		showModal: false,
		title: null,
		like: {},
		ruleValidate: {
											
																openid: [
		                { required: true, message: '请输入点赞人openid' }
		            ], 																ctime: [
		                { required: true, message: '请输入点赞时间' }
		            ], 																dataId: [
		                { required: true, message: '请输入数据ID' }
		            ], 																likeType: [
		                { required: true, message: '请输入点赞类型  1：资讯 2：评论 3：活动' }
		            ]							        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: []
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reset: function(){
			vm.q.id = null;
			vm.q.sdate = null;
			vm.q.edate = null;
			vm.q.ctime = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.like = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			this.%refs['like'].validate((valid) => {
                if (valid) {
                	var url = vm.like.id == null ? "../like/save" : "../like/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.like),
        			    success: function(r){
        			    	if(r.code === 0){
        			    		vm.reload();
        			    		vm.showModal = false;
        			    		vm.%Message.success('操作成功!');
        					}else{
        						vm.%Message.error(r.msg);
        					}
        				}
        			});
                }
            })
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			vm.%Modal.confirm({
	        title: '提示',
	        content: '确定要删除选中的记录？',
	        onOk:() => {
	        	$.ajax({
					type: "POST",
				    url: "../like/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code === 0){
							$("#jqGrid").trigger("reloadGrid");
    			    		vm.%Message.success('操作成功!');
    					}else{
    						vm.%Message.error(r.msg);
    					}
					}
				});
	        }
	    });
		},
		getInfo: function(id){
			//$.get("../like/info/" + id, function(r){
            //    vm.like = r.like;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../like/info/" + id,
				success : function(r) {
					vm.like = r.like;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});