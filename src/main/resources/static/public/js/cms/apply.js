$(function () {
    $("#jqGrid").jqGrid({
        url: '../apply/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 15, key: true },
			{ label: '活动标题', name: 'applyTitle', index: 'apply_title', width: 80 },
			{ label: '活动开始时间', name: 'startTime', index: 'start_time', width: 30 },
			{ label: '活动结束时间', name: 'endTime', index: 'end_time', width: 30 },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 30 },
			{ label: '活动地址', name: 'applyLocation', index: 'apply_location', width: 50 },
			{ label: '创建人', name: 'createPeople', index: 'create_people', width: 25 },
			{ label: '审核状态', name: 'auditStatus', index: 'audit_status', width: 20 ,
				formatter: function (value, options, row) {
					if (value == 'pass') {
						return "<span class='label label-success'>通过</span>";
					}else if (value == 'reject') {
						return "<span class='label label-danger'>驳回("+row.auditMsg+")</span>";
					}else if (value == 'pending') {
						return "<span class='label label-warning'>审核中</span>";
					}else{
						return "<span class='label label-primary'>未提交</span>";
					}
				}
			},
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
					var dom =  "<button type='button' class='ivu-btn ivu-btn-primary' onclick='details("+row.id+")'>查看</button>&nbsp;";

					if(row.auditStatus == 'uncommit' || row.auditStatus == 'reject' ){
						dom += "<button type='button' class='ivu-btn ivu-btn-primary' onclick='commitApply("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>提交审核</span></button>&nbsp;";
					}
					if(row.auditStatus == 'pending'){
						dom += "<button type='button' class='ivu-btn ivu-btn-primary' onclick='revocation("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>撤回审核</span></button>&nbsp;";
					}
					if(row.showStatus == 't'){
						dom += "<button type='button' class='ivu-btn ivu-btn-primary' onclick='stopApply("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>小程序端暂停展示</span></button>&nbsp;";
					}
					if(row.showStatus == 'f'){
						dom += "<button type='button' class='ivu-btn ivu-btn-primary' onclick='startApply("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>小程序端展示</span></button>&nbsp;";
					}
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "scroll" });
        }
    });
});
function dateF(time) {
	var date=new Date(time);
	var year=date.getFullYear();
	/* 在日期格式中，月份是从0开始的，因此要加0
     * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
     * */
	var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
	var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
	var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
	var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
	// 拼接
	return year+"-"+month+"-"+day+" "+hours+":"+minutes;
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
function commitApply(id) {
	$.ajax({
		type: "GET",
		url: "../apply/commit/" + id,
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

function stopApply(id) {
	vm.apply.id = id;
	vm.apply.showStatus = 'f';
	$.ajax({
		type: "POST",
		url: "../apply/update",
		contentType: "application/json",
		data: JSON.stringify(vm.apply),
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
//查看
function details(id){
	if(id == null){
		return ;
	}
	vm.showList = false;

	vm.title = "查看";
	vm.getInfo(id)
	vm.showModal5 = true;
}

function startApply(id) {
	vm.apply.id = id;
	vm.apply.showStatus = 't';
	$.ajax({
		type: "POST",
		url: "../apply/update",
		contentType: "application/json",
		data: JSON.stringify(vm.apply),
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
		url: "../apply/revocation/" + id,
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
    			url: "../apply/logic_del/" + id,
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
		dateArr: [],
		bannerImgSrc:null,
		showModal5 :false,
		showBannerImage:false,
		apply: {},
		ruleValidate: {
						applyTitle: [
							{ required: true, message: '请输入活动标题' }
						],
						dateTimeRange: [
							{ required: true, message: '请选择活动开始时间' }
						],
						applyLocation: [
							{ required: true, message: '请输入活动地址' }
						]
					},
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			applyTitle: null
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
			vm.showBannerImage = false;
			vm.bannerImgSrc = null;
			editor.setValue(null);
			vm.title = "新增";
			vm.apply = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showModal5 =false;
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(id)
		},
		handleSuccess1 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBannerImage = true;
				vm.bannerImgSrc = res.data.url;
				vm.apply.banner = vm.bannerImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		saveOrUpdate: function (event) {
			this.$refs['apply'].validate((valid) => {
                if (valid) {
					var content = editor.getValue();
					if(!content){
						vm.$Message.error('请输入活动详情');
						return;
					}
					vm.apply.startTime = dateF(vm.apply.dateTimeRange[0]);
					vm.apply.endTime = dateF(vm.apply.dateTimeRange[1]);
					vm.apply.applyContent = content;
                	var url = vm.apply.id == null ? "../apply/save" : "../apply/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.apply),
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
				    url: "../apply/delete",
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
			//$.get("../apply/info/" + id, function(r){
            //    vm.apply = r.apply;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../apply/info/" + id,
				success : function(r) {
					vm.apply = r.apply;
					editor.setValue(r.apply.applyContent);
					vm.bannerImgSrc = r.apply.banner;
					vm.dateArr[0] =vm.apply.startTime;
					vm.dateArr[1] =vm.apply.endTime;
					vm.apply.dateTimeRange = vm.dateArr;
					vm.showBannerImage = true;
					if(vm.apply.auditStatus == 'pending'){
						vm.showList = true;
						vm.$Message.success('此活动已经提交，请先撤回再修改!');
						return;
					}
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