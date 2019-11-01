$(function () {
});
function applyData(id){
	layer.open({
		type: 2,
		skin: 'layui-layer-lan',
		title: "报名活动数据列表",
		area: ['1150px', '800px'],
		shadeClose: false,
		content: 'applydata.html?id=' + id
	});
}

function showApplyImg(id) {
	vm.showModal3 = true;
	vm.title = "报名活动二维码";
	vm.srcApplyImg ="../apply/applyImg/" + id;
}
//修改
function edit(id){
	if(id == null){
		return ;
	}
//	vm.showList = false;
	vm.showModal = true;
    vm.title = "查看";
    vm.getInfo(id)
}
function deleteTag() {
	vm.saveApplyTag();
}
function saveTag() {
	vm.saveApplyTag();
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
    			url: "../apply/logic_del/" + id,
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
		tagData:[],
		showModal3:false,
		srcApplyImg:null,
		tagName:null,
		isChecked:null,
		columns7: [
			{
				title: '姓名',
				key: 'name',
			},
			{
				title: '是否默认选中',
				key: 'checked'
			},
			{
				title: '操作',
				key: 'action',
				width: 150,
				align: 'center',
				render: (h, params) => {
					return h('div', [
						h('Button', {
							props: {
								type: 'primary',
								size: 'small'
							},
							style: {
								marginRight: '5px'
							},
							on: {
								click: () => {
									vm.tagData[params.index].checked=!params.row.checked;
									this.saveTag();
								}
							}
						}, '切换状态'),
						h('Button', {
							props: {
								type: 'error',
								size: 'small'
							},
							on: {
								click: () => {
									vm.tagData.splice(params.index,1);
									this.deleteTag();
								}
							}
						}, '删除')
					]);
				}
			}
		],
		apply: {},
		ruleValidate: { },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: [],
			company:null
		}
	},
	created: function(){
		$.ajax({
			type: "GET",
			url: '../apply/applyTag',
			contentType: "application/json",
			success: function(r){
				vm.tagData = JSON.parse(r.msg);

			}
		});
	},

	methods: {
		query: function () {
			vm.reload();
		},
		addApplyTag : function(){
			var tagArr = [];
			var tagObj = {};
			tagObj['name'] = vm.tagName;
			tagObj['checked'] = vm.isChecked;
			var json = JSON.stringify(tagObj);
			var jsonObj = JSON.parse(json);
			vm.tagData.push(jsonObj);
			this.saveApplyTag();
		},
		saveApplyTag : function(){
			var s =JSON.stringify(vm.tagData);
				$.ajax({
					type: "POST",
					url: '../apply/saveApplyTag',
					contentType: "application/json",
					data: s,
					dataType:"json",
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

		},
		reset: function(){
			vm.q.id = null;
			vm.q.sdate = null;
			vm.q.edate = null;
			vm.q.ctime = null;
			vm.q.company = null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModal = true;
			vm.title = "新增";
			vm.apply = {};
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
			this.$refs['apply'].validate((valid) => {
                if (valid) {
                	var url = vm.apply.id == null ? "../apply/save" : "../apply/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.apply),
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
				    url: "../apply/delete",
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
			//$.get("../apply/info/" + id, function(r){
            //    vm.apply = r.apply;
            //});
            
            $.ajax({
				type : "GET",
				async: false,
				url : "../apply/info/" + id,
				success : function(r) {
					vm.apply = r.apply;
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{"id": vm.q.id,"company":vm.q.company, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});