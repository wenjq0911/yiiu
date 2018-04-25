<#include "../common/layout.ftl"/>
<@html page_title="编辑用户" page_tab="admin">
<div class="row">
  <div class="col-md-2 hidden-sm hidden-xs">
    <#include "../components/admin_left.ftl">
    <@admin_left page_tab="user"/>
  </div>
  <div class="col-md-10">
    <div class="panel panel-default">
      <div class="panel-heading">编辑用户</div>
      <div class="panel-body">
        <form action="/admin/user/${user.id}/edit" method="post">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <div class="form-group">
            <label for="nickname">账户</label>
            <input type="text" disabled id="username" value="${user.username}" class="form-control"/>
          </div>
            <div class="form-group">
                <label for="bio">昵称</label>
                <input type="text" id="realName" name="realName" value="${user.realName}" class="form-control"/>
            </div>
            <div class="form-group">
                <label for="phone">电话</label>
                <input type="text" id="phone" name="phone"  class="form-control"  value="${user.phone!}"/>
            </div>
            <div class="form-group">
                <label for="qq">QQ</label>
                <input type="text" id="qq" name="qq"  class="form-control"  value="${user.qq!}"/>
            </div>
          <#--<div class="form-group">-->
            <#--<label for="score">积分</label>-->
            <#--<input type="text" id="score" name="score" value="${user.score?c}" class="form-control"/>-->
          <#--</div>-->
          <div class="form-group">
            <label for="roles">角色</label>
            <div>
              <#list roles as role>
                <input type="radio" name="roleId" value="${role.id}" id="role_${role.id}" <#if user.username=="admin">disabled</#if>>
                <label for="role_${role.id}">${role.description}</label>&nbsp;
              </#list>
              <script type="text/javascript">
                  <#list user.roles as role>
                  $("#role_${role.id}").attr("checked", true);
                  </#list>
              </script>
            </div>
          </div>
          <div class="form-group">
                <label for="nodes">业务系统</label>
                <div>
              <#list nodes as node>
                <input type="checkbox" name="nodeIds" value="${node.id}" id="node_${node.id}" <#if user.username=="admin">disabled</#if>>
                <label for="node_${node.id}">${node.name}</label>&nbsp;
              </#list>
                    <script type="text/javascript">
                            <#list user.nodes as node>
                  $("#node_${node.id}").attr("checked", true);
                            </#list>
                    </script>
                </div>
            </div>
          <button type="submit" class="btn btn-sm btn-default">保存</button>
        </form>
      </div>
    </div>
  </div>
</div>
</@html>