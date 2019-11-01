$(function () {
    $("#jqGrid").jqGrid({
        url: '../card/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '姓名', name: 'name', index: 'name', width: 80 },
			{ label: '头像', name: 'portrait', index: 'portrait', width: 80,
				formatter: function (value, options, row) {
					if(value == null){
						return "<span>无</span>";
					}
					return "<img src='"+value+"' width='80px' height='50px'/>";				}
			},
			{ label: '职位', name: 'position', index: 'position', width: 80 },
			{ label: '公司', name: 'company', index: 'company', width: 80 },
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 80 },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '更新时间', name: 'utime', index: 'utime', width: 80 },
			{ label: '名片夹数量', name: 'cardTotal', width: 80,
			formatter: function (value, options, row) {
					if(value != null){
						return "<a class='btn btn-info' onclick='cardInfo(\""+row.openid+"\")'>"+value+"</a>";
					}else{
						return "<span></span>";
					}
				}
			},
			{ label: '名片二维码', name: 'qrcode', index: 'qrcode', width: 80,
				formatter: function (value, options, row) {
					if(value == null){
						return "<span>无</span>";
					}
					return "<img src='"+value+"' width='80px' height='50px'/>";
				}
			},
			{ label: '性别', name: 'gender', index: 'gender', width: 80,
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
			{ label: '自我介绍', name: 'introduce', index: 'introduce', width: 80 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>查看</span></button>&nbsp;";
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
function cardInfo(id){
	layer.open({
		type: 2,
		skin: 'layui-layer-lan',
		title: "名片列表",
		area: ['1600px', '800px'],
		shadeClose: false,
		content: 'cardInfo.html?id=' + id
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
    			url: "../card/logic_del/" + id,
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
		qrcodeSrc: null,
		card: {},
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
		            ], 																qrcode: [
		                { required: true, message: '请输入名片二维码' }
		            ], 																gender: [
		                { required: true, message: '请输入性别 man woman' }
		            ], 																weixin: [
		                { required: true, message: '请输入微信号' }
		            ], 																email: [
		                { required: true, message: '请输入邮箱' }
		            ], 																address: [
		                { required: true, message: '请输入地址' }
		            ], 																introduce: [
		                { required: true, message: '请输入自我介绍' }
		            ], 																memberId: [
		                { required: true, message: '请输入' }
		            ], 																firstLetter: [
		                { required: true, message: '请输入名称首字母' }
		            ], 																openid: [
		                { required: true, message: '请输入微信用户ID' }
		            ]							        },
        q:{
			mobile: null,
			sdate: null,
			edate: null,
			ctime: [],
			company: null,
			id: null
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reset: function(){
			vm.q.mobile = null;
			vm.q.id = null;
			vm.q.sdate = null;
			vm.q.edate = null;
			vm.q.ctime = null;
			vm.q.company = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.card = {};
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
			this.$refs['card'].validate((valid) => {
                if (valid) {
                	var url = vm.card.id == null ? "../card/save" : "../card/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.card),
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
				    url: "../card/delete",
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
			//$.get("../card/info/" + id, function(r){
            //    vm.card = r.card;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../card/info/" + id,
				success : function(r) {
					vm.card = r.card;
					vm.qrcodeSrc = r.card.qrcode;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id":vm.q.id,"mobile": vm.q.mobile, "sdate":vm.q.sdate,"company":vm.q.company, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});