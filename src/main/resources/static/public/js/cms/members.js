$(function () {
    $("#jqGrid").jqGrid({
        url: '../member/list',
        datatype: "json",
        colModel: [			
			{ label: '编号', name: 'id', index: 'id', width: 30, key: true },
			{ label: '会员ID', name: 'code', index: 'code', width: 80 },
			{ label: '昵称', name: 'nickname', index: 'nickname', width: 80 },
			{ label: '性别', name: 'gender', index: 'gender', width: 30,
				formatter: function (value, options, row) {
					if(value == '1'){
						return "<span class='label label-warning'>男</span>";
					}else if (value == '2'){
						return "<span class='label label-warning'>女</span>";
					}else{
						return "<span class='label label-warning'>未知</span>";
					}
				}
			},
			{ label: '认证状态', name: 'auditStatus', index: 'audit_status', width: 50,
				formatter: function (value, options, row) {
					if(value == 'pass'){
						return "<span class='label label-warning'>认证通过</span>";
					}else if (value == 'pending'){
						return "<span class='label label-warning'>请认证</span>";
					}else{
						return "<span class='label label-warning'>无</span>";
					}
				}
			},
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 60 },
			{ label: '邮箱', name: 'email', index: 'email', width: 50 },
			{ label: '是否拥有VIP', name: 'showVip', index: 'show_vip', width: 30,
				formatter: function (value, options, row) {
					if(value == 't'){
						return "<span class='label label-warning' >是</span>";
					}else{
						return "<span class='label label-warning'>否</span>";
					}
				}
			},/*
			{ label: '手机号2 ：非必填', name: 'phone', index: 'phone', width: 80 },*/
			{ label: '微信号', name: 'weixinNumber', index: 'weixin_number', width: 50 },
			{ label: '出生日期', name: 'birthday', index: 'birthday', width: 40 },
			{ label: '国籍', name: 'nationality', index: 'nationality', width: 50 },
			{ label: '当前居住地址', name: 'address', index: 'address', width: 60 },
			{ label: '个人简介', name: 'profile', index: 'profile', width: 60 },/*
			{ label: '公司介绍', name: 'companyProfile', index: 'company_profile', width: 80 },
			{ label: '拥有资源', name: 'havaResource', index: 'hava_resource', width: 80 },
			{ label: '需要资源', name: 'needResource', index: 'need_resource', width: 80},
			{ label: '手机号1所属地区', name: 'mobileCountry', index: 'mobile_country', width: 80 },
			{ label: '手机号2所属地区', name: 'phoneCountry', index: 'phone_country', width: 80 },*/
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {

                	var dom ="<button type='button' class='ivu-btn ivu-btn-primary' onclick='showDesc("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>查看</span></button>&nbsp;";
					if(row.auditStatus =='pending'){
						dom +="<button type='button' class='ivu-btn ivu-btn-primary' onclick='showMember("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>认证</span></button>&nbsp;";
					}
					if(row.type =='vip'){
						if(row.showVip =='t'){
							dom +="<button type='button' class='ivu-btn ivu-btn-primary' onclick='removeVipLogo("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>取消VIP图标</span></button>&nbsp;";
						}else{
							dom +="<button type='button' class='ivu-btn ivu-btn-primary' onclick='addVipLogo("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>增加VIP图标</span></button>&nbsp;";
						}
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

function showDesc(v){
	vm.show(v);
}
//修改
function edit(id){
	if(id == null){
		return ;
	}
	vm.showList = false;
	//vm.showModal = true;
    vm.title = "修改";
    vm.getInfo(id)
}

function showMember(id){
	if(id == null){
		return ;
	}
	//vm.showList = false;
	vm.showModal2 = true;
	vm.title = "修改";
	vm.getInfo(id)
}

//取消vipLogo
function removeVipLogo(id){
	if(id == null){
		return ;
	}
	$.ajax({
	type: "GET",
	url: "../member/removeVipLogo/" + id,
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

//增加vipLogo
function addVipLogo(id){
	if(id == null){
		return ;
	}
	$.ajax({
		type: "GET",
		url: "../member/addVipLogo/" + id,
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
		qrcode: null,
		title: null,
		showModal4: false,
		showModal2: false,
		showMemberImage: false,
		memberImgSrc: null,
		member: {},
		ruleValidate: {
			nickname: [
		                { required: true, message: '请输入昵称' }
		                ]
		},
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			code:null
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
			vm.q.code = null;
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
		show: function (id) {
			//var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
			vm.title = "查看";
			vm.getInfo(id)
		},
		noPass: function (event) {
			vm.showModal2 = false;
			vm.showModal4 = true;
			vm.title = "请输入不通过原因";
		},
		memberPass: function(event){
			$.ajax({
				type: "POST",
				url: "../member/updateType",
				contentType: "application/json",
				data: JSON.stringify(vm.member),
				success: function(r){
					if(r.code === 0){
						vm.showModal = false;
						vm.showModal4 = false;
						vm.showModal2 =false;
						vm.member.auditMsg = null;
						vm.$Message.success('操作成功!');
						vm.reload();
					}else{
						vm.$Message.error(r.msg);
					}
				}
			});
		},
		noMemberPass: function(event){
			$.ajax({
				type: "POST",
				url: "../member/updateTypeNoPass",
				contentType: "application/json",
				data: JSON.stringify(vm.member),
				success: function(r){
					if(r.code === 0){
						vm.showModal = false;
						vm.showModal4 = false;
						vm.showModal2 =false;
						vm.member.auditMsg = null;
						vm.$Message.success('操作成功!');
						vm.reload();
					}else{
						vm.$Message.error(r.msg);
					}
				}
			});
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
					vm.qrcode = vm.member.qrCode;
					vm.memberImgSrc = vm.member.portrait;
					if(vm.member.gender == '1'){
						vm.member.gender= '男';
					}else{
						vm.member.gender= '女';

					}
					vm.showMemberImage = true;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"code":vm.q.code, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});