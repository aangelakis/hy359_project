/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*function show_register() {
 document.getElementById("form").style.display = "block";
 document.getElementById("login_form").style.display = "none";
 }*/

/*function init_show() {
 document.getElementById("login_form").style.display = "none";
 document.getElementById("form").style.display = "none";
 }*/

function loginPOST() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            //setChoicesForLoggedUser();
            $("#login").load("choices1.html");
            $("#ajax_update").load("update.html");
            $("#ajax_update").hide();
            $("#ajax_form").html("<h1>Successful Login</h1><br>");

        } else if (xhr.status !== 200) {

            $("#login_error").html("Wrong Credentials");
            //('Request failed. Returned status of ' + xhr.status);
        }
    };
    var data = $('#loginForm').serialize();
    xhr.open('POST', 'Login');
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send(data);
}

function isLoggedIn() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            console.log(responseData);
            $("#login").load("choices1.html");
            $("#ajax_update").load("update.html");
            $("#ajax_update").hide();
            //$("#ajax_form").load("user.html");
            $("#ajax_form").html("<h1>Welcome back " + responseData.username + "</h1>");
        } else if (xhr.status !== 200) {
            $("#ajax_update").hide();
            $("#ajax_form").hide();
            $('#login').load("login_register.html");
            //alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('GET', 'Logout');
    xhr.setRequestHeader("Content-type", "application/json");

    xhr.send();
}

function logout() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#login').load("login_register.html");
            $("#ajax_form").html("<h1>Successful Logout</h1><br>");
            $("#ajax_update").hide();
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('POST', 'Logout');
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send();
}


function createTableFromJSON(data) {
    var html = "<table class=" + "table table-dark" + "><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    html += "<hr size=" + "8" + "width=" + "90%" + "color=" + "red>"
    return html;
}

var data;
var age;

function getDataRequest() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);

            data = responseData;

            age = responseData["birthdate"].toString().substr(0, 4);
            age = 2021 - age;
            delete responseData["user_id"];
            $('#ajax_form').html("<h1>Your Data</h1>");
            $('#ajax_form').append(createTableFromJSON(responseData));
            $("#ajax_form").show();
            $("#ajax_update").hide();
            document.getElementById("bmi").disabled = false;
            document.getElementById("ideal_weight").disabled = false;
            document.getElementById("change").disabled = false;

            // $("#myForm").hide();
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };

    xhr.open('GET', 'Login');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function showUpdateForm() {
    $("#ajax_form").hide();
    $("#ajax_update").show();
    console.log(data["firstname"]);

    document.getElementById("firstname").value = data["firstname"];
    document.getElementById("lastname").value = data["lastname"];
    document.getElementById("username").value = data["username"];
    document.getElementById("email").value = data["email"];
    document.getElementById("address").value = data["address"];
    document.getElementById("city").value = data["city"];
    document.getElementById("country").value = data["country"];
    document.getElementById("birthdate").value = data["birthdate"];
    document.getElementById("height").value = data["height"];
    document.getElementById("gender").value = data["gender"];
    document.getElementById("weight").value = data["weight"];
    document.getElementById("amka").value = data["amka"];
    document.getElementById("telephone").value = data["telephone"];
    document.getElementById("bloodtype").value = data["bloodtype"];



}

function getBmi() {
    const data1 = null;

    // getDataFromUser();
    console.log(data["weight"] + " " + data["height"] + " " + age);

    const xhr1 = new XMLHttpRequest();
    xhr1.withCredentials = true;

    xhr1.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const responseData = JSON.parse(xhr1.responseText);
            console.log(responseData);
            $('#ajax_form').html("<h1>Your BMI is: " + responseData.data.bmi + "</h1>");
            $('#ajax_form').append("<h3>You are " + responseData.data.health + ". Your healthy BMI range is: " + responseData.data.healthy_bmi_range + "</h3>");
            $("#ajax_form").show();
            $("#ajax_update").hide();
        }
    });

    xhr1.open("GET", "https://fitness-calculator.p.rapidapi.com/bmi?age=" + age + "&weight=" + data["weight"] + "&height=" + data["height"]);
    xhr1.setRequestHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com");
    xhr1.setRequestHeader("x-rapidapi-key", "f02e0addd4msh104747e67169815p1bca4fjsn394646e1a455");

    xhr1.send(data1);
}

function getIdealWeight() {
    const data1 = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const responseData = JSON.parse(xhr.responseText);
            console.log(responseData);
            $('#ajax_form').html("<h1>Your Ideal Weight according to Devine's Formula is: " + responseData.data.Devine + "</h1>");
            $("#ajax_form").show();
            $("#ajax_update").hide();
        }
    });

    xhr.open("GET", "https://fitness-calculator.p.rapidapi.com/idealweight?gender=" + data["gender"].toString().toLowerCase() + "&height=" + data["height"]);
    xhr.setRequestHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "f02e0addd4msh104747e67169815p1bca4fjsn394646e1a455");

    xhr.send(data1);
}

function getCertifiedDoctors() {

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            $('#ajax_update').hide();
            $('#ajax_form').html("<h1>Certified Doctors</h1>");
            for (let doctor in responseData) {
                var json = {};
                for (let x in responseData[doctor]) {

                    if (x == 'firstname' || x == 'lastname' || x == 'address' || x == 'city' || x == 'doctor_info' || x == 'specialty' || x == 'telephone') {
                        json[x] = responseData[doctor][x];
                    }
                }
                console.log(json);
                $('#ajax_form').append(createTableFromJSON(json));
                $('#ajax_form').show();
            }
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };

    xhr.open('GET', 'getDoctors');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

var doc_lat = [];
var doc_lon = [];
var doc_names = [];

function getAllDoctors() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            $('#ajax_update').hide();
            $('#ajax_form').html("<h1>Doctors</h1> <button id='show_map' class='btn btn-dark' onclick='showMap()'>Show on map</button><br><div id='Map_doc' style='display: none; height:600px; width:100%; border:1px solid'></div>");

            for (let doctor in responseData) {
                var json = {};
                for (let x in responseData[doctor]) {

                    if (x == 'lat') {
                        doc_lat.push(responseData[doctor][x]);
                    }

                    if (x == 'lon') {
                        doc_lon.push(responseData[doctor][x]);
                    }

                    if (x == 'lastname') {
                        doc_names.push(responseData[doctor][x]);
                    }

                    if (x == 'firstname' || x == 'lastname' || x == 'address' || x == 'city' || x == 'doctor_info' || x == 'specialty' || x == 'telephone') {
                        json[x] = responseData[doctor][x];
                    }
                }
                console.log(json);
                $('#ajax_form').append(createTableFromJSON(json));
                $('#ajax_form').show();
            }
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };

    xhr.open('GET', 'getAllDoctors');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

/* Function that shows the map. */
function showMap() {
    var position;

    document.getElementById("Map_doc").style.display = "block";
    document.getElementById("show_map").disabled = true;
    
    map = new OpenLayers.Map("Map_doc");
    mapnik = new OpenLayers.Layer.OSM();
    map.addLayer(mapnik);

    markers = new OpenLayers.Layer.Markers("Markers");
    map.addLayer(markers);

    for (var i = 0; i < doc_lat.length; i++) {
        position = setPosition(doc_lat[i], doc_lon[i]);

        mar = new OpenLayers.Marker(position);
        markers.addMarker(mar);

        mar.events.register('mousedown', mar, function (evt) {
            handler(position, doc_names[i]);
        });
        
    }
    
    //Orismos zoom
    const zoom = 11;
    map.setCenter(position, zoom);
}


/* Functions that returns the position. */
function setPosition(lat, lon) {
    var fromProjection = new OpenLayers.Projection("EPSG:4326"); // Transform from WGS 1984
    var toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
    var position = new OpenLayers.LonLat(lon, lat).transform(fromProjection,
            toProjection);
    return position;
}

/* When a marker is clicked */
function handler(position, message) {
    var popup = new OpenLayers.Popup.FramedCloud("Popup",
            position, null,
            message, null,
            true // <-- true if we want a close (X) button, false otherwise
            );
    map.addPopup(popup);
}



function username_check() {

    var username = document.getElementById('username').value;
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('username_exists').innerHTML = "";
        } else if (xhr.status !== 200) {
            document.getElementById('username_exists').innerHTML = "<h3>username '" + username + "' already exists!<br></h3>";
        }
    };


    var jsonData = {};
    jsonData['username'] = username;
    var lol = JSON.stringify(jsonData);

    xhr.open('POST', 'username_check');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(lol);

}


function email_check() {

    var email = document.getElementById('email').value;
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('email_exists').innerHTML = "";
        } else if (xhr.status !== 200) {
            document.getElementById('email_exists').innerHTML = "<h3>email '" + email + "' already exists!<br></h3>";
        }
    };

    var jsonData = {};
    jsonData['email'] = email;
    var lol = JSON.stringify(jsonData);
    console.log(lol);

    xhr.open('POST', 'email_check');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(lol);
}

function amka_check() {
    var amka = document.getElementById('amka').value;
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('erroramka').innerHTML = "";
        } else if (xhr.status !== 200) {
            document.getElementById('erroramka').innerHTML = "<h3>AMKA '" + amka + "' already exists!</h3>";
        }
    };

    var jsonData = {};
    jsonData['amka'] = amka;
    var lol = JSON.stringify(jsonData);
    console.log(lol);

    xhr.open('POST', 'amka_check');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(lol);
}