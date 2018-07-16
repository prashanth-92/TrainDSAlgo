# TrainDSAlgo
io.sockets.on('connection', function (socket) {
    socket.on('create-session', function (sessionId) {
        sessions[sessionId] = {cardData : [1, 2, 3, 8, 15, 23], storyID: 'FID-123', storyDesc: 'As a user, I want my planning poker game to look as cool as Fidelity Planning Poker!'};
        console.log(sessions);
    });
    socket.on('change-story', function (data) {
        sessions[socket.sessionId].storyID = data.id;
        sessions[socket.sessionId].storyDesc = data.desc;
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

        socket.emit('change-card-values', sessions[socket.sessionId].cardData);
        const storyData = {id: sessions[socket.sessionId].storyID, desc: sessions[socket.sessionId].storyDesc};
        socket.emit('change-story', storyData);
        
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
        var filteredUsers = users.filter((user) => user.sessionId == socket.sessionId);
        filteredUsers.forEach((user) => {
            user.score = 0;
        });
        io.sockets.in(socket.sessionId).emit('reset-scores', filteredUsers);
    });
    socket.on('change-card-values', function (data) {
        sessions[socket.sessionId].cardData = data;
        io.sockets.in(socket.sessionId).emit('change-card-values', data);
    });
    socket.on('disconnect', function () {
        delete users[socket.userName];
        socket.emit('notify-room', socket.user + 'has left');
        socket.leave(socket.sessionId);
    });
});

# socket-helper.js
socket.on('change-story', function (data) {
    storyID = data.id;
    storyDesc = data.desc;
    $("#storyIDView").html(data.id);
    $("#storyDescView").html(data.desc);
});
