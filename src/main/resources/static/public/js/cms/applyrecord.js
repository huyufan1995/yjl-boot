$(function () {
    $("#jqGrid").jqGrid({
        url: '../applyrecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '报名时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '报名模板ID', name: 'templateApplyId', index: 'template_apply_id', width: 80 },
			{ label: '报名活动ID', name: 'applyId', index: 'apply_id', width: 80 },
			{ label: '报名人', name: 'joinNickName', index: 'join_nick_name', width: 80 },
			{ label: '报名提交数据明细', name: 'itemDetail', index: 'item_detail', width: 80 },
			{ label: '超级管理员', name: 'superiorName',width: 80 },
			{ label: '创建人', name: 'memberName',width: 80 },
			{ label: '分享人', name: 'shareName',width: 80 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom ="";
/*
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>修改</span></button>&nbsp;";
*/
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
	
	vm.$Modal.confirm({
        title: '提示',
        content: '确定要删除吗？',
        onOk:() => {
        	$.ajax({
    			type: "GET",
    			url: "../applyrecord/logicdel/" + id,
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
		applyRecord: {},
		ruleValidate: {
											
																ctime: [
		                { required: true, message: '请输入报名时间' }
		            ], 																isDel: [
		                { required: true, message: '请输入' }
		            ], 																templateApplyId: [
		                { required: true, message: '请输入报名模板ID' }
		            ], 																applyId: [
		                { required: true, message: '请输入报名ID' }
		            ], 																joinOpenid: [
		                { required: true, message: '请输入报名人openid' }
		            ], 																itemDetail: [
		                { required: true, message: '请输入报名提交数据明细' }
		            ], 																joinPortrait: [
		                { required: true, message: '请输入报名人头像' }
		            ], 																superiorId: [
		                { required: true, message: '请输入报名创建人上级memberid' }
		            ], 																memberId: [
		                { required: true, message: '请输入报名创建人memberid' }
		            ]							        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			memberName: null
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
			vm.q.memberName = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.applyRecord = {};
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
			this.$refs['applyRecord'].validate((valid) => {
                if (valid) {
                	var url = vm.applyRecord.id == null ? "../applyrecord/save" : "../applyrecord/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.applyRecord),
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
				    url: "../applyrecord/delete",
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
			//$.get("../applyrecord/info/" + id, function(r){
            //    vm.applyRecord = r.applyRecord;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../applyrecord/info/" + id,
				success : function(r) {
					vm.applyRecord = r.applyRecord;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"memberName":vm.q.memberName, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});