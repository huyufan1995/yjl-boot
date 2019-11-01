$(function () {
    $("#jqGrid").jqGrid({
        url: '../userecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden:true },
			{ label: '用户ID', name: 'openid', index: 'openid', width: 80 },
			{ label: '用户头像', name: 'avatarUrl', index: 'avatar_url', width: 30, formatter: function(value, options, row){
				if(value == null){
					return "<span>未授权</span>";
				}
				return "<img style='height:50px;' src='"+value+"' alt='' class='img-rounded'>";
			}},
			{ label: '模板名称', name: 'templateName', index: 'template_name', width: 50 }, 			
			{ label: '模板ID', name: 'templateId', index: 'template_id', width: 30 }, 			
			{ label: '模板', name: 'templateImageExample', index: 'template_image_example', width: 50, formatter:function(value, options, row){
				if(value == null){
					return "<span>无</span>";
				}
				return "<img style='height:100px;' src='"+value+"' alt='' class='img-rounded'>";
			}}, 			
			{ label: '结果', name: 'templateImageResult', index: 'template_image_result', width: 50, formatter:function(value, options, row){
				if(value == null){
					return "<span>无</span>";
				}
				return "<img style='height:100px;' src='"+value+"' alt='' class='img-rounded'>";
			}},		
			{ label: '使用时间', name: 'useTime', index: 'use_time', width: 80 },
			{ label: '耗时(毫秒)', name: 'consumeTime', index: 'consume_time', width: 80 }
        ],
		viewrecords: true,
		height: $(window).height() - 250,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
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
		useRecord: {},
		q:{
			openid: null,
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reset:function(){
			$(".grid-btn input").val("");
			vm.q.openid = null;
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.useRecord = {};
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
			var url = vm.useRecord.id == null ? "../userecord/save" : "../userecord/update";
			$.ajax({
				type: "POST",
			    url: url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.useRecord),
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
				    url: "../userecord/delete",
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
			$.get("../userecord/info/"+id, function(r){
                vm.useRecord = r.useRecord;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{"openid": vm.q.openid},
                page:page
            }).trigger("reloadGrid");
		}
	}
});