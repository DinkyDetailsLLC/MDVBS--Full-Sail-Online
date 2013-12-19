function checkMovie() {
	var movieName = document.getElementById('name').value;
	webapi.changeActivity(movieName);
}

//Function so the app can talk to the native code. 