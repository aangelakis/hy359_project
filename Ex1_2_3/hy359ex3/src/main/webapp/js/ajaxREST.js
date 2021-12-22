function createTableFromJSON(data,i) {
	var html = "<h4>BloodTest "+i+"</h4><table><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category=x;
        var value=data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table><br>";
    return html;

}


function getBloodTest(){
    const xhr = new XMLHttpRequest();
    var amka = document.getElementById("amka2").value;
xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
                var i=0;
		const obj = JSON.parse(xhr.responseText);
                document.getElementById("msg").innerHTML="";
                for(id in obj){
			document.getElementById("msg").innerHTML+=createTableFromJSON(obj[id],i);
			i++;
			
		}
                  
        } else if (xhr.status !== 200) {
            document.getElementById('msg').innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>" + JSON.stringify(xhr.responseText);
        }
    };
xhr.open("GET", "http://localhost:8080/Computers_REST_API/bloodtests/bloodTestRequests/bloodTests/" + amka);
xhr.setRequestHeader("Accept", "application/json");
xhr.setRequestHeader("Content-Type", "application/json");
xhr.send();
}

function addBloodTest() {
    let myForm = document.getElementById('myForm');
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    
    if(data["blood_sugar"] === "") {
        delete data.blood_sugar;
    }
    if(data["cholesterol"] === "") {
        delete data.cholesterol;
    }
    if(data["iron"] === "") {
        delete data.iron;
    }
    if(data["vitamin_b12"] === "") {
        delete data.vitamin_b12;
    }
    if(data["vitamin_d3"] === "") {
        delete data.vitamin_d3;
    }
             
    var jsonData=JSON.stringify(data);
    
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
           document.getElementById('msg').innerHTML=JSON.stringify(xhr.responseText);
            
        } else if (xhr.status !== 200) {
            document.getElementById('msg')
                    .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"+
					JSON.stringify(xhr.responseText);
 
        }
    };
    xhr.open('POST', 'http://localhost:8080/Computers_REST_API/bloodtests/bloodTestRequests/newBloodTest');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}



function getBloodTestWithMeasure(){
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            const obj = JSON.parse(xhr.responseText);
            var i=1;
            document.getElementById("msg").innerHTML="";
            for(id in obj){
                document.getElementById("msg").innerHTML+=createTableFromJSON(obj[id],i);
                i++;
            }

        }else if (xhr.status !== 200) {
            document.getElementById('msg').innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"+JSON.stringify(xhr.responseText);
        }
    };

    var amka=document.getElementById("amka_measure").value;
    var measure=document.getElementById("measure").value;
    xhr.open("GET", "http://localhost:8080/Computers_REST_API/bloodtests/bloodTestRequests/bloodTestMeasure/"+ amka + "/" + measure);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}


function updateBloodTest() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
           document.getElementById('msg').innerHTML=JSON.stringify(xhr.responseText);
            
        } else if (xhr.status !== 200) {
            document.getElementById('msg').innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"+JSON.stringify(xhr.responseText);
 
        }
    };
        var id = document.getElementById("bloodtestid").value;
	var measure=document.getElementById("measure2").value;
	var value=document.getElementById("value").value;
    xhr.open('PUT', 'http://localhost:8080/Computers_REST_API/bloodtests/bloodTestRequests/bloodTest/'+id+"/"+measure+"/"+value);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function deleteBloodTest() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
           document.getElementById('msg').innerHTML=JSON.stringify(xhr.responseText);
            
        } else if (xhr.status !== 200) {
            document.getElementById('msg')
                    .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>"+
					JSON.stringify(xhr.responseText);
 
        }
    };
    var id=document.getElementById("bloodtestid_2").value;
    xhr.open('DELETE', 'http://localhost:8080/Computers_REST_API/bloodtests/bloodTestRequests/bloodTestDeletion/'+id);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}


