function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/order";
}

var orderId;

var global;

function getItemsUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content");
    return baseUrl + "/api/order-item";
}

$(document).ready( function () {
    var currentUrl = window.location.href;
    var url = new URL(currentUrl);
    var searchParams = new URLSearchParams(url.search);


    orderId = searchParams.get('id');
    var status = searchParams.get('status');
    if(status != null && status != 'completed'){
        validateInputsAndToggleButtons(['update-item'], ['editQuantity', 'editSellingPrice']);
        validateInputsAndToggleButtons(['add-item'], ['inputBarcode', 'inputQuantity', 'inputSellingPrice']);
        validateInputsAndToggleButtons(['generate-invoice'], ['customerName', 'customerEmail', 'customerPhone']);
    }
    window.history.pushState('Positron', 'Positron', $("meta[name=baseUrl]").attr("content") + "/ui/manage-order");
    init();
    getOrderItems(orderId);
});


function getOrderItems(orderId){
    var url = getItemsUrl() +"/" + orderId;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data){
            showOrderItems(data);
        },
        error: function(){
            window.location.href = $("meta[name=baseUrl]").attr("content") + "/ui/manage-order";
        }
    });
}

function showOrderItems(data){
    var table = $('#order-table').DataTable();
    table.clear().draw();
    var total = 0;
    for(var i in data){
        var e = data[i];
        var buttonHtml = '<button onclick="displayEditItem(' + e.prodId + ')" class="btn btn-warning text-white mr-2"><i class="fa-solid fa-pen-to-square" style="color: #ffffff;"></i></button>';
        buttonHtml += ' <button onclick="deleteItem(' + e.prodId + ')" class="btn btn-danger text-white"><i class="fa-solid fa-trash" style="color: #ffffff;"></i></button>';

        table.row.add([
        	e.barcode,
        	e.productBrandName,
        	Number(e.quantity).formatLongNumber(),
        	Number(e.mrp).formatLongNumber(),
        	Number(e.sellingPrice).formatLongNumber(),
        	Number(e.itemTotal).formatLongNumber(),
        	buttonHtml
        ]).draw();
        total += e.itemTotal;
    }
    $('.total-amount').text(roundUpToTwoDecimalPlaces(total*1.12));
}


function clearOrderItems(){
    var table = $('#order-table').DataTable();
    table.clear().draw();
    $('.total-amount').text("0");
}


//BUTTON ACTIONS
function addItem(event){
	var $form = $("#order-form");
	var json = toJson($form);
	var url = getItemsUrl() + "/" + orderId;

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getOrderItems(orderId);
	   		$('#order-form input[name=barcode]').val('');
	   		$('#order-form input[name=quantity]').val('');
	   		$('#order-form input[name=sellingPrice]').val('');
	   		$('#add-item').prop('disabled', true);

	   },
	   error: handleAjaxError
	});

	return false;
}

function displayEditItem(prodId){
    var url = getItemsUrl() + "/" + orderId +"/" +prodId;
    $.ajax({
        url:url,
        type:'GET',
        success: function(data){
            showItem(data);
        },
        error:handleAjaxError
    });

}

function showItem(data){
    resetAllFormErrors();
	$('#edit-item-modal').modal('toggle');
	$('#edit-item-form input[name=barcode]').val(data.barcode.formatLongNumber());
	$('#edit-item-form input[name=quantity]').val(Number(data.quantity).formatLongNumber());
	$('#edit-item-form input[name=sellingPrice]').val(Number(data.sellingPrice).formatLongNumber());
}

function updateItem(event){
    $('#edit-item-modal').modal('toggle');
    var url = getItemsUrl() + "/" + orderId;
    var $form = $("#edit-item-form");
    var json = toJson($form);



    $.ajax({
        url: url,
    	type: 'PUT',
        data: json,
    	headers: {
            'Content-Type': 'application/json'
        },
    	success: function(response) {
    	    getOrderItems(orderId);
    	},
    	error: handleAjaxError
    });
}

function deleteItem(prodId){
    var url = getItemsUrl() + "/" + orderId +"/" +prodId;
    $.ajax({
        url:url,
        type:'DELETE',
        success: function(response){
            getOrderItems(orderId);

        },
        error:handleAjaxError
    });
}

function deleteOrder(){
    var url = getOrderUrl() + "/" + orderId;
    $.ajax({
        url:url,
        type:'DELETE',
        success: function(response){
             window.location.href = $("meta[name=baseUrl]").attr("content") + "/ui/manage-order"
        },
        error: handleAjaxError
    });
}

function completeOrder(){
    resetAllFormErrors();
    $('#customer-details').modal('toggle');
}

function generateInvoice(){
    $form = $('#customer-details-form');
    json = toJson($form);

        var url = getOrderUrl() + "/complete-order/" + orderId;
        $.ajax({
            url:url,
            type:'PUT',
            xhrFields: {
                  responseType: 'blob'
            },
            data: json,
            headers: {
                'Content-Type': 'application/json'
            },
            success: function(data) {
                var blob = new Blob([data]);
                var url = URL.createObjectURL(blob);
                var link = document.createElement('a');
                link.href = url;
                link.download = 'invoice.pdf';
                link.click();
                URL.revokeObjectURL(url);
                url = $("meta[name=baseUrl]").attr("content") + "/ui/manage-order?amount="+parseInt($('.total-amount').text());
                window.open(url);
            },
            error: function(){
                handleError("Oops! Something went wrong in our end. please wait for sometime or try again later");
            }
        });
}

function processData(){
	event.preventDefault();
    	var file = $('#orderFile')[0].files[0];
    	if(file == null){
            handleError("Oops! looks like you forgot to add a file before uploading");
            return;
        }
    	var formData = new FormData();
        formData.append('file', file);
        var url= getItemsUrl() + "/upload/" + orderId;
        $.ajax({
          url: url,
          type: 'POST',
          data: formData,
          processData:false,
          contentType: false,
          cache: false,
         success: function(){
             getOrderItems(orderId);
             handleSuccess("Yeyy!! Order tsv uploaded successfully");
         },
         error: function(jqXHR, textStatus, errorThrown){
            var data = jqXHR.responseJSON;
            showUploadError(data);
         }
        });
}

function showUploadError(data){
    $("#upload-order-modal").modal('toggle');
    if(data.errors != null){
            $("#error-order-modal").modal('toggle');
            $("#order-error-table tbody").empty();
            data.errors.sort(sortFunction)
            for(var i = 0 ; i < data.errors.length ; i++){
                var row = "<tr>";
                for(var j = 0 ; j < data.errors[i].length ; j++){
                    row += "<td>"+data.errors[i][j]+"</td>";
                }
                row += "</tr>";
                $("#order-error-table tbody").append(row);
            }
        }
        handleError(data.message);
}

function displayUploadData(){
	$('#upload-order-modal').modal('toggle');
}

function showAddItem(){
    resetAllFormErrors();
    $('#order-form input[name=barcode]').val('');
	$('#order-form input[name=quantity]').val('');
	$('#order-form input[name=sellingPrice]').val('');
    $('#add-order-item-modal').modal('toggle')
}

function showDeleteOrderModal(){
    $('#delete-order-confirmation').modal('toggle');
}

////INITIALIZATION CODE
function init(){
    $('#show-add-item').click(showAddItem);
	$('#add-item').click(addItem);
	$('#update-item').click(updateItem);
	$('#complete-order').click(completeOrder);
	$('#delete-order').click(deleteOrder);
	$('#show-delete-order-modal').click(showDeleteOrderModal);
	$('#upload-data').click(displayUploadData);
    $('#generate-invoice').click(generateInvoice);
    $('#process-data').click(processData);
}