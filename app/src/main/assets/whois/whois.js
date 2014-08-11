// Config

var GAME_ENTER = 0;
var GAME_READY = 1;
var GAME_ON = 2;
var GAME_WAIT = 3;
var GAME_OVER = 4;
var NUMBER_OF_STATES = 4;

var IS_READY = 1;
var NOT_READY = 0;

var UNDEFINED = -1;

// on opening page, set state to GAME_ENTER
var state = GAME_ENTER;
// only host can start game
var isHost = 0;

var UserList;
var GameUserList;

var numGameUser;
var numGameAnswer;
var numExpectedGameAnswer;

var numQuestion = undefined;
var questionCount = 0;
var questionCheckTable;
var questionIndex = 0;
var questions = [
"Who is the most beautiful person?",
"Who is the most handsome?",
"Who is the greediest person?",
"Who is easiest person to get along with?",
"Who is your best friend?",
"Who has the best voice?",
"Who is the kindest person?",
"Who knows the most about World History?",
"Who is the best singer?",
"Who is the most physically fit?",
"Who has the most analytical mind?",
"Who can eat the most?",
"Who has the largest feet?",
"Who is the best chef?",
"Who has the best smile?",
"Who is the most flirtatious?",
"Who is the most adventurous?",
"Who is the biggest sci fi fan?",
"Who laughs the loudest?",
"Who has the smelliest feet ?",
"Who is always being late ?",
"Who drink the most?",
"Who is the best actor/ actress?",
"Who is the love expert?",
"Who is working hardest?",
"Who is the most romantic person?",
"Who talks the most?",
"Who is the hottest?",
"Who is the easiest person got scared?"
];
//function Center(){
//	this.broadcasting=function(msg){
//		sendOut(msg);
//	}
//}
var connect = new GameCenter();

function initState(){
	state = GAME_ENTER;
	numQuestion = undefined;
	questionCount = 0;
}

// to move State forward
// 0 - 1 - 2 - 3 - 4 - 1
// GAME_ENTER - GAME_READY - GAME_ON - GAME_WAIT - GAME_OVER - GAME_READY
function nextState(){
	state = state + 1;
	if(state>NUMBER_OF_STATES){
		state = state % NUMBER_OF_STATES;
	}
	
	if(state == GAME_ON){
		//update question count
		questionCount++;
		
		DisplayQuestion();
	}
	if(state == GAME_OVER){
		caculateRank();
		DisplayResults();

		GameUserList=[];
		UserList=[];
	}
	
	return state;
	
	function caculateRank(){
		for(var i=0; i<GameUserList.length; i++){
			maxIndex = -1;
			maxVote = -1;
			for(var j=0; j<GameUserList.length; j++){
				if(GameUserList[j].Rank == UNDEFINED){
					if(GameUserList[j].Count>maxVote){
						maxVote = GameUserList[j].Count;
						maxIndex = j;
					}
				}
			}
			console.log(maxIndex);
			GameUserList[maxIndex].Rank = i+1;
		}
	}
}


function GetGameUsersList(){
	return GameUserList;
}

function updateQuestionIndex(){
	do{
		questionIndex = Math.floor((Math.random() * questions.length));
	} while(questionCheckTable[questionIndex]==1)
	
	questionCheckTable[questionIndex] = 1;
	return questionIndex;
}

function startGame(){
	// start the game

	//also we want to make sure that
	//in one game no question appears twice
	if(isHost) {
		questionIndex = updateQuestionIndex();
	}
	
	//when sending the start game message
	//host should broadcase a question
	//i think we could usethe value:question
	var dataobject={"type":"startGame", "questionIndex":questionIndex, 
			"questionCount":questionCount, "numQuestion":numQuestion};
    connect.broadcasting(dataobject);
}

function answerQuestion(name){
	//answer question and send it out
	console.log("name: "+name);
	var dataobject={type:"answer", value:name, id:myName};
	connect.broadcasting(dataobject);
	connect.setUser(myName,NOT_READY);

	nextState();
}

myName = "";

function UserIsReady(name){
	//send the name to the server
	console.log("UserIsReady");
	connect.setUser(name, IS_READY);
	myName= name;
	GameUserList = [];
	//GetGameUsersList();
	//move state forward
	nextState();
	return state;
}

//mock up
mocked_UserList = new Object();

/*
function setUser(name, state){
	mocked_UserList_tmp = new Object();
	mocked_UserList_tmp[0] = name;
	mocked_UserList_tmp[1] = state;
	console.log(mocked_UserList_tmp);
	var dataobject={type:"mocked", value:mocked_UserList_tmp};
	connect.broadcasting(dataobject);
}
*/

function receivedSharedMemory(name, body){

}

function detectHost(){
	UserList[0].isHost = "1";
}

var user = function(name, property){
	this.name = name;
	this.id = null;
	this.isHost = null;
	this.property= property;
	this.socket = null;
}

function receivedUserlist(receivedMessage){
	var body = receivedMessage.body;
	console.log("receivedUserlist:"+body);
	body = body.split("#SYSTEM#");

	var variables = JSON.parse(receivedMessage.variables);

	if(variables.key == "USERSOCKETS"){
		for(var i=0; i<body.length; i++){
			UserList[i].socket = body[i];
		}
	}
	else if(variables.key == "USERS"){
		UserList = [];
		for(var i=0; i<body.length; i++){
			var currentBody = body[i].split("#PROPERTY#");
			var currentUser = new user(currentBody[0], currentBody[1]);
			UserList.push(currentUser);
		}
		return;
	}

	detectHost();

	//detect host
	for(var i=0; i<UserList.length; i++){
		if(UserList[i]["name"]==myName){
			isHost = UserList[i]["isHost"];
			break;
		}
	}
	if(state == GAME_READY){
		UpdateReadyList();
	}
	if(GameUserList!=undefined){
		//handle user left
		for(var i=0; i<GameUserList.length; i++){
			already_quit = 1;
			if(GameUserList[i]["AnswerStatus"]==1){
				//if user already answer the question, then it is fine
				continue;
			}
			for(var j=0; j<UserList.length; j++){
				if(UserList[j].name==GameUserList[i].Username){
					// user is still in
					already_quit = 0;
					break;
				}
			}
			console.log("quit:"+already_quit);
			if(already_quit == 1){
				//delete it from GameUserList
				//GameUserList.splice(i,1);
				numExpectedGameAnswer -= 1;
				console.log("numExpectedGameAnswer:"+numExpectedGameAnswer);
			}
		}
		//if we are waiting for him to answer, see if we can go to next state
		if(state == GAME_WAIT){
			//TODO
			if(numGameAnswer>=numExpectedGameAnswer){
				//everyone finish the question
				nextState();
			}
		}
	}
	if(isHost == 1 && state == GAME_READY){
		EnableStartButton();
		EnableNumChooser();
	}
	else{
		DisableStartButton();
		DisableNumChooser();
	}
}

function sendWelcome(){
	var dataobject={type:"welcome", value:myName};
    connect.broadcasting(dataobject);
}
function whoIsOn(){
	if(isHost==1&&myName!=""){
		var dataobject={type:"whoIsOn", value:myName};
    	connect.broadcasting(dataobject);
	}
	
}

mocked_Rank = 0;

/*
function setGameOn(){
	//initiate GameUserList
	//object {Userame:"", Rank:1, Count:1}
	GameUserList = [];
	for (var key in UserList){
	
		var obj = {"Username":Ulist[key], "Rank":UNDEFINED, "Count":0};
		GameUserList.push(obj);
		
	}
	numGameUser = GameUserList.length;
	numGameAnswer = 0;
	
	nextState();
}
*/

// receive msg
function recievedCallBack(object){
	console.log("recievedCallBack: "+object);
		//var dataobject={type:"updateLocation",userid:myid,location:Mylocation,time:null,actions:realTimeActions(CurrentPath)};
		
		if(object.type=="startGame"){
			//everyone start the game
			if(state!=GAME_READY){
				return;
			}
			
			//TODO
			//when user get the start game message
			//there should be a question
			

			//initiate GameUserList
			//object {Userame:"", Rank:1, Count:1}
			GameUserList = [];
			mocked_Rank+=1;
			for (var i=0; i<UserList.length; i++){
				if(UserList[i].property==IS_READY){
					var obj = {"Username":UserList[i]["name"], "Rank":UNDEFINED, "Count":0, "AnswerStatus":0};
					GameUserList.push(obj);
				}
			}
			//numGameUser = GameUserList.length;
			numGameAnswer = 0;
			numExpectedGameAnswer = GameUserList.length;
			questionIndex = object.questionIndex;
			questionCount = object.questionCount;
			numQuestion = object.numQuestion;

			console.log(questionIndex+"//"+questionCount);

			nextState();
			
			//whoIsOn();
			//setTimeout(function(){setGameOn()},1000);
		}
		if(object.type == "whoIsOn"){
			sendWelcome();
		}
		//greg added
		/*
		if(object.type == "welcome"){
			var exist=0;
			for(var i in Ulist)
			{
				if(Ulist[i]==object.value){
					exist=1;
				}
			}
			if(exist=0){
				Ulist.push(object.value);
			}

		}
		*/
		if(object.type=="answer"){
			if(state!=GAME_ON && state!=GAME_WAIT){
				return;
			}
			numGameAnswer = numGameAnswer+1;
			for(var i=0; i<GameUserList.length; i++){
				// record who is selected
				if(GameUserList[i]["Username"]==object.value){
					GameUserList[i]["Count"] += 1;
				}
				//record who answers it
				if(GameUserList[i]["Username"]==object.id){
					GameUserList[i]["AnswerStatus"] = 1;
				}
			}
			
			if(numGameAnswer>=numExpectedGameAnswer){
				//everyone finish the question
				nextState();
			}
			UpdateNotAnsweredList();
		}
		//mock up
		if(object.type == "mocked"){
			mocked_UserList[object.value[0]] = object.value[1];
			receivedUserlist(object.value);
		}
		
		if(object.type=="numQuestion"){
			console.log("set num question: "+object.value);
			numQuestion = object.value;
		}
}

function GetQuestion(){
	var num = numQuestion;
	if(numQuestion==undefined)
		num = "?";
	
	return (""+questionCount+"/"+numQuestion+" "+questions[questionIndex]);
}

function SetNumQuestion(num){
	console.log(num+","+numQuestion);
	if(numQuestion != num){
		numQuestion = num;
		questionCount = 0;
		questionCheckTable = new Array(questions.length);
		for(var i=0; i<questionCheckTable.length; i++)
			questionCheckTable[i] = 0;
			
		var dataobject={type:"numQuestion", value:numQuestion};
    	connect.broadcasting(dataobject);
	}
}

function gameIsRestart(){
	if(numQuestion == questionCount){
		return 1;
	}
	else{
		return 0;
	}
}
