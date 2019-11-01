$(function () {
    $("#jqGrid").jqGrid({
        url: '../grouprecord/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '用户ID', name: 'openId', index: 'open_id', width: 80 }, 			
			{ label: '用户头像', name: 'wxUserEntity.avatarUrl', index: 'avatar_url', width: 80 , formatter: function(value, options, row){
				if(value == null){
					return "<span>无</span>";
				}
				return "<img style='height:50px;' src='"+value+"' alt='' class='img-rounded'>";
			}}, 			
			{ label: '用户昵称', name: 'wxUserEntity.nickName', index: 'nick_name', width: 80 }, 			
			{ label: '性别', name: 'wxUserEntity.gender', index: 'gender', width: 80 , formatter: function(value, options, row){
				var dom = "";
				switch(value){
				case "1":
					dom += "<span class='label label-success'>男</span> "
					break;
				case "2":
					dom += "<span class='label label-warning'>女</span> "
					break;
				default:
					dom += "<span class='label label-danger'>未知</span> "
				}
				return dom;
			}}, 			
			{ label: '国家', name: 'wxUserEntity.country', index: 'country', width: 80 }, 			
			{ label: '省', name: 'wxUserEntity.province', index: 'province', width: 80 }, 			
			{ label: '市', name: 'wxUserEntity.city', index: 'city', width: 80 }, 			
			{ label: '注册时间', name: 'wxUserEntity.ctime', index: 'ctime', width: 80 }, 			
			{ label: '转发方式', name: 'type', index: 'type', width: 80  , formatter: function (value, options, row) {
            	if(row.type == 'group'){
            		return "<span class='label label-success'>群发</span>";
            	}else if(row.type == 'personal'){
            		return "<span class='label label-warning'>转发个人</span>";
            	}else{
            		return "<span class='label label-warning'>未知</span>";
            	}
  			} },  			
			{ label: '转发量', name: 'group_cnt', index: 'group_cnt', width: 80 , formatter: function(value, options, row){
				return '<a href="javascript:void(0)" onclick="shareInfo(\''+row.type+'\',\''+row.openId+'\')">'+value+'</a>';
			}}			
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
function shareInfo(type,openId){
	if(type == null||openId == null){
		return ;
	}
	vm.getUserGroupInfo(type,openId);
    vm.$Modal.info({
    	okText: '关闭',
    	width: 1000,
    	title: '分享记录',
        render: (h) => {
        	return h('Table', {
                props: {
                	columns: vm.shareColumns,
                	data: vm.groupUserRecord,
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
		groupRecord: {},
		groupUserRecord: [],
		shareColumns: [
            {
                title: '用户ID',
                key: 'openId'
            },
             {
                title: '转发类型',
                key: 'type'
            },
            {
                title: '分享时间',
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
			vm.groupRecord = {};
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
			var url = vm.groupRecord.id == null ? "../grouprecord/save" : "../grouprecord/update";
			$.ajax({
				type: "POST",
			    url: url,
			    contentType: "application/json",
			    data: JSON.stringify(vm.groupRecord),
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
				    url: "../grouprecord/delete",
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
			$.get("../grouprecord/info/"+id, function(r){
                vm.groupRecord = r.groupRecord;
            });
		},
		getUserGroupInfo: function(type,openId){
			$.get("../grouprecord/infoByUser/" + type+"/"+openId, function(r){
                vm.groupUserRecord = r.groupUserRecord;
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