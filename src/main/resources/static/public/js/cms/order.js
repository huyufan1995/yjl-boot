$(function () {
    $("#jqGrid").jqGrid({
        url: '../order/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '订单编号', name: 'orderSn', index: 'order_sn', width: 80 },
			{ label: '会员Id', name: 'memberId', index: 'member_id', width: 80 },
			{ label: '会员名称', name: 'memberName', width: 80 },
			{ label: '手机号', name: 'mobile', width: 80 },
			{ label: '公司名', name: 'company', width: 80 },
			{ label: '账号数量', name: 'quantity', width: 80 },
			{ label: '有效期（天）', name: 'time', width: 80 },
			{ label: '开通类型', name: 'type', index: 'type', width: 80 ,
				formatter: function (value, options, row) {
					if(value=='upgrade'){
						return "升级";
					}else if(value=='renewal'){
						return "续费";
					}else{
						return "新购";
					}
				}
			},
			{ label: 'vip开始时间', name: 'startTime', width: 80 },
			{ label: 'vip到期时间', name: 'endTime', width: 80 },
			{ label: '公司名', name: 'company', width: 80 },
			{ label: '支付时间', name: 'paymentTime', index: 'payment_time', width: 80 },
			{ label: '支付金额', name: 'payMoney', index: 'pay_money', width: 80 },
			{ label: '下单时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '百度用户ID', name: 'baiduUserId', index: 'baidu_user_id', width: 80 },
			{ label: '百度订单ID', name: 'baiduOrderId', index: 'baidu_order_id', width: 80 },
			{ label: '平台', name: 'platform', index: 'platform', width: 80 },
			{ label: '状态', name: 'status', index: 'status', width: 80,
				formatter: function (value, options, row) {
					if(value=='paid'){
					return "<span class='label label-warning'>已支付</span>";

					}
					return "未支付";
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
    			url: "../order/logic_del/" + id,
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
		order: {},
		ruleValidate: {
											
																orderSn: [
		                { required: true, message: '请输入订单编号' }
		            ], 																memberId: [
		                { required: true, message: '请输入' }
		            ], 																paymentTime: [
		                { required: true, message: '请输入支付时间' }
		            ], 																payMoney: [
		                { required: true, message: '请输入支付金额' }
		            ], 																startTime: [
		                { required: true, message: '请输入' }
		            ], 																endTime: [
		                { required: true, message: '请输入' }
		            ], 																baiduUserId: [
		                { required: true, message: '请输入百度用户ID' }
		            ], 																baiduOrderId: [
		                { required: true, message: '请输入百度订单ID' }
		            ], 																platform: [
		                { required: true, message: '请输入平台' }
		            ], 																status: [
		                { required: true, message: '请输入状态' }
		            ]							        },
        q:{
			sdate: null,
			edate: null,
			ctime: [],
			orderSn: null,
			mobile: null,
			company: null
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reset: function(){
			vm.q.orderSn = null;
			vm.q.sdate = null;
			vm.q.edate = null;
			vm.q.ctime = null;
			vm.q.company = null;
			vm.q.mobile = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.order = {};
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
			this.$refs['order'].validate((valid) => {
                if (valid) {
                	var url = vm.order.id == null ? "../order/save" : "../order/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.order),
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
				    url: "../order/delete",
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
			//$.get("../order/info/" + id, function(r){
            //    vm.order = r.order;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../order/info/" + id,
				success : function(r) {
					vm.order = r.order;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"orderSn": vm.q.orderSn,"mobile": vm.q.mobile,"company": vm.q.company, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});