$(function () {
    $("#jqGrid").jqGrid({
        url: '../daysrecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '用户ID', name: 'openId', index: 'open_id', width: 80 }, 			
			{ label: '获取使用天数', name: 'days', index: 'days', width: 80 }, 			
			{ label: '规则类别', name: 'type', index: 'type', width: 80 , formatter: function (value, options, row) {
            	if(row.type == 'registe'){
            		return "<span class='label label-success'>新用户注册</span>";
            	}else if(row.type == 'send'){
            		return "<span class='label label-warning'>转发</span>";
            	}else if(row.type == 'old'){
            		return "<span class='label label-danger'>老用户获取天数</span>";
            	}else if(row.type == 'cms'){
            		return "<span class='label label-primary'>后台手动</span>";
            	}else{
            		return "<span class='label label-default'>"+value+"未知</span>";
            	}
  			} }, 	 			
			{ label: '更新日期', name: 'createTime', index: 'create_time', width: 80 }		
        ],
		viewrecords: true,
        height: 385,
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
		daysRecord: {},
		q:{
			openid: null,
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.daysRecord = {};
		},
		reset:function(){
			$(".grid-btn input").val("");
			vm.q.openid = null;
			vm.reload();
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
			var url = vm.daysRecord.id == null ? "../daysrecord/save" : "../daysrecord/update";
			$.ajax({
				type: "POST",
			    url: url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.daysRecord),
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
				    url: "../daysrecord/delete",
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
			$.get("../daysrecord/info/"+id, function(r){
                vm.daysRecord = r.daysRecord;
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