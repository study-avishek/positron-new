$(document).ready( function () {
    $("#product_nav_link").addClass("active");
    $("#home_nav_link").removeClass("active");
    $('#product-table').DataTable({
        "processing": true,
        "language": {
            'processing': '<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i><span class="sr-only">Loading...</span> '
        }
    });
    validateInputsAndToggleButtons(['add-product'], ['inputName', 'inputBarcode','inputBrand','inputCategory','inputMrp']);
    validateInputsAndToggleButtons(['update-product'], ['editName', 'editBarcode','editBrand','editCategory','editMrp']);
    init();
    getProductList();

} );



function displaySelectDropdowns(data, brandId='#inputBrand', catId='#inputCategory', brandInput='', categoryInput=''){
    var brandDropdown = $(brandId);
    var brands = [...new Set(data.map(item => item.brand))]; // Get unique names
    var categoryDropdown = $(catId);
    categoryDropdown.empty();
    brandDropdown.empty()
    brandDropdown.append('<option value="">Select Brand</option>');
    categoryDropdown.append('<option value="">Select Category</option>');
    categoryDropdown.prop('disabled', true);
    brands.forEach(function(brand) {
      if(brand==brandInput){
      brandDropdown.append('<option value="' + brand + '" selected><pre>' + brand + '</pre></option>')
      }
      else{
      brandDropdown.append('<option value="' + brand + '"><pre>' + brand + '</pre></option>');
      }

    });

    brandDropdown.selectpicker('refresh');

    if(categoryInput != ''){
            var selectedName =brandInput;
            categoryDropdown.empty();
            categoryDropdown.append('<option value="">Select Category</option>');
            var categories = data.filter(function(item) {
                      return item.brand === selectedName;
                    }).map(function(item) {
                      return item.category;
                    });
            categories.forEach(function(category) {
            if(category==categoryInput){
                  categoryDropdown.append('<option value="' + category + '" selected><pre>' + category + '</pre></option>');
                  }
            else{
                categoryDropdown.append('<option value="' + category + '">' + category + '</option>');
            }

            });
            categoryDropdown.prop("disabled", categories.length == 0);
            brandDropdown.selectpicker('refresh');
            categoryDropdown.selectpicker('refresh');
    }

      brandDropdown.change(function() {
        var selectedName = $(this).val();
        var categoryDropdown = $(catId);
        categoryDropdown.empty();
        categoryDropdown.append('<option value="">Select Category</option>');
        var categories = data.filter(function(item) {
          return item.brand === selectedName;
        }).map(function(item) {
          return item.category;
        });

        categories.forEach(function(category) {
           categoryDropdown.append('<option value="' + category + '">' + category + '</option>');
        });

        categoryDropdown.prop("disabled", categories.length == 0);
        brandDropdown.selectpicker('refresh');
        categoryDropdown.selectpicker('refresh');
      });

      brandDropdown.selectpicker('refresh');
      categoryDropdown.selectpicker('refresh');
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/admin/product";
}

function getProductUrlStandard(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function showAddProductModal(){
    $('#add-product-modal').modal('toggle');
    resetAllFormErrors();
    $('#product-form input[name=name]').val('');
    $('#product-form input[name=barcode]').val('');
    $('#product-form input[name=mrp]').val('');

    var url = getBrandUrl();
        	$.ajax({
        	   url: url,
        	   type: 'GET',
        	   success: function(data) {
        	   		displaySelectDropdowns(data);
        	   },
        	   error: handleAjaxError
    });
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
	   		$('#add-product-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getProductUrlStandard();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}


function processData(){
	    event.preventDefault();
    	var file = $('#productFile')[0].files[0];
    	if(file == null){
    	    handleError("Oops! looks like you forgot to add a file before uploading");
            return;
    	}
    	var formData = new FormData();
        formData.append('file', file);
        var url= getProductUrl() + "/upload";
        $.ajax({
          url: url,
          type: 'POST',
          data: formData,
          processData:false,
          contentType: false,
          cache: false,
          success: function(){
             getProductList();
             handleSuccess("Yeyy!! Product tsv uploaded successfully");
             $("#upload-product-modal").modal('toggle');
         },
         error: function(jqXHR, textStatus, errorThrown){
                var data = jqXHR.responseJSON;
                showUploadError(data);
           }
        });
}

function showUploadError(data){
    if(data.errors != null){
        $("#upload-product-modal").modal('toggle');
        $("#error-product-modal").modal('toggle');
        $("#product-error-table tbody").empty();
        for(var i = 0 ; i < data.errors.length ; i++){
            var row = "<tr>";
            for(var j = 0 ; j < data.errors[i].length ; j++){
                row += "<td>"+data.errors[i][j]+"</td>";
            }
            row += "</tr>";
            $("#product-error-table").append(row);
        }
    }
    handleError(data.message);
}

function displayProductList(data){
    var table = $('#product-table').DataTable();
	table.clear();
	var rows = [];
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button onclick="displayEditProduct(' + e.id + ')" class="btn btn-warning text-white"><i class="fa-solid fa-pen-to-square mr-2" style="color: #ffffff;"></i>edit</button>';
		var col = [       		    e.name,
                          		    e.brand,
                          		    e.category,
                          		    e.barcode,
                          		    Number(e.mrp),
                          		    buttonHtml
                          		]
		rows.push(col);
	}
	table.rows.add(rows).draw();
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
	   },
	   error: handleAjaxError
	});
}


function displayUploadData(){
    $('#upload-product-modal input').val('');
    $('#upload-product-modal label').text('Choose your file');
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
    var url = getBrandUrl();
    $.ajax({
        url: url,
    	type: 'GET',
    	success: function(list) {
    	   displaySelectDropdowns(list, '#editBrand', '#editCategory', data.brand, data.category);
    	},
    	error: handleAjaxError
    });
    resetAllFormErrors();
	$("#product-edit-form input[name=name]").val(data.name);
    $("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=id]").val(data.id);
	$('#edit-product-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
    $('#show-add-modal').click(showAddProductModal);
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
}