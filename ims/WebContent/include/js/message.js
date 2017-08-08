/**
 * 
 */
$(function() {
	var wsocket = new WebSocket("ws://localhost:8088/ims/notifyall");
	wsocket.onopen = onOpen;
	wsocket.onmessage = onMessage;
	wsocket.onclose = onClose;
	wsocket.onerror = onError;
	function onOpen(evt) {
		$("#msgOpen").text(evt.data);
	}
	function onMessage(evt) {
		$("#msg").css({
			"display" : "block",
			"position" : "relative",
			"top" : "0",
			"left" : "0",
			// "overflow" : "auto",
			"border" : "2px solid lightgreen",
			"border-radius" : "5px",
			// "width" : "100%",
			"background-color" : "#fff",
			"color" : "red",
			"z-index" : "-1"
		});
		$("#msg").text(evt.data);
	}
	function onClose(evt) {
		$("#msgClose").text(evt.data);
	}
	function onError(evt) {
		$("#msgError").text(evt.data);
	}
});// end of $(document).ready()
