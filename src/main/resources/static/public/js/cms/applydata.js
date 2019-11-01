$(function () {

	/*    $("#jqGrid").jqGrid({
            url: '../apply/applyData?id='+id,
            datatype: "json",
            colModel: [
                { label: 'id', name: 'id', index: 'id', width: 50, key: true },
                { label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },
                { label: '更新时间', name: 'utime', index: 'utime', width: 80 },
                { label: '超级管理员名称', name: 'superiorName', width: 80 },
                { label: '创建人', name: 'memberName',width: 80 },
                { label: '申请人数', name: 'applyCount', index: 'apply_count', width: 80 },
                { label: '报名模板ID', name: 'templateApplyId', index: 'template_apply_id', width: 80 },
                { label: '名称标题', name: 'name', index: 'name', width: 80 },
                { label: '描述', name: 'describe', index: 'describe', width: 80 },
                { label: '开始时间', name: 'startTime', index: 'start_time', width: 80 },
                { label: '结束时间', name: 'endTime', index: 'end_time', width: 80 },
                { label: '图片集', name: 'images', index: 'images', width: 80,
                    formatter: function (value, options, row) {
                        if(value != null){
                            return "<a class='btn btn-info' onclick='applyData(\""+row.id+"\")'>"+报名数据+"</a>";
                        }else{
                            return "<span></span>";
                        }
                    }
                },
                { label: '表单项', name: 'items', index: 'items', width: 80 },
                { label: '权限 public private', name: 'permission', index: 'permission', width: 80,
                    formatter: function (value, options, row) {
                        if(value == 'private'){
                            return "个人";
                        }
                        if(value == 'public'){
                            return "公司";
                        }
                    }
                },
                { label: '地址', name: 'address', index: 'address', width: 80 },
                {
                    label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                    formatter: function (value, options, row) {
                        var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>修改</span></button>&nbsp;";
                        dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='logic_del("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>删除</span></button>&nbsp;";
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
        });*/
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
//修改
function edit(id){
	if(id == null){
		return ;
	}
//	vm.showList = false;
	vm.showModal = true;
    vm.title = "修改";
    vm.getInfo(id)
}
function getUrlParam(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r!=null) return unescape(r[2]); return null;
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
		pagination:{},
		currPage: null,
		totalCount: null,
		applyData:[],
		apply: {},
		columns7:[],
		data6:[],
		ruleValidate: {
											
																ctime: [
		                { required: true, message: '请输入创建时间' }
		            ], 																utime: [
		                { required: true, message: '请输入更新时间' }
		            ], 																openid: [
		                { required: true, message: '请输入创建人openid' }
		            ], 																superiorId: [
		                { required: true, message: '请输入创建了上级memberid' }
		            ], 																memberId: [
		                { required: true, message: '请输入创建了memberid' }
		            ], 																applyCount: [
		                { required: true, message: '请输入申请人数' }
		            ], 																templateApplyId: [
		                { required: true, message: '请输入报名模板ID' }
		            ], 																name: [
		                { required: true, message: '请输入名称标题' }
		            ], 																describe: [
		                { required: true, message: '请输入描述' }
		            ], 																startTime: [
		                { required: true, message: '请输入开始时间' }
		            ], 																endTime: [
		                { required: true, message: '请输入结束时间' }
		            ], 																images: [
		                { required: true, message: '请输入图片集' }
		            ], 																items: [
		                { required: true, message: '请输入表单项' }
		            ], 																permission: [
		                { required: true, message: '请输入权限 public private' }
		            ], 																isDel: [
		                { required: true, message: '请输入删除标志' }
		            ], 																address: [
		                { required: true, message: '请输入地址' }
		            ], 																addressLongitude: [
		                { required: true, message: '请输入中心经度' }
		            ], 																addressLatitude: [
		                { required: true, message: '请输入中心纬度' }
		            ]							        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: []
		}
	},
	created: function(){
		var id = getUrlParam("id");
		$.ajax({
			type: "GET",
			url: '../apply/applyData?id='+id,
			contentType: "application/json",
			success: function(r){
				var obj = r.page.list;
				vm.pagination = r.page;
				for(j =0;j<obj.length;j++){
					var objData = JSON.parse(obj[j].itemDetail);
					for (var key in objData)
					{
						//如果在结果数组result中没有找到arr[i],则把arr[i]压入结果数组result中
						if (vm.data6.indexOf(key) == -1) {
							vm.data6.push(key);
						}
					}
					vm.applyData.push(objData);
				}
				for(var key in vm.data6){
					vm.columns7.push({
						title:vm.data6[key],
						key:vm.data6[key]
					});
				}

			}
		});
	},
	methods: {
		query: function () {
			vm.reload();
		},
		showlist: function(val){
			console.log(val);
		},
		reset: function(){
			vm.q.id = null;
			vm.q.sdate = null;
			vm.q.edate = null;
			vm.q.ctime = null;
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
				postData:{"id": vm.q.id, "sdate":vm.q.sdate, "edate":vm.q.edate},
                page:page
            }).trigger("reloadGrid");
		},
		formateDate (val){
        	vm.q.sdate = val[0];
        	vm.q.edate = val[1];
        }
	}
});