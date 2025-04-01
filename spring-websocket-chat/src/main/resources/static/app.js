document.getElementById('join-button').addEventListener('click', joinChat);
document.getElementById('leave-channel-button').addEventListener('click', leaveChat);

let username = '';
let channel = '';
let socket = null;
let stompClient = null;

function joinChat() {
    username = document.getElementById('username').value;
    channel = document.getElementById('channel').value;

    if (username && channel) {
        document.getElementById("channel-name").textContent = `Channel: ${channel}`;
        document.getElementById('login-section').style.display = 'none';
        document.getElementById('chat-section').style.display = 'block';

        connectToWebSocket();
    } else {
        alert("Both fields must be filled out.");
    }
}

function connectToWebSocket() {
    socket = new SockJS("/websocket");

    stompClient = Stomp.over(socket);

    stompClient.connect({username: username, channel: channel}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public/' + channel, function (messageOutput) {
            showMessage(JSON.parse(messageOutput.body));
        });
    });
}

function sendMessage() {
    const content = document.getElementById('message').value;
    if (content && stompClient) {
        stompClient.send("/app/chat.sendMessage/" + channel, {}, JSON.stringify({
            username: username,
            content: content,
            eventType: 'MESSAGE'
        }));
        document.getElementById('message').value = '';
    }
}

document.getElementById('send-message-button').addEventListener('click', sendMessage);

function showMessage(message) {
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');

    if (message.eventType === 'JOINED') {
        messageElement.classList.add('joined');
        messageElement.innerHTML = `<span class="text">${message.username} has joined the channel.</span>`;
    } else if (message.eventType === 'LEFT') {
        messageElement.classList.add('left');
        messageElement.innerHTML = `<span class="text">${message.username} has left the channel.</span>`;
    } else {
        messageElement.innerHTML = `<span class="username">${message.username}:</span> <span class="text">${message.content}</span>`;
    }

    document.getElementById('messages').appendChild(messageElement);
    document.getElementById('messages').scrollTop = document.getElementById('messages').scrollHeight;
}

function leaveChat() {
    if (stompClient) {
        stompClient.disconnect();
        document.getElementById('chat-section').style.display = 'none';
        document.getElementById('login-section').style.display = 'block';
        document.getElementById('username').value = '';
        document.getElementById('channel').value = '';
    }
}
