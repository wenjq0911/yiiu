<#include "../common/layout.ftl"/>
<@html page_title="导入用户" page_tab="admin">
<div class="row">
    <div class="col-md-2 hidden-sm hidden-xs">
    <#include "../components/admin_left.ftl">
    <@admin_left page_tab="user"/>
    </div>
    <div class="col-md-10">
        <div class="panel panel-default">
            <div class="panel-heading">导入用户</div>
            <div class="panel-body">
                <ul id="myTab" class="nav nav-tabs">
                    <li class="active">
                        <a href="#import_api" data-toggle="tab">
                            接口导入
                        </a>
                    </li>
                    <li><a href="#import_excel" data-toggle="tab">Excel导入</a></li>
                </ul>
                <div id="myTabContent" class="tab-content">
                    <div class="tab-pane fade in active" id="import_api">
                        <form id="form_import">
                            <div class="form-group">
                                <label for="api_address">业务系统</label>
                                <select class="form-control" id="select_node" >
                                    <#list nodes as m>
                                        <option value="${m.id}">${m.name}</option>
                                    </#list>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="api_address">接口地址</label>
                                <input type="text" class="form-control" id="api_address" placeholder="请输入业务系统的获取用户接口地址" value="http://IP:Port/Api/User/AllUserList">
                            </div>
                            <div class="form-group">
                                <label for="api_address">票据</label>
                                <input type="text" class="form-control" id="api_ticket" placeholder="请输入业务系统的接入所需的Ticket" value="ABCDEFGHIJKLMNOPQRSTUVWXYZ">
                            </div>
                            <div class="form-group">
                                <label for="name_field">账户字段名称</label>
                                <input type="text" class="form-control" id="account_field" placeholder="请输入接口返回数据中账户的字段名称" value="UserName">
                            </div>
                            <div class="form-group">
                                <label for="name_field">姓名字段名称</label>
                                <input type="text" class="form-control" id="name_field" placeholder="请输入接口返回数据中真实姓名的字段名称" value="RealName">
                            </div>
                            <div class="for-group">
                                <button type="button" class="btn btn-default btnApiSubmit">保存</button>
                                <button type="button" class="btn btn-link"
                                        data-toggle="popover"
                                        data-trigger="focus"
                                        title="接口数据格式"
                                        data-content="[{UserName:'zhangsan',RealName:'张三',...},{...}]">
                                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                </button>
                                <span id="error_message"></span>
                            </div>
                        </form>
                        <form id="form_exist" style="display: none">
                            <div>
                                <h3>以下账户在系统中已存在</h3>
                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>用户名</th>
                                    <th>原真实名</th>
                                    <th>现真实名</th>
                                    <th>错误信息</th>
                                    <#--<th>操作</th>-->
                                </tr>
                                </thead>
                                <tbody id="exist_table">

                                </tbody>
                            </table>
                        </form>
                    </div>
                    <div class="tab-pane fade" id="import_excel">
                        <h2>还在努力开发中，敬请期待.</h2>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $('[data-toggle="popover"]').popover();
    $(".btnApiSubmit").click(function(){
        $.get('/admin/user/importSave',{
            apiUrl:$('#api_address').val(),
            apiTicket:$("#api_ticket").val(),
            nodeid:$("#select_node").val(),
            account:$("#account_field").val(),
            name:$("#name_field").val(),
            roleid:$("#select_role").val()
        },function(data){
            //如果有重复的数据，则加载列表
            data=JSON.parse(data);
            if(data.existUserModels.length!=0){
                $("#form_import").hide();
                formartTable(data.existUserModels);
                $("#form_exist").show();
            }else{
                window.location.href="/admin/user/list";
            }
        });
    });

    function formartTable(data){
        var html ="";
        for(var i=0;i<data.length;i++){
            html+="<tr>";
            html+='<td>'+data[i].username+'</td>' +
                    '<td>'+data[i].existRealName+'</td>' +
                    '<td>'+data[i].newRealName+'</td>' +
                    '<td>'+data[i].errorMsg+'</td>'
                    //+ '<td>'+(data[i].errorStatus==2?'<a href="/admin/user/\'+data[i].id+\'" class="btn btn-xs btn-warning">添加权限</a>':'')+'</td>';
            html+="</tr>";
        }
        $("#exist_table").html(html);
    }
</script>
</@html>