<#include "../common/layout.ftl"/>
<@html page_title="添加用户" page_tab="admin">
<div class="row">
    <div class="col-md-2 hidden-sm hidden-xs">
    <#include "../components/admin_left.ftl">
    <@admin_left page_tab="user"/>
    </div>
    <div class="col-md-10">
        <div class="panel panel-default">
            <div class="panel-heading">添加用户</div>
            <div class="panel-body">
                <form action="/admin/user/add" method="post" id="userForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <div class="form-group">
                        <label for="nickname">账户</label>
                        <input type="text" id="username"  name="username" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <label for="realName">昵称</label>
                        <input type="text" id="realName" name="realName"  class="form-control"/>
                    </div>
                    <div class="form-group">
                        <label for="phone">电话</label>
                        <input type="text" id="phone" name="phone"  class="form-control"/>
                    </div>
                    <div class="form-group">
                        <label for="qq">QQ</label>
                        <input type="text" id="qq" name="qq"  class="form-control"/>
                    </div>
                <#--<div class="form-group">-->
                <#--<label for="score">积分</label>-->
                <#--<input type="text" id="score" name="score" value="${user.score?c}" class="form-control"/>-->
                <#--</div>-->
                <div class="form-group">
                        <label for="roles">角色</label>
                        <div>
                          <#list roles as role>
                            <input type="radio" name="roleId"  value="${role.id}" id="role_${role.id}"
                                   <#if site.newUserRole == role.name>checked="true"</#if>>
                            <label for="role_${role.id}">${role.description}</label>&nbsp;
                          </#list>

                        </div>
                    </div>
                    <div class="form-group">
                        <label for="nodes">业务系统</label>
                         <div>
                          <#list nodes as node>
                            <input type="checkbox" name="nodeIds" value="${node.id}" id="node_${node.id}">
                            <label for="node_${node.id}">${node.name}</label>&nbsp;
                          </#list>
                        </div>
                    </div>
                    <button type="button" id="userBtn" onclick="userSubmit()" class="btn btn-sm btn-default">保存</button>
                    <span id="error_message"></span>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    function userSubmit() {
        var errors = 0;
        var em = $("#error_message");
        var username = $("#username").val();
        var bio=$("#bio").val();

        if (username.length === 0) {
            errors++;
            em.html("用户名不能为空");
        }

        var roleid = $("input[name='roleId']:checked").val();
        if(roleid==null||roleid==undefined||roleid==""){
            errors++;
            em.html("请选择角色");
        }
        if (errors === 0) {
            var form = $("#userForm");
            form.submit();
        }
    }
</script>
</@html>