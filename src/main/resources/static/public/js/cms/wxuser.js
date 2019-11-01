$(function () {
    $("#jqGrid").jqGrid({
        url: '../wxuser/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden:true },
			{ label: '用户ID', name: 'openId', index: 'open_id', width: 100 }, 			
			{ label: '头像', name: 'avatarUrl', index: 'avatar_url', width: 50, formatter: function(value, options, row){
				if(value == null){
					return "<span>无</span>";
				}
				return "<img style='height:50px;' src='"+value+"' alt='' class='img-rounded'>";
			}}, 
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 80 },
			{ label: '昵称', name: 'nickName', index: 'nick_name', width: 80 }, 			
			{ label: '性别', name: 'gender', index: 'gender', width: 30, formatter: function(value, options, row){
				var dom = "";
				switch(value){
				case "1":
					dom += "<span class='label label-warning'>男</span> "
					break;
				case "2":
					dom += "<span class='label label-warning'>女</span> "
					break;
				default:
					dom += "<span class='label label-danger'>未知</span> "
				}
				return dom;
			}}, 			
			{ label: '国家', name: 'country', index: 'country', width: 50 }, 			
			{ label: '省份', name: 'province', index: 'province', width: 50 }, 			
			{ label: '城市', name: 'city', index: 'city', width: 50 }	,
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },	
			{ label: '最后登入时间', name: 'loginTime', index: 'login_time', width: 80 }	
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
		wxUser: {},
		q:{
			mobile: null,
			nickName: null,
			sdate: null,
			edate: null
		}
	},
	methods: {
		query: function () {
			vm.q.sdate = $("#sdate").val();
			vm.q.edate = $("#edate").val();
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.wxUser = {};
		},
		reset:function(){
			$(".grid-btn input").val("");
			vm.q.mobile = null;
			vm.q.nickName = null;
			vm.q.sdate = null;
			vm.q.edate = null;
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
			var url = vm.wxUser.id == null ? "../wxuser/save" : "../wxuser/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.wxUser),
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
				    url: "../wxuser/delete",
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
			$.get("../wxuser/info/"+id, function(r){
                vm.wxUser = r.wxUser;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{"nickName": vm.q.nickName, "mobile": vm.q.mobile, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		}
	}
});