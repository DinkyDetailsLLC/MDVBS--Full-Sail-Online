function checkMovie() {
	var movieValue = document.getElementById('name').value;
	webapi.changeActivity(movieValue);
}

//Function so the app can talk to the native code. 