var data;
var table;
$(document).ready( function () {
    $("#inventory_nav_link").addClass("active");
    $("#home_nav_link").removeClass("active");
    table  = $('#inventory-table').DataTable(
    {
            paging: false,
            scrollCollapse: true,
            scrollY: '68vh'
    });
    init();
    getBrandList();
    getInventoryList(false);
} );

function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/inventory";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	        data = response;
	   },
	   error: handleAjaxError
	});
}

function init(){
    $('#show-filter-modal').click(toggleFilterModal);
    $('#apply-filter').click(getInventoryList)
}

function toggleFilterModal(){
    $('#filter-modal').modal('toggle');
    displayFilterList(data);
}

function getInventoryList(def = true){
	var url = getInventoryUrl();
	var $form = $("#filter-edit-form");
    var json = toJson($form);
	$.ajax({
	   url: url,
	   type: 'POST',
       data: json,
       headers: {
          'Content-Type': 'application/json'
       },
	   success: function(response) {
	        displayInventoryList(response);
	        if(def){
	            $('#filter-modal').modal('toggle');
            	handleSuccess("Filter applied successfully");
            }
	   },
	   error: handleAjaxError
	});
}

function displayFilterList(data){
    var brandDropdown = $('#inputBrand');
        var categoryDropdown = $('#inputCategory');
        var brands = [...new Set(data.map(item => item.brand))]; // Get unique names
        var categories = [...new Set(data.map(item => item.category))];
        brandDropdown.empty()
        categoryDropdown.empty();

        brandDropdown.append('<option value="" selected>Select Brand</option>');
        categoryDropdown.append('<option value="" selected>Select Brand</option>');
        brands.forEach(function(brand) {
            brandDropdown.append('<option value="' + brand + '"><pre>' + brand + '</pre></option>');
        });
        categories.forEach(function(category){
            categoryDropdown.append('<option value="' + category + '"><pre>' + category + '</pre></option>');
        });

        brandDropdown.selectpicker('refresh');
        categoryDropdown.selectpicker('refresh');
}


function displayInventoryList(data){
	table.clear();
	for(var i in data){
        var e = data[i];
        table.row.add([e.brand, e.category, e.quantity]);
    }
    table.draw();
}