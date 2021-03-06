$(function () {
    $("#jqGrid").jqGrid({
        url: '../memberbanner/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '会员ID', name: 'code', width: 80 },
			{ label: '会员Banner', name: 'memberBanner', index: 'member_banner', width: 80,
				formatter: function(value, options, row) {
					if (row.memberBanner == null) {
						return "<span>无</span>";
					}
					return "<img style='height:50px;' src='" + row.memberBanner + "' alt='' class='img-rounded'>";
				}
			},
			{ label: '会员名称', name: 'nickName', width: 80 },
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
	vm.editFlag = true;
	vm.showModal = true;
    vm.title = "修改";
    vm.getMemberList();
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
    			url: "../memberbanner/logic_del/" + id,
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
		editFlag: false,
		title: null,
		showModal2: false,
		memberList: [],
		memberBanner: {},
		bannerImgSrc: null,
		showBannerImage: false,
		ruleValidate: {
			memberId: [
		                { required: true, message: '请选择会员' }
		            ]							        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			code: null,
			nickName:null
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
			vm.q.nickName = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.editFlag = false;
			vm.bannerImgSrc = null;
			vm.memberBanner = {};
			vm.getMemberList();
		},
		getMemberList: function () {
			$.ajax({
				url: "../member/queryAll",
				async: false,
				type: "GET",
				success: function (r) {
					vm.memberList = r.memberList;
				}
			});
		},
		handleSuccess1 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBannerImage = true;
				vm.bannerImgSrc = res.data.url;
				vm.memberBanner.memberBanner = vm.bannerImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(id);
		},
		saveOrUpdate: function (event) {
			this.$refs['memberBanner'].validate((valid) => {
                if (valid) {
                	if(vm.bannerImgSrc == null){
						vm.$Message.error("请上传图片");
						return;
					}
                	vm.memberBanner.memberBanner =vm.bannerImgSrc;
                	var url = vm.memberBanner.id == null ? "../memberbanner/save" : "../memberbanner/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.memberBanner),
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
				    url: "../memberbanner/delete",
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
			//$.get("../memberbanner/info/" + id, function(r){
            //    vm.memberBanner = r.memberBanner;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../memberbanner/info/" + id,
				success : function(r) {
					vm.memberBanner = r.memberBanner;
					vm.bannerImgSrc = vm.memberBanner.memberBanner;
					vm.showBannerImage =true;
					vm.memberList.id =vm.memberBanner.memberId;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id, "nickName":vm.q.nickName,"code":vm.q.code,"sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});