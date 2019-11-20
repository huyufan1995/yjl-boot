$(function () {
    $("#jqGrid").jqGrid({
        url: '../member/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '状态', name: 'status', index: 'status', width: 80,
				formatter: function (value, options, row) {
					if(value == 'normal'){
						return "<span>正常</span>";
					}else{
						return "<span>封禁</span>";
					}
				}
			},
			{ label: '姓名', name: 'nickname', index: 'nickname', width: 80 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='detail("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>查看</span></button>&nbsp;";
                	/*dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='logic_del("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>删除</span></button>&nbsp;";*/
                	if(row.status=='freeze'){
						dom +="<button type='button' class='ivu-btn ivu-btn-primary' onclick='openMember("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>解禁</span></button>&nbsp;";

					}else{
						dom +="<button type='button' class='ivu-btn ivu-btn-primary' onclick='forbiddenMember("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>封禁</span></button>&nbsp;";
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});
function memberInfo(id){
	layer.open({
		type: 2,
		skin: 'layui-layer-lan',
		title: "用户列表",
		area: ['850px', '450px'],
		shadeClose: false,
		content: 'memberInfo.html?superiorId=' + id
	});
}
//解禁
function openMember(id) {
	if(id == null){
		return
	}
	$.ajax({
		type: "get",
		url: "../member/openMember?id="+id,
		contentType: "application/json",
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
function companyInfo(company){
	layer.open({
		type: 2,
		skin: 'layui-layer-lan',
		title: "企业会员列表",
		area: ['1150px', '800px'],
		shadeClose: false,
		content: 'companyInfo.html?company=' + company
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

function detail(id){
	if(id == null){
		return ;
	}
//	vm.showList = false;
	vm.showModal2 = true;
	vm.title = "查看";
	vm.getInfo(id)
}
//封禁用户
function forbiddenMember(id){
	if(id == null){
		return ;
	}
	vm.showModal4 = true;
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
var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		showModal: false,
		showModal2:false,
		showModal4:false,
		title: null,
		member: {},
		forbidden:{},
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
			superiorId: null,
			type: null
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
		freezeMember: function (event) {
			vm.forbidden.openId =vm.member.openid;
			vm.forbidden.forbiddenMsg =vm.member.forbiddenMsg;
			if(vm.forbidden.forbiddenMsg==null){
				return;
			}
            var param = JSON.stringify(vm.forbidden);
            $.ajax({
                type: "POST",
                url: "../member/forbiddenMember",
				contentType: "application/json",
                data: param,
                success: function(r){
                    if(r.code === 0){
                    	vm.showModal4=false;
                        $("#jqGrid").trigger("reloadGrid");
                        vm.$Message.success('封禁用户成功!');
                    }else{
                        vm.$Message.error(r.msg);
                    }
                }
            });
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
				postData:{"id": vm.q.id,"mobile": vm.q.mobile, "sdate":vm.q.sdate, "superiorId":vm.q.superiorId,"type":vm.q.type,"edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }

	}
});