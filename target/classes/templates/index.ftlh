<#import "parts/common.ftlh" as c>
<@c.page>
<div>
    ${message}
</div>
<script>
    xhr.open("GET",'http://' + window.location.host + '/get_csrf_api', false);
    xhr.onload = function(){};
    xhr.send();
</script>
<a href="/index">Main page</a>
<a href="/login">Login</a>
<a href="/registration">Register</a>
</@c.page>