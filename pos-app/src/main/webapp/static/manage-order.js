$(document).ready( function () {
    var currentUrl = window.location.href;
    var url = new URL(currentUrl);
    var searchParams = new URLSearchParams(url.search);
    var amount = searchParams.get('amount');
    window.history.pushState('Positron', 'Positron', $("meta[name=baseUrl]").attr("content") + "/ui/manage-order")
    if(amount != null){
        generateUPIQRCode(amount);
    }
    $("#manage_order_nav_link").addClass("active");
    $("#home_nav_link").removeClass("active");
    $('#manage-order-table').DataTable({
                            "ordering": false,
                            "columnDefs": [ {
                                      "targets": 1,
                                      "searchable": true,
                                      "orderable": true
                                    },{
                                              "targets": 1, // your case first column
                                              "className": "text-center",
                                              "vertical-align": "center"
                                         } ]

                                   });
    getOrderList();
} );

function generateUPIQRCode(amount){
    $('#upi-modal').modal('toggle');
    var vpa = 'study.avishek@okhdfcbank';
    var upiLink = 'upi://pay?pa=' + encodeURIComponent(vpa) + '&pn=Avishek&mc=&tid=&tr=&tn=&am=' + amount + '&cu=INR';
    var qrcode = new QRCode(document.getElementById("qrcode"), {
        text: upiLink,
        width: 400,
        height: 400
    });
}

function getOrderUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderList(){
    var url = getOrderUrl();
    $.ajax({
        url:url,
        type: 'GET',
        success: function(data) {
        	displayOrderList(data);
        },
        error: handleAjaxError
    });


}

function displayOrderList(data){
    data = data.reverse();
    var table = $('#manage-order-table').DataTable();;
    table.clear();
    for(var i in data){
    	var e = data[i];
    	var button1, button2, total, badge;
    	var status = "'" + e.orderStatus.toLowerCase() + "'";

    	if(e.orderStatus == "ACTIVE"){
    	    badge = '<h5><span class="badge badge-pill badge-success">active</span></h5>';
    	    button1 = ' <button onclick="viewOrder(' + e.id +','+status + ')" class="btn btn-primary text-white mr-2"><i class="fa-solid fa-eye mr-2" style="color: #ffffff;"></i>view</button>';
    	    button2 = ' <button onclick="showDeleteOrder(' + e.id + ')" class="btn btn-danger text-white pr-4"><i class="fa-solid fa-trash mr-2" style="color: #ffffff;"></i>delete</button>';
    	    total = button1+button2;
    	}
    	else{
    	    badge = '<h5><span class="badge badge-pill badge-secondary">invoiced</span></h5>';
            button1 = ' <button onclick="viewOrder(' + e.id +','+status + ')" class="btn btn-primary text-white mr-2"><i class="fa-solid fa-eye mr-2" style="color: #ffffff;"></i>view</button>';
            button2 = ' <a href="/positron/api/resource/invoice-'+e.id+'.pdf" class="btn btn-warning text-white"><i class="fa-solid fa-cloud-arrow-down mr-2" style="color: #ffffff;"></i>download</a>';
            total = button1+button2;
    	}
    	table.row.add([
    		e.id,
    		badge,
    		e.itemCount,
    		roundUpToTwoDecimalPlaces(e.revenue) + ' Rs',
    		total
    		]);
    }
    table.draw();
}

function showDeleteOrder(id){
    $('#delete-order-confirmation').modal('toggle');
    $('#confirm-delete').attr('onclick', 'deleteOrder('+id+')');
}

function deleteOrder(id){
    var url = getOrderUrl() + "/" + id;
    $.ajax({
        url: url,
        type: 'DELETE',
        success: function(){
            $('#delete-order-confirmation').modal('toggle');
            getOrderList();
            handleSuccess('Order deleted successfully');
        },
        error: handleAjaxError
    });
}

function viewOrder(id, status){
    var currentPath = $("meta[name=baseUrl]").attr("content") + "/ui/order?id="+id +"&status="+status;
    window.location.href = currentPath;
}


