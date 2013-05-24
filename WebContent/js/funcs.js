function init() {
    map = {
        refresh: refreshMap,
        send: sendMap,
        rows: 0,
        cols: 0,
        cellWidth: 0,
        cellHeight: 0,
        cellText: '<div class="cell"></div>',
        infoDelText: '<span class="action" onclick="removeObject(this.parentNode.parentNode.rowIndex-1);">Удалить</span>',
        riverText: '<div class="river ob_{id}" ></div>',
        obText: '<div class="object_{type} ob_{id}" ></div>',
        arrText: '<div class="arr arr_{dir}"></div>',
        subIndex: [0, 0],
        submenuText: '<table cellpadding="0" cellspacing="0" class="submenu">' +
            '<tr><td onclick="switchOb(0, {f0});"><span>{action0} арку</span></td></tr>' +
            '<tr><td onclick="switchOb(1, {f1});"><span>{action1} сундук</span></td></tr>' +
            '<tr><td onclick="switchOb(2, {f2});"><span>{action2} мага</span></td></tr>' +
            '</table>',
        subActions: ["Добавить", "Удалить"],
        obTypes: ["arch", "gold", "mage"],
        isDrawing: false,
        tempRoute: [],
        objs: [],
        exitText: '<div class="exit"></div>',
        exitIndexes: [0, 0],
        wallText: '<div class="wall" id="{id}" onclick="switchWall(this);"></div>'
    };
}

function isRiverAt(pair) {
    for (var i = 0; i < map.objs.length; ++i) {
        if (map.objs[i].type == "river") {
            var f = false;
            for (var j = 0; j < map.objs[i].route.length; ++j) {
                if ((map.objs[i].route[j][0] == pair[0][0] && map.objs[i].route[j][1] == pair[0][1]) ||
                    (map.objs[i].route[j][0] == pair[1][0] && map.objs[i].route[j][1] == pair[1][1])) {
                    if (f) {
                        return true;
                    } else {
                        f = true;
                    }
                } else {
                    f = false;
                }
            }
        }
    }
    return false;
}

function isWallBetween(pair) {
    if (pair[0][1] == pair[1][1]) {
        var vpair = pair[0][0] < pair[1][0] ? [pair[0], pair[1]] : [pair[1], pair[0]];
        return $("#x" + vpair[1][0] + "-" + vpair[0][1])[0].classList.contains("exist");
    } else {
        var vpair = pair[0][1] < pair[1][1] ? [pair[0], pair[1]] : [pair[1], pair[0]];
        return $("#y" + vpair[1][1] + "-" + vpair[0][0])[0].classList.contains("exist");
    }

}

function switchWall(wall) {
    var xy = $(wall).attr("id").substr(1).split('-');
    var pair = [[0, 0], [0, 0]];
    var horInd = ($(wall).attr("id"))[0] == "x" ? 1 : 0;
    pair[0][horInd] = pair[1][horInd] = parseInt(xy[1]);
    pair[0][1-horInd] = parseInt(xy[0]) - 1;
    pair[1][1-horInd] = parseInt(xy[0]);
    if (isRiverAt(pair)) return;
    var id = 0;
    if (map.objs.length > 0) {
        id = map.objs[map.objs.length-1].id + 1;
    }
    if (wall.classList.contains("exist")) {
        $(wall).removeClass("exist");
        for (var i = 0; i < map.objs.length; ++i) {
            if (map.objs[i].type == "wall" && map.objs[i].triad == $(wall).attr("id")) {
                var infoTable = $("#info")[0];
                infoTable.deleteRow(i+1);
                map.objs.splice(i, 1);
            }
        }
    } else {
        $(wall).addClass("exist");
        $(wall).addClass("ob_"+id);
        map.objs.push({type: "wall", triad: $(wall).attr("id"), id: id});
        addObjectInfo("Стена"+id);
    }
}

function drawWalls() {
    for (var i = 1; i < map.rows; ++i) {
        for (var j = 0; j < map.cols; ++j) {
            $("#map").append(map.wallText.replace("{id}", "x"+i+"-"+j));
            $("#map .wall:last-child").css({
                width: map.cellWidth,
                height: map.cellHeight * 0.15,
                top: (i-0.075)*map.cellHeight,
                left: j*map.cellWidth
            });
        }
    }
    for (var i = 1; i < map.cols; ++i) {
        for (var j = 0; j < map.rows; ++j) {
            $("#map").append(map.wallText.replace("{id}", "y"+i+"-"+j));
            $("#map .wall:last-child").css({
                width: map.cellWidth * 0.15,
                height: map.cellHeight,
                left: (i-0.075)*map.cellHeight,
                top: j*map.cellWidth
            });
        }
    }
}

function showSubMenu(ev) {
    var rem = $("#map")[0];
    if ($(rem).find(".submenu").length > 0) {
        rem.removeChild($(rem).find(".submenu")[0]);
    }
    $(this).addClass("withSubMenu");
    var subIndex = $(this).index();
    var rowIndex = Math.floor(subIndex / map.cols);
    var colIndex = subIndex % map.cols;
    map.subIndexes = [rowIndex, colIndex];
    var acts = [0, 0, 0];
    for (var i = 0; i < map.objs.length; ++i) {
        if (map.objs[i].type == "arch" && map.objs[i].pos[0] == rowIndex && map.objs[i].pos[1] == colIndex) {
            acts[0] = 1;
        } else if (map.objs[i].type == "gold" && map.objs[i].pos[0] == rowIndex && map.objs[i].pos[1] == colIndex) {
            acts[1] = 1;
        } else if (map.objs[i].type == "mage" && map.objs[i].pos[0] == rowIndex && map.objs[i].pos[1] == colIndex) {
            acts[2] = 1;
        }
    }
    var st = map.submenuText;
    for (var i = 0; i < 3; ++ i) {
        st = st.replace("{action"+i+"}", map.subActions[acts[i]]).replace("{f"+i+"}", acts[i]);
    }
    $("#map").append(st);
    var h = $("table.submenu").height();
    $("table.submenu").css({
        top: rowIndex*map.cellHeight + ev.originalEvent.layerY - 5,
        left: colIndex*map.cellWidth + ev.originalEvent.layerX - 5,
        height: 0
    }).css("display", "block");
    $("table.submenu").stop(true);
    $("table.submenu").animate({height: h}, 200);
}

function hideSubMenu() {
    $("table.submenu").css("display", "none");
    $("#map .cell").removeClass("withSubMenu");

}

function moveExit(indexes) {
    $("#map .exit").css({
        width: map.cellWidth * 0.2,
        height: map.cellHeight * 0.2,
        top: (indexes[0]+0.1)*map.cellHeight,
        left: (indexes[1]+0.7)*map.cellWidth
    });
    $("#map .exit").css("display", "block");
}

function removeObject(index) {
    if (map.objs[index].type == "wall") {
        $("#"+map.objs[index].triad).click();
        return;
    }
    var infoTable = $("#info")[0];
    infoTable.deleteRow(index+1);
    while ($("#map .ob_"+map.objs[index].id).length > 0) {
        var rem = $("#map")[0];
        rem.removeChild($(rem).find(".ob_"+map.objs[index].id)[0]);
    }
    map.objs.splice(index, 1);
}

function refreshMap() {
    $("#map").html("");
    map.rows = parseInt($("#irow").val());
    map.cols = parseInt($("#icol").val());
    map.cellWidth = $("#map").width() / map.cols;
    map.cellHeight = $("#map").height() / map.rows;
    for (var i = 0; i < map.rows; ++i) {
        for (var j = 0; j < map.cols; ++j) {
            $("#map").append(map.cellText);
            $("#map .cell:last-child").css({top: i*map.cellHeight, left: j*map.cellWidth});
        }
    }
    $("#map").append(map.exitText);
    drawWalls();
    $("#map .cell").mousedown(beginDrawing).mouseup(finishDrawing).mouseenter(moveDrawing).dblclick(showSubMenu);
    map.objs = [];
    var infoTable = $("#info")[0];
    while (infoTable.rows.length > 1) {
        infoTable.deleteRow(1);
    }
    cellsAppear(0);
}

function sendMap() {
    alert("Здесь отсылается карта!");
}

function beginDrawing(ev) {
    var rowIndex = Math.floor($(this).index() / map.cols);
    var colIndex = $(this).index() % map.cols;
    if (ev.ctrlKey) {
        map.exitIndexes = [rowIndex, colIndex];
        moveExit(map.exitIndexes);
        return;
    }
    var f = false;
    for (var i = 0; i < map.objs.length; ++i) {
        if (map.objs[i].type == "river") {
            for (var j = 0; j < map.objs[i].route.length; ++j) {
                if (rowIndex == map.objs[i].route[j][0] && colIndex == map.objs[i].route[j][1]) {
                    f = true;
                    break;
                }
            }
            if (f) break;
        }
    }
    if (!f && !map.isDrawing) {
        map.isDrawing = true;
        map.tempRoute = [[rowIndex, colIndex]];
        drawRiver1(rowIndex, colIndex);
    }
}

function drawRiver1(i, j) {
    var id = 0;
    if (map.objs.length > 0) {
        id = map.objs[map.objs.length-1].id + 1;
    }
    $("#map").append(map.riverText.replace("{id}", id));
    $("#map .river:last-child").mouseup(finishDrawing);
    $("#map .river:last-child").css({
        top: (i+0.3)*map.cellHeight,
        left: (j+0.3)*map.cellWidth,
        width: 0.4*map.cellWidth,
        height: 0.4*map.cellHeight
    });
    $("#map .river:last-child").css("display", "block");
}

function drawRiver2(i1, j1, i2, j2) {
    var id = 0;
    if (map.objs.length > 0) {
        id = map.objs[map.objs.length-1].id + 1;
    }
    $("#map").append(map.riverText.replace("{id}", id));
    $("#map .river:last-child").mouseup(finishDrawing);
    if (i1 == i2) {
        if (j1 < j2) {
            $("#map .river:last-child").css({
                top: (i1+0.3)*map.cellHeight,
                left: (j1+0.7)*map.cellWidth,
                width: 0.6*map.cellWidth,
                height: 0.4*map.cellHeight
            });
            $("#map .river:last-child").html(map.arrText.replace("{dir}", "r"));
        } else {
            $("#map .river:last-child").css({
                top: (i2+0.3)*map.cellHeight,
                left: (j2+0.7)*map.cellWidth,
                width: 0.6*map.cellWidth,
                height: 0.4*map.cellHeight
            });
            $("#map .river:last-child").html(map.arrText.replace("{dir}", "l"));
        }
    } else {
        if (i1 < i2) {
            $("#map .river:last-child").css({
                top: (i1+0.7)*map.cellHeight,
                left: (j1+0.3)*map.cellWidth,
                width: 0.4*map.cellWidth,
                height: 0.6*map.cellHeight
            });
            $("#map .river:last-child").html(map.arrText.replace("{dir}", "b"));
        } else {
            $("#map .river:last-child").css({
                top: (i2+0.7)*map.cellHeight,
                left: (j2+0.3)*map.cellWidth,
                width: 0.4*map.cellWidth,
                height: 0.6*map.cellHeight
            });
            $("#map .river:last-child").html(map.arrText.replace("{dir}", "t"));
        }
    }
    $("#map .river:last-child").css("display", "block");
    drawRiver1(i2, j2);
}

function moveDrawing() {
    hideSubMenu();
    if (map.isDrawing) {
        var rowIndex = Math.floor($(this).index() / map.cols);
        var colIndex = $(this).index() % map.cols;
        var last = map.tempRoute[map.tempRoute.length-1];
        if (isWallBetween([last, [rowIndex, colIndex]])) return;
        if ((rowIndex == last[0] && Math.abs(colIndex - last[1]) == 1) || (Math.abs(rowIndex - last[0]) == 1 && colIndex == last[1])) {
            if (map.tempRoute.length > 1) {
                if (rowIndex == map.tempRoute[map.tempRoute.length-2][0] && colIndex == map.tempRoute[map.tempRoute.length-2][1]) return;
            }
            map.tempRoute.push([rowIndex, colIndex]);
            drawRiver2(map.tempRoute[map.tempRoute.length-2][0], map.tempRoute[map.tempRoute.length-2][1], rowIndex, colIndex);
            var f = false;
            for (var i = 0; i < map.objs.length; ++i) {
                if (map.objs[i].type == "river") {
                    for (var j = 0; j < map.objs[i].route.length; ++j) {
                        if (rowIndex == map.objs[i].route[j][0] && colIndex == map.objs[i].route[j][1]) {
                            f = true;
                            break;
                        }
                    }
                    if (f) break;
                }
            }
            for (var i = 0; i < map.tempRoute.length - 1; ++i) {
                if (map.tempRoute[i][0] == rowIndex && map.tempRoute[i][1] == colIndex) {
                    f = true;
                    break;
                }
            }
            if (f) finishDrawing();
        }
    }
}

function finishDrawing() {
    if (map.isDrawing) {
        map.isDrawing = false;
        var id = 0;
        if (map.objs.length > 0) {
            id = map.objs[map.objs.length-1].id + 1;
        }
        if (map.tempRoute.length > 1) {
            map.objs.push({type: "river", route: map.tempRoute, id: id});
            addObjectInfo("Река"+id);
        } else if(map.tempRoute.length == 1) {
            var rem = $("#map")[0];
            rem.removeChild($(rem).find(".ob_"+id)[0]);
        }

    }
}

function drawOb(ob, indexes, id) {
    $("#map").append(map.obText.replace("{id}", id).replace("{type}", ob));
    $("#map .ob_"+id).css({
        width: map.cellWidth * 0.2,
        height: map.cellHeight * 0.2,
        top: (indexes[0]+0.1)*map.cellHeight,
        left: (indexes[1]+0.1)*map.cellWidth,
        opacity: 0
    });
    if (ob > 0) $("#map .ob_"+id).css({top: (indexes[0]+0.7)*map.cellHeight});
    if (ob > 1) $("#map .ob_"+id).css({left: (indexes[1]+0.7)*map.cellWidth});
    $("#map .ob_"+id).css("display", "block");
    $("#map .ob_"+id).stop(true);
    $("#map .ob_"+id).animate({opacity: 1}, 250);
}

function switchOb(ob, f) {
    if (f == 0) {
        var id = 0;
        if (map.objs.length > 0) {
            id = map.objs[map.objs.length-1].id + 1;
        }
        map.objs.push({type: map.obTypes[ob], pos: map.subIndexes, id: id});
        var tex = ["Арка", "Сундук", "Маг"];
        addObjectInfo(tex[ob]+id);
        drawOb(ob, map.subIndexes, id);
    } else {
        var types
        for (var i = 0; i < map.objs.length; ++i) {
            if (map.objs[i].type == map.obTypes[ob] && map.objs[i].pos[0] == map.subIndexes[0] && map.objs[i].pos[1] == map.subIndexes[1]) {
                removeObject(i);
                return;
            }
        }
    }
}

function showObject() {
    var index = this.rowIndex - 1;
    $("#map div").removeClass("active");
    $("#map .ob_"+map.objs[index].id).addClass("active");
}

function hideObjects() {
    $("#map div").removeClass("active");
}

function addObjectInfo(obj) {
    var infoTable = $("#info")[0];
    var lastRow = infoTable.insertRow($("#info tr").length);
    $(lastRow).mouseenter(showObject).mouseleave(hideObjects);
    var tmp = lastRow.insertCell(0);
    $(tmp).html(obj);
    tmp = lastRow.insertCell(1);
    $(tmp).html(map.infoDelText);
}

function cellsAppear(index) {
    if (index < map.rows * map.cols) {
        var temp = $("#map .cell")[index];
        $(temp).css("display", "block");
        $(temp).animate({
            width: map.cellWidth,
            height: map.cellHeight
        }, 500);
        setTimeout(function(){cellsAppear(index + 1)}, 700 / (map.rows * map.cols))
    }
}