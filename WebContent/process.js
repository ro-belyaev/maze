//<!--

$(function() {
    var movesInTable = 0;
    var state = {
        players: {
            0: {
                name: 'Roman',
                turn: 1,
                moves: {
                    0: {
                        action: 'go up',
                        result: 'smash into wall'
                    },
                    1: {
                        action: 'shoot left',
                        result: 'miss'
                    }
                }
            },
            1: {
                name: 'Lob',
                turn: 2,
                moves: {
                    0: {
                        action: 'shoot up',
                        result: 'miss'
                    },
                    1: {
                        action: 'go right',
                        result: 'go to 1 cell right'
                    }
                }
            },
            2: {
                name: 'Karo',
                turn: 3,
                moves: {
                    0: {
                        action: 'shoot down',
                        result: 'miss'
                    }
                }
            }
        },
        movesCount: 5,
        playersCount: 3
    };
    refreshTable(movesInTable, state);
});

function refreshTable(movesInTable, stateObj) {
    var playersCount = stateObj.playersCount;
    var movesCount = stateObj.movesCount;
    while(movesCount - movesInTable > 0) {
        var currentPlayer = movesInTable % playersCount;
        console.log(currentPlayer);
        var moveOfCurrentPlayer = Math.floor(movesInTable/playersCount);
        var ul = $('<ul></ul>');
        $('<li></li>')
            .text(stateObj.players[currentPlayer].moves[moveOfCurrentPlayer].action)
            .appendTo(ul);
        $('<li></li>')
            .text(stateObj.players[currentPlayer].moves[moveOfCurrentPlayer].result)
            .appendTo(ul);
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
        $('#process-table tr:first')
            .after(row);
        console.log(row);
    }
    return movesInTable;
}
//-->