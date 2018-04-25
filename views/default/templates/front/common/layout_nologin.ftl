<#macro html page_title page_tab="">
<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <#if site.googleZZ??>
    <meta name="google-site-verification" content="${site.googleZZ}" />
  </#if>
  <#if site.baiduZZ??>
    <meta name="baidu-site-verification" content="${site.baiduZZ}" />
  </#if>
  <title>${page_title!site.name}</title>
  <link rel="shortcut icon" href="/static/favicon.svg">
  <link rel="stylesheet" href="/static/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="/static/font-awesome/css/font-awesome.min.css">
  <link rel="stylesheet" href="/static/css/app.css">

  <#if site.GA?? && site.GA != "">
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=${site.GA}"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());

      gtag('config', '${site.GA}');
    </script>
  </#if>

  <#if site.baiduTJ?? && site.baiduTJ != "">
    <script>
      var _hmt = _hmt || [];
      (function() {
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?${site.baiduTJ}";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
      })();
    </script>
  </#if>

  <script src="/static/js/jquery.min.js"></script>
  <script src="/static/bootstrap/js/bootstrap.min.js"></script>
  <script>
    $(function () {
      var n = $("#goTop");
      n.click(function () {
        return $("html,body").animate({
          scrollTop: 0
        });
      });
    });
  </script>
</head>
<body>
<div class="wrapper">

  <nav class="navbar navbar-default" style="border-radius: 0; margin-bottom: 10px;">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" style="font-weight: 700; font-size: 27px;" href="/">${site.name!}</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse header-navbar">
            <ul class="nav navbar-nav navbar-right">
        <#if sec.isAuthenticated()>
          <li class="hidden-md hidden-lg">
              <a href="/topic/create">发布话题</a>
          </li>
          <li <#if page_tab == 'user'> class="active" </#if>>
              <a href="/user/${sec.getPrincipal()!}">
                ${sec.getPrincipal()!}
                  <span class="badge" id="badge"></span>
              </a>
          </li>
          <li <#if page_tab == 'setting'> class="active" </#if>><a href="/user/profile">设置</a></li>
          <#if sec.allGranted("admin:index")>
            <li <#if page_tab == 'admin'> class="active" </#if>><a href="/admin/index">进入后台</a></li>
          </#if>
          <li><a href="javascript:if(confirm('确定要登出${site.name!}吗？'))location.href='/logout'">退出</a></li>
        <#else>
          <li <#if page_tab == "login">class="active"</#if>><a href="/login">登录</a></li>
          <li <#if page_tab == "register">class="active"</#if>><a href="/register">注册</a></li>
        </#if>
            </ul>
        </div>
    </div>
</nav>
  <div class="container" style="padding: 0 25px;">
    <#--<form class="hidden-lg hidden-md" style="margin: 0 -10px;" role="search" action="/search" method="get">-->
      <#--<div class="form-group has-feedback" style="margin-bottom: 10px;">-->
        <#--<input type="text" class="form-control" name="q" value="${q!}" placeholder="回车搜索">-->
        <#--<span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>-->
      <#--</div>-->
    <#--</form>-->
    <#nested />
  </div>
</div>
  <#include "./footer.ftl">
  <@footer/>
</body>
</html>
</#macro>