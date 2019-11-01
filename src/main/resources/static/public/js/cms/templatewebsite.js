$(function () {
    $("#jqGrid").jqGrid({
        url: '../templatewebsite/list',
        datatype: "json",
        colModel: [
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '创建时间', name: 'ctime', index: 'ctime', width: 80 },
			{ label: '更新时间', name: 'utime', index: 'utime', width: 80 },
			{ label: '是否发布', name: 'isRelease', index: 'is_release', width: 80 },
			{ label: '初始使用量', name: 'initUseCnt', index: 'init_use_cnt', width: 80 },
			{ label: '使用量', name: 'useCnt', index: 'use_cnt', width: 80 },
			{ label: '样图', name: 'examplePath', index: 'example_path', width: 80 },
			{
                label: '操作', name: '', index: 'operate', width: 100, align: 'left', sortable: false,
                formatter: function (value, options, row) {
                	var dom = "<button type='button' class='ivu-btn ivu-btn-primary' onclick='edit("+row.id+")'><i class='ivu-icon ivu-icon-minus'></i><span>修改</span></button>&nbsp;";
					if(row.isRelease=="t"){
						dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='revocation_template("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>撤回</span></button>&nbsp;";
					}else{
						dom += "<button type='button' class='ivu-btn ivu-btn-error' onclick='release_template("+row.id+")'><i class='ivu-icon ivu-icon-close'></i><span>发布</span></button>&nbsp;";

					}
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
    });
});
function release_template (id){
	$.ajax({
		type : "GET",
		async: false,
		url : "../templatewebsite/releasetemplate/" + id,
		success : function(r) {
			vm.reload();
		}
	});
}

function revocation_template (id){
	$.ajax({
		type : "GET",
		async: false,
		url : "../templatewebsite/revocationtemplate/" + id,
		success : function(r) {
			vm.reload();
		}
	});
}
//修改
function edit(id){
	if(id == null){
		return ;
	}
//	vm.showList = false;
	vm.showModalEdit = true;
	vm.showIndexImage=true;
	vm.showPhoneImage=true;
	vm.showShareImage=true;
	vm.showBox1Bg=true;
	vm.showBox1MpBg=true;
	vm.showBox2Top=true;
	vm.showBox2Bg=true;
	vm.showBox3Top= true;
	vm.showBox3Bg=true;
	vm.showBox4Bg=true;
	vm.showBox5Top= true;
	vm.showBox5Bg= true;
	vm.showBox6Bg= true;
	vm.showBox7Bg=true;
	vm.showBox7Box=true;
	vm.showBox7Btn =true;
	vm.showBox8Bg=true;
	vm.showExamplePath=true;
    vm.title = "修改";
    vm.getInfo(id)
}

//逻辑删除
function logic_del(id){
	if(id == null){
		return ;
	}
	confirm('确定要删除选中的记录？', function(){
		$.ajax({
			type: "GET",
			url: "../templatewebsite/logic_del/"+id,
			contentType: "application/json",
			success: function(r){
				if(r.code == 0){
					alert('操作成功', function(index){
						vm.reload();
					});
				}else{
					alert(r.msg);
				}
			}
		});
	});
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		showModalAdd: false,
		uploadData: {},
		catalog:null,
		showModalEdit: false,
		showIndexImage: false,
		showPhoneImage: false,
		showShareImage: false,
		showBox1Bg: false,
		showBox1MpBg: false,
		showBox2Top: false,
		showBox2Bg: false,
		showBox3Top: false,
		showBox3Bg: false,
		showBox4Bg: false,
		showBox5Top: false,
		showBox5Bg: false,
		showBox6Bg: false,
		showBox7Bg: false,
		showBox7Box:false,
		showBox7Btn:false,
		showBox8Bg: false,
		showExamplePath:false,
		indexImgSrc: null,
		phoneImgSrc: null,
		shareImgSrc: null,
		box1BgImgSrc: null,
		box1MpImgSrc: null,
		box2TopImgSrc:null,
		box2BgImgSrc: null,
		box3TopImgSrc: null,
		box3BgImgSrc: null,
		box4BgImgSrc:null,
		box5TopImgSrc: null,
		box5BgImgSrc: null,
		box6BgImgSrc: null,
		box7BgImgSrc: null,
		box7BoxImgSrc: null,
		box7BtnImgSrc:null,
		box8BgImgSrc: null,
		examplePathImgSrc: null,
		title: null,
		templateWebsite: {},
		ruleValidate: {
													        },
        q:{
			id: null,
			sdate: null,
			edate: null,
			ctime: []
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},

		reset: function(){
			vm.q.id = null;
			vm.q.sdate = null;
			vm.q.edate = null;
			vm.q.ctime = null;
			vm.showIndexImage=null;
			vm.showPhoneImage=null;
			vm.showShareImage=null;
			vm.showBox1Bg=null;
			vm.showBox1MpBg=null;
			vm.showBox2Top=null;
			vm.showBox2Bg=null;
			vm.showBox3Top=null;
			vm.showBox3Bg=null;
			vm.showBox4Bg=null;
			vm.showBox5Top=null;
			vm.showBox5Bg=null;
			vm.showBox6Bg=null;
			vm.showBox7Bg=null;
			vm.showBox7Box=null;
			vm.showBox7Btn=null;
			vm.showBox8Bg=null;
			vm.showExamplePath=null;
			vm.indexImgSrc=null;
			vm.phoneImgSrc=null;
			vm.shareImgSrc=null;
			vm.box1BgImgSrc=null;
			vm.box1MpImgSrc=null;
			vm.box2TopImgSrc=null;
			vm.box2BgImgSrc=null;
			vm.box3TopImgSrc=null;
			vm.box3BgImgSrc=null;
			vm.box4BgImgSrc=null;
			vm.box5TopImgSrc=null;
			vm.box5BgImgSrc=null;
			vm.box6BgImgSrc=null;
			vm.box7BgImgSrc=null;
			vm.box7BoxImgSrc=null;
			vm.box7BtnImgSrc=null;
			vm.box8BgImgSrc=null;
			vm.examplePathImgSrc=null;
			vm.title=null;
			vm.templateWebsite=null;
		},
		add: function(){
			//vm.showList = false;
			vm.showModalAdd = true;
			vm.title = "新增";
			vm.templateWebsite = {};
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
			this.$refs['templateWebsite'].validate((valid) => {
                if (valid) {
                	var url = vm.templateWebsite.id == null ? "../templatewebsite/save" : "../templatewebsite/update";
        			$.ajax({
        				type: "POST",
        			    url: url,
        			    contentType: "application/json",
        			    data: JSON.stringify(vm.templateWebsite),
        			    success: function(r){
        			    	if(r.code === 0){
        			    		vm.reload();
        			    		if(vm.showModalAdd == true){
									vm.showModalAdd = false;
								}else{
									vm.showModalEdit = false;
								}
								this.$Message.success('操作成功');
        					}else{
        						this.$Message.error(r.msg);
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

			this.$Modal.confirm({
	        title: '提示',
	        content: '确定要删除选中的记录？',
	        onOk:() => {
	        	$.ajax({
					type: "POST",
				    url: "../templatewebsite/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code === 0){
							$("#jqGrid").trigger("reloadGrid");
    			    		this.$Message.success('操作成功!');
    					}else{
    						this.$Message.error(r.msg);
    					}
					}
				});
	        }
	    });
		},
		handleSuccess1 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showIndexImage = true;
				vm.indexImgSrc = res.data.url;
				vm.templateWebsite.indexImgSrc = vm.indexImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess2 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showPhoneImage = true;
				vm.phoneImgSrc = res.data.url;
				vm.templateWebsite.phoneImgSrc = vm.phoneImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess3 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showShareImage = true;
				vm.shareImgSrc = res.data.url;
				vm.templateWebsite.shareImgSrc = vm.shareImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess4 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox1Bg = true;
				vm.box1BgImgSrc = res.data.url;
				vm.templateWebsite.box1BgImgSrc = vm.box1BgImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess5 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox1MpBg = true;
				vm.box1MpImgSrc = res.data.url;
				vm.templateWebsite.box1MpImgSrc = vm.box1MpImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess6 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox2Top = true;
				vm.box2TopImgSrc = res.data.url;
				vm.templateWebsite.box2TopImgSrc = vm.box2TopImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess7 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox2Bg = true;
				vm.box2BgImgSrc = res.data.url;
				vm.templateWebsite.box2BgImgSrc = vm.box2BgImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},

		handleSuccess8 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox3Top = true;
				vm.box3TopImgSrc = res.data.url;
				vm.templateWebsite.box3TopImgSrc = vm.box3TopImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},

		handleSuccess9 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox3Bg = true;
				vm.box3BgImgSrc = res.data.url;
				vm.templateWebsite.box3BgImgSrc = vm.box3BgImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},

		handleSuccess10 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox4Bg = true;
				vm.box4BgImgSrc = res.data.url;
				vm.templateWebsite.box4BgImgSrc = vm.box4BgImgSrc;
			}else{
				this.$Message.success('大小超过3M!');
			}
		},

		handleSuccess11 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox5Top = true;
				vm.box5TopImgSrc = res.data.url;
				vm.templateWebsite.box5TopImgSrc = vm.box5TopImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},

		handleSuccess12 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox5Bg = true;
				vm.box5BgImgSrc = res.data.url;
				vm.templateWebsite.box5BgImgSrc = vm.box5BgImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},

		handleSuccess13 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox6Bg = true;
				vm.box6BgImgSrc = res.data.url;
				vm.templateWebsite.box6BgImgSrc = vm.box6BgImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess14 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox7Bg = true;
				vm.box7BgImgSrc = res.data.url;
				vm.templateWebsite.box7BgImgSrc = vm.box7BgImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess15 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox7Box = true;
				vm.box7BoxImgSrc = res.data.url;
				vm.templateWebsite.box7BoxImgSrc = vm.box7BoxImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess16 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox7Btn = true;
				vm.box7BtnImgSrc = res.data.url;
				vm.templateWebsite.box7BtnImgSrc = vm.box7BtnImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess17 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showBox8Bg = true;
				vm.box8BgImgSrc = res.data.url;
				vm.templateWebsite.box8BgImgSrc = vm.box8BgImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleSuccess18 (res, file) {
			console.log(res)
			if(res.code != 500){
				vm.showExamplePath = true;
				vm.examplePathImgSrc = res.data.url;
				vm.templateWebsite.examplePath = vm.examplePathImgSrc;

			}else{
				this.$Message.success('大小超过3M!');
			}
		},
		handleBeforeUpload(){
			this.uploadData.data =this.catalog;
		},
		getInfo: function(id){
			//$.get("../templatewebsite/info/" + id, function(r){
            //    vm.templateWebsite = r.templateWebsite;
            //});

            $.ajax({
				type : "GET",
				async: false,
				url : "../templatewebsite/info/" + id,
				success : function(r) {
					vm.templateWebsite = r.templateWebsite;
					vm.indexImgSrc=vm.templateWebsite.indexImgSrc;
					vm.phoneImgSrc=vm.templateWebsite.phoneImgSrc;
					vm.shareImgSrc = vm.templateWebsite.shareImgSrc;
					vm.box1BgImgSrc =vm.templateWebsite.box1BgImgSrc;
					vm.box1MpImgSrc = vm.templateWebsite.box1MpImgSrc;

					vm.box2TopImgSrc=vm.templateWebsite.box2TopImgSrc;

					vm.box2BgImgSrc=vm.templateWebsite.box2BgImgSrc;

					vm.box3TopImgSrc=vm.templateWebsite.box3TopImgSrc;

					vm.box3BgImgSrc = vm.templateWebsite.box3BgImgSrc;

					vm.box4BgImgSrc = vm.templateWebsite.box4BgImgSrc;

					vm.box5TopImgSrc = vm.templateWebsite.box5TopImgSrc;

					vm.box5BgImgSrc = vm.templateWebsite.box5BgImgSrc;

					vm.box6BgImgSrc = vm.templateWebsite.box6BgImgSrc;

					vm.box7BgImgSrc = vm.templateWebsite.box7BgImgSrc;

					vm.box7BoxImgSrc = vm.templateWebsite.box7BoxImgSrc;

					vm.box7BtnImgSrc = vm.templateWebsite.box7BtnImgSrc;

					vm.box8BgImgSrc = vm.templateWebsite.box8BgImgSrc;

					vm.examplePathImgSrc = vm.templateWebsite.examplePath;
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