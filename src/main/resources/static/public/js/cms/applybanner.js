$(function () {
    $("#jqGrid").jqGrid({
        url: '../applybanner/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '活动banner图', name: 'bannerImg', index: 'banner_img', width: 80,
				formatter: function(value, options, row){
					if(row.bannerImg == null){
						return "<span>无</span>";
					}
					return "<img style='height:100px;' src='"+row.bannerImg+"' alt='' class='img-rounded'>";
				}
			},
			{ label: '活动标题', name: 'applyTitle',width: 80 },
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
	vm.showModal = true;
	vm.getApplyList();
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
    			url: "../applybanner/logic_del/" + id,
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
		showBanner:false,
		bannerImgSrc:null,
		applyList:[],
		title: null,
		applyBanner: {},
		ruleValidate: {
															applyId: [
		                { required: true, message: '请输入活动Id' }
		            ]	},
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			applyTitle:null
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
			vm.q.applyTitle = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.applyBanner = {};
			vm.getApplyList();
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getApplyList();
            vm.getInfo(id)
		},
		getApplyList: function () {
			$.ajax({
				url: "../apply/queryAll",
				async: false,
				type: "GET",
				success: function (r) {
					vm.applyList = r.applyList;
				}
			});
		},
		saveOrUpdate: function (event) {
			this.$refs['applyBanner'].validate((valid) => {
                if (valid) {
                	if(vm.bannerImgSrc ==null){
						vm.$Message.error("请上传活动Banner");
						return;
					};

                	var url = vm.applyBanner.id == null ? "../applybanner/save" : "../applybanner/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.applyBanner),
        			    success: function(r){
        			    	if(r.code === 0){
        			    		vm.reload();
        			    		vm.bannerImgSrc =null;
        			    		vm.showBanner =false;
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
				    url: "../applybanner/delete",
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
			//$.get("../applybanner/info/" + id, function(r){
            //    vm.applyBanner = r.applyBanner;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../applybanner/info/" + id,
				success : function(r) {
					vm.applyBanner = r.applyBanner;
					vm.showBanner = true;
					vm.bannerImgSrc = vm.applyBanner.bannerImg;
				}
			});
		},
		closeModal:function(event){
			vm.bannerImgSrc =null;
			vm.showBanner =false;
			vm.showModal = false;
		},
		handleSuccess1 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBanner = true;
				vm.bannerImgSrc = res.data.url;
				vm.applyBanner.bannerImg = vm.bannerImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"applyTitle":vm.q.applyTitle, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});