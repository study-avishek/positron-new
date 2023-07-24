var global;
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function roundUpToTwoDecimalPlaces(number) {
  return Math.ceil(number * 100) / 100;
}

function logout(){
    url = $("meta[name=baseUrl]").attr("content") + "/session/logout";
    $.ajax({
        url: url,
        type: 'GET',
        success: function(){
            window.location.replace($("meta[name=baseUrl]").attr("content")) ;
        },
        error: function(){
         handleError("Logout failed please try again later");
        }
    })
}


function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	if(response.errorList != null){
	Swal.fire({
      position: 'top-end',
      icon: 'error',
      title: response.message,
      text: ((response.errorList == null)?"":response.errorList.join(" , ")),
      showConfirmButton: false,
      showCancelButton: true,
      cancelButtonText: 'Dismiss',
      timer: 5000
    })}
    else{
    Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'error',
          text: response.message,
          showConfirmButton: false,
          showCancelButton: true,
          cancelButtonText: 'Dismiss',
          timer: 5000
        })}
    }

function handleSuccess(text){
    Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: "success",
          text: text,
          showConfirmButton: false,
          showCancelButton: true,
          cancelButtonText: 'Dismiss',
          timer: 30000
        })
}

function handleError(text){
    Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: "error",
          text: text,
          showConfirmButton: false,
          showCancelButton: true,
          cancelButtonText: 'Dismiss',
          timer: 3000
        })
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}

function createNewOrder(){
    var url = $("meta[name=baseUrl]").attr("content") + "/api/order";
    $.ajax({
        url: url,
        type: 'POST',
        success: function(data){
            global = data;
            var id = data.id;
            var status = data.orderStatus;
            url = $("meta[name=baseUrl]").attr("content") + "/ui/order?id=" + id + "&status="+status.toLowerCase();
            window.open(url, "_blank")
        },
        error: handleAjaxError
    });
}

function downloadPDF(tableId) {
        const table = document.getElementById(tableId);

          // Create a new window to open the printable version of the table
          const printWindow = window.open('', '_blank');

          // Generate the HTML for the printable version of the table
          const printableHTML = `
            <html>
              <head>
                <style>
                  @media print {
                    table { width: 100%; }
                  }
                </style>
              </head>
              <body>
                ${table.outerHTML}
              </body>
            </html>
          `;

          // Write the HTML to the new window and trigger printing
          printWindow.document.open();
          printWindow.document.write(printableHTML);
          printWindow.document.close();
          printWindow.print();
    }

function downloadTSV(tableId) {
      var tsvContent = '';

      var headers = $('#' + tableId + ' thead th').map(function() {
        return $(this).text();
      }).get();
      tsvContent += headers.join('\t') + '\n';

      $('#' + tableId + ' tbody tr').each(function() {
        var row = $(this).find('td');

        row.each(function() {
          tsvContent += $(this).text() + '\t';
        });
        tsvContent = tsvContent.substring(0, tsvContent.length-1);
        tsvContent += '\n';
      });

      var blob = new Blob([tsvContent], { type: 'text/tab-separated-values' });
      var url = URL.createObjectURL(blob);

      var link = $('<a></a>');
      link.attr('href', url);
      link.attr('download', 'table_data.tsv');
      link[0].click();
}

function handleFileUpload(file, fileName) {
  var fileInput = document.getElementById(file);
  var fileInputLabel = document.getElementById(fileName);

  if (fileInput.files && fileInput.files.length > 0) {
    const fileName = fileInput.files[0].name;
    fileInputLabel.innerText = fileName;
  }
  else{
    fileInputLabel.innerText = "Choose file"
  }
}



function sortFunction(a, b) {
    if (a[0] === b[0]) {
        return 0;
    }
    else {
        return (a[0] < b[0]) ? -1 : 1;
    }
}

function isValidInput(inputElement) {
  return inputElement.checkValidity();
}

function setupRealTimeValidation(form) {
  const inputs = form.querySelectorAll('input');

  inputs.forEach(input => {
    input.addEventListener('input', function() {
      if (!isValidInput(input)) {
        input.classList.add('is-invalid');
        if(input.title != null && input.title.length > 0){
            showError(input, input.title);
        }
        else showError(input, input.validationMessage);
      } else {
        input.classList.remove('is-invalid');
        clearError(input);
      }
    });

    input.addEventListener('focus', function() {
      input.classList.remove('is-invalid');
      clearError(input);
    });
  });
}

function showError(inputElement, errorMessage) {
  const errorSpan = inputElement.nextElementSibling;
  if (errorSpan && errorSpan.classList.contains('error')) {
    errorSpan.textContent = errorMessage;
  } else {
    const newErrorSpan = document.createElement('span');
    newErrorSpan.textContent = errorMessage;
    newErrorSpan.className = 'error invalid-feedback';
    inputElement.parentNode.appendChild(newErrorSpan);
  }
}

function clearError(inputElement) {
  const errorSpan = inputElement.nextElementSibling;
  if (errorSpan && errorSpan.classList.contains('error')) {
    errorSpan.textContent = '';
  }
}

function resetAllFormErrors() {
  const forms = document.querySelectorAll('form');
  forms.forEach(form => {
    const inputs = form.querySelectorAll('input');
    inputs.forEach(input => {
      input.classList.remove('is-invalid');
      clearError(input);
    });
  });
}

function validateInputsAndToggleButtons(buttonIds, inputIds) {
            const buttons = buttonIds.map(id => document.getElementById(id));
            const inputs = inputIds.map(id => document.getElementById(id));

            function areAllInputsValid() {
                return inputs.every(input => input.checkValidity());
            }

            function toggleButtonStates() {
                const areInputsValid = areAllInputsValid();
                buttons.forEach(button => {
                    button.disabled = !areInputsValid;
                });
            }

            inputs.forEach(input => {
                input.addEventListener('input', toggleButtonStates);
                input.addEventListener('invalid', toggleButtonStates);
            });

            const observer = new MutationObserver(toggleButtonStates);
            inputs.forEach(input => observer.observe(input, { attributes: true }));

            toggleButtonStates();
        }


// Initialize real-time validation for all forms
const forms = document.querySelectorAll('form');
forms.forEach(form => setupRealTimeValidation(form));