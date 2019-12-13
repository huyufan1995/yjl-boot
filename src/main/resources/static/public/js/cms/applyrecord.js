$(function () {
    $("#jqGrid").jqGrid({
        url: '../applyrecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '报名时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '活动标题', name: 'applyTitle', index: 'apply_title', width: 80 },
			{ label: '报名人', name: 'nickName', width: 80 }/*,
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>修改</span></button>&nbsp;";
                	dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='logic_del("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>删除</span></button>&nbsp;";
                	return dom;
                }
            }*/
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
    			url: "../applyrecord/logic_del/" + id,
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
		applyRecord: {},
		ruleValidate: {
											
																applyId: [
		                { required: true, message: '请输入' }
		            ], 																applyTitle: [
		                { required: true, message: '请输入活动标题' }
		            ], 																openid: [
		                { required: true, message: '请输入报名人openid' }
		            ]							        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			nickName:null,
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
			vm.q.nickName = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.applyRecord = {};
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
			this.$refs['applyRecord'].validate((valid) => {
                if (valid) {
                	var url = vm.applyRecord.id == null ? "../applyrecord/save" : "../applyrecord/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.applyRecord),
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
				    url: "../applyrecord/delete",
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
			//$.get("../applyrecord/info/" + id, function(r){
            //    vm.applyRecord = r.applyRecord;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../applyrecord/info/" + id,
				success : function(r) {
					vm.applyRecord = r.applyRecord;
				}
			});
		},
		easyExcel: function(id){
			var p1 = vm.q.applyTitle==null?"":vm.q.applyTitle;
			var p2 = vm.q.nickName==null?"":vm.q.nickName;
			var d1 = vm.q.sdate==null?"":vm.q.sdate;
			var d2 = vm.q.edate==null?"":vm.q.edate;
			window.open("../applyrecord/applyRecordPoi/?applyTitle="+p1+"&nickName="+p2+"&sdate="+d1+"&edate="+d2);
			/*$.ajax({
				type : "post",
				async: false,
				responseType: 'blob',
				url : "../applyrecord/applyRecordPoi/",
				success : function(res) {
					// 将文件流转成blob形式
					const blob = new Blob([res.data], {type: 'application/vnd.ms-excel'})
					let filename = 'test.xls'
					// 创建一个超链接，将文件流赋进去，然后实现这个超链接的单击事件
					const elink = document.createElement('a')
					elink.download = filename
					elink.style.display = 'none'
					elink.href = URL.createObjectURL(blob)
					document.body.appendChild(elink)
					elink.click()
					URL.revokeObjectURL(elink.href) // 释放URL 对象
					document.body.removeChild(elink)
				}
			});*/
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"applyTitle":vm.q.applyTitle,"nickName":vm.q.nickName, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});