$(function () {
    $("#jqGrid").jqGrid({
        url: '../information/list',
        datatype: "json",
        colModel: [
			{ label: '标题', name: 'title', index: 'title', width: 60 },
			{ label: '资讯视频地址', name: 'videoLink', index: 'video_link', width: 50 },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 30 },
			{ label: '资讯类型', name: 'informationTypeName', width: 20 },
			{ label: '修改时间', name: 'updateTime', index: 'update_time', width: 20 },
			{ label: '审核状态', name: 'auditStatus', index: 'audit_status', width: 50 ,
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
                		dom += "<button type='button' class='ivu-btn ivu-btn-primary' onclick='commitInformation("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>提交审核</span></button>&nbsp;";
					}
                	if(row.auditStatus == 'pending'){
						dom += "<button type='button' class='ivu-btn ivu-btn-primary' onclick='revocation("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>撤回审核</span></button>&nbsp;";
					}
					if(row.showStatus == 't'){
						dom += "<button type='button' class='ivu-btn ivu-btn-primary' onclick='stopInformation("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>小程序前台暂停展示</span></button>&nbsp;";
					}
					if(row.showStatus == 'f'){
						dom += "<button type='button' class='ivu-btn ivu-btn-primary' onclick='startInformation("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>展示资讯</span></button>&nbsp;";
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

//修改
function edit(id){
	if(id == null){
		return ;
	}
//	vm.showList = false;
    vm.title = "修改";
    vm.getInfo(id);
	vm.showList = false;
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
function commitInformation(id) {
	$.ajax({
		type: "GET",
		url: "../information/commit/" + id,
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

function stopInformation(id) {
	vm.information.id = id;
	vm.information.showStatus = 'f';
	$.ajax({
		type: "POST",
		url: "../information/updateShowStatus",
		contentType: "application/json",
		data: JSON.stringify(vm.information),
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

function startInformation(id) {
	vm.information.id = id;
	vm.information.showStatus = 't';
	$.ajax({
		type: "POST",
		url: "../information/updateShowStatus",
		contentType: "application/json",
		data: JSON.stringify(vm.information),
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
		url: "../information/revocation/" + id,
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
    			url: "../information/logic_del/" + id,
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
		bannerImgSrc:null,
		showModal5:false,
		showBannerImage:false,
		title: null,
		information: {},
		informationTypeList:[],
		ruleValidate: {
					title: [
		                { required: true, message: '请输入' }
									],
							browsTotal: [
					{ required: true, message: '请输入默认资讯浏览量' }
						],
			isDel: [
		                { required: true, message: '请输入t:代表逻辑删除,f:不删除' }
		            ], 																createTime: [
		                { required: true, message: '请输入' }
		            ], 																updateTime: [
		                { required: true, message: '请输入' }
		            ]							        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			title: null,
			informationTypeName:null
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
			vm.q.title = null;
			vm.q.informationTypeName = null;
		},
		add: function(){
			vm.showList = false;
			vm.showBannerImage = false;
			vm.bannerImgSrc = null;
			editor.setValue(null);
			vm.title = "新增";
			vm.information = {};
			vm.getInformationType();
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showModal5 =false;
			vm.showList = false;
			vm.title = "修改";
			vm.getInformationType();
            vm.getInfo(id)
		},
		getInformationType: function () {
			$.ajax({
				url: "../informationstype/queryAll",
				async: false,
				type: "GET",
				success: function (r) {
					vm.informationTypeList = r.informationTypeList;
				}
			});
		},
		handleSuccess1 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBannerImage = true;
				vm.bannerImgSrc = res.data.url;
				vm.information.banner = vm.bannerImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		saveOrUpdate: function (event) {

			this.$refs['information'].validate((valid) => {
                if (valid) {
					var content = editor.getValue();
					if(!content){
						vm.$Message.error('请输入资讯内容');
						return;
					}
					/*var base = new Base64();
					var result = base.encode(content);//解密为decode
					console.log("result="+result);*/
					vm.information.content = content;

					if(!vm.information.informationType){
						vm.$Message.error('请选择资讯类别');
						return;
					}
                	var url = vm.information.id == null ? "../information/save" : "../information/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.information),
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
			
			vm.Modal.confirm({
	        title: '提示',
	        content: '确定要删除选中的记录？',
	        onOk:() => {
	        	$.ajax({
					type: "POST",
				    url: "../information/delete",
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
			//$.get("../information/info/" + id, function(r){
            //    vm.information = r.information;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../information/info/" + id,
				success : function(r) {
					editor.setValue(r.information.content);
					vm.information = r.information;
					vm.bannerImgSrc = r.information.banner;
					vm.showBannerImage = true;
					vm.informationType = r.information.informationType;
					if(vm.information.auditStatus == 'pending'){
						vm.$Message.success('此资讯已经提交，请先撤回再修改!');
						return false;
					}
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"informationTypeName":vm.q.informationTypeName,"title":vm.q.title,"sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});