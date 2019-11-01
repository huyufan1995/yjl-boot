$(function () {
    $("#jqGrid").jqGrid({
        url: '../template/selected_list',
        datatype: "json",
        colModel: [			
			{ label: 'ID', name: 'id', index: 'id', width: 30, key: true },
			{ label: '模板名称', name: 'name', index: 'name', width: 50 }, 			
			{ label: '模板', name: 'imageTemplate', index: 'image_template', width: 80,formatter:function(value, options, row){
				if(value == null){
					return "<span>无</span>";
				}
				return "<img style='height:100px;' src='"+value+"' alt='' class='img-rounded'>";
			}}, 			
			{ label: '实例', name: 'imageExample', index: 'image_example', width: 80,formatter:function(value, options, row){
				if(value == null){
					return "<span>无</span>";
				}
				return "<img style='height:100px;' src='"+value+"' alt='' class='img-rounded'>";
			}}, 			
//			{ label: '分类', name: 'categoryId', index: 'category_id', width: 30 }, 			
			{ label: '删除标志', name: 'isDel', index: 'is_del', width: 80, hidden:true }, 
			{ label: '使用量', name: 'useCnt', index: 'use_cnt', width: 30 }, 			
			{ label: '浏览量', name: 'viewCnt', index: 'view_cnt', width: 30 }, 			
			{ label: '点赞量', name: 'praiseCnt', index: 'praise_cnt', width: 30  , formatter: function(value, options, row){
				return '<a href="javascript:void(0)" onclick="praiseInfo(\''+row.id+'\')">'+value+'</a>';
			}},
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 }, 			
			{ label: '更新时间', name: 'utime', index: 'utime', width: 80 }		
					
//			{ label: '是否免费', name: 'isFree', index: 'is_free', width: 30, formatter: function (value, options, row) {
//	            	if(row.isRelease == 'true'){
//	            		return "<span>免费</span>";
//	            	}else{
//	            		return "<span>付费</span>";
//	            	}
//          		}
//			},
//			{ label: '模板宽', name: 'width', index: 'width', width: 30 }, 			
//			{ label: '模板高', name: 'height', index: 'height', width: 30 },
//			{ label: '发布状态', name: 'isRelease', index: 'is_release', width: 30,formatter: function (value, options, row) {
//	            	if(row.isRelease == 'true'){
//	            		return "<span>已发布</span>";
//	            	}else{
//	            		return "<span>未发布</span>";
//	            	}
//	          	}
//	        }
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

function praiseInfo(id){
	if(id == null){
		return ;
	}
	vm.getUserPraiseInfo(id);
    vm.$Modal.info({
    	okText: '关闭',
    	width: 1000,
    	title: '点赞记录',
        render: (h) => {
        	return h('Table', {
                props: {
                	columns: vm.praiseColumns,
                	data: vm.praiseRecord,
                	border : true,
                	width: 950,
                	height: 500
                },
            })
        }
    })
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		template: {},
		praiseRecord:[],
		praiseColumns: [
           
             {
                title: '模板ID',
                key: 'templateId'
            },
            {
                title: '用户ID',
                key: 'openId'
            },
            {
                title: '用户头像',
                key: 'avatarUrl',
                render: (h, params) => {
                    return h('img',{domProps:{
                    src:params.row.avatarUrl
                  }})}
            },
            {
                title: '用户昵称',
                key: 'nickName'
            },
            {
                title: '点赞日期',
                key: 'createTime'
            }
        ]
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
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
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
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
		getUserPraiseInfo: function(id){
			$.get("../praiserecord/praiseList/" + id, function(r){
                vm.praiseRecord = r.praiseRecordList;
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