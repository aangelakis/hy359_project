function bdayAMKA() {
    var bday = document.getElementById("bday").value;
    var amka = bday.charAt(8) + bday.charAt(9) + bday.charAt(5) + bday.charAt(6) + bday.charAt(2) + bday.charAt(3);
    document.getElementById("amka").value = amka;
}

function checkbdayAMKA() {
    var bday = document.getElementById("bday").value;
    var amka = bday.charAt(8) + bday.charAt(9) + bday.charAt(5) + bday.charAt(6) + bday.charAt(2) + bday.charAt(3);
    var tmp = document.getElementById("amka").value;
    var newamka = tmp.charAt(0) + tmp.charAt(1) + tmp.charAt(2) + tmp.charAt(3) + tmp.charAt(4) + tmp.charAt(5);
    if (amka !== newamka) {
        document.getElementById("erroramka").innerHTML = "Your AMKA does not match with your birth date."
        document.getElementById("submit").disabled = true;

    } else {
        document.getElementById("erroramka").innerHTML = ""
        document.getElementById("submit").disabled = false;
    }

}


function checkAMKA() {
    var amka = document.getElementById("amka").value;

    if (amka.length != 11) {
        console.log(amka.length);
        document.getElementById("erroramka").innerHTML = "Please put a valid AMKA.";
        document.getElementById("sumbit").disabled = true;
    }
}

function show_login() {
    $("#ajax_form").load("login.html");
    $("#ajax_form").show();
}

function  show_register() {
    $("#ajax_form").load("registration.html");
    $("#ajax_form").show();
}

function passWeakness1() {
    var password = document.getElementById("pwd").value;
    var numbers;
    var count = 0;
    var arrayOfCounts = new Map();
    let charOccurence = 0;
    let uniqueOccurence = 0;

    /* mapping the letters with their occurences. */
    for (let i = 0; i < password.length; i++) {
        letter = password.charAt(i);
        count = 0;
        for (let j = 0; j < password.length; j++) {
            if (password.charAt(j) === letter) {
                count++;
            }
        }
        arrayOfCounts.set(letter, count);
    }

    if (password.length >= 8) {
        document.getElementById("progressbars").style.display = "flex";
        numbers = 0;

        /* If half or more of the password are numbers. */
        for (var i = 0; i < password.length; i++) {
            if (!isNaN(password.charAt(i))) {
                numbers++;
                //console.log(numbers);
            }
        }

        /* Weak if a characters occurs 50% or more in the password. */
        for (let [key, value] of arrayOfCounts) {
            if (value >= password.length / 2) {
                charOccurence = 1;
            }
        }

        /* If the unique chars are 80% or more of the pass. */
        if (arrayOfCounts.size >= ((80 / 100) * password.length)) {
            uniqueOccurence = 1;
        }

    } else {
        initProgressBars();
    }

    if (numbers >= password.length / 2) {
        document.getElementById("nonepwd").style.display = "none";
        document.getElementById("weakpwd").style.display = "flex";
        document.getElementById("mediumpwd").style.display = "none";
        document.getElementById("strongpwd").style.display = "none";
        console.log("first");
    } else if (charOccurence === 1) {
        document.getElementById("nonepwd").style.display = "none";
        document.getElementById("weakpwd").style.display = "flex";
        document.getElementById("mediumpwd").style.display = "none";
        document.getElementById("strongpwd").style.display = "none";
        console.log("second");
    } else if (uniqueOccurence === 1) {
        document.getElementById("strongpwd").style.display = "flex";
        document.getElementById("weakpwd").style.display = "none";
        document.getElementById("mediumpwd").style.display = "none";
        document.getElementById("nonepwd").style.display = "none";
        console.log("third");
    } else if (password.length >= 8) {
        document.getElementById("strongpwd").style.display = "none";
        document.getElementById("weakpwd").style.display = "none";
        document.getElementById("mediumpwd").style.display = "flex";
        document.getElementById("nonepwd").style.display = "none";
        console.log("fourth");
    }
}



function checkTerms() {

    var agreements = document.getElementById("agreement").checked
    var passver = document
    if (agreements === false) {
        document.getElementById("terms").innerHTML = "Please accept the terms of use in order to continue!";
        document.getElementById("submit").disabled = true;
    } else if (agreements === true && !passVerify()) {
        document.getElementById("terms").innerHTML = "";
        document.getElementById("submit").disabled = true;
    } else if (agreements === true && passVerify()) {
        document.getElementById("submit").disabled = false;
        document.getElementById("terms").innerHTML = "";
    } else {
        document.getElementById("terms").innerHTML = "";
        document.getElementById("submit").disabled = false;
    }
}

// Initialize the progress bars so they are not displaying. 
function initProgressBars() {
    document.getElementById("nonepwd").style.display = "flex";
    document.getElementById("weakpwd").style.display = "none";
    document.getElementById("mediumpwd").style.display = "none";
    document.getElementById("strongpwd").style.display = "none";
}

// If user is a doctor then change some things on the form. 
function userDoc() {
    var doctor = document.getElementById("typeDoc").checked;
    var specialty = document.getElementById("specialization");
    var docTextArea = document.getElementById("doctext");
    if (doctor === true) {
        document.getElementById("addrLabel").innerHTML = "Office's Address:*";
        specialty.style.display = "block";
        docTextArea.style.display = "block";
        document.getElementById("internist").required = true;
    } else {
        document.getElementById("addrLabel").innerHTML = "Address:*";
        specialty.style.display = "none";
        docTextArea.style.display = "none";
        document.getElementById("internist").required = false;
    }
}


// Checking the weakness of the passwords. 
function passWeakness() {
    var password = document.getElementById("pwd").value;
    var numbers;
    var count = 0;
    var arrayOfCounts = new Map();
    let charOccurence = 0;
    let uniqueOccurence = 0;

    /* mapping the letters with their occurences. */
    for (let i = 0; i < password.length; i++) {
        letter = password.charAt(i);
        count = 0;
        for (let j = 0; j < password.length; j++) {
            if (password.charAt(j) === letter) {
                count++;
            }
        }
        arrayOfCounts.set(letter, count);
    }

    if (password.length >= 8) {
        document.getElementById("progressbars").style.display = "flex";
        numbers = 0;

        /* If half or more of the password are numbers. */
        for (var i = 0; i < password.length; i++) {
            if (!isNaN(password.charAt(i))) {
                numbers++;
                //console.log(numbers);
            }
        }

        /* Weak if a characters occurs 50% or more in the password. */
        for (let [key, value] of arrayOfCounts) {
            if (value >= password.length / 2) {
                charOccurence = 1;
            }
        }

        /* If the unique chars are 80% or more of the pass. */
        if (arrayOfCounts.size >= ((80 / 100) * password.length)) {
            uniqueOccurence = 1;
        }

    } else {
        initProgressBars();
    }

    if (numbers >= password.length / 2) {
        document.getElementById("nonepwd").style.display = "none";
        document.getElementById("weakpwd").style.display = "flex";
        document.getElementById("mediumpwd").style.display = "none";
        document.getElementById("strongpwd").style.display = "none";
        console.log("first");
    } else if (charOccurence === 1) {
        document.getElementById("nonepwd").style.display = "none";
        document.getElementById("weakpwd").style.display = "flex";
        document.getElementById("mediumpwd").style.display = "none";
        document.getElementById("strongpwd").style.display = "none";
        console.log("second");
    } else if (uniqueOccurence === 1) {
        document.getElementById("strongpwd").style.display = "flex";
        document.getElementById("weakpwd").style.display = "none";
        document.getElementById("mediumpwd").style.display = "none";
        document.getElementById("nonepwd").style.display = "none";
        console.log("third");
    } else if (password.length >= 8) {
        document.getElementById("strongpwd").style.display = "none";
        document.getElementById("weakpwd").style.display = "none";
        document.getElementById("mediumpwd").style.display = "flex";
        document.getElementById("nonepwd").style.display = "none";
        console.log("fourth");
    }
}



/* Verifying the passwords. */
function passVerify() {
    var password = document.getElementById("pwd").value;
    var passwordver = document.getElementById("pwdver").value;
    if (password.length !== 0) {
        console.log(password.length);
        if (password != passwordver) {
            document.getElementById("passErr").innerHTML = "Passwords do not match!";
            //document.getElementById("submit").disabled = true;
        } else {
            document.getElementById("passErr").innerHTML = "";
            if (document.getElementById("agreement").checked) {
                document.getElementById("submit").disabled = false;
            }
            return true;
        }
    }
    return false;
}


/* Verifying the passwords. */
function passVerify1() {
    var password = document.getElementById("pwd").value;
    var passwordver = document.getElementById("pwdver").value;
    if (password.length !== 0) {
        console.log(password.length);
        if (password != passwordver) {
            document.getElementById("passErr").innerHTML = "Passwords do not match!";
            //document.getElementById("submit").disabled = true;
        } else {
            document.getElementById("passErr").innerHTML = "";
            return true;
        }
    }
    return false;
}


/* Function for showing the password */
function showPass() {
    if ((document.getElementById("pwd")).type == "password") {
        document.getElementById("pwd").type = "text";
        document.getElementById("showpass").className = "btn btn-success"
    } else {
        document.getElementById("pwd").type = "password";
        document.getElementById("showpass").className = "btn btn-danger"
    }
}

/* Function for showing the verification password */
function showPassVer() {
    if ((document.getElementById("pwdver")).type == "password") {
        document.getElementById("pwdver").type = "text";
        document.getElementById("showpassver").className = "btn btn-success"
    } else {
        document.getElementById("pwdver").type = "password";
        document.getElementById("showpassver").className = "btn btn-danger"
    }
}


var lat;
var lon;
var mapShown = 0;

/* AJAX code for API. */
function loadDoc() {
    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const obj = JSON.parse(xhr.responseText);
            console.log(xhr.responseText);
            if (obj[0] === undefined) {
                document.getElementById("addrError").innerHTML = "Could not find your address.";
                document.getElementById("crete").innerHTML = " ";
                document.getElementById("map").disabled = true;

            } else {
                var text = obj[0].display_name;
                lat = obj[0].lat; // Storing lat
                lon = obj[0].lon; // Storing lon
                console.log(text);
                console.log(obj[0].lat);
                if (includesCrete(text)) {
                    document.getElementById("addrError").innerHTML = "";
                    document.getElementById("map").disabled = false;
                }


            }
        }
    });

    var addressName = document.getElementById("address").value;
    var city = document.getElementById("city").value;
    var country = document.getElementById("country").value;
    var address = addressName + " " + city + " " + country;
    console.log(address);


    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q=" + address + "&format=json&accept-language=en&polygon_threshold=0.0");
    xhr.setRequestHeader("x-rapidapi-host", "forward-reverse-geocoding.p.rapidapi.com", );
    xhr.setRequestHeader("x-rapidapi-key", "f02e0addd4msh104747e67169815p1bca4fjsn394646e1a455");

    xhr.send(data);
}


/* If we already have typed one address. */
function addrChanged() {
    if (mapShown === 1) {
        document.getElementById("Map").style.display = "none";
        document.getElementById("Map").removeChild(document.getElementById("Map").firstChild);
        document.getElementById("map").disabled = true;
    }
}


/* Function that changes a div if the address is in crete. */
function includesCrete(text) {
    if (text.includes("Crete")) {
        document.getElementById("crete").innerHTML = " ";
        return 1;
    } else {
        document.getElementById("crete").innerHTML = "The service is only available in Crete at the moment.";
    }
    return 0;
}



/* Function that shows the map. */
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


/* Check if geolocation is supported by the user's browser. */
function checkGeolocation() {

    if (!navigator.geolocation) {
        document.getElementById("errorgeo").innerHTML = "Geolocation is not supported by this browser.";
    } else {
        document.getElementById("errorgeo").innerHTML = "";
    }
}

/* Finds the current location of the user. */
function autocompleteGeolocation() {
    navigator.geolocation.getCurrentPosition(autocompletelocation);
}

/* Autocompletes the address of the connected user. */
function autocompletelocation(pos) {
    console.log('Your current position is:');
    lat = pos.coords.latitude;
    lon = pos.coords.longitude;
    console.log(`More or less ${pos.coords.accuracy} meters.`);

    const data = null;

    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            console.log(xhr.responseText);
            const obj = JSON.parse(xhr.responseText);
            console.log(this.responseText);

            if (obj === undefined) {
                document.getElementById("addrError").innerHTML = "Could not find your address.";
                document.getElementById("crete").innerHTML = " ";
                document.getElementById("map").disabled = true;
            } else {
                addrChanged();
                console.log(obj);
                const road = obj.address.road;
                const number = obj.address.house_number;
                const country = obj.address.country;
                const state = obj.address.state;
                var city = obj.address.city
                var town = obj.address.town;

                if (city !== undefined) {
                    city.split(" ")[0];
                    document.getElementById("city").value = city;
                }

                if (town !== undefined) {
                    town.split("")[0]
                    document.getElementById("city").value = town;
                }

                if (state.includes("Crete")) {
                    document.getElementById("crete").innerHTML = "";
                    document.getElementById("map").disabled = false;
                }

                if (number === undefined) {
                    document.getElementById("address").value = road;
                } else {
                    document.getElementById("address").value = road + " " + number;
                }
                document.getElementById("country").value = country;
            }
        }
    });

    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/reverse?lat=" + lat + "&lon=" + lon + "&accept-language=en&format=json&polygon_threshold=0.0");
    xhr.setRequestHeader("x-rapidapi-host", "forward-reverse-geocoding.p.rapidapi.com", );
    xhr.setRequestHeader("x-rapidapi-key", "f02e0addd4msh104747e67169815p1bca4fjsn394646e1a455", );

    xhr.send(data);
}

function RegisterPOST() {
    let myForm = document.getElementById('form1');
    let formData = new FormData(myForm);
    formData.append('lat', lat);
    formData.append('lon', lon);
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(xhr.responseText);
            /* Put the following line in comment for ex3 */
            const responseData = JSON.parse(xhr.responseText);
            
            /* Remove the following line from comment for ex3 */
            //document.getElementById("register").innerHTML = xhr.responseText + "<br>";
            
            if (JSONdata.includes("User")) {
                document.getElementById('register').innerHTML += "Registration successfully completed!<br>";

            } else {
                document.getElementById('register').innerHTML += "Registration successfully completed, but you need to be certified by the administrator!<br>";
            }
        } else if (xhr.status !== 200) {
            document.getElementById('register').innerHTML = 'Registration failed. Returned status of ' + xhr.status + "<br>";
        }
    };

    const data = {};
    formData.forEach((value, key) => (data[key] = value));

    var JSONdata = JSON.stringify(data);

    if (JSONdata.includes("User")) {
        xhr.open('POST', 'Register_Simple_User');
    } else {
        xhr.open('POST', 'Register_Doctor');
    }
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSONdata);
}


function UpdateForm() {

    let myForm = document.getElementById('form2');
    let formData = new FormData(myForm);
    formData.append('lat', data["lat"]);
    formData.append('lon', data["lon"]);

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(xhr.responseText);
            //const responseData = JSON.parse(xhr.responseText);
            document.getElementById('register').innerHTML = "Update successfully completed!<br>In order to see your new data you have to logout and login again.";
        } else if (xhr.status !== 200) {
            document.getElementById('register').innerHTML = 'Update failed. Returned status of ' + xhr.status + "<br>";
        }
    };

    const data2 = {};
    formData.forEach((value, key) => (data2[key] = value));

    var JSONdata = JSON.stringify(data2);
    alert(JSONdata);

    xhr.open('POST', 'Update_Simple_User');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSONdata);

}



/* Fixing the scroll error. */
const eventListenerOptionsSupported = () => {
    let supported = false;

    try {
        const opts = Object.defineProperty({}, 'passive', {
            get() {
                supported = true;
            }
        });

        window.addEventListener('test', null, opts);
        window.removeEventListener('test', null, opts);
    } catch (e) {
    }

    return supported;
}

const defaultOptions = {
    passive: false,
    capture: false
};
const supportedPassiveTypes = [
    'scroll', 'wheel',
    'touchstart', 'touchmove', 'touchenter', 'touchend', 'touchleave',
    'mouseout', 'mouseleave', 'mouseup', 'mousedown', 'mousemove', 'mouseenter', 'mousewheel', 'mouseover'
];
const getDefaultPassiveOption = (passive, eventName) => {
    if (passive !== undefined)
        return passive;

    return supportedPassiveTypes.indexOf(eventName) === -1 ? false : defaultOptions.passive;
};

const getWritableOptions = (options) => {
    const passiveDescriptor = Object.getOwnPropertyDescriptor(options, 'passive');

    return passiveDescriptor && passiveDescriptor.writable !== true && passiveDescriptor.set === undefined ?
            Object.assign({}, options) :
            options;
};

const overwriteAddEvent = (superMethod) => {
    EventTarget.prototype.addEventListener = function (type, listener, options) {
        const usesListenerOptions = typeof options === 'object' && options !== null;
        const useCapture = usesListenerOptions ? options.capture : options;

        options = usesListenerOptions ? getWritableOptions(options) : {};
        options.passive = getDefaultPassiveOption(options.passive, type);
        options.capture = useCapture === undefined ? defaultOptions.capture : useCapture;

        superMethod.call(this, type, listener, options);
    };

    EventTarget.prototype.addEventListener._original = superMethod;
};

const supportsPassive = eventListenerOptionsSupported();

if (supportsPassive) {
    const addEvent = EventTarget.prototype.addEventListener;
    overwriteAddEvent(addEvent);
}