/**
 * Inserts a new DIV with message after the menu DIV when WebSocket received a
 * message
 */
$(function() {
	var wsocket = new WebSocket("ws://localhost:8080/ims/notifyall");
	wsocket.onerror = onError;
	wsocket.onopen = onOpen;
	wsocket.onmessage = onMessage;
	wsocket.onclose = onClose;
	function onError(evt) {
	}
	function onOpen(evt) {
	}
	function onMessage(evt) {
		// disable the previous one
		if ($("#message").length > 0) {
			$("#message").remove();
		}

		// enable a new one
		var msg = $("<div></div>").text(evt.data);
		msg.attr("id", "message");
		msg.css({
			"display" : "block",
			"position" : "relative",
			"top" : "0",
			"left" : "0",
			// "overflow" : "auto",
			"border" : "3px solid green",
			"border-radius" : "5px",
			// "width" : "100%",
			"background-color" : "#fff",
			"color" : "red",
			"z-index" : "-1"
		});
		$("#menu").after(msg);
	}// end of function onMessage
	function onClose(evt) {
	}
});// end of $(document).ready()