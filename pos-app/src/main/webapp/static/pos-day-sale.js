var startDate = "";
var endDate = "";
var table;

function refreshTable() {
  getReportList();
}

function toggleFilterModal(){
    $('#filter-modal').modal('toggle');
}

function updateCountdown() {
  var now = new Date();
  var secondsUntilNextMinute = (60 - now.getSeconds() + 5)%60;
  $("#counter").text(secondsUntilNextMinute + " seconds");
  if (now.getSeconds() === 5) {
    refreshTable();
  }
}
updateCountdown();
setInterval(updateCountdown, 1000);





function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/pos-day-sale";
}


function getReportList(success = false){
    startDate = startDate==""?"0":startDate;
    endDate = endDate==""?"0":endDate;
	var url = getReportUrl() + "/" + startDate + "/" + endDate;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayReportList(data);
	   		if(success){
	   		    handleSuccess('Filter applied');
	   		}
	   },
	   error: handleAjaxError
	});
}



function displayReportList(data){
    data.sort(sortFunction).reverse();
    table.clear();
    var rows = [];
	for(var i in data){
		var e = data[i];
		rows.push([e.date, e.itemCount, Number(e.orderCount), Number(e.totalRevenue)]);
	}
    table.rows.add(rows).draw();
}

function applyFilter(){
    startDate = $('#inputStartDate').val();
    endDate = $('#inputEndDate').val();
    getReportList(true);
    toggleFilterModal();
}

function clearStartDate(){
    $('#inputEndDate').datepicker('setStartDate', false);
    $('#inputStartDate').datepicker('clearDates');
}

function clearEndDate(){
    $('#inputStartDate').datepicker('setEndDate', false);
    $('#inputEndDate').datepicker('clearDates');
}


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
      getReportList();
      init();
});



function init(){
    $('#show-filter-modal').click(toggleFilterModal);
    $('#apply-filter').click(applyFilter);
    table = $('#report-table').DataTable({
                                                 paging: false,
                                                 scrollCollapse: true,
                                                 scrollY: '68vh'
                                             });
}