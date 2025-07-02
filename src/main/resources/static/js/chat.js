let stompClient = null;
let username = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/public', function (message) {
            const msg = JSON.parse(message.body);
            showMessage(msg);
        });

        // Send join message
        stompClient.send("/app/addUser", {}, JSON.stringify({
            sender: username,
            type: 'JOIN'
        }));
    });
}

function sendMessage() {
    const userInput = document.getElementById('username');
    const messageInput = document.getElementById('message');
    if (!username) {
        if (!userInput.value.trim()) return;
        username = userInput.value.trim();
        userInput.disabled = true;
        connect();
    }

    const message = messageInput.value.trim();
    if (message && stompClient) {
        const chatMessage = {
            sender: username,
            content: message,
            type: 'CHAT'
        };
        stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

function showMessage(message) {
    const chatWindow = document.getElementById('chat-window');
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');

    if (message.type === 'JOIN') {
        messageElement.classList.add('join');
        messageElement.innerText = `${message.sender} joined the chat.`;
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('leave');
        messageElement.innerText = `${message.sender} left the chat.`;
    } else {
        messageElement.innerHTML = `<strong>${message.sender}:</strong> ${message.content}`;
    }

    chatWindow.appendChild(messageElement);
    chatWindow.scrollTop = chatWindow.scrollHeight;
}
