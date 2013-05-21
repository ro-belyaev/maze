//<!--

$(function() {
    $('#send').click(function() {
    	var rows = map.rows;
        var cols = map.cols;
        var configurationXml = '';
//        var boardElements = [];
        configurationXml += addWall(rows, cols);
//        boardElements.push(addWall(rows, cols));
        configurationXml += addBoardSize(rows, cols);
//        boardElements.push(addBoardSize(rows, cols));
        configurationXml += addExit(rows, cols);
//        boardElements.push(addExit(rows, cols));
        configurationXml += addMageAndArch(rows, cols);
//        boardElements.push(addMageAndArch(rows, cols));
        configurationXml += addTreasure(rows, cols);
//        boardElements.push(addTreasure(rows, cols));
        configurationXml += addRiver(rows, cols);
//        boardElements.push(addRiver(rows, cols));
        configurationXml += addPlayers();
        
        configurationXml = element('configuration', configurationXml, {});
//        boardElements.push(addPlayers());
//        console.log(boardElements);
        $.ajax({
            type: 'GET',
            url: 'MakeAction',
            data: {
                action: 'generate',
                gameXml: configurationXml
            }
        });
        console.log(configurationXml);
    });
});

function addPlayers() {
	var players = '';
	$('.player-container')
        .each(function() {
            $this = $(this);
            var x = parseInt($this.find('#select-x').val());
            x--;
            var y = parseInt($this.find('#select-y').val());
            y--;
            var name = $this.find('.player-cell').children('span').eq(1).text();
            players += element('element', '', {pointX: (x).toString(), pointY: (y).toString(), name: name});
        });
    return element('boardElement', players, {type: 'player'});
}

function addBoardSize(rows, cols) {
    return element('boardElement', element('element', '',
        {pointX: (cols).toString(), pointY: (rows).toString()}), {type: 'boardSize'});
}

function addExit(rows, cols) {
    var x = map.exitIndexes[1];
    var y = convertYCoordinate(map.exitIndexes[0], rows);
    var innerElem = element('element', '', {pointX: (x).toString(), pointY: (y).toString()});
    return element('boardElement', innerElem, {type: 'exit'});
}

function addWall(rows, cols) {
    var wallElements = '';
    for (var id in map.objs) {
        var obj = map.objs[id];
        var firstX, firstY, secondX, secondY;
        if (obj.type == 'wall') {
        	var axis = obj.triad.substr(0, 1);
            var triad = obj.triad.substring(1).split('-');
            if (axis == 'x') {
                firstX = secondX = parseInt(triad[1]);
                firstY = convertYCoordinate(parseInt(triad[0]) - 1, rows);
                secondY = convertYCoordinate(parseInt(triad[0]), rows);
            } else {
                firstX = parseInt(triad[0]) - 1;
                secondX = parseInt(triad[0]);
                firstY = secondY = convertYCoordinate(parseInt(triad[1]), rows);
            }
            var firstInnerElem = element('element', '', {pointX: (firstX).toString(), pointY: (firstY).toString()});
            var secondInnerElem = element('element', '', {pointX: (secondX).toString(), pointY: (secondY).toString()});
            wallElements += element('boardElement', firstInnerElem + secondInnerElem, {type: 'wall'});
        }
    }
    return wallElements;
}

function addMageAndArch(rows, cols) {
    var mageInnerElement = '',
        archInnerElement = '';
    for (var id in map.objs) {
        var obj = map.objs[id];
        if (obj.type == 'mage' || obj.type == 'arch') {
            var x = obj.pos[1];
            var y = convertYCoordinate(parseInt(obj.pos[0]), rows);
            var innerElement = element('element', '', {pointX: (x).toString(), pointY: (y).toString()});
            if (obj.type == 'mage') {
                mageInnerElement += innerElement;
            } else {
                archInnerElement += innerElement;
            }
        }
    }
    var mageElement = '',
        archElement = '';
    if (mageInnerElement != '') {
        mageElement += element('boardElement', mageInnerElement, {type: 'mage'});
    }
    if (archInnerElement != '') {
        archElement = element('boardElement', archInnerElement, {type: 'arch'});
    }
    return mageElement + archElement;
}

function addTreasure(rows, cols) {
    var treasureInnerElement = '';
    for (var id in map.objs) {
        var obj = map.objs[id];
        if (obj.type == 'gold') {
            var real = 'true';
            var x = obj.pos[1];
            var y = convertYCoordinate(parseInt(obj.pos[0]), rows);
            treasureInnerElement += element('element', '', {pointX: (x).toString(), pointY: (y).toString(), real: real});
        }
    }
    var treasureElement = '';
    if (treasureInnerElement != '') {
        treasureElement = element('boardElement', treasureInnerElement, {type: 'treasure'});
    }
    return treasureElement;
}

function addRiver(rows, cols) {
    var allRiverElements = '';
    for (var id in map.objs) {
        var obj = map.objs[id];
        if(obj.type == 'river') {
            var innerRiverElement = '';
            var route = obj.route;
            for (var i = 0; i < route.length; i++) {
                var someElement = obj.route[i];
                var x = someElement[1];
                var y = convertYCoordinate(parseInt(someElement[0]), rows);
                innerRiverElement += element('element', '', {pointX: (x).toString(), pointY: (y).toString(), step: '1'});
            }
            allRiverElements += element('boardElement', innerRiverElement, {type: 'river'});
        }
    }
    return allRiverElements;
}


function convertYCoordinate(y, rows) {
    return rows - 1 - y;
}

//-->