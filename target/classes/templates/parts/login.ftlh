<#macro login path>
<form action="${path}" method="post">

<#if path == "/login">

<!-- Top Container -->
<section class="top" id="top">
  <!-- header -->
  <header><img class="logo" src="/css/poli.png" width="40" height="40" align="left" alt=""/>
    <h2 class="head_text">WEBProject</h2>
	<h2 class="head_text_r"><a href="/registration">Registration</a></h2>
  </header>
	</section>

<!-- Mid Container -->
<section class="mid" id="mid">
 	<div  class="info_block" id="info" >
		 <p class="insert" style="color: #E8E8E8" >A project that help you to organize a joint viewing of any video   </p>
		</div>
  <div  class="bblock" >
	  <h3 class="text" style="color: #1A2618">Enter username and password</h3>
	  <p><h4 class="input" style="color: #1A2618">Username:</h3>
    <input class="put_text" maxlength="25" size="31%" name="username"></p>
			  <p>
			  <h4 class="input" style="color: #1A2618">Password:</h3>
    <input class="put_text" type="password" maxlength="25" size="31" name="password" class="form-control" id="password"></p>
   <input type="hidden" name="_csrf" value="${_csrf.token}" />
     <input type="submit" class="butt" id="butt" value="Login">
</div>
</section>
</#if>


<#if path == "/registration">
<!-- Top Container -->
<section class="top" id="top">
  <!-- header -->
  <header><img class="logo" src="/css/poli.png" width="40" height="40" align="left" alt=""/>
    <h2 class="head_text">WEBProject</h2>
  </header>
	</section>

<!-- Mid Container -->
<section class="mid" id="mid">
 	<div  class="info_block" id="info" >
		 <p class="insert" style="color: #E8E8E8" >By registering on the site you agree to the community rules, as well as the license agreement   </p>
		 <p class="insert" style="color: #E8E8E8" >The login field can contain numbers and letters of the Latin alphabet. The minimum password length is 8 characters   </p>
		</div>
  <div  class="bblock" >
	  <h3 class="text" style="color: #1A2618">Add new user</h3>
	  <p><h4 class="input" style="color: #1A2618">Username:</h3>
    <input class="put_text" maxlength="25" size="31%" name="username"></p>
	  <p><h4 class="input" style="color: #1A2618">Password:</h3>
    <input class="put_text" type="password" maxlength="25" size="31" name="password" class="form-control" id="password"></p>
	  <p><h4 class="input" style="color: #1A2618">Repeat password:</h3>
    <input class="put_text" type="password" maxlength="25" size="31" name="pass_chk" class="form-control" id="password_repit"></p>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <input type="submit" class="butt" id="butt" value="Registration">
</div>
</section>
</#if>

</form>
</#macro>

<#macro logout>
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
	<input type="submit" class="butt_menu" value="Sign Out"/>
</form>
</#macro>