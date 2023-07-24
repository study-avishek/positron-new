var data;
$(document).ready( function () {
    $("#brand_nav_link").addClass("active");
    $("#home_nav_link").removeClass("active");
    init();
    getBrandList();
    $('#brand-table').DataTable({
        paging: false,
        scrollCollapse: true,
        scrollY: '68vh'
    });
} );

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function toggleFilterModal(){
    $('#filter-modal').modal('toggle');
    displayFilterList(data);
}

function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(response) {
	        data = response;
	   		displayBrandList(data);
	   },
	   error: handleAjaxError
	});
}

function displayFilterList(data){
    var brandDropdown = $('#inputBrand');
    var categoryDropdown = $('#inputCategory');
    var brands = [...new Set(data.map(item => item.brand))];
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

function applyFilter(){
    var brandFilter = $('#inputBrand').val();
    var categoryFilter = $('#inputCategory').val();
    displayBrandList(data, brandFilter,categoryFilter);
    $('#filter-modal').modal('toggle');
    handleSuccess("Filter applied successfully");

}

function displayBrandList(data, brandFilter='', categoryFilter=''){
    var table  = $('#brand-table').DataTable();
	table.clear().draw();
	var rows = []
	for(var i in data){
        var e = data[i];
        if(brandFilter == '' || brandFilter==e.brand){
            if(categoryFilter=='' || categoryFilter == e.category){
                rows.push([ e.brand, e.category]);
            }
        }
    }
    table.rows.add(rows).draw();
}

function init(){
    $('#show-filter-modal').click(toggleFilterModal);
    $('#apply-filter').click(applyFilter)
}