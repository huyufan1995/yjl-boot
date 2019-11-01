$(function () {
	var id = getUrlParam("id");
	$("#jqGrid").jqGrid({
		url: '../card/cardInfoList?id='+id,
		datatype: "json",
		colModel: [
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '姓名', name: 'name', index: 'name', width: 80 },
			{ label: '头像', name: 'portrait', index: 'portrait', width: 80 },
			{ label: '职位', name: 'position', index: 'position', width: 80 },
			{ label: '公司', name: 'company', index: 'company', width: 80 },
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 80 },
			{ label: '', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '', name: 'utime', index: 'utime', width: 80 },
			{ label: '', name: 'isDel', index: 'is_del', width: 80 },
			{ label: '性别 man woman', name: 'gender', index: 'gender', width: 80,
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
			{ label: '微信号', name: 'weixin', index: 'weixin', width: 80 },
			{ label: '邮箱', name: 'email', index: 'email', width: 80 },
			{ label: '地址', name: 'address', index: 'address', width: 80 },
			{ label: '', name: 'memberId', index: 'member_id', width: 80 },
			{ label: '名称首字母', name: 'firstLetter', index: 'first_letter', width: 80 },
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

	vm.Modal.confirm({
		title: '提示',
		content: '确定要删除吗？',
		onOk:() => {
		$.ajax({
			type: "GET",
			url: "../cardholder/logic_del/" + id,
			success: function(r){
				if(r.code == 0){
					$("#jqGrid").trigger("reloadGrid");
					vm.Message.success('操作成功!');
				}else{
					vm.Message.error(r.msg);
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
		cardHolder: {},
		ruleValidate: {

			name: [
				{ required: true, message: '请输入姓名' }
			], 																portrait: [
				{ required: true, message: '请输入头像' }
			], 																position: [
				{ required: true, message: '请输入职位' }
			], 																company: [
				{ required: true, message: '请输入公司' }
			], 																mobile: [
				{ required: true, message: '请输入手机号' }
			], 																ctime: [
				{ required: true, message: '请输入' }
			], 																utime: [
				{ required: true, message: '请输入' }
			], 																isDel: [
				{ required: true, message: '请输入' }
			], 																gender: [
				{ required: true, message: '请输入性别 man woman' }
			], 																weixin: [
				{ required: true, message: '请输入微信号' }
			], 																email: [
				{ required: true, message: '请输入邮箱' }
			], 																address: [
				{ required: true, message: '请输入地址' }
			], 																memberId: [
				{ required: true, message: '请输入' }
			], 																firstLetter: [
				{ required: true, message: '请输入名称首字母' }
			]							        },
		q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			mobile: null,
			company: null
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
			vm.q.mobile = null;
			vm.q.company = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.cardHolder = {};
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
			this.refs['cardHolder'].validate((valid) => {
				if (valid) {
					var url = vm.cardHolder.id == null ? "../cardholder/save" : "../cardholder/update";
					$.ajax({
						type: "POST",
						url: url,
						contentType: "application/json",
						data: JSON.stringify(vm.cardHolder),
						success: function(r){
							if(r.code === 0){
								vm.reload();
								vm.showModal = false;
								vm.Message.success('操作成功!');
							}else{
								vm.Message.error(r.msg);
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

			vm.Modal.confirm({
				title: '提示',
				content: '确定要删除选中的记录？',
				onOk:() => {
				$.ajax({
					type: "POST",
					url: "../cardholder/delete",
					data: JSON.stringify(ids),
					success: function(r){
						if(r.code === 0){
							$("#jqGrid").trigger("reloadGrid");
							vm.Message.success('操作成功!');
						}else{
							vm.Message.error(r.msg);
						}
					}
				});
		}
		});
		},
		getInfo: function(id){
			//$.get("../cardholder/info/" + id, function(r){
			//    vm.cardHolder = r.cardHolder;
			//});

			$.ajax({
				type : "GET",
				async: false,
				url : "../cardholder/info/" + id,
				success : function(r) {
					vm.cardHolder = r.cardHolder;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{"id": vm.q.id, "sdate":vm.q.sdate,"company":vm.q.company,"mobile":vm.q.mobile, "edate":vm.q.edate},
				page:page
			}).trigger("reloadGrid");
		},
		formateDate (val){
			vm.q.sdate = val[0];
			vm.q.edate = val[1];
		}
	}
});