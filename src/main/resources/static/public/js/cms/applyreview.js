$(function () {
    $("#jqGrid").jqGrid({
        url: '../applyreview/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 20, key: true },
			{ label: '活动标题', name: 'applyTitle',width: 40 },
			{ label: '活动回顾', name: 'applyReviewContent', index: 'apply_review_content', width: 80 },
			{ label: '是否展示', name: 'showStatus', index: 'show_status', width: 30,
				formatter: function (value, options, row) {
					if(value =='t'){
						return "<span class='label label-success'>展示</span>";

					}else{
						return "<span class='label label-success'>暂停</span>";

					}
				}
			},
			{ label: '审核状态', name: 'auditStatus', index: 'audit_status', width: 20,
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
			{ label: '审核意见', name: 'auditMsg', index: 'audit_msg', width: 30 },
			{ label: '内容类型', name: 'applyReviewType', index: 'apply_review_type', width: 30,
				formatter: function (value, options, row) {
					if(value =='1'){
						return "<span class='label label-success'>文本</span>";

					}else if(value == '2'){
						return "<span class='label label-success'>图片</span>";

					}else{
						return "<span class='label label-success'>视频</span>";

					}
				}
			},
			{ label: '视频链接', name: 'videoLink', index: 'video_link', width: 50 },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 30 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
				formatter: function (value, options, row) {
					var dom = "";
					if(row.auditStatus == 'uncommit' || row.auditStatus == 'reject' ){
						dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='commitApplyReview("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>提交审核</span></button>&nbsp;";
					}
					if(row.auditStatus == 'pending'){
						dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='revocation("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>撤回审核</span></button>&nbsp;";
					}
					if(row.showStatus == 't'){
						dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='stopApplyReview("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>暂停活动</span></button>&nbsp;";
					}
					if(row.showStatus == 'f'){
						dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='startApplyReview("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>展示活动</span></button>&nbsp;";
					}
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "scroll" });
        }
    });
});
function commitApplyReview(id) {
	$.ajax({
		type: "GET",
		url: "../applyreview/commit/" + id,
		success: function(r){
			if(r.code == 0){
				$("#jqGrid").trigger("reloadGrid");
				vm.$Message.success('提交审核成功!');
			}else{
				vm.$Message.error(r.msg);
			}
		}
	});
}

function stopApplyReview(id) {
	var apply={};
	apply.id =id;
	apply.showStatus = 'f';
	$.ajax({
		type: "POST",
		url: "../applyreview/update",
		contentType: "application/json",
		data: JSON.stringify(apply),
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
}

function startApplyReview(id) {
	var apply={};
	apply.id =id;
	apply.showStatus = 't';
	$.ajax({
		type: "POST",
		url: "../applyreview/update",
		contentType: "application/json",
		data: JSON.stringify(apply),
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
}

//撤回
function revocation(id) {
	$.ajax({
		type: "GET",
		url: "../applyreview/revocation/" + id,
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
				contentType: "application/json",
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
		showModal: true,
		title: null,
		applyReview: {},
		editFlag:false,
		applyList: [],
		ruleValidate: {
					applyId: [
		                { required: false, message: '请输入活动id'}
		            ], 	applyReviewContent: [
		                { required: false, message: '请输入活动回顾'}
		            ]
		},
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			applyTitle:null
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
			vm.q.applyTitle = null;
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.applyReview = {};
			vm.getApplyList();
			editor.setValue("");
			vm.editFlag = false;
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.editFlag = true;
			vm.getApplyList();
            vm.getInfo(id);
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
					contentType: "application/json",
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
					vm.applyList.id =vm.applyReview.applyId;
				}
			});
		},
		getApplyList: function () {
			$.ajax({
				url: "../applyreview/applyList",
				async: false,
				type: "GET",
				success: function (r) {
					vm.applyList = r.applyList;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"applyTitle":vm.q.applyTitle,"sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});