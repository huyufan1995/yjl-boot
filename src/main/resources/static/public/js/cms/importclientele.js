$(function () {
    $("#jqGrid").jqGrid({
        url: '../clientele/importList',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '客户名称', name: 'name', index: 'name', width: 80 },
			{ label: '客户手机号', name: 'mobile', index: 'mobile', width: 80 },
			{ label: '备注数量', name: 'tcrTotal', width: 80,
				formatter: function (value, options, row) {
					if(value == null){
						value = 0;
						return "<a class='btn btn-info' >"+value+"</a>";
					}
					return "<a class='btn btn-info' onclick='tcrInfo("+row.id+")'>"+value+"</a>";
				}

			},
			{ label: '标签数量', name: 'tclTotal', width: 80,
				formatter: function (value, options, row) {
					if(value == null){
						value = 0;
						return "<a class='btn btn-info' >"+value+"</a>";
					}
					return "<a class='btn btn-info' onclick='tclInfo("+row.id+")'>"+value+"</a>";
				}

			},
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '客户公司', name: 'company', index: 'company', width: 80 },
			{ label: '客户职位', name: 'position', index: 'position', width: 80 },
			{ label: '录入人', name: 'functionaryName', width: 80 },
			{ label: '来源', name: 'source',index:'source', width: 80,
				formatter: function (value, options, row) {
					if(value == 'manual'){
						return "手工录入";
					}
					if(value == 'card'){
						return "名片";
					}
					if(value == 'website'){
						return "官网";
					}
					if(value == 'imports'){
						return "Excel导入";
					}
					if(value == 'cases'){
						return "案例";
					}
					if(value == 'apply'){
						return "活动报名";
					}
				}
			},
			/*{ label: '', name: 'isDel', index: 'is_del', width: 80 },*/
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>查看</span></button>&nbsp;";
/*
                	dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='logic_del("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>删除</span></button>&nbsp;";
*/
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
	
	vm.$Modal.confirm({
        title: '提示',
        content: '确定要删除吗？',
        onOk:() => {
        	$.ajax({
    			type: "GET",
    			url: "../clientele/logic_del/" + id,
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
		clientele: {},
		ruleValidate: {
											
																name: [
		                { required: true, message: '请输入姓名' }
		            ], 																mobile: [
		                { required: true, message: '请输入' }
		            ], 																portrait: [
		                { required: true, message: '请输入头像' }
		            ], 																ctime: [
		                { required: true, message: '请输入' }
		            ], 																company: [
		                { required: true, message: '请输入公司' }
		            ], 																position: [
		                { required: true, message: '请输入职位' }
		            ], 																memberId: [
		                { required: true, message: '请输入负责人' }
		            ], 																isDel: [
		                { required: true, message: '请输入' }
		            ]							        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			name:null,
			mobile: null,
			company:null
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
			vm.q.company = null;
			vm.q.name = null;
			vm.q.mobile = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.clientele = {};
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
			this.$refs['clientele'].validate((valid) => {
                if (valid) {
                	var url = vm.clientele.id == null ? "../clientele/save" : "../clientele/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.clientele),
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
				    url: "../clientele/delete",
				    data: JSON.stringify(ids),
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
			//$.get("../clientele/info/" + id, function(r){
            //    vm.clientele = r.clientele;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../clientele/clienteleInfo/" + id,
				success : function(r) {
					vm.clientele = r.clientele;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id, "sdate":vm.q.sdate,"company":vm.q.company,"name":vm.q.name,"mobile":vm.q.mobile, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});