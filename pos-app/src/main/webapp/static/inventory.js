$(document).ready( function () {
    $("#inventory_nav_link").addClass("active");
    $("#home_nav_link").removeClass("active");
    $('#invenotry-table').DataTable({"columnDefs": [ {
                                                                          "targets": -1,
                                                                          "searchable": false,
                                                                          "orderable": false
                                                                        } ]});

    validateInputsAndToggleButtons(['update-inventory'], ['inputQuantity']);
    init();
    getInventoryList();
} );

function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/inventory";
}

function getInventoryUrlStandard(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}
//BUTTON ACTIONS
function showUpdateInventory(id){
    resetAllFormErrors();
    var url = getInventoryUrlStandard() + "/" + id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data){
            $("#displayId").val(data.id);
            $("#displayBarcode").val(data.barcode);
            $("#displayProduct").val(data.prod);
            $("#displayBrand").val(data.brand);
            $("#displayCategory").val(data.category);
            $("#displayQuantity").val(Number(data.quantity).formatLongNumber());
            $("#inputQuantity").val('');
            $("#edit-inventory-modal").modal('toggle');

        },
        error: handleAjaxError
    })
}

function updateInventory(){
    var id = $('#displayId').val();

    var url = getInventoryUrl() + "/" + id;
    var $form = $('#inventory-edit-form');
    var json = toJson($form);

    $.ajax({
     	url: url,
     	type: 'PUT',
     	data: json,
     	headers: {
           'Content-Type': 'application/json'
        },
     	success: function(response) {
     	   getInventoryList();
     	   handleSuccess("Yeyy!! inventory updated successfully");
            $("#edit-inventory-modal").modal('toggle');
     	},
     	error: handleAjaxError
    });

    return false;
}



function getInventoryList(){
	var url = getInventoryUrlStandard();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: handleAjaxError
	});
}


function processData(){
	event.preventDefault();
    	var file = $('#inventoryFile')[0].files[0];
    	if(file == null){
            	    handleError("Oops! looks like you forgot to add a file before uploading");
                    return;
            	}
    	var formData = new FormData();
        formData.append('file', file);
        var url= getInventoryUrl() + "/upload";
        $.ajax({
          url: url,
          type: 'POST',
          data: formData,
          processData:false,
          contentType: false,
          cache: false,
         success: function(){
             getInventoryList();
             handleSuccess("Yeyy!! Inventory tsv uploaded successfully");
         },
         error: function(jqXHR, textStatus, errorThrown){
                var data = jqXHR.responseJSON;
                showUploadError(data);
           }
        });
}

function showUploadError(data){
    if(data.errors != null){
        $("#upload-inventory-modal").modal('toggle');
        $("#error-inventory-modal").modal('toggle');
        $("#inventory-error-table tbody").empty();
        for(var i = 0 ; i < data.errors.length ; i++){
            var row = "<tr>";
            for(var j = 0 ; j < data.errors[i].length ; j++){
                row += "<td>"+data.errors[i][j]+"</td>";
            }
            row += "</tr>";
            $("#inventory-error-table tbody").append(row);
        }
    }
    handleError(data.message);
}


//UI DISPLAY METHODS

function displayInventoryList(data){
    var table = $('#inventory-table').DataTable();
	table.clear();
	var rows = [];
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="showUpdateInventory(' + e.id + ')" class="btn btn-warning text-white"><i class="fa-solid fa-pen-to-square mr-2" style="color: #ffffff;"></i>edit</button></div>';
		var col =[
		    e.barcode,
		    e.prod,
		    e.brand,
		    e.category,
		    Number(e.quantity).formatLongNumber(),
		    buttonHtml
		];
		rows.push(col);
	}
	table.rows.add(rows).draw();
}




function displayUploadData(){
	$('#upload-inventory-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
}


