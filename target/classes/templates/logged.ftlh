<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
<script>
        function setfile(filename){
            var xhr = new XMLHttpRequest();
            xhr.open("POST",'http://' + window.location.host + '/videos/setvideo/' + filename, false);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.send('_csrf=' + encodeURIComponent(csrfToken));
            document.location.reload();
        }
        function printname(filename){
            var t = filename.indexOF('_') + 1;
            console.log(filename.substring(t));
            return filename.substring(t);
        }
</script>

<!-- Top Container -->

<section class="top" id="top">
  <!-- header -->
  <header><img class="logo" src="/css/poli.png" width="40" height="40" align="left" alt=""/>
    <h2 class="head_text">WEBProject</h2>

            <h3 class="head_menu"><a href="#win1">Join room</a></h3>
            <h3 class="head_menu"><a href="#win2">Create room</a></h3>
            <h3 class="head_menu"><a href="#win3">Upload</a></h3>
            <h3 class="head_menu"><a href="#win4"">Sign Out</a></h3>

  </header>


<a href="#x" class="overlay" id="win1"></a>
<div class="popup">
	<h2 class="text" style="color: #1A2618">Join room:</h2>
	<p></p>
	<form action="join_invite" method="post">
        <div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="text" id="join_invite" name="join_invite" placeholder="User Name"/>
            <button class="butt_menu" name="join_invite"> Join existing</button>
        </div>
    </form>
<a class="close"title="Close" href="#close"></a>
</div>

<a href="#x" class="overlay" id="win2"></a>
<div class="popup">
	<h2 class="text" style="color: #1A2618">are you sure you want to leave the current room and create a new one?</h2>
	<form modelAttribute="create_room" action="croom">
        <div>
            <button class="butt_menu" name="create_room">Create new room</button>
        </div>
    </form>
<a class="close"title="Close" href="#close"></a>
</div>

<a href="#x" class="overlay" id="win3"></a>
<div class="popup">
	<h2 class="text" style="color: #1A2618">Select file:</h2>
    <form action="doupload?${_csrf.parameterName}=${_csrf.token}" method="post" enctype="multipart/form-data">
        <div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="file" class="put_file" name="file">
            <button class="butt_menu" type="submit">Upload</button>
        </div>
    </form>
<a class="close"title="Close" href="#close"></a>
</div>

<a href="#x" class="overlay" id="win4"></a>
<div class="popup">
	<h2 class="text" style="color: #1A2618">Are you sure you want to log out?</h2>
	<@l.logout />
<a class="close"title="Close" href="#close"></a>
</div>

	</section>


<section class="mid" id="top">
<div>
	<p><h4 class="head_menu" >${userName}</h4>
	<p><h4 class="head_menu" >${userRoom}</h4>
    <meta name="csrf-token" content=${_csrf.token}>
	<h4 class="head_menu"><a  href="#win5">Account Settings</a></h4>
</div>

<a href="#x" class="overlay" id="win5"></a>
<div class="popup">
	<meta name="csrf-token" content=${_csrf.token}>
        <form action="chg_psw" method="post">
            <div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <h2 class="text" style="color: #1A2618">Fill following fields:</h2>
                <p><h4 class="input" style="color: #1A2618">Old password:</h3>
                <input type="password" class="put_text" id="old_password" name="old_password" placeholder="Old password" maxlength="25" size="40%" ></p>

                <p><h4 class="input" style="color: #1A2618">New password:</h3>
                <input type="password" class="put_text" id="new_password" name="new_password" placeholder="New password" maxlength="25" size="40%" >
                <h4 class="input" style="color: #1A2618">Repeat new password:</h3>
                <input type="password"  class="put_text"id="new_password_check" name="new_password_check" placeholder="Repeat new password" maxlength="25" size="40%" >

                <button class="butt_menu" name="chg_psw"> Change password</button>
        </form>

        <form action="erase_me" method="post">
            <div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button class="butt_menu" name="erase_me">Delete account</button>
            </div>
        </form>
</div>
<a class="close"title="Close" href="#close"></a>
</div>


<div  class="v_info_block" id="info" >
<p class="insert" style="color: #E8E8E8" >Select a video from the playlist: </p>
<select onchange="window['setfile'](this.value);">
        <option disabled selected value> -- select file -- </option>
        <#list files as file>
                <option type="btn" value = '${file.mask}'> ${file.mask} </option>
        </#list>
</select>
<br>
</div>

</section>

<#include "parts/video.html">

</@c.page>