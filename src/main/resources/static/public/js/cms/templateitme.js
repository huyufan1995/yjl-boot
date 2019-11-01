$(function () {
    $("#jqGrid").jqGrid({
        url: '../templateitme/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden:true },
			{ label: '模板ID', name: 'templateId', index: 'template_id', width: 80 }, 			
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80, hidden:true }, 			
			{ label: '更新时间', name: 'utime', index: 'utime', width: 80, hidden:true }, 			
			{ label: '是否删除', name: 'isDel', index: 'is_del', width: 80, hidden:true }, 			
			{ label: '序号', name: 'sortNum', index: 'sort_num', width: 80, hidden:true },		
			{ label: '类型', name: 'type', index: 'type', width: 80 }, 			
			{ label: '字体名称', name: 'fontName', index: 'font_name', width: 80 }, 			
			{ label: '字体样式', name: 'fontStyle', index: 'font_style', width: 80 }, 			
			{ label: '字体大小', name: 'fontSize', index: 'font_size', width: 80 },
			{ label: '字间距', name: 'wordSpace', index: 'word_space', width: 80 }, 			
			{ label: '字行距', name: 'lineSpace', index: 'line_space', width: 80 }, 	
			{ label: 'R', name: 'fontColorR', index: 'font_color_r', width: 80 }, 			
			{ label: 'G', name: 'fontColorG', index: 'font_color_g', width: 80 }, 			
			{ label: 'B', name: 'fontColorB', index: 'font_color_b', width: 80 }, 			
			{ label: '居中', name: 'isCenter', index: 'is_center', width: 80 },
			{ label: '多行', name: 'isMultiLine', index: 'is_multi_line', width: 80 }, 			
			{ label: '描述', name: 'describe', index: 'describe', width: 80 }, 			
			{ label: '删除线', name: 'fontDeletedLine', index: 'font_deleted_line', width: 80 },		
			{ label: '下划线', name: 'fontUnderLine', index: 'font_under_line', width: 80 },
			{ label: '坐标X', name: 'x', index: 'x', width: 80 },
			{ label: '坐标Y', name: 'y', index: 'y', width: 80 },
			{ label: '宽', name: 'width', index: 'width', width: 80 }, 			
			{ label: '高', name: 'height', index: 'height', width: 80 },
			{ label: '图片路径', name: 'imagePath', index: 'image_path', width: 80 }, 			
			{ label: '图片形状', name: 'imageShape', index: 'image_shape', width: 80 }
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
		templateItme: {},
		type: null,
		q:{
			templateId: null,
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reset:function(){
			$(".grid-btn input").val("");
			vm.q.templateId = null;
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.templateItme = {};
			vm.type = "font";
			vm.templateItme.type = "font";
			vm.templateItme.fontName = "微软雅黑";
			vm.templateItme.fontStyle = "0";
			vm.templateItme.fontDeletedLine = "false";
			vm.templateItme.fontUnderLine = "false";
			vm.templateItme.isCenter = "false";
			vm.templateItme.imagePath = "template/模板ID/图片名.png";
				
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(id);
            vm.templateItme.fontStyle = vm.templateItme.fontStyle + "";
            vm.type = vm.templateItme.type;
		},
		saveOrUpdate: function (event) {
			console.log(JSON.stringify(vm.templateItme));
			var url = vm.templateItme.id == null ? "../templateitme/save" : "../templateitme/update";
			$.ajax({
				type: "POST",
			    url: url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.templateItme),
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
				    url: "../templateitme/delete",
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
			 $.ajax({
	                async: false,  
	                url: "../templateitme/info/" + id,  
	                type: "GET",  
	                success: function (r) {	
	                	vm.templateItme = r.templateItme;
	                	console.log(JSON.stringify(vm.templateItme));
	                }
	         });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{"templateId": vm.q.templateId},
                page:page
            }).trigger("reloadGrid");
		},
		change_type: function(){
			vm.type = vm.templateItme.type;
		}
	}
});