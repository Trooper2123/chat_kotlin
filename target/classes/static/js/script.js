'use strict';
document.querySelector('#welcomeForm').addEventListener('submit', connect, true)
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true)
var stompClient = null;
var name = null;
function connect(event) {
	name = document.querySelector('#name').value.trim();
	if (name) {
		document.querySelector('#welcome-page').classList.add('hidden');
		document.querySelector('#dialogue-page').classList.remove('hidden');
		var socket = new SockJS('/websocket');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, connectionSuccess);
	}
	event.preventDefault();
}
function connectionSuccess() {
	stompClient.subscribe('/topic/websocket', onMessageReceived);
	stompClient.send("/app/chat.newUser", {}, JSON.stringify({
		sender : name,
		type : 'newUser'
	}))
}
function sendMessage(event) {
	var messageContent = document.querySelector('#chatMessage').value.trim();
	if (messageContent && stompClient) {
		var chatMessage = {
			sender : name,
			content : document.querySelector('#chatMessage').value,
			type : 'CHAT'
		};
		stompClient.send("/app/chat.sendMessage", {}, JSON
			.stringify(chatMessage));
		document.querySelector('#chatMessage').value = '';
	}
	event.preventDefault();
}
function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
	var messageElement = document.createElement('li');
	if (message.type === 'newUser') {
		messageElement.classList.add('event-data');
		message.content = message.sender + ' has joined the chat';
	} else if (message.type === 'Leave') {
		messageElement.classList.add('event-data');
		message.content = message.sender + ' has left the chat';
	} else {
		messageElement.classList.add('message-data');
		var element = document.createElement('i');
		var text = document.createTextNode(message.sender[0]);
		element.appendChild(text);
		messageElement.appendChild(element);
		var usernameElement = document.createElement('span');
		var usernameText = document.createTextNode(message.sender);
		usernameElement.appendChild(usernameText);
		messageElement.appendChild(usernameElement);
	}
	var textElement = document.createElement('p');
	var messageText = document.createTextNode(message.content);
	textElement.appendChild(messageText);
	messageElement.appendChild(textElement);
	document.querySelector('#messageList').appendChild(messageElement);
	document.querySelector('#messageList').scrollTop = document
		.querySelector('#messageList').scrollHeight;
}

// This is timer producer
const INTERVAL_DURATION = 60000;


const url = process.env.CLOUDAMQP_URL || "amqp://pzfmgmdb:48oHHAJQFmDgsSXXY9SG3evjwgkDCqPG@clam.rmq.cloudamqp.com/pzfmgmdb";
const amqp = require('amqplib');

exports.startTimer = () => {
	amqp.connect(url).then((connection) => {
		connection.createChannel().then((channel) => {
			const exchange = 'quote';
			channel.assertExchange(exchange, 'direct', {durable: false});
			// channel.assertQueue(quote_queue, { durable: true});
			setInterval(() => {
				const message = "Let go get the quote";
				console.log('send to queue', message);
				// channel.sendToQueue(quote_queue, Buffer.from(message),  {
				//     persistent: true
				// });
				channel.publish(exchange, '', Buffer.from(message));
			}, INTERVAL_DURATION)
		});
	}).catch((err) => {
		console.error('Connect amqp failed', err);
	})
}