$(function () {
    $("#jqGrid").jqGrid({
        url: '../banner/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden:true },
			{ label: '图片', name: 'imagePath', index: 'image_path', width: 80, sortable: false, formatter: function(value, options, row){
					if(row.imagePath == null){
						return "<span>无</span>";
					}
					return "<img style='height:100px;' src='"+row.imagePath+"' alt='' class='img-rounded'>";
				}
			}, 			
			{ label: '类型', name: 'type', index: 'type', width: 80, formatter: function (value, options, row) {
	            	if(row.type == 'h5'){
	            		return "<span class='label label-success'>h5页</span>";
	            	}else if(row.type == 'img'){ 
	            		return "<span class='label label-warning'>图片</span>";
	            	}else{
	            		return "<span class='label label-primary'>小程序</span>";
	            	}
      			}
			}, 			
			{ label: '序号', name: 'sortNum', index: 'sort_num', width: 80 }, 			
			{ label: '跳转值', name: 'bannerVal', index: 'banner_val', width: 80 },
			{ label: '小程序跳转地址', name: 'appPath', index: 'app_path', width: 80 , formatter: function (value, options, row) {
            	if(row.type == 'app'){
            		return "<span class='label label-success'>"+value+"</span>";
            	}else{
            		return "<span class='label label-warning'>未知</span>";
            	}
  			}
		}, 
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>编辑</span></button>&nbsp;";
                	dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='del("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>删除</span></button>&nbsp;";
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

//编辑
function edit(id){
	if(id == null){
		return ;
	}
//	vm.showList = false;
	vm.showModal = true;
    vm.title = "修改";
	vm.getInfo(id)
	
	if(vm.banner.imagePath != null && vm.banner.imagePath != ''){
		console.log(vm.banner.imagePath);
		vm.showImage = true;
		vm.imageSrc = vm.banner.imagePath;
	}
}

//真实删除
function del(id){
	if(id == null){
		return ;
	}

	vm.$Modal.confirm({
        title: '提示',
        content: '确定要删除选中的记录？',
        onOk:() => {
        	$.ajax({
    			type: "POST",
    			url: "../banner/del/" + id,
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
		showImage: false,
		imageSrc: null,
		title: null,
		banner: {},
		 ruleValidate: {
			imagePath: [
                { required: true, message: '请上传图片'}
			],
			type: [
                { required: true, message: '请选择类型'}
			]
        }
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			$(".ivu-upload-list li").remove();
			// vm.showList = false;
			vm.showModal = true;
			vm.showImage = false;
			vm.title = "新增";
			vm.banner = {};
			vm.banner.sortNum = 1;
			vm.banner.type = 'img';
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			// vm.showList = false;
			vm.showModal = true;
            vm.title = "修改";
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			console.log(JSON.stringify(vm.banner));
			this.$refs['banner'].validate((valid) => {
                if (valid) {
                	var url = vm.banner.id == null ? "../banner/save" : "../banner/update";
					$.ajax({
						type: "POST",
						url: url,
						contentType: "application/json",
						data: JSON.stringify(vm.banner),
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
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../banner/delete",
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
				type : "GET",
				async: false,
				url : "../banner/info/" + id,
				success : function(r) {
					 vm.banner = r.banner;
				}
			});
		},
		handleSuccess (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.banner.imagePath = res.data.url;
				vm.showImage = true;
				vm.imageSrc = res.data.url;
			}
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