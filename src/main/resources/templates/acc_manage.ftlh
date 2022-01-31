<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    <div>
        <@l.logout />
    </div>
    <div>
        ${userMessage}
    </div>
    <meta name="csrf-token" content=${_csrf.token}>
    <form action="chg_psw" method="post">
        <div>
            Fill following fields
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div>
            <input type="password" id="old_password" name="old_password" placeholder="Old password"
                   class="old_password"/>
            </div>
            <div>
            <input type="password" id="new_password" name="new_password" placeholder="New password"
                               class="new_password"/>
            </div>
            <div>
            <input type="password" id="new_password_check" name="new_password_check" placeholder="Repeat new password"
                               class="new_password_check"/>
            </div>
            <button class="btn" name="change_password"> Change password</button>
        </div>
    </form>

    <form action="erase_me" method="post">
        <div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="btn" name="erase_me">Delete account</button>
        </div>
    </form>
</@c.page>