$(function () {
    $("#jqGrid").jqGrid({
        url: '../certificate/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 40, key: true },
			{ label: '名片用户Id', name: 'memberId', index: 'member_id', width: 40 },
			{ label: '认证类型', name: 'type', index: 'type', width: 40,
				formatter: function (value, options, row) {
					if(value == 'enterprise'){
						return "<span>企业</span>";
					}else{
						return "<span>个人</span>";
					}
				}
			},
			{ label: '营业执照', name: 'license', index: 'license', width: 50 },
			{ label: '身份证正面', name: 'identityCardFront', index: 'identity_card_front', width: 50 },
			{ label: '手持身份证', name: 'identityCardFull', index: 'identity_card_full', width: 50 },
			{ label: '身份证反面', name: 'identityCardReverse', index: 'identity_card_reverse', width: 50 },
			{ label: '姓名', name: 'name', index: 'name', width: 40 },
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 50 },
			{ label: '身份证号', name: 'identityCardNumber', index: 'identity_card_number', width: 70 },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 70 },
			{ label: '更新时间', name: 'utime', index: 'utime', width: 70 },
			{ label: '审核状态', name: 'status', index: 'status', width: 80,
				formatter: function (value, options, row) {
					if(value == 'pass'){
						return "<span>通过</span>";
					}else if(value == 'reject'){
						return "<span>驳回</span>";
					}else{
						return "<span>审核中</span>";
					}
				}

			},
			{ label: '拒绝理由', name: 'reason', index: 'reason', width: 80 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>认证</span></button>&nbsp;";
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
    			url: "../certificate/logic_del/" + id,
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
		licenseImg: null,
		certificateFlag: null,
		identityCardFrontImg: null,
		identityCardFullImg:null,
		identityCardReverseImg:null,
		certificate: {},
		ruleValidate: {

																memberId: [
		                { required: true, message: '请输入' }
		            ]
		},
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			mobile:null
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
			vm.q.mobile =null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.certificate = {};
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
			this.$refs['certificate'].validate((valid) => {
                if (valid) {

                	var url = vm.certificate.id == null ? "../certificate/save" : "../certificate/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.certificate),
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
				    url: "../certificate/delete",
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
			//$.get("../certificate/info/" + id, function(r){
            //    vm.certificate = r.certificate;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../certificate/info/" + id,
				success : function(r) {
					if(r.certificate.type=='enterprise'){
						vm.certificateFlag="企业认证";
					}else{
						vm.certificateFlag="个人认证";
					}

					vm.licenseImg=r.certificate.license;
					vm.identityCardFrontImg=r.certificate.identityCardFront;
					vm.identityCardFullImg=r.certificate.identityCardFull;
					vm.identityCardReverseImg=r.certificate.identityCardReverse;
					vm.certificate = r.certificate;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"mobile":vm.q.mobile, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});