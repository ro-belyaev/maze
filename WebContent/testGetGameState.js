//<!--

$(function() {
	$.ajax({
		type: 'GET',
		url: 'GetGameState',
		data: {
			movesInTable: 0,
			gameId: 0
		},
		dataType: 'json'
	})
	.done(function(gameStateJson) {
		console.log(gameStateJson);
	})
	.fail(function() {
		alert('fail when sending ajax request on server');
	});
});

//-->