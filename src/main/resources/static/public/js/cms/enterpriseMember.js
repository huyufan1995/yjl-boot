$(function () {
    $("#jqGrid").jqGrid({
        url: '../member/enterpriseList',
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
			{ label: '公司', name: 'company', width: 80/* ,
				formatter: function (value, options, row) {
					if(value == ''){
						return "<span>无</span>";
					}
				}*/},
			{ label: '会员开始时间', name: 'startTime', index: 'start_time', width: 80 },
			{ label: '会员结束时间', name: 'endTime', index: 'end_time', width: 80 },
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 80 },
			/*{ label: '微信用户ID', name: 'openid', index: 'openid', width: 80 },*/
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
			{ label: '子账号数量', name: 'staffMaxCount', index: 'staff_max_count', width: 80,
				formatter: function (value, options, row) {
				if(value == null){
					value = 0;
					return "<a class='btn btn-info' >"+value+"</a>";
				}
				return "<a class='btn btn-info' onclick='memberInfo("+row.id+")'>"+value+"</a>";
				}

			},
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='detail("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>查看</span></button>&nbsp;";
					dom +="<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>修改</span></button>&nbsp;";
                	/*dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='logic_del("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>删除</span></button>&nbsp;";*/
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
function memberInfo(id){
	layer.open({
		type: 2,
		skin: 'layui-layer-lan',
		title: "评论列表",
		area: ['850px', '450px'],
		shadeClose: false,
		content: 'memberInfo.html?superiorId=' + id
	});
}
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
	var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
	// 拼接
	return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
}
//修改
function edit(id){
	if(id == null){
		return ;
	}
	vm.memberImg = vm.member.portrait;
//	vm.showList = false;
	vm.showModal = true;
    vm.title = "修改";
    vm.getInfo(id)
}

function detail(id){
	if(id == null){
		return ;
	}
	vm.memberImg = vm.member.portrait;
//	vm.showList = false;
	vm.showModal2 = true;
	vm.title = "查看";
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
    			url: "../member/logic_del/" + id,
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
		showModal2:false,
		title: null,
		member: {},
		memberImg:null,
		ruleValidate: {
											
																portrait: [
		                { required: true, message: '请输入头像' }
		            ], 																nickname: [
		                { required: true, message: '请输入昵称' }
		            ], 																gender: [
		                { required: true, message: '请输入性别' }
		            ], 																ctime: [
		                { required: true, message: '请输入' }
		            ], 																role: [
		                { required: true, message: '请输入角色' }
		            ], 																isDel: [
		                { required: true, message: '请输入删除标志' }
		            ], 																status: [
		                { required: true, message: '请输入状态' }
		            ], 																superiorId: [
		                { required: true, message: '请输入上级ID' }
		            ], 																realName: [
		                { required: true, message: '请输入姓名' }
		            ], 																startTime: [
		                { required: true, message: '请输入会员开始时间' }
		            ], 																endTime: [
		                { required: true, message: '请输入会员结束时间' }
		            ], 																/*mobile: [
		                { required: true, message: '请输入手机号' }
		            ], 												*/			/*	openid: [
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
			superiorId: null,
			type:null,
			company: null
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
			vm.q.type = null;
			vm.q.company = null;
			vm.q.mobile = null;
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
                	//对日期格式化
					vm.member.startTime =dateF(vm.member.startTime);
					vm.member.endTime =dateF(vm.member.endTime);
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
		saveOrUpdate: function (event) {
			this.$refs['member'].validate((valid) => {
				if (valid) {
					//对日期格式化
					vm.member.startTime =dateF(vm.member.startTime);
					vm.member.endTime =dateF(vm.member.endTime);
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
				postData:{"id": vm.q.id,"company":vm.q.company,"type":vm.q.type,"mobile": vm.q.mobile, "sdate":vm.q.sdate, "superiorId":vm.q.superiorId,"edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});