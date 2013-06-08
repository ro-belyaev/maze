//<!--

$(function() {
	var gameInformation = {
			movesInTable : 0,
			currentPlayerId : 0,
			numberOfPlayers : 3
	};
	var intervalId = setInterval(function() {
		$.ajax({
			type: 'GET',
			url: 'GetGameState',
			timeout: '2800',
			data: {
				movesInTable: gameInformation.movesInTable,
				gameId: 0
			},
			dataType: 'json'
		})
		.done(function(gameState) {
			var lastValueOfMovesInTable = gameState.lastValueOfMovesInTable;
			if (lastValueOfMovesInTable == gameInformation.movesInTable) {
				var moveArray = gameState.moves;
				if (gameInformation.movesInTable == 0) {
					var playersArray = gameState.players;
					createProcessTable(playersArray);
				}
				hideCurrentCell();
				refreshProcessTable(moveArray, gameInformation);
				displayCurrentPlayer(gameInformation.currentPlayerId);
				displayCurrentCell(gameInformation.currentPlayerId);
			}
		})
		.fail(function() {
			alert('some error while sending ajax request to server');
		});
	}, 3000);
});


function refreshProcessTable(moveArray, gameInformation) {
	for (var moveIndex = 0; moveIndex < moveArray.length; moveIndex++) {
		var move = moveArray[moveIndex];
		gameInformation.currentPlayerId = parseInt(move.playerId);
		
		var movesInTable = gameInformation.movesInTable;
		var numberOfPlayers = gameInformation.numberOfPlayers;
		var columnIndex = parseInt(move.playerId) + 1;
		var resultArray = move.moveInformation;
		
		if (movesInTable != 0 && moveIndex == 0) {
//			should append result of player's action to already existed table cell
			var cell = $('#process-table > tbody > tr')
				.last()
				.children('td')
				.eq(columnIndex)
				.children('ul')
				.eq(0);
			for (var resultIndex = 0; resultIndex < resultArray.length; resultIndex++) {
				var result = resultArray[resultIndex];
				$('<li></li>')
					.append(result)
					.appendTo(cell);
			}
		} else {
//			should create new row in table and fill some cell with player's actions
			movesInTable++;
			gameInformation.movesInTable++;
			var newRow = $('<tr></tr>');
//			add first column (number of current move)
			$('<td></td>')
				.append(movesInTable)
				.appendTo(newRow);
//			add cell with player's actions and results
			for (var i = 0; i < columnIndex - 1; i++) {
				newRow.append('<td></td>');
			}
			var listOfResults = $('<ul></ul>');
			for (var resultIndex = 0; resultIndex < resultArray.length; resultIndex++) {
				var result = resultArray[resultIndex];
				$('<li></li>')
					.append(result)
					.appendTo(listOfResults);
			}
			$('<td></td>')
				.append(listOfResults)
				.appendTo(newRow);
			for (var i = columnIndex + 1; i <= numberOfPlayers; i++) {
				newRow.append('<td></td>');
			}
			$('#process-table > tbody')
				.append(newRow);
		}
	}
}

function displayCurrentPlayer(currentPlayerId) {
	var firstRow = $('#process-table > thead > tr:eq(0)');
	firstRow
		.children('th')
		.each(function() {
			$(this).removeAttr('style');
		});
	firstRow
		.children('th')
		.eq(currentPlayerId + 1)
		.css('background', '#fc0');
}

function hideCurrentCell() {
	$('#process-table > tbody > tr')
		.last()
		.children('tr > td')
		.each(function() {
			$(this).removeAttr('style');
		});
}

function displayCurrentCell(currentPlayerId) {
	$('#process-table > tbody > tr:last > td')
		.eq(currentPlayerId + 1)
		.css('background', '#fc0');
}

function createProcessTable(playersArray) {
	var table = $('#process-table');
	var tr = $('<tr></tr>');
	$('<th></th>')
		.text('#')
		.appendTo(tr);
	for (var i = 0; i < playersArray.length; i++) {
		var playerName = playersArray[i].name;
		$('<th></th>')
			.text(playerName)
			.appendTo(tr);
	}
	$('<thead></thead>')
		.append(tr)
		.appendTo(table);
	$('<tbody></tbody>')
		.appendTo(table);
}

//-->