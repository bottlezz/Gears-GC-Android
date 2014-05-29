
//client network layer

//can send data in binary
	//init objects
	// var recievedObject;	  // recieved json object

function GameCenter() {

	var wsPort;

	var ID;
	//var mazeID;
	var connection;
	//to close connection connection.close();
	(this.initial = function() {
		console.log("loading!");
		//check preconditions for web socket support
		if (window.MozWebSocket) {

	        console.log('using MozillaWebSocket');
	        window.WebSocket = window.MozWebSocket;
	    } else if (!window.WebSocket) {
	    	
	        console.log('browser does not support websockets!');
	        alert('browser does not support websockets!');
	        return;
	    }

		wsPort = "8081";
		var matches = document.URL.match(/http:\/\/([\d.]+)[\/:].*/);
        var ip = matches[1];
        //var ip="192.168.0.48";
        console.log("IP: " + ip);
        
		connection = new WebSocket("ws://" + ip + ":" + wsPort);

		connection.onopen = function(event) { onConnection() };
		connection.onerror = function(error) { connectionError(error) };
		connection.onmessage = function(message) { receiveMessage(message) };
		connection.onclose = function(event) { onCloseEvent() };
	})();

	//connection error handling
	var connectionError = function(error) {
		console.log("connection error: " + error);
		alert(error);
		document.getElementById('test').innerHTML = error;
	}

	//initial connection sequence
	var onConnection = function() {
		console.log("connected");
		// sendOut(gameStateObject);
	}

	var onCloseEvent = function() {
		console.log("closing");
	}

	var userList = [];

	var receiveMessage = function(message) {
		//convert JSON
		console.log(message);

		try {
			var receivedMessage = JSON.parse(message.data);
			

			if(receivedMessage.user_id != null) {
				ID = receivedObject.user_id;
				
				return;
			} else if (receivedMessage.action == "broadcasting") {

		
				console.log(receivedMessage.body);
				recievedCallBack(receivedMessage.body);
			} else if (receivedMessage.action == "get_shared_memory") {
				receivedSharedMemory(receivedMessage.name, receivedMessage.body);
			} else if (receivedMessage.action == "user_list"){
		

				console.log(receiveMessage.userList);
				receivedUserlist(receivedMessage.userList);

			}else {
				console.log("undefined action: " + receivedMessage.action);
			}

			console.log("Recevied Message " + receivedMessage);
		} catch(error) {
			console.log('message is not a JSON object' + error);
		}
	}

	var user = function(name, property){
		this.name = name;
		this.id = null;
		this.isHost = null;
		this.property = property
	}

	var sendMessage = function(action, name, body) {
		var timestamp = new Date();

		var message = {
			"action": action,
			"timestamp": timestamp,
			"userID": null,
			"name": name,
			"body": body 
		}

		if(connection.readyState == 1) {
			connection.send(JSON.stringify(message));
		} else {
			console.log("connection not ready!");
		}
		console.log("SENT");
	}

	this.broadcasting = function(body) {
		sendMessage("broadcasting", "message", body);
	}

	this.setSharedMemory = function(name, body) {
		sendMessage("set_shared_memory", name, body);
	}

	this.getSharedMemory = function(name) {
		sendMessage("get_shared_memory", name, null);
	}

	this.setUser = function(name, property) {

		// var existingUser = 0;
		// for (var i = this.userList.length - 1; i >= 0; i--) {
		// 	if(this.userList[i].name = name) {
		// 		this.userList[i].property = property;
		// 		existingUser = 1;
		// 		break;
		// 	}
		// };

		// if(!existingUser) {
			var newUser = new user(name, property);
			// this.userList.push(newUser);
		// }

		sendMessage("set_user", name, newUser);
	}


	this.getUserList = function () {
		sendMessage("get_user_list", null, null);
	}

}
