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

var randevouzID_button;

function loginPOST() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(xhr.responseText);
            if (xhr.responseText === "user") {
                $("#login").load("choices1.html");
            } else if (xhr.responseText === "doc") {
                $("#login").load("choicesDoctor.html");
            } else {
                $("#login").load("choicesAdmin.html");
            }
            $("#ajax_update").load("update.html");
            $("#ajax_update").hide();
            $("#ajax_form").html("<h1>Successful Login</h1><br>");

        } else if (xhr.status !== 200) {
            console.log(xhr.responseText);
            if (xhr.responseText === "user") {
                $("#login_error").html("Wrong Credentials");
            } else if (xhr.responseText === "not certified") {
                $("#login_error").html("Not Certified");
            } else {
                $("#login_error").html("Wrong Credentials");
            }
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
            if (responseData.username === "admin") {
                $("#login").load("choicesAdmin.html");
            } else if (responseData.doctor_id === undefined) {
                $("#login").load("choices1.html");
            } else {
                $("#login").load("choicesDoctor.html");
            }
            $("#ajax_update").load("update.html");
            $("#ajax_update").hide();
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
    html += "<hr size=" + "8" + "width=" + "90%" + "color=" + "red>";
    return html;
}


function createTableFromJSONCertify(data) {
    var html = "<table class=" + "table table-dark" + "><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    html += "<button class='btn btn-dark' id='" + data['doctor_id'] + "' onclick='certifyDoctor(" + data['doctor_id'] + ")'> Certify Doctor " + data['lastname'] + "</button>";
    html += "<div id='" + data['doctor_id'] + "'></div>";
    html += "<hr size=" + "8" + "width=" + "90%" + "color=" + "red>";
    return html;
}

function createTableFromJSONDeleteUser(data) {
    var html = "<table class=" + "table table-dark" + "><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    html += "<button class='btn btn-dark' id='" + data['user_id'] + "' onclick='deleteUser(" + data['user_id'] + ")'> Delete user " + data['username'] + "</button>";

    html += "<div id='" + data['user_id'] + "'></div>";
    html += "<hr size=" + "8" + "width=" + "90%" + "color=" + "red>";
    return html;
}

function createTableFromJSONDeleteDoctor(data) {
    var html = "<table class=" + "table table-dark" + "><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    html += "<button class='btn btn-dark' id='" + data['doctor_id'] + "' onclick='deleteDoctor(" + data['doctor_id'] + ")'> Delete doctor " + data['lastname'] + "</button>";

    html += "<div id='" + data['doctor_id'] + "'></div>";
    html += "<hr size=" + "8" + "width=" + "90%" + "color=" + "red>";
    return html;
}


var compareList = "[";

function createTableFromJSONCompareBloodTests(data) {
    // compareList = "[";
    var html = "<table class=" + "table table-dark" + "><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    html += "<button style='margin-left:5px' class='btn btn-dark' id='" + data['bloodtest_id'] + "' onclick='addToCompareList(" + data['bloodtest_id'] + ")'> Add bloodtest to compare list." + "</button>";
    html += "<button style='margin-left:5px' class='btn btn-dark' id='" + data['bloodtest_id'] + "_rem' onclick='removeFromCompareList(" + data['bloodtest_id'] + ")'> Remove bloodtest from compare list." + "</button>";
    html += "<button style='margin-left:5px' class='btn btn-dark' id='" + data['bloodtest_id'] + "_treatment' onclick='showTreatments(" + data['bloodtest_id'] + ")'> Show treatments for this bloodtest." + "</button>";

    html += "<div id='" + data['bloodtest_id'] + "_div'></div>";
    html += "<hr size=" + "8" + "width=" + "90%" + "color=" + "red>";
    return html;
}


function createTableFromJSONCompareList(data, count) {
    console.log(count);
    var html = "<table style='table-layout: fixed; width: 100%' class=" + "table table-dark" + "><tr><th>Category</th>";
    for (var i = 0; i < count; i++) {
        html += "<th>BloodTest " + (i + 1) + "</th>";
    }
    html += "</tr>";
    var j = 0;
    for (const x in data[j]) {
        html += "<tr>";
        flag = 0;
        for (k = j; k < count; k++) {
            var category = x;
            var value = data[k][x];
            if (flag === 0) {
                flag = 1;
                html += "<td>" + category + "</td><td>" + value + "</td>";
            } else {
                html += "<td>" + value + "</td>";
            }
        }
        html += "</tr>";
    }
    html += "</table>";
    html += "<hr size=" + "8" + "width=" + "90%" + "color=" + "red>";
    return html;
}

function createTableFromJSONCancelRandevouz(data) {
    var html = "<table class=" + "table table-dark" + "><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";

    html += "<button class='btn btn-dark' id='" + data['randevouz_id'] + "' onclick='cancelRandevouz(" + data['randevouz_id'] + ")'> Cancel Randevouz " + "</button>";

    html += "<hr size=" + "8" + "width=" + "90%" + "color=" + "red>";
    return html;
}

function createTableFromJSONUpdateRandevouz(data) {
    var html = "<table class=" + "table table-dark" + "><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";

    html += "<button class='btn btn-dark' id='" + data['randevouz_id'] + "' onclick='showUpdateRandevouzForm(this.id)'> Update Randevouz " + "</button>";

    html += "<hr size=" + "8" + "width=" + "90%" + "color=" + "red>";
    return html;
}


function compareFunctions() {
    $('#ajax_form').html("<h1>Please select how you want to compare your bloodtests</h1><br>");

    var html = "<button style='margin-left:5px' class='btn btn-dark' id='table_btn' onclick='getCompareList()'>Table</button>";
    html += "<button style='margin-left:5px' class='btn btn-dark' id='charts_btn' onclick='getCharts()'>Charts</button>";
    $("#ajax_form").show();
    $("#ajax_update").hide();
    $("#ajax_form").append(html);

}


function getCharts() {

    if (compareList !== "" && compareList !== "[") {
        JSONCompareList = compareList.slice(0, -1);
        JSONCompareList += "]";
        JSONCompareList = JSON.parse(JSONCompareList);
        console.log(JSONCompareList);
        count = JSONCompareList.length;

        $('#ajax_form').html("<h1>Please select how you want to compare your bloodtests</h1><br>");

        var html = "<button style='margin-left:5px' class='btn btn-dark' id='table_btn' onclick='getCompareList()'>Table</button>";
        html += "<button style='margin-left:5px' class='btn btn-dark' id='charts_btn' onclick='getCharts()'>Charts</button>";
        $("#ajax_form").append(html);


        $('#ajax_form').append("<h1>Charts</h1>");
        $("#ajax_form").show();
        $("#ajax_update").hide();
        $('#table_btn').attr('disabled', false);
        $('#charts_btn').attr('disabled', true);

        for (var i = 0; i < count; i++) {
            $("#ajax_form").append("<div id='piechart" + i + "'</div>");
            drawChart(i, JSONCompareList);
        }
    }
}

function drawChart(i, JSONCompareList) {

    var data = google.visualization.arrayToDataTable([
        ["den xero", "ayto prepei na einai string gia kapoion logo"],
        ["Vitamin D3", JSONCompareList[i]['vitamin_d3']],
        ["Vitamin B12", JSONCompareList[i]['vitamin_b12']],
        ["Cholesterol", JSONCompareList[i]['cholesterol']],
        ["Blood Sugar", JSONCompareList[i]['blood_sugar']],
        ["Iron", JSONCompareList[i]['iron']],
    ]);

    var options = {
        title: "BloodTest " + (i + 1) + " - TestDate: " + JSONCompareList[i]['test_date'] + " - Medical Center: " + JSONCompareList[i]['medical_center'] + " - AMKA: " + JSONCompareList[i]['amka'],
        //colors: ['#9d0000', '#CCCC00', '#0000FF'],
        is3D: false,
        width: $(window).width()*0.48,
        height: $(window).height()*0.25,
        pieSliceText: 'value'
    };

    var chart = new google.visualization.PieChart(document.getElementById('piechart' + i));

    chart.draw(data, options);
}


function addToCompareList(id) {
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        console.log(xhr.responseText);
        const responseData = JSON.parse(xhr.responseText);

        if (xhr.readyState === 4 && xhr.status === 200) {

            if (compareList.includes("\"bloodtest_id\":" + responseData['bloodtest_id'])) {
                document.getElementById(responseData['bloodtest_id']).innerHTML = "BloodTest already in compare list";
                document.getElementById(responseData['bloodtest_id']).disabled = "true";

            } else {
                if (compareList[compareList.length - 1] === "]") {
                    compareList = compareList.slice(0, -1);
                    if (compareList.length !== 1) {
                        compareList += ",";
                    }
                }
                compareList += xhr.responseText + ",";
                //console.log(JSON.parse(compareList));
                document.getElementById(responseData['bloodtest_id']).innerHTML = "Succesfully added";

            }
            console.log(compareList);
        } else {
            document.getElementById(responseData['bloodtest_id']).innerHTML = "There was an error adding to compare list";
        }
    };

    let text = {};
    text["id"] = id;
    var JSONdata = JSON.stringify(text);
    console.log(JSONdata);

    xhr.open('POST', 'getBloodTest');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSONdata);
}


function removeFromCompareList(id) {
    if (compareList !== "" && compareList !== "[") {
        JSONCompareList = compareList.slice(0, -1);
        JSONCompareList += "]";
        JSONCompareList = JSON.parse(JSONCompareList);
        var flag = 0;
        for (var i = 0; i < JSONCompareList.length; i++) {
            if (id === JSONCompareList[i]['bloodtest_id']) {
                flag = 1;
                delete JSONCompareList[i];
                //JSONCompareList = JSONCompareList.splice(i,1);
                JSONCompareList = JSONCompareList.filter(function (el) {
                    return el != null;
                });

                compareList = JSON.stringify(JSONCompareList);
                console.log(compareList);
                document.getElementById(id + "_rem").innerHTML = "Succesfully deleted";

                break;
            }
        }
        if (flag === 0) {
            document.getElementById(id + "_rem").innerHTML = "BloodTest does not exist in compare list";
        }
    } else {
        document.getElementById(id + "_rem").innerHTML = "BloodTest does not exist in compare list";
    }

}


function showTreatments(id) {
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        console.log(xhr.responseText);
        const responseData = JSON.parse(xhr.responseText);

        if (xhr.readyState === 4 && xhr.status === 200) {
            count = responseData.length;
            for (treatment in responseData) {
                document.getElementById(id + "_div").innerHTML = "<br><h3>Treatments for bloodtest_id " + id + "</h3><br>" + createTableFromJSON(responseData[treatment]);
            }
            if (count === 0) {
                document.getElementById(id + "_div").innerHTML = "<br><h3>There are no treatments for bloodtest_" + id + "</h3>";
            }
        } else {
            document.getElementById(id + "_div").innerHTML = "<br><h3>There was an error finding the treatments for bloodtest_" + id + "</h3>";
        }
    };

    let text = {};
    text["id"] = id;
    var JSONdata = JSON.stringify(text);
    console.log(JSONdata);

    xhr.open('POST', 'getTreatment');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSONdata);
}


function showDateOfRandevouz() {
    $("#ajax_update").hide();
    $("#ajax_form").load("calendar.html");
    $("#ajax_form").show();
}

function seeRandevouz() {
    let myForm = document.getElementById("calendarForm");
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    for (var key in data) {
        if (data[key] === "") {
            delete data[key];
        }
    }

    var jsonData = JSON.stringify(data);

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);

            for (id in responseData) {
                for (val in responseData[id]) {
                    console.log(responseData[id][val]);
                    if (responseData[id][val] === "null") {
                        delete responseData[id][val];
                    }
                }
            }

            $("#ajax_form").html("");
            for (let randevouz in responseData) {
                var json = {};
                for (let x in responseData[randevouz]) {
                    json[x] = responseData[randevouz][x];
                }

                $("#ajax_form").append("<br>");
                $('#ajax_form').append(createTableFromJSON(json));
                $('#ajax_form').show();
            }

            if (responseData.length === 0) {
                console.log("its null");
                $("#ajax_form").html("No Randevouz found");
            }
        } else if (xhr.status !== 200) {
            alert(xhr.responseText);

        }
    };
    console.log(jsonData);
    xhr.open("POST", "seeRandevouz");
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

function removeFromCompareList(id) {
    if (compareList !== "" && compareList !== "[") {
        JSONCompareList = compareList.slice(0, -1);
        JSONCompareList += "]";
        JSONCompareList = JSON.parse(JSONCompareList);
        var flag = 0;
        for (var i = 0; i < JSONCompareList.length; i++) {
            if (id === JSONCompareList[i]['bloodtest_id']) {
                flag = 1;
                delete JSONCompareList[i];
                //JSONCompareList = JSONCompareList.splice(i,1);
                JSONCompareList = JSONCompareList.filter(function (el) {
                    return el != null;
                });

                compareList = JSON.stringify(JSONCompareList);
                console.log(compareList);
                document.getElementById(id + "_rem").innerHTML = "Succesfully deleted";

                break;
            }
        }
        if (flag === 0) {
            document.getElementById(id + "_rem").innerHTML = "Bloodtest does not exist in compare list";
        }
    } else {
        document.getElementById(id + "_rem").innerHTML = "Bloodtest does not exist in compare list";
    }

}


function getCompareList() {
    if (compareList !== "" && compareList !== "[") {
        JSONCompareList = compareList.slice(0, -1);
        JSONCompareList += "]";
        JSONCompareList = JSON.parse(JSONCompareList);
        console.log(JSONCompareList);
        count = JSONCompareList.length;

        $('#ajax_form').html("<h1>Please select how you want to compare your bloodtests</h1><br>");

        var html = "<button style='margin-left:5px' class='btn btn-dark' id='table_btn' onclick='getCompareList()'>Table</button>";
        html += "<button style='margin-left:5px' class='btn btn-dark' id='charts_btn' onclick='getCharts()'>Charts</button>";
        $("#ajax_form").append(html);

        $('#ajax_form').append("<h1>Compare List</h1>");
        $("#ajax_form").show();
        $("#ajax_update").hide();

        //for (id in JSONCompareList) {
        //delete obj[id]["bloodtest_id"];
        $('#ajax_form').append(createTableFromJSONCompareList(JSONCompareList, count));

        //}

    } else {
        $('#ajax_form').append("<h1>Your Compare List is empty</h1>");
    }
    $('#table_btn').attr('disabled', true);
    $('#charts_btn').attr('disabled', false);

}


function deleteDoctor(id) {
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        console.log(xhr.responseText);
        const responseData = JSON.parse(xhr.responseText);

        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById(responseData['doctor_id']).disabled = "true";
            document.getElementById(responseData['doctor_id']).innerHTML = "Doctor " + responseData['lastname'] + " successfully deleted";
        } else {
            document.getElementById(responseData['doctor_id']).innerHTML = "There was an error deleting doctor " + responseData['lastname'];
        }
    };

    let text = {};
    text["id"] = id;
    var JSONdata = JSON.stringify(text);
    console.log(JSONdata);

    xhr.open('POST', 'DeleteDoctors');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSONdata);
}


function deleteUser(id) {
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        console.log(xhr.responseText);
        const responseData = JSON.parse(xhr.responseText);

        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById(responseData['user_id']).disabled = "true";
            document.getElementById(responseData['user_id']).innerHTML = "User " + responseData['username'] + " successfully deleted";
        } else {
            document.getElementById(responseData['user_id']).innerHTML = "There was an error deleting user " + responseData['username'];
        }
    };

    let text = {};
    text["id"] = id;
    var JSONdata = JSON.stringify(text);
    console.log(JSONdata);

    xhr.open('POST', 'DeleteUsers');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSONdata);
}


function certifyDoctor(id) {
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        console.log(xhr.responseText);
        const responseData = JSON.parse(xhr.responseText);

        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById(responseData['doctor_id']).disabled = "true";
            document.getElementById(responseData['doctor_id']).innerHTML = "Doctor " + responseData['lastname'] + " successfully certified";
        } else {
            document.getElementById(responseData['doctor_id']).innerHTML = "There was an error certifying doctor " + responseData['lastname'];
        }
    };

    let text = {};
    text["id"] = id;
    var JSONdata = JSON.stringify(text);
    console.log(JSONdata);

    xhr.open('POST', 'CertifyDoctors');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSONdata);
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

function getDoctorDataRequest() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            data = responseData;

            delete responseData["user_id"];
            $('#ajax_form').html("<h1>Your Data</h1>");
            $('#ajax_form').append(createTableFromJSON(responseData));
            $("#ajax_form").show();
            $("#ajax_update").hide();
            document.getElementById("change").disabled = false;

        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };

    xhr.open('GET', 'Login');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function randevouzManager() {
    $("#ajax_update").hide();
    $("#ajax_form").load("RandevouzManager.html");
    $("#ajax_form").show();
}

function showRandevouzForm() {
    $("#ajax_update").hide();
    $("#ajax_form").load("RandevouzForm.html");
    // $("#ajax_form").show();
}

function addRandevouz() {
    let myForm = document.getElementById("randevouzForm");
    let formData = new FormData(myForm);
    const data = {};
    formData.append("status", "free");
    formData.forEach((value, key) => (data[key] = value));
    for (var key in data) {
        if (data[key] === "") {
            delete data[key];
        }
    }

    var jsonData = JSON.stringify(data);
    console.log(jsonData);

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#ajax_form").load("RandevouzManager.html");
            console.log(xhr.responseText);
        } else if (xhr.status !== 200) {
            alert(xhr.responseText);
            console.log("oops gamoto");
        }
    };
    xhr.open("POST", "addRandevouz");
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

function cancelRandevouz(id) {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#ajax_form").load("RandevouzManager.html");
        } else if (xhr.status !== 200) {

        }
    };

    let text = {};
    text["id"] = id;
    var JSONdata = JSON.stringify(text);
    console.log(JSONdata);

    xhr.open("POST", "cancelRandevouz");
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSONdata);
}

function updateRandevouz() {
    let myForm = document.getElementById('randevouzFormUpdate');
    let formData = new FormData(myForm);
    formData.append("randevouz_id", randevouzID_button);
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    for (var key in data) {
        if (data[key] === "") {
            delete data[key];
        }
    }

    var jsonData = JSON.stringify(data);
    console.log(jsonData);

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#ajax_form").load("RandevouzManager.html");
        } else if (xhr.status !== 200) {
            alert(xhr.responseText);
        }
    };
    xhr.open('POST', 'updateRandevouz');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

function showUpdateRandevouzForm(id) {
    $("#ajax_form").load("RandevouzFormUpdate.html");
    randevouzID_button = id;
}

function getAllRandevouzCancel() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            $('#ajax_update').hide();
            $('#ajax_form').html("<h1>Randevouz</h1>");

            for (id in responseData) {
                for (val in responseData[id]) {
                    console.log(responseData[id][val]);
                    if (responseData[id][val] === "null") {
                        delete responseData[id][val];
                    }
                }
            }

            for (let randevouz in responseData) {
                var json = {};
                for (let x in responseData[randevouz]) {
                    json[x] = responseData[randevouz][x];
                }
                console.log(json);
                $('#ajax_form').append(createTableFromJSONCancelRandevouz(json));
                $('#ajax_form').show();
            }
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };

    xhr.open('GET', 'getAllRandevouz');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function getAllRandevouzUpdate() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            console.log(responseData[0].randevouz_id);
            $('#ajax_update').hide();
            $('#ajax_form').html("<h1>Randevouz</h1>");

            for (id in responseData) {
                for (val in responseData[id]) {
                    console.log(responseData[id][val]);
                    if (responseData[id][val] === "null") {
                        delete responseData[id][val];
                    }
                }
            }

            for (let randevouz in responseData) {
                var json = {};
                for (let x in responseData[randevouz]) {
                    json[x] = responseData[randevouz][x];
                }
                console.log(json);
                $('#ajax_form').append(createTableFromJSONUpdateRandevouz(json));
                $('#ajax_form').show();
            }
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };

    xhr.open('GET', 'getAllRandevouz');
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

function showBloodTestForm() {
    $("#ajax_update").hide();
    $("#ajax_form").load("BloodTestForm.html");
    $("ajax_form").show();
}

function addBloodTest() {
    let myForm = document.getElementById('bloodtestForm');
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    for (var key in data) {
        if (data[key] == "") {
            delete data[key];
        }
    }

    var jsonData = JSON.stringify(data);
    console.log(jsonData);

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('msg').innerHTML = JSON.stringify(xhr.responseText);

        } else if (xhr.status !== 200) {
            document.getElementById('msg')
                    .innerHTML = 'Request failed. Returned status of ' + xhr.status + "<br>" +
                    JSON.stringify(xhr.responseText);

        }
    };
    xhr.open('POST', 'addBloodTest');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

function getBloodTestAMKA() {
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            const obj = JSON.parse(xhr.responseText);
            console.log(obj);
            var i = 1;
            $("#ajax_form").show();
            $("#ajax_update").hide();
            var count = Object.keys(obj).length;
            $('#ajax_form').html("<h3>" + count + " BloodTests</h3>");
            for (id in obj) {
                //delete obj[id]["bloodtest_id"];
                for (val in obj[id]) {
                    if (obj[id][val] == 0) {
                        delete obj[id][val];
                        delete obj[id][val + "_level"];
                    }
                }
                $('#ajax_form').append("<h3>Bloodtest_id " + obj[id]['bloodtest_id'] + "</h3>");
                $('#ajax_form').append(createTableFromJSONCompareBloodTests(obj[id]));
                i++;
            }


        } else if (xhr.status !== 200) {
            $('#ajax_form').html('Request failed. Returned status of ' + xhr.status + "<br>"
                    + JSON.stringify(xhr.responseText));
        }
    };

    xhr.open("GET", "addBloodTest");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}



function getTreatments() {
    const xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            const obj = JSON.parse(xhr.responseText);
            console.log(obj);
            var i = 1;
            $("#ajax_form").show();
            $("#ajax_update").hide();
            var count = Object.keys(obj).length;
            $('#ajax_form').html("<h3>" + count + " Treatments</h3>");
            for (id in obj) {
                $('#ajax_form').append(createTableFromJSON(obj[id]));
            }

        } else if (xhr.status !== 200) {
            $('#ajax_form').html('Request failed. Returned status of ' + xhr.status + "<br>"
                    + JSON.stringify(xhr.responseText));
        }
    };

    xhr.open("GET", "getTreatment");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
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

function getUncertifiedDoctors() {

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            $('#ajax_update').hide();
            $('#ajax_form').html("<h1>Uncertified Doctors</h1>");
            for (let doctor in responseData) {
                var json = {};
                for (let x in responseData[doctor]) {
                    json[x] = responseData[doctor][x];
                }
                console.log(json);
                $('#ajax_form').append(createTableFromJSONCertify(json));
                $('#ajax_form').show();
            }
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };

    xhr.open('GET', 'getUncertifiedDoctors');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function getAllUsersAdmin() {

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            $('#ajax_update').hide();
            $('#ajax_form').html("<h1>Users</h1>");
            for (let user in responseData) {
                var json = {};
                for (let x in responseData[user]) {
                    json[x] = responseData[user][x];
                }
                console.log(json);
                $('#ajax_form').append(createTableFromJSONDeleteUser(json));
                $('#ajax_form').show();
            }
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };

    xhr.open('GET', 'getAllUsers');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function getAllDoctorsAdmin() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            $('#ajax_update').hide();
            $('#ajax_form').html("<h1>Doctors</h1>");
            for (let doctor in responseData) {
                var json = {};
                for (let x in responseData[doctor]) {
                    json[x] = responseData[doctor][x];
                }
                console.log(json);
                $('#ajax_form').append(createTableFromJSONDeleteDoctor(json));
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
            $('#ajax_form').html("<h1>Doctors</h1> <button id='show_map' class='btn btn-dark' onclick='showMapFindDoctors()'>Show on map</button><br><div id='Map_doc' style='display: none; height:600px; width:100%; border:1px solid'></div>");

            for (let doctor in responseData) {
                var json = {};
                for (let x in responseData[doctor]) {

                    if (x === 'lat') {
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
/*function showMap() {
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
 }*/

function showMapFindDoctors() {
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

function showMap() {
    var position;

    document.getElementById("Map").style.display = "block";
    document.getElementById("map").disabled = true;


    map = new OpenLayers.Map("Map");
    mapnik = new OpenLayers.Layer.OSM();
    map.addLayer(mapnik);

    markers = new OpenLayers.Layer.Markers("Markers");
    map.addLayer(markers);

    mapShown = 1;

    position = setPosition(lat, lon);

    mar = new OpenLayers.Marker(position);
    markers.addMarker(mar);

    mar.events.register('mousedown', mar, function (evt) {
        handler(position, document.getElementById("address").value);
    });

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