$(function () {
    $("#jqGrid").jqGrid({
        url: '../caseleave/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '案例标题', name: 'caseTitle', width: 50},
			{ label: '案例', name: 'caseId', index: 'case_id', width: 80,
				formatter: function (value, options, row) {
					if(value != null){
						return "<a class='btn btn-info' onclick='casesInfo(\""+row.caseId+"\")'>"+'详情'+"</a>";
					}else{
						return "<span></span>";
					}
				}
			},
			{ label: '留言内容', name: 'content', index: 'content', width: 80 },
			{ label: '留言时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '留言人姓名', name: 'name', index: 'name', width: 80 },
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 80 },
			{ label: '留言读取状态', name: 'status', index: 'status', width: 80,
				formatter: function (value, options, row) {
					if(value == 't'){
						return "已读";
					}else {
						return "未读";
					}
				}
			},
			{ label: '分享人姓名', name: 'shareRealName',width: 80 }/*,
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>修改</span></button>&nbsp;";
                	dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='logic_del("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>删除</span></button>&nbsp;";
                	return dom;
                }
            }*/
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
function casesInfo(id){
	layer.open({
		type: 2,
		skin: 'layui-layer-lan',
		title: "案例详情数据列表数据列表",
		area: ['1150px', '800px'],
		shadeClose: false,
		content: 'casus.html?id=' + id
	});
}
//逻辑删除
function logic_del(id){
	if(id == null){
		return ;
	}
	
	vm.$Modal.confirm({
        title: '提示',
        content: '确定要删除吗？',
        onOk:() => {
        	$.ajax({
    			type: "GET",
    			url: "../caseleave/logic_del/" + id,
    		    success: function(r){
    		    	if(r.code == 0){
    					$("#jqGrid").trigger("reloadGrid");
    		    		vm.$Message.success('操作成功!');
    				}else{
    					vm.$Message.error(r.msg);
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
		caseLeave: {},
		ruleValidate: {
											
																content: [
		                { required: true, message: '请输入留言内容' }
		            ], 																openid: [
		                { required: true, message: '请输入留言人openid' }
		            ], 																ctime: [
		                { required: true, message: '请输入' }
		            ], 																portrait: [
		                { required: true, message: '请输入留言人头像' }
		            ], 																name: [
		                { required: true, message: '请输入留言人姓名' }
		            ], 																mobile: [
		                { required: true, message: '请输入手机号' }
		            ], 																status: [
		                { required: true, message: '请输入状态' }
		            ], 																isDel: [
		                { required: true, message: '请输入' }
		            ], 																caseId: [
		                { required: true, message: '请输入案例ID' }
		            ], 																shareMemberId: [
		                { required: true, message: '请输入分享人ID' }
		            ]							        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			name:null,
			mobile:null
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
			vm.q.name = null;
			vm.q.mobile = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.caseLeave = {};
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
			this.$refs['caseLeave'].validate((valid) => {
                if (valid) {
                	var url = vm.caseLeave.id == null ? "../caseleave/save" : "../caseleave/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.caseLeave),
        			    success: function(r){
        			    	if(r.code === 0){
        			    		vm.reload();
        			    		vm.showModal = false;
        			    		vm.$Message.success('操作成功!');
        					}else{
        						vm.$Message.error(r.msg);
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
			
			vm.$Modal.confirm({
	        title: '提示',
	        content: '确定要删除选中的记录？',
	        onOk:() => {
	        	$.ajax({
					type: "POST",
				    url: "../caseleave/delete",
				    data: JSON.stringify(ids),
					contentType: "application/json",
				    success: function(r){
						if(r.code === 0){
							$("#jqGrid").trigger("reloadGrid");
    			    		vm.$Message.success('操作成功!');
    					}else{
    						vm.$Message.error(r.msg);
    					}
					}
				});
	        }
	    });
		},
		getInfo: function(id){
			//$.get("../caseleave/info/" + id, function(r){
            //    vm.caseLeave = r.caseLeave;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../caseleave/info/" + id,
				success : function(r) {
					vm.caseLeave = r.caseLeave;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"name":vm.q.name, "sdate":vm.q.sdate, "edate":vm.q.edate,"mobile":vm.q.mobile},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});