$(document).ready(function(){
    $("#init").click(init);
    validateInputsAndToggleButtons(['init'], ['email','password']);
});

function getInitUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/public/init";
}


function init(event){
    var $form = $("#init-form");
    var json = toJson($form);
    var url = getInitUrl();

    $.ajax({
   	   url: url,
   	   type: 'POST',
       data: json,
       headers: {
           'Content-Type': 'application/json'
       },
       success: function(){
       window.location.href = $("meta[name=baseUrl]").attr("content") + "/site/login";
       },
       error: handleAjaxError
    });
}