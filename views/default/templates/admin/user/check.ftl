<#include "../common/layout.ftl"/>
<@html page_title="注册审核" page_tab="admin">
<div class="row">
  <div class="col-md-2 hidden-sm hidden-xs">
    <#include "../components/admin_left.ftl">
    <@admin_left page_tab="check"/>
  </div>
  <div class="col-md-10">
      <div class="panel panel-default">
          <div class="panel-heading">
              注册审核
          </div>
          <div class="table-responsive">
              <table class="table table-striped">
                  <thead>
                  <tr>
                      <th>ID</th>
                      <th>账户</th>
                      <th>昵称</th>
                      <th>时间</th>
                      <th>注册信息</th>
                      <th>操作</th>
                  </tr>
                  </thead>
                  <tbody>
            <#list page.getContent() as user>
            <tr>
                <td>${user.id}</td>
                <td>
                    ${user.username}
                </td>
                <td>${user.realName}</td>
                <td>${model.formatDate(user.inTime)}</td>
                <td>${user.checkMsg}</td>
                <td>
                    <#if sec.allGranted("check:checkok")>
                      <a href="#" onclick="checkok(${user.id})" class="btn btn-xs btn-warning">通过</a>
                    </#if>
                     <#if sec.allGranted("check:checkno")>
                      <a href="#" onclick="checkno(${user.id})" class="btn btn-xs btn-danger">拒绝</a>
                     </#if>
                </td>
            </tr>
            </#list>
                  </tbody>
              </table>
          </div>
          <div class="panel-body" style="padding: 0 15px;">
        <#include "../components/paginate.ftl"/>
        <@paginate currentPage=(page.getNumber() + 1) totalPage=page.getTotalPages() actionUrl="/admin/check/list" urlParas="" showdivide="no"/>
          </div>
      </div>
  </div>
</div>
<script>
    function checkok(id){
        $.ajax({
            url: "/admin/check/"+id+"/checkok",
            async: false,
            cache: false,
            type: "post",
            dataType: "json",
            data: {
                '${_csrf.parameterName}': '${_csrf.token}',
            },
            success:function(data){
                if(data.code==200){
                    window.location.href="/admin/check/list";
                }else{
                    alert(data.description);
                }
            }
        });
    }
    function checkno(id){
        $.ajax({
            url: "/admin/check/"+id+"/checkno",
            async: false,
            cache: false,
            type: "post",
            dataType: "json",
            data: {
                '${_csrf.parameterName}': '${_csrf.token}',
            },
            success:function(data){
                if(data.code==200){
                    window.location.href="/admin/check/list";
                }else{
                    alert(data.description);
                }
            }
        });
    }
</script>
</@html>