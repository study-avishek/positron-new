var data;
var table;

$(document).ready(function(){
    var startDatePicker=$('#inputStartDate').datepicker({
              autoclose: true,
              todayHighlight: true,
              format: 'dd-mm-yyyy',
              inline: true
            }).on('changeDate', function(selected) {
              $('#inputEndDate').datepicker('setStartDate', selected.date);
            });
      var endDatePicker = $('#inputEndDate').datepicker({
            autoclose: true,
            todayHighlight: true,
            format: 'dd-mm-yyyy',
            inline: true
            }).on('changeDate', function(selected) {
                 $('#inputStartDate').datepicker('setEndDate', selected.date);
             });


      $('#clearBtnStart').on('click', clearStartDate);
      $('#clearBtnEnd').on('click', clearEndDate);

      init();
      $('#sales-report-table').DataTable({
                                                 paging: false,
                                                 scrollCollapse: true,
                                                 scrollY: '68vh'
                                             });
      getBrandList();
      getReportList();
});

function getReportList(){
    var $form = $('#filter-edit-form');
    var json = toJson($form);
    var url = getReportUrl();

     $.ajax({
            url: url,
            type: 'POST',
            data: json,
            headers: {
                'Content-Type': 'application/json'
            },
            success: function(data) {
            	  showReportList(data);
            },
            error: handleAjaxError
        })
}

function toggleFilterModal(){
    $('#filter-modal').modal('toggle');
    displayFilterList(data);
}

function clearStartDate(){
    $('#inputEndDate').datepicker('setStartDate', false);
    $('#inputStartDate').datepicker('clearDates');
}

function clearEndDate(){
    $('#inputStartDate').datepicker('setEndDate', false);
    $('#inputEndDate').datepicker('clearDates');
}


function getBrandList(){
    var url = getBrandUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(response){
            data = response;
            displayFilterList(data)
        },
        error: handleAjaxError
    })
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

function getReportUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/sales";
}

function getBrandUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function applyFilter(){
    var $form = $('#filter-edit-form');
    var json = toJson($form);
    var url = getReportUrl();

    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(data) {
        	  showReportList(data);
        	  handleSuccess("Filter applied successfully")
        	  $('#filter-modal').modal('toggle');
        },
        error: handleAjaxError
    })
}

function showReportList(data){
    var table = $('#sales-report-table').DataTable();
	table.clear();
	var rows = [];
	for(var i in data){
		var e = data[i];
		rows.push([
		    e.brand,
		    e.category,
		    e.quantity,
		    e.revenue
		]);
	}
	table.rows.add(rows).draw();
}

function init(){
    $('#show-filter-modal').click(toggleFilterModal);
    $('#apply-filter').click(applyFilter)
}