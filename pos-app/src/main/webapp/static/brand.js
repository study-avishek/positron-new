$(document).ready( function () {
    $("#brand_nav_link").addClass("active");
    $("#home_nav_link").removeClass("active");
    validateInputsAndToggleButtons(['add-brand'], ['inputBrand','inputCategory']);
    validateInputsAndToggleButtons(['update-brand'], ['editBrand', 'editCategory']);

    init();
    getBrandList();
} );

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/brand";
}

function getBrandUrlStandard(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function showAddBrandModal(){
    resetAllFormErrors();
    $('#brand-form input').val('');
    $('#add-brand-modal').modal('toggle');
}


function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBrandList();
	   		handleSuccess("Yeyy!! Brand Category added successfully")
	   		$('#add-brand-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBrandList();
	   		handleSuccess("Yeyy!! Brand Category updated successfully")
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandList(){
	var url = getBrandUrlStandard();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
	   },
	   error: handleAjaxError
	});
}




function processData(event){
    event.preventDefault();
	var file = $('#brandFile')[0].files[0];
	if(file == null){
	    handleError("Oops! looks like you forgot to add a file before uploading");
	    return;
	}
	var formData = new FormData();
    formData.append('file', file);
    var url= getBrandUrl() + "/upload";
    $.ajax({
      url: url,
      type: 'POST',
      data: formData,
      processData:false,
      contentType: false,
      cache: false,
     success: function(){
         getBrandList();
         handleSuccess("Yeyy!! Brand Category tsv uploaded successfully");
         $("#upload-brand-modal").modal('toggle');
     },
     error: function(jqXHR, textStatus, errorThrown){
            var data = jqXHR.responseJSON;
            showUploadError(data);
       }
    });


}

function showUploadError(data){
    $("#upload-brand-modal").modal('toggle');
    if(data.errors != null){
        $("#error-brand-modal").modal('toggle');
        $("#brand-error-table tbody").empty();
        data.errors.sort(sortFunction)

        for(var i = 0 ; i < data.errors.length ; i++){
            var row = "<tr>";
            for(var j = 0 ; j < data.errors[i].length ; j++){
                row += "<td>"+data.errors[i][j]+"</td>";
            }
            row += "</tr>";
            $("#brand-error-table tbody").append(row);
        }
    }
        handleError(data.message);
}

//UI DISPLAY METHODS

function displayBrandList(data){
    var table = $('#brand-table').DataTable();
	table.clear().draw();
	var rows = []
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button onclick="displayEditBrand(' + e.id + ')" class="btn btn-warning text-white"><i class="fa-solid fa-pen-to-square mr-2" style="color: #ffffff;"></i>edit</button>';
		rows.push([
		    e.brand,
		    e.category,
		    buttonHtml
		]);
	}
	table.rows.add(rows).draw();
}

function displayEditBrand(id){

	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: handleAjaxError
	});
}

function displayUploadData(){
    $('#upload-brand-modal input').val('');
    $('#upload-brand-modal label').text('Choose your file');
	$('#upload-brand-modal').modal('toggle');
}

function displayBrand(data){
    resetAllFormErrors();
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}



//INITIALIZATION CODE
function init(){
    $('#show-add-modal').click(showAddBrandModal);
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
}




