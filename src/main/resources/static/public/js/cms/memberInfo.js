$(function () {
	var superiorId = getUrlParam("superiorId");
    $("#jqGrid").jqGrid({
        url: '../member/superiorId?superiorId='+superiorId,
        datatype: "json",
		colModel: [
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '头像', name: 'portrait', index: 'portrait', width: 80,
				formatter: function (value, options, row) {
					if(value == null){
						return "<span>无</span>";
					}
					return "<img src='"+value+"' width='80px' height='50px'/>";
				}
			},
			{ label: '昵称', name: 'nickname', index: 'nickname', width: 80 },
			{ label: '性别 ', name: 'gender', index: 'gender', width: 80 ,
				formatter: function (value, options, row) {
					if(value == 'man'){
						return "<span class='label label-warning'>男</span>";
					}else if (value == 'woman'){
						return "<span class='label label-warning'>女</span>";
					}else{
						return "<span class='label label-warning'>未知</span>";

					}
				}
			},
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '角色', name: 'role', index: 'role', width: 80 ,
				formatter: function (value, options, row) {
					if(value == 'admin'){
						return "<span>管理员</span>";
					}else if(value == 'staff'){
						return "<span>用户</span>";
					}else{
						return "<span>超级管理员</span>";
					}
				}
			},
			{ label: '状态', name: 'status', index: 'status', width: 80,
				formatter: function (value, options, row) {
					if(value == 'normal'){
						return "<span>正常</span>";
					}else{
						return "<span>冻结</span>";
					}
				}
			},
			{ label: '超级管理员', name: 'memberBossName', width: 80 },
			{ label: '姓名', name: 'realName', index: 'real_name', width: 80 },
			{ label: '会员开始时间', name: 'startTime', index: 'start_time', width: 80 },
			{ label: '会员结束时间', name: 'endTime', index: 'end_time', width: 80 },
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 80 },
			{ label: '微信用户ID', name: 'openid', index: 'openid', width: 80 },
			{ label: '认证类型', name: 'certType', index: 'cert_type', width: 80,
				formatter: function (value, options, row) {
					if(value == 'personage'){
						return "<span>个人</span>";
					}else if(value == 'enterprise'){
						return "<span>企业</span>";
					}else{
						return "<span>没有认证</span>";
					}
				}
			},
			{ label: '会员类型', name: 'type', index: 'type', width: 80,
				formatter: function (value, options, row) {
					if(value == 'vip'){
						return "<span>会员</span>";
					}else{
						return "<span>普通用户</span>";
					}
				}
			},
			{ label: '授权状态', name: 'authStatus', index: 'auth_status', width: 80 ,
				formatter: function (value, options, row) {
					if(value == 't'){
						return "<span>是</span>";
					}else{
						return "<span>否</span>";
					}
				}
			},
			{ label: '子账号数量', name: 'staffMaxCount', index: 'staffMaxCount', width: 80,
				formatter: function (value, options, row) {
					if(value == null){
						value = 0;
					}
					return "<a class='btn btn-info' onclick='showNoteComments("+row.id+")'>"+value+"</a>";
				}

			},
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

//详情
function details(id){
	if(id == null){
		return ;
	}
//	vm.showList = false;
	vm.showModal = true;
    vm.title = " 种草详情";
    vm.getInfo(id)
}


//回复
function answerComment(id,noteId,memberId) {

	if(id == null){
		return ;
	}
	if(noteId == null){
		return ;
	}
	vm.commentNoteReply = {};
	vm.commentNoteReply.memberId = memberId;
	vm.commentNoteReply.commentNoteId = id;
	vm.commentNoteReply.noteId = noteId;
	layer.open({
		type: 1,
		skin: 'layui-layer-lan',
		title: "回复评论",
		area: ['500px', '350px'],
		shadeClose: false,
		content: jQuery("#allotLayer_answer"),
		btn: ['确定','取消'],
		btn1: function (index) {
			if(vm.commentNoteReply.content == null || vm.commentNoteReply.content == ''){
				layer.alert('请填写回复内容!');
				return;
			}
			var url = "../commentnotereply/save";
			$.ajax({
				type: "POST",
			    url: url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.commentNoteReply),
			    success: function(r){
					if(r.code == 0){
						layer.close(index);
						alert('操作成功', function(index){
							$("#jqGrid").trigger("reloadGrid");
						});
					}else{
						alert(r.msg);
					}
				}
			});
    }
	});
}
//查看回复
function answerShow(id) {
	if(id == null){
		return ;
	}
	   $.ajax({
			type : "GET",
			async: false,
			url : "../commentnotereply/info/" + id,
			success : function(r) {
				vm.commentNoteReply = r.commentNoteReply;
				layer.open({
					type: 1,
					skin: 'layui-layer-lan',
					title: "查看评论回复",
					area: ['500px', '350px'],
					shadeClose: false,
					content: jQuery("#allotLayer_answer"),
					btn: ['确定'],
					btn1: function (index) {
						layer.close(index);
					}
				});
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
    			url: "../commentnote/logic_del/" + id,
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
		member: {},
		memberImg:null,
		ruleValidate: {

			portrait: [
				{ required: true, message: '请输入头像' }
			], 																nickname: [
				{ required: true, message: '请输入昵称' }
			], 																gender: [
				{ required: true, message: '请输入性别 man woman' }
			], 																ctime: [
				{ required: true, message: '请输入' }
			], 																role: [
				{ required: true, message: '请输入角色 boss admin staff' }
			], 																isDel: [
				{ required: true, message: '请输入删除标志 t f' }
			], 																status: [
				{ required: true, message: '请输入状态 normal freeze' }
			], 																superiorId: [
				{ required: true, message: '请输入上级ID' }
			], 																realName: [
				{ required: true, message: '请输入姓名' }
			], 																startTime: [
				{ required: true, message: '请输入会员开始时间' }
			], 																endTime: [
				{ required: true, message: '请输入会员结束时间' }
			], 																mobile: [
				{ required: true, message: '请输入手机号' }
			], 															/*	openid: [
		                { required: true, message: '请输入微信用户ID' }
		            ], 	*/															certType: [
				{ required: true, message: '请输入认证类型 unknown personage enterprise' }
			], 																type: [
				{ required: true, message: '请输入会员类型 common vip' }
			], 																authStatus: [
				{ required: true, message: '请输入授权状态 t or f' }
			]							        },
		q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			mobile: null,
			superiorId: null
		}
	},
	methods: {
		handleChange1: function(v){
			vm.member.startTime =v;
		},
		handleChange2: function(v){
			vm.member.endTime =v;
		},
		query: function () {
			vm.reload();
		},
		reset: function(){
			vm.q.id = null;
			vm.q.sdate = null;
			vm.q.edate = null;
			vm.q.ctime = null;
			vm.q.superiorId = null;
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
			this.$refs['member'].validate((valid) => {
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
					url: "../member/delete",
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
				postData:{"id": vm.q.id,"mobile": vm.q.mobile, "sdate":vm.q.sdate, "superiorId":vm.q.superiorId,"edate":vm.q.edate},
				page:page
			}).trigger("reloadGrid");
		},
		formateDate (val){
			vm.q.sdate = val[0];
			vm.q.edate = val[1];
		}
	}
});