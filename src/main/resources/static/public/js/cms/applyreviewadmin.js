$(function () {
    $("#jqGrid").jqGrid({
        url: '../applyreview/adminList',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '活动id', name: 'applyId', index: 'apply_id', width: 80 },
			{ label: '活动回顾', name: 'applyReviewContent', index: 'apply_review_content', width: 80 },
			{ label: 't:展示 f:暂停', name: 'showStatus', index: 'show_status', width: 80 },
			{ label: '审核状态', name: 'auditStatus', index: 'audit_status', width: 80,
				formatter: function (value, options, row) {
					if (value == 'pass') {
						return "<span class='label label-success'>通过</span>";
					}else if (value == 'reject') {
						return "<span class='label label-danger'>驳回</span>";
					}else if (value == 'pending') {
						return "<span class='label label-warning'>审核中</span>";
					}else{
						return "<span class='label label-primary'>未提交</span>";
					}
				}
			},
			{ label: '审核意见', name: 'auditMsg', index: 'audit_msg', width: 80 },
			{ label: '1:text文本 2：image图片 3video视频', name: 'applyReviewType', index: 'apply_review_type', width: 80 },
			{ label: '视频链接', name: 'videoLink', index: 'video_link', width: 80 },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
				formatter: function (value, options, row) {
					var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='release("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>通过</span></button>&nbsp;";
					dom += "<button type='button' class='ivu-btn ivu-btn-primary' onclick='openAudit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>不通过</span></button>&nbsp;";
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
function release(id) {
	$.ajax({
		type: "GET",
		url: "../applyreview/release/" + id,
		success: function(r){
			if(r.code == 0){
				$("#jqGrid").trigger("reloadGrid");
				vm.$Message.success('审核成功!');
			}else{
				vm.$Message.error(r.msg);
			}
		}
	});
}

function openAudit(id) {
	vm.showAudit = true;
	vm.applyReview.id = id;
}
//撤回
function revocation(id) {
	$.ajax({
		type: "GET",
		url: "../applyReview/revocation/" + id,
		success: function(r){
			if(r.code == 0){
				$("#jqGrid").trigger("reloadGrid");
				vm.$Message.success('撤回成功!');
			}else{
				vm.$Message.error(r.msg);
			}
		}
	});
}
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
    			url: "../applyreview/logic_del/" + id,
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
		showAudit: false,
		applyReview: {},
		ruleValidate: {
					applyId: [
		                { required: false, message: '请输入活动id' }
		            ], 	applyReviewContent: [
		                { required: false, message: '请输入活动回顾' }
		            ]
		},
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
			vm.showList = false;
			vm.title = "新增";
			vm.applyReview = {};
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
		rejectApplyReview: function(event){
			if(vm.applyReview.auditMsg == null){
				vm.$Message.error('请输入不通过意见');
				return;
			}
			$.ajax({
				type: "POST",
				url: "../applyreview/reject",
				contentType: "application/json",
				data: JSON.stringify(vm.applyReview),
				success: function(r){
					if(r.code === 0){
						vm.reload();
						vm.showAudit = false;
						vm.$Message.success('操作成功!');
					}else{
						vm.$Message.error(r.msg);
					}
				}
			});

		},
		saveOrUpdate: function (event) {
			this.$refs['applyReview'].validate((valid) => {
                if (valid) {
					var content = editor.getValue();
					if(!content){
						vm.$Message.error('请输入活动回顾');
						return;
					}
					vm.applyReview.applyReviewContent = content;
                	var url = vm.applyReview.id == null ? "../applyreview/save" : "../applyreview/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.applyReview),
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
				    url: "../applyreview/delete",
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
			//$.get("../applyreview/info/" + id, function(r){
            //    vm.applyReview = r.applyReview;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../applyreview/info/" + id,
				success : function(r) {
					vm.applyReview = r.applyReview;
					editor.setValue(r.applyReview.applyReviewContent);
					if(vm.applyReview.auditStatus == 'pending'){
						vm.$Message.success('此活动回顾已经提交，请先撤回再修改!');
						return false;
					}
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