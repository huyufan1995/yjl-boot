$(function () {
    $("#jqGrid").jqGrid({
        url: '../member/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '头像', name: 'portrait', index: 'portrait', width: 80 },
			{ label: '昵称', name: 'nickname', index: 'nickname', width: 80 },
			{ label: '性别 man woman', name: 'gender', index: 'gender', width: 80 },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '删除标志 t f', name: 'isDel', index: 'is_del', width: 80 },
			{ label: '状态 normal freeze', name: 'status', index: 'status', width: 80 },
			{ label: '姓名', name: 'realName', index: 'real_name', width: 80 },
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 80 },
			{ label: '微信用户ID', name: 'openid', index: 'openid', width: 80 },
			{ label: '会员类型 common vip supervip', name: 'type', index: 'type', width: 80 },
			{ label: '公司名称', name: 'company', index: 'company', width: 80 },
			{ label: '邮箱', name: 'email', index: 'email', width: 80 },
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
    			url: "../member/logic_del/" + id,
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
		member: {},
		ruleValidate: {
											
																portrait: [
		                { required: true, message: '请输入头像' }
		            ], 																nickname: [
		                { required: true, message: '请输入昵称' }
		            ], 																gender: [
		                { required: true, message: '请输入性别 man woman' }
		            ], 																ctime: [
		                { required: true, message: '请输入创建时间' }
		            ], 																isDel: [
		                { required: true, message: '请输入删除标志 t f' }
		            ], 																status: [
		                { required: true, message: '请输入状态 normal freeze' }
		            ], 																realName: [
		                { required: true, message: '请输入姓名' }
		            ], 																mobile: [
		                { required: true, message: '请输入手机号' }
		            ], 																openid: [
		                { required: true, message: '请输入微信用户ID' }
		            ], 																type: [
		                { required: true, message: '请输入会员类型 common vip supervip' }
		            ], 																company: [
		                { required: true, message: '请输入公司名称' }
		            ], 																email: [
		                { required: true, message: '请输入邮箱' }
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
			vm.member = {};
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
			this.%refs['member'].validate((valid) => {
                if (valid) {
                	var url = vm.member.id == null ? "../member/save" : "../member/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.member),
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
				    url: "../member/delete",
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
			//$.get("../member/info/" + id, function(r){
            //    vm.member = r.member;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../member/info/" + id,
				success : function(r) {
					vm.member = r.member;
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