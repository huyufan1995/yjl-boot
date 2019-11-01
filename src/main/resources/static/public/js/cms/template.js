$(function () {
    $("#jqGrid").jqGrid({
        url: '../template/list',
        datatype: "json",
        colModel: [			
			{ label: 'ID', name: 'id', index: 'id', width: 30, key: true },
			{ label: '模板名称', name: 'name', index: 'name', width: 50 }, 			
			{ label: '模板', name: 'imageTemplate', index: 'image_template', width: 60,formatter:function(value, options, row){
				if(value == null){
					return "<span>无</span>";
				}
				return "<img style='height:100px;' src='"+value+"' alt='' class='img-rounded'>";
			}}, 			
			{ label: '实例', name: 'imageExample', index: 'image_example', width: 60,formatter:function(value, options, row){
				if(value == null){
					return "<span>无</span>";
				}
				return "<img style='height:100px;' src='"+value+"' alt='' class='img-rounded'>";
			}}, 	
			{ label: '模板类型', name: 'isFun', index: 'is_fun', width: 40, formatter: function (value, options, row) {
	            	if(value == 't'){
	            		return "<span>趣图</span>";
	            	}else{
	            		return "<span>首页场景图</span>";
	            	}
	      		}
			},
			{ label: '分类ID', name: 'categoryId', index: 'category_id', width: 25 }, 			
			{ label: '分类', name: 'categoryEntity.name', index: 'categoryName', width: 30 }, 			
			{ label: '删除标志', name: 'isDel', index: 'is_del', width: 80, hidden:true }, 			
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 60 }, 			
			{ label: '更新时间', name: 'utime', index: 'utime', width: 60 }, 			
			{ label: '使用量', name: 'useCnt', index: 'use_cnt', width: 30 }, 			
			{ label: '浏览量', name: 'viewCnt', index: 'view_cnt', width: 30 }, 			
//			{ label: '是否免费', name: 'isFree', index: 'is_free', width: 30, formatter: function (value, options, row) {
//	            	if(row.isRelease == 'true'){
//	            		return "<span>免费</span>";
//	            	}else{
//	            		return "<span>付费</span>";
//	            	}
//          		}
//			},
			{ label: '模板宽', name: 'width', index: 'width', width: 30 }, 			
			{ label: '模板高', name: 'height', index: 'height', width: 30 },
			{ label: '发布状态', name: 'isRelease', index: 'is_release', width: 30,formatter: function (value, options, row) {
	            	if(row.isRelease == 'true'){
	            		return "<span>已发布</span>";
	            	}else{
	            		return "<span>未发布</span>";
	            	}
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		categorys: [],
		template: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.getCategorys();
			vm.template = {};
			vm.template.name = "模板";
			vm.template.imageTemplate = "template/id/t.jpg";
			vm.template.imageExample = "template/id/e.jpg";
			vm.template.useCnt = 0
			vm.template.viewCnt = 0
			vm.template.isFree = "true";
			vm.template.width = 690;
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getCategorys();
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			if(vm.template.isFun == null || vm.template.isFun==''){
				layer.msg("请选择模板类型",{icon:2});
				return;
			}
			var url = vm.template.id == null ? "../template/save" : "../template/update";
			$.ajax({
				type: "POST",
			    url: url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.template),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../template/delete",
				    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		release: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要发布选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../template/release",
				    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		cancelRelease: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要取消选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../template/cancelRelease",
				    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get("../template/info/"+id, function(r){
                vm.template = r.template;
            });
		},
		getCategorys : function () {
	        	$.ajax({
	                async: false,  
	                url: "../category/all",
	                type: "GET",  
	                success: function (r) {
	                	vm.categorys = r.categorys;
	                }  
	            });  
	    },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});