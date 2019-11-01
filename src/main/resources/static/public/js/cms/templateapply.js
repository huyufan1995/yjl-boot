$(function () {
    $("#jqGrid").jqGrid({
        url: '../templateapply/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 60 },
			{ label: '更新时间', name: 'utime', index: 'utime', width: 60 },
			{ label: '是否发布', name: 'isRelease', index: 'is_release', width: 80,
				formatter: function (value, options, row) {
					if(row.isRelease=="t"){
						return "是";
					}else{
						return "否";
					}
				}
			},
			{ label: '初始使用量', name: 'initUseCnt', index: 'init_use_cnt', width: 80 },
			{ label: '使用量', name: 'useCnt', index: 'use_cnt', width: 80 },
			{ label: '样图', name: 'examplePath', index: 'example_path', width: 80 },
			{ label: '布局', name: 'layout', index: 'layout', width: 80 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>修改</span></button>&nbsp;";
					if(row.isRelease=="t"){
						dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='revocation_template("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>撤回</span></button>&nbsp;";
					}else{
						dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='release_template("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>发布</span></button>&nbsp;";
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
function release_template (id){
	$.ajax({
		type : "GET",
		async: false,
		url : "../templateapply/releaseapply/" + id,
		success : function(r) {
			vm.reload();
		}
	});
}

function revocation_template (id){
	$.ajax({
		type : "GET",
		async: false,
		url : "../templateapply/revocationapply/" + id,
		success : function(r) {
			vm.reload();
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
    			url: "../templateapply/logic_del/" + id,
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
		showBtn:null,
		showBg:null,
		showExample:null,
		exampleImgSrc:null,
		btnImgSrc:null,
		bgImgSrc:null,
		templateApply: {},
		ruleValidate: {},
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: []
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
			vm.title = null;
			vm.showBtn=null;
			vm.showBg=null;
			vm.showExample=null;
			vm.exampleImgSrc=null;
			vm.btnImgSrc=null;
			vm.bgImgSrc=null;
			vm.templateApply=null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.templateApply = {};
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
			this.$refs['templateApply'].validate((valid) => {
                if (valid) {
                	var url = vm.templateApply.id == null ? "../templateapply/save" : "../templateapply/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.templateApply),
        			    success: function(r){
        			    	if(r.code === 0){
        			    		vm.reload();
								vm.reset();
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
				    url: "../templateapply/delete",
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
		handleSuccess1 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBtn = true;
				vm.btnImgSrc = res.data.url;
				vm.templateApply.btn = vm.btnImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess2 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBg = true;
				vm.bgImgSrc = res.data.url;
				vm.templateApply.bg = vm.bgImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess3 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showExample = true;
				vm.exampleImgSrc = res.data.url;
				vm.templateApply.examplePath = vm.exampleImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		getInfo: function(id){
			//$.get("../templateapply/info/" + id, function(r){
            //    vm.templateApply = r.templateApply;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../templateapply/info/" + id,
				success : function(r) {
					vm.templateApply = r.templateApply;
					vm.showBtn=true;
					vm.showBg=true;
					vm.showExample=true;
					vm.exampleImgSrc=vm.templateApply.examplePath;
					vm.btnImgSrc=vm.templateApply.btn;
					vm.bgImgSrc=vm.templateApply.bg;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});