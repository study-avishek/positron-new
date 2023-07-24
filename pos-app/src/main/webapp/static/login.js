$(document).ready(function(){
    $("#login").click(login);
    validateInputsAndToggleButtons(['login'], ['email','password']);
});

function getLoginUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/public/login";
}


function login(event){
    var $form = $("#login-form");
    var json = toJson($form);
    var url = getLoginUrl();

    $.ajax({
   	   url: url,
   	   type: 'POST',
       data: json,
       headers: {
           'Content-Type': 'application/json'
       },
       success: function(){
       window.location.href = $("meta[name=baseUrl]").attr("content") + "/ui/home";
       },
       error: handleAjaxError
    });
}