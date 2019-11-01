$(function () {
    $("#jqGrid").jqGrid({
        url: '../casus/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '标题', name: 'title', index: 'title', width: 80 },
			{ label: '浏览量', name: 'viewCnt', index: 'view_cnt', width: 80 },
			{ label: '创建人', name: 'memberName', width: 80 },
			{ label: '手机号', name: 'mobile', width: 80 },
			{ label: '公司名称', name: 'company',width: 80 },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '更新时间', name: 'utime', index: 'utime', width: 80 },
			{ label: '详情JSON', name: 'company', width: 80,
				formatter: function (value, options, row) {
					if(value != null){
						return "<a class='btn btn-info' onclick='detailsInfo(\""+row.id+"\")'>"+'详情'+"</a>";
					}else{
						return "<span></span>";
					}
				}
			},
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-error' onclick='showCaseImg("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>案例详情二维码</span></button>&nbsp;";
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
	var id = getUrlParam("id");
	if(id !=null || id !='' ) {
		vm.q.id = id;
		vm.query();
	}
});


function detailsInfo(id){
	layer.open({
		type: 2,
		skin: 'layui-layer-lan',
		title: "案例详情数据列表数据列表",
		area: ['1150px', '800px'],
		shadeClose: false,
		content: 'casusjson.html?id=' + id
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
//显示案例详情二维码
function showCaseImg(id) {
	vm.showModal3 = true;
	vm.title = "案例详情二维码";
	vm.casusImg ="../casus/infoImg/" + id;
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
    			url: "../casus/logic_del/" + id,
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
function getUrlParam(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r!=null) return unescape(r[2]); return null;
}
var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		showModal: false,
		casusImg: null,
		showModal3: false,
		title: null,
		casus: {},
		ruleValidate: {
			title: [
		                { required: true, message: '请输入' }
		            ], 																viewCnt: [
		                { required: true, message: '请输入浏览量' }
		            ], 																memberId: [
		                { required: true, message: '请输入会员ID' }
		            ], 																superiorId: [
		                { required: true, message: '请输入上级会员ID' }
		            ], 																ctime: [
		                { required: true, message: '请输入创建时间' }
		            ], 																utime: [
		                { required: true, message: '请输入更新时间' }
		            ], 																isDel: [
		                { required: true, message: '请输入删除标志' }
		            ], 																details: [
		                { required: true, message: '请输入详情JSON' }
		            ]							        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			company: null,
			mobile: null
		}
	},
	mounted: function() {
		var id = getUrlParam("id");
		if(id !=null || id !='' ){
			this.$nextTick(function () {
					//vm.getFirstClass();
					//vm.addSku();
					vm.q.id=id;
				vm.showList = true;
				var page = $("#jqGrid").jqGrid('getGridParam','page');
				$("#jqGrid").jqGrid('setGridParam',{
					postData:{"id": vm.q.id,"company":vm.q.company, "sdate":vm.q.sdate, "edate":vm.q.edate},
					page:page
				}).trigger("reloadGrid");
			})
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
			vm.q.company = null;
			vm.q.mobile = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.casus = {};
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
			this.$refs['casus'].validate((valid) => {
                if (valid) {
                	var url = vm.casus.id == null ? "../casus/save" : "../casus/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.casus),
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
				    url: "../casus/delete",
					contentType: "application/json",
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
			//$.get("../casus/info/" + id, function(r){
            //    vm.casus = r.casus;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../casus/info/" + id,
				success : function(r) {
					vm.casus = r.casus;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"company":vm.q.company, "sdate":vm.q.sdate, "edate":vm.q.edate,"mobile":vm.q.mobile},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});