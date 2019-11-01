$(function () {
    $("#jqGrid").jqGrid({
        url: '../article/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden:true },
			{ label: '标题', name: 'title', index: 'title', width: 80 }, 			
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 }, 			
			{ label: '更新时间', name: 'utime', index: 'utime', width: 80 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom =  "<a class='btn btn-primary' onclick='details("+row.id+")'>查看</a>&nbsp;";
                	dom += "<a class='btn btn-danger' onclick='edit("+row.id+")'>编辑</a>&nbsp;";
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

//查看
function details(id){
	if(id == null){
		return ;
	}
	vm.showList = false;
	vm.isInfo = true;
    vm.title = "查看";
    vm.getInfo(id)
}

//编辑
function edit(id){
	if(id == null){
		return ;
	}
	vm.showList = false;
	vm.isInfo = false;
    vm.title = "修改";
    vm.getInfo(id)
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		isInfo: false,
		title: null,
		article: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.isInfo = false;
			vm.title = "新增";
			vm.article = {};
			editor.setValue(null);
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
			vm.isInfo = false;
            vm.title = "修改";
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			
			if(!vm.article.title){
				layer.msg("请输入标题",{icon:2});
	    		return;
			}
			
			//表单验证
			var content = editor.getValue();
			console.log(content)
	    	if(!content){
	    		layer.msg("请输入内容",{icon:2});
	    		return;
	    	}
	    	vm.article.content = content;
	    	
			var url = vm.article.id == null ? "../article/save" : "../article/update";
			$.ajax({
				type: "POST",
				contentType: "application/json",
			    url: url,
			    data: JSON.stringify(vm.article),
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
					contentType: "application/json",
				    url: "../article/delete",
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
			$.get("../article/info/"+id, function(r){
				editor.setValue(r.article.content);
                vm.article = r.article;
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