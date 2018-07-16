# TrainDSAlgo
# index.js
const app = require('express')();
const express = require('express');
const bodyParser = require('body-parser')
const server = require('http').Server(app);
const io = require('socket.io')(server);
const request = require('request');
app.use(bodyParser.urlencoded({
    extended: true
}));
//Allow use of static assets. 
//Included below line to allow referring to relative path in index.html
app.use(express.static('public'));
/*Data Endpoints Start*/
app.post('/issue/description/:issueId', function (req, res) {
    const url = "http://" + getUserName(req) + ":" + getPassword(req) + "@<jira-url>/rest/api/2/issue/" + getIssueId(req);
    request({ url: url }, function (err, response, body) {
        if(response.statusCode == 200){
        const data = {statusCode: response.statusCode, data: JSON.parse(body).fields.description};
        console.log(JSON.parse(body).fields.description);
        }
        const data = {statusCode: response.statusCode, data: ''};
        res.send(data);
    });
});
app.post('/login', function (req, res) {
    const url = "https://" + getUserName(req) + ":" + getPassword(req) + "@<stash-url>/rest/api/1.0/users/" + getUserName(req);
    request({ url: url }, function (err, response, body) {
        const data = {statusCode: response.statusCode, data: body};
        console.log(data);
        res.send(data);
    })
});
/*Data Endpoints End*/
function getUserName(req) {
    console.log(req.body.username);
    return req.body.username;
}
function getPassword(req) {
    console.log(req.body.password);
    return req.body.password;
}
function getIssueId(req) {
    console.log(req.params.issueId);
    return req.params.issueId;
}
app.get('/', function (req, res) {
    res.sendFile(__dirname + '/public/index.html');
});
//Home
app.get('/view/home', function (req, res) {
    res.sendFile(__dirname + '/public/views/home.html');
});
app.get('/home', function (req, res) {
    res.sendFile(__dirname + '/public/index.html');
});
//Login
app.get('/view/login', function (req, res) {
    res.sendFile(__dirname + '/public/views/login.html');
});
app.get('/login', function (req, res) {
    res.sendFile(__dirname + '/public/index.html');
});
//Login
app.get('/view/error', function (req, res) {
    res.sendFile(__dirname + '/public/views/error.html');
});
app.get('/error', function (req, res) {
    res.sendFile(__dirname + '/public/index.html');
});
//Play
app.get('/view/play', function (req, res) {
    res.sendFile(__dirname + '/public/views/play.html');
});
app.get('/play', function (req, res) {
    res.sendFile(__dirname + '/public/index.html');
});
app.get('/play/:sessionId', function (req, res) {
    res.sendFile(__dirname + '/public/index.html');
});

//Socket Handling
var users = [];

var sessions = {};

server.listen(8081);
io.sockets.on('connection', function (socket) {
    socket.on('create-session', function (sessionId) {
        sessions[sessionId] = [1, 2, 3, 8, 15, 23];
        console.log(sessions);
    });
    socket.on('change-story', function (data) {
        io.sockets.in(socket.sessionId).emit('change-story', data);        
    });
    socket.on('join-session', function (data) {
        console.log(data);
        socket.sessionId = data.sessionId;
        socket.userName = data.userName;
        users.push(data);
        socket.join(data.sessionId);
        //Send to everyone connected that new user joined
        io.sockets.in(socket.sessionId).emit('notify-users', data);
        //Send All users to newly added user
        socket.emit('session-users', users.filter((user) => user.sessionId == socket.sessionId));
                
        socket.emit('change-card-values', sessions[socket.sessionId]);
    });
    socket.on('message', function (data) {
        io.sockets.in(socket.sessionId).emit('message', socket.userName, data);
    });
    socket.on('save-score-user', function (data) {
        console.log('save-score-user');
        console.log(socket.sessionId);
        console.log(socket.userName);
        console.log(users);
        console.log(data);
        users.forEach((item) => {
            if (item.sessionId == socket.sessionId && item.userName == socket.userName) {
                item.score = data;
            }
        });
        console.log('complete----')
        console.log(users);
        io.sockets.in(socket.sessionId).emit('play-complete', socket.userName);
    });
    socket.on('reveal-all', function () {
        io.sockets.in(socket.sessionId).emit('reveal-all', users.filter((user) => user.sessionId == socket.sessionId));
    });
    socket.on('reset-scores', function () {
        var filteredUsers = users.filter((user)=> user.sessionId == socket.sessionId);
        filteredUsers.forEach((user)=>{
            user.score = 0;
        });
        io.sockets.in(socket.sessionId).emit('reset-scores', filteredUsers);
    });
    socket.on('change-card-values', function (data) {
        sessions[socket.sessionId] = data;        
        io.sockets.in(socket.sessionId).emit('change-card-values', data);
    });
    socket.on('disconnect', function () {
        delete users[socket.userName];
        socket.emit('notify-room', socket.user + 'has left');
        socket.leave(socket.sessionId);
    });
});

# public index.html
<!DOCTYPE html>
<html>

<head>
    <title>Planning Poker</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-rc.2/css/materialize.min.css">

    <script src="/socket.io/socket.io.js"></script>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
</head>

<body>

    <script type="text/javascript" src="/js/util-helper.js"></script>
    <script type="text/javascript" src="/js/route-helper.js"></script>
    <script type="text/javascript" src="/js/socket-helper.js"></script>
    <script type="text/javascript" src="/js/main.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-rc.2/js/materialize.min.js"></script>

    <nav class="navbar-fixed">
        <div class="nav-wrapper">
            <div class="container">
                <a href="/" class="brand-logo" style="font-size:1.5em"><i class="material-icons">style</i>Fidelity Planning Poker</a>
                <ul id="nav-mobile" class="right hide-on-med-and-down">
                    <li><a href="#about" class="modal-trigger">About <i class="material-icons left">info</i></a></li>
                </ul>
            </div>
            <div class="progress" id="loading-bar" style="display:none;">
                <div class="indeterminate"></div>
            </div>
        </div>
    </nav>
    <div id="placeholder">

    </div>
    <div id="about" class="modal" style="background:white">
        <div class="modal-content">
            <div class="container">
                <div class="row">
                    <h5 class="center-align">Developed and maintained by <br/><br/><b>Prashanth Ramakrishnan</b></h5>
                </div>
                <div class="row">
                    <div class="col s6 center-align">
                        <a href="sip:prashanth.ramakrishnan@gmail.com"><i class="material-icons" style="font-size:3rem">chat</i></a>
                    </div>
                    <div class="col s6 center-align">
                        <a href="maito:prashanth.ramakrishnan@gmail.com"><i class="material-icons" style="font-size:3rem">email</i></a>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="javascript:cancel()" class="modal-close waves-effect waves-green btn-flat">Close</a>
        </div>
    </div>

</body>

</html>

# public js main.js
var userName = '';
var passWord = '';
var fullName = '';
var sessionId = '';
const routes = [
    { path: 'home', endPoint: '/view/home' },
    { path: 'login', endPoint: '/view/login' },
    { path: 'play', endPoint: '/view/play' },
    { path: 'error', endPoint: '/view/error' },
    { path: '/play/:sessionId', endPoint: '/login'},
    { path: '/', endPoint: '/home' }
];
var values = [];
$(function(){
    const path = window.location.pathname;
    init(path);
    $('.modal').modal();    
});
window.onpopstate = function(){
    loadRoute(window.location.pathname);
}
function getSession() {
    return sessionId;
}
function setSession(id) {
    sessionId = id;
}
function init(path) {
    if(path.length == 1 && path.indexOf("/")==0)
        navigateTo('home');
    else if(isDirectSessionUrl(path)){
        setSession(getSessionFromPath(path));
        navigateTo('login');
    }
    else
        loadRoute(path);
}
function isDirectSessionUrl(path){
    return path.indexOf('/play/')>=0;
}
function getSessionFromPath(path) {
    return path.replace("/play/", "");
}
function cancel(){
    console.log('cancel');
}

# public js route-helper.js
function loadRoute(path) {
    $.get(getHTMLForPath(path), function (data) {
        $("#placeholder").html(data);
    });
}
function getHTMLForPath(path) {
    const route = routes.find((route) => {
        return path.indexOf(route.path) >= 0;
    });
    return route.endPoint;
}
function navigateTo(path){
    history.pushState(null, null, '/'+path);
    loadRoute(path);
}

# public js socket-helper.js
var socket = io.connect('http://localhost:8081');
const userElement = `
<li class="collection-item avatar">
    <img class="circle responsive-img" id="$$userName-img" src="http://fcservices.fmr.com/fcservices-web/services/ww/photos/$$userName"></img>
    <span class="title"><b>$$fullName</b></span>
    <p>$$userName</p>
    <i class="material-icons secondary-content done"  id="$$userName-done" style="display:none;">done</i>
    <span id="$$userName-score" class="badge collection-item secondary-content" style="display:none;"><b>$$score</b></span>
</li>
`;
socket.on('connect', function () {
});
socket.on('notify-users', function (data) {
    console.log('notify');
    console.log(data);
    //Add newly joined user to user list
    if ($('#users li').length != 0)
        $("#users").append(userElement.replace("$$fullName", data.fullName)
            .replace("$$userName", data.userName)
            .replace("$$userName", data.userName)
            .replace("$$userName", data.userName)
            .replace("$$userName", data.userName)
            .replace("$$userName", data.userName));
    $("#" + userName + "-img").addClass('active-user');
});
socket.on('session-users', function (data) {
    console.log('session-users');
    console.log(data);
    //First time populate all users in the room
    if ($('#users li').length == 0) {
        data.forEach(function (item) {
            $("#users").append(userElement.replace("$$fullName", item.fullName)
                .replace("$$userName", item.userName)
                .replace("$$userName", item.userName)
                .replace("$$userName", item.userName)
                .replace("$$userName", item.userName)
                .replace("$$userName", item.userName));
        });
    }
    $("#" + userName + "-img").addClass('active-user');
});
socket.on('play-complete', function (data) {
    console.log("play-complete");
    console.log(data);
    //Show tick marks for people who have voted
    //Dont show tick mark for the current user. Actual score is shown
    if (data != userName) {
        $("#" + data + "-done").show();
    }
});
socket.on('reveal-all', function (data) {
    console.log("reveal-all");
    console.log(data);
    //Hide tick marks
    $(".done").hide();
    //Populate actual scores and show
    data.forEach((item) => {
        $("#" + item.userName + "-score").html('<b>' + item.score + '</b>');
        $("#" + item.userName + "-score").show();
    });
});
socket.on('change-story', function (data) {
    $("#storyIDView").html(data.id);
    $("#storyDescView").html(data.desc);
});
socket.on('reset-scores', function (data) {
    data.forEach((item) => {
        $("#" + item.userName + "-score").html("<b>$$score</b>");
        $("#" + item.userName + "-score").hide();
        $("#" + item.userName + "-done").hide();
    });
});
socket.on('change-card-values', function (data) {
    values = data;
    resetCards(data);
});

# public views error.html
<div class="full-page" style="background:white">
    <div class="container" style="padding-top:100px">
        <div class="row">
            <div class="col s6">
                <p style="font-size:1.5em;">An Error Occurred. 
                    Probably you entered incorrect login information, while trying to login!
                </p>
                <button class="btn waves-effect waves-light" type="button" onclick="goToLogin()">Go to Login Page
                        <i class="material-icons right">chevron_right</i>
                    </button>
            </div>
            <div class="col s6 center-align" style="padding-top:20px;">
                <i class="large material-icons">error_outline</i>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function goToLogin(){
        navigateTo('login');
    }
</script>

# public views home.html
<style>
    .full-page {
        /*position: absolute;*/
        width: 100%;
        height: 100%;
        top: 0;
        left: 0;
    }
</style>
<div class="full-page" style="background:white">
    <div class="container" style="padding-top:30px">
        <div class="row">
            <div class="col s6">
                <h2>Story Estimation with fun!</h2>
                <p style="font-size:1.5em;">Truly agile, calls for story estimation with the whole team. Planning poker excels by doing it right, the
                    fun way!
                </p>
                <button class="btn waves-effect waves-light" type="button" onclick="getStarted()">Get Started
                    <i class="material-icons right">chevron_right</i>
                </button>
            </div>
            <div class="col s6 center-align" style="padding-top:117px;">
                <i class="large material-icons" style="font-size:9rem">style</i>
            </div>
        </div>
    </div>
</div>
<div class="full-page" style="background:white">
    <div class="container" style="padding-top:5px">
        <div class="row">
            <h4 class="center-align">Features</h4>
            <h6 class="center-align">Few things that we do best!</h6>
        </div>
        <div class="row">
            <div class="col s4 center-align">
                <i class="large material-icons">settings_input_component</i>
                <p style="font-size:1.5em;">Jira Integration</p>
            </div>
            <div class="col s4 center-align">
                <i class="large material-icons">record_voice_over</i>
                <p style="font-size:1.5em;">Everyone's voice is heard</p>
            </div>
            <div class="col s4 center-align">
                <i class="large material-icons">chat</i>
                <p style="font-size:1.5em;">Real time Collaboration</p>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function getStarted() {
        navigateTo('login');
    }

</script>

# public views login.html
<div id="user-details">
    <div class="container" style="padding-top:40px;">
        <div class="row">
            <div class="col s3">
            </div>
            <form class="col s6 container">
                <div class="row">
                    <div class="input-field col s12">
                        <i class="material-icons prefix">account_circle</i>
                        <input id="username" type="text" class="validate">
                        <label for="username">Username</label>
                    </div>
                </div>
                <div class="row">
                    <div class="input-field col s12">
                        <i class="material-icons prefix">vpn_key</i>
                        <input id="password" type="password" class="validate">
                        <label for="password">Password</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col s6 center-align">
                        <button onclick="login()" type="button" class="btn waves-effect waves-light"><i class="material-icons left">https</i>Login</button>
                    </div>
                    <div class="col s6 center-align">
                        <button onclick="cancel()" type="button" class="btn waves-effect waves-light"><i class="material-icons left">clear</i>Cancel</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    function login() {
        $("#loading-bar").show();
        userName = $('#username').val();
        passWord = $('#password').val();
        //Uncomment below and comment the post request method if you dont want login mechanism
        /*fullName = userName;
        handleSessions();
        navigateTo('play/'+getSession());*/
        const loginInfo = { 'username': userName, 'password': passWord };
        $.post('login', loginInfo, function (response, status) {
            $("#loading-bar").hide();
            console.log(response);
            if (response.statusCode == 200) {
                const responseJSON = JSON.parse(response.data);
                userName = responseJSON.name;
                fullName = responseJSON.displayName;
                console.log('userName' + userName);
                console.log('fullName' + fullName);
                handleSessions();
                navigateTo('play/' + getSession());
            }
            else {
                navigateTo('error');
            }
        });
    }
    function handleSessions() {
        if (getSession() == "")
            createSession();
    }
    function createSession() {
        setSession(new Date().getMilliseconds());
        socket.emit('create-session', getSession());
    }
    function cancel() {
        navigateTo('home');
    }

</script>

# public views play.html
<style>
    .fix-to-bottom {
        position: fixed;
        bottom: -38px;
        width: 100%;
    }

    .active-user {
        border: 2px solid #009688;
        background-color: #009688;
    }
</style>
<div class="container">
    <div class="row">
        <div class="container" style="padding-top: 12px;">
            <div class="col s4">
                <button onclick="showAll()" class="btn waves-effect waves-light"><i class="material-icons left">remove_red_eye</i>Show Score</button>
            </div>
            <div class="col s4">
                <button onclick="clearAll()" class="btn waves-effect waves-light"><i class="material-icons left">clear_all</i>Clear Scores</button>
            </div>
            <div class="col s4">
                <button data-target="cardsSettings" class="btn waves-effect waves-light modal-trigger tooltipped" data-position="bottom" data-tooltip="Change card values here"><i class="material-icons left">settings</i>Card Settings</button>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col s4">
            <h4 class="header">Active Players</h4>
            <ul class="collection" id="users">
            </ul>
        </div>
        <div class="col s8">
            <div class="card blue-grey darken-1 small">
                <div class="card-content white-text">
                    <span class="card-title view" id="storyIDView">FID-123</span>
                    <input class="edit" style="display:none; color:white" type="text" id="storyID" />
                    <div class="view" style="overflow-y:auto; height:200px">
                        <span id="storyDescView" style="white-space: pre-wrap;">As a user, I want my planning poker game to look as cool as Fidelity Planning Poker!</span>
                    </div>
                    <!--loader for jira -->
                    <div class="preloader-wrapper small active" style="display:none;" id="jiraLoading">
                        <div class="spinner-layer spinner-green-only">
                            <div class="circle-clipper left">
                                <div class="circle"></div>
                            </div>
                            <div class="gap-patch">
                                <div class="circle"></div>
                            </div>
                            <div class="circle-clipper right">
                                <div class="circle"></div>
                            </div>
                        </div>
                    </div>
                    <!--loader for jira -->
                    <textarea class="edit" rows="10" cols="30" id="storyDesc" style="display:none; width:100%; height:100%; color:white"></textarea>
                </div>
                <div class="card-action">
                    <a class="view" href="javascript:edit()"> Edit</a>
                    <a class="edit" style="display:none" href="javascript:done()"> Done</a>
                    <a class="edit tooltipped" style="display:none" href="javascript:getStoryDescriptionFromJIRA()" data-position="bottom" data-tooltip="Provide a story ID and click me to get Story Description from JIRA">Get Description from JIRA</a>
                </div>
            </div>
        </div>
    </div>
    <div class="row fix-to-bottom" id="cards">
    </div>
</div>
<div id="cardsSettings" class="modal">
    <div class="modal-content">
        <h4>Cards Settings</h4>
        <div id="card-values-input" class="chips"></div>
    </div>
    <div class="modal-footer">
        <a href="javascript:saveCardValues()" class="modal-close waves-effect waves-green btn-flat">Save</a>
        <a href="javascript:cancel()" class="modal-close waves-effect waves-green btn-flat">Cancel</a>
    </div>
</div>

<script type="text/javascript">
    init();
    function init() {
        //Initialize tooltips
        $('.tooltipped').tooltip();
        //init modal for materialize
        $('.modal').modal({
            onOpenStart: function () {
                //Init card Settings page
                cardSettingsInit(values);
            }
        });
        //Join Session   
        joinSession();
    }
    function cardSettingsInit(values) {
        cardValues = [];
        for (var count = 0; count < values.length; count++) {
            cardValues.push({ tag: values[count] });
        }
        $('#card-values-input').chips({
            data: cardValues,
            placeholder: 'Enter story points.',
            secondaryPlaceholder: '+Story Point',
            limit: 8,
            minLength: 1
        });
    }
    function resetCards(values) {
        $("#cards").html('');
        formCards(values);
    }
    function saveCardValues() {
        var instance = M.Chips.getInstance($('#card-values-input'));
        console.log(instance.chipsData);
        const chipData = instance.chipsData;
        values = [];
        if (chipData.length > 0) {
            chipData.forEach((chip) => {
                values.push(chip.tag);
            });

        }
        resetCards(values);
        socket.emit('change-card-values', values);
    }
    function cancel() {
        console.log('cancel');
    }
    function getStoryDescriptionFromJIRA() {
        const loginInfo = { 'username': userName, 'password': passWord };
        $("#jiraLoading").show();
        $("#storyDesc").hide();
        $.post('/issue/description/' + $("#storyID").val(), loginInfo, function (response, data) {
            $("#jiraLoading").hide();
            $("#storyDesc").show();
            if (response.statusCode == 200) {
                $("#storyDesc").val(response.data);
            }
        });
    }
    function edit() {
        $(".view").hide();
        $(".edit").show(100);
        $("#jiraLoading").hide();
        $("#storyID").val($("#storyIDView").html());
        $("#storyDesc").val($("#storyDescView").html());
    }
    function done() {
        $("#storyIDView").html($("#storyID").val());
        $("#storyDescView").html($("#storyDesc").val());
        $(".edit").hide();
        $(".view").show(100);
        broadcastChangeStory();
    }
    function broadcastChangeStory() {
        const data = { id: $("#storyID").val(), desc: $("#storyDesc").val() };
        socket.emit('change-story', data);
    }
    function riseUp(self) {
        console.log('in');
        $(self).animate({ marginTop: "-30px" });
    }
    function resetCard(self) {
        console.log('out');
        $(self).stop();
        $(self).css("marginTop", "0px");
        $(self).css("bottom", "-38px");
    }
    function joinSession() {
        console.log('roomname' + getSession());
        console.log('userName' + userName);
        console.log('fullName' + fullName);
        var data = { sessionId: getSession(), userName: userName, fullName: fullName };
        socket.emit('join-session', data);
    }
    function formCards(values) {
        const cardHTML = `
        <div class="col s1 score-cards" style="cursor: pointer;" onclick="play($$value)" onmouseleave="resetCard(this)" onmouseenter="riseUp(this)">
            <div class="card-panel teal">
                <span class="white-text center-align" style="font-size:2em">$$value</span>
            </div>
        </div>`;

        values.forEach((value) => {
            $("#cards").append(cardHTML.replace("$$value", value).replace("$$value", value));
        })
    }
    function play(value) {
        socket.emit('save-score-user', value);
        $("#" + userName + "-score").html('<b>' + value + '</b>');
        $("#" + userName + "-score").show();
    }
    function showAll() {
        socket.emit('reveal-all');
    }
    function clearAll() {
        socket.emit('reset-scores');
    }

</script>

# package.json
{
  "name": "planning-poker",
  "version": "1.0.0",
  "description": "Agile planning poker game",
  "main": "index.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "Prashanth",
  "license": "ISC",
  "dependencies": {
    "body-parser": "^1.18.3",
    "express": "^4.16.3",
    "materialize-css": "^0.100.2",
    "request": "^2.87.0",
    "socket.io": "^2.1.1"
  }
}
