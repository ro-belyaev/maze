//<!--

$(function() {
    var movesInTable = 0;
    var state = {
        players: [
            {
                name: 'Roman',
                turn: 1,
                moves: [
                        ['go up',
                        'smash into wall'],
                        
                        ['shoot left',
                        'miss']
                ]
            },
            {
                name: 'Lob',
                turn: 2,
                moves: [
                        ['under arch',
                        'shoot up',
                        'miss'],
                        
                        ['go right',
                        'go to 1 cell right']
                ]
            },
            {
                name: 'Karo',
                turn: 3,
                moves: [
                        ['shoot down',
                        'miss']
                ]
            }
        ],
        movesCount: 5,
        playersCount: 3
    };
    refreshTable(movesInTable, state);
});

function refreshTable(movesInTable, stateObj) {
    var playersCount = stateObj.playersCount;
    var movesCount = stateObj.movesCount;
    while (movesCount - movesInTable > 0) {
        var currentPlayer = movesInTable % playersCount;
        console.log(currentPlayer);
        var moveOfCurrentPlayer = Math.floor(movesInTable/playersCount);
        var ul = $('<ul></ul>');
        var moves = stateObj.players[currentPlayer].moves[moveOfCurrentPlayer];
        for (var i = 0; i < moves.length; i++) {
        	$('<li></li>')
        		.text(moves[i])
        		.appendTo(ul);
        }
        
        movesInTable++;
        var numOfMoveCell = $('<td></td>')
            .text(movesInTable);
        var row = $('<tr></tr>')
            .append(numOfMoveCell);
        for(var i = 0; i < currentPlayer; i++) {
            row.append('<td></td>');
        }
        $('<td></td>')
            .append(ul)
            .appendTo(row);
        for(var i = currentPlayer + 1; i < playersCount; i++) {
            row.append('<td></td>');
        }
        $('#process-table tr:last')
            .after(row);
        console.log(row);
    }
    return movesInTable;
}
//-->