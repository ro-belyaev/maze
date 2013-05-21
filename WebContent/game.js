//<!--

$(function() {
    var goActionIsSelected = 0;
    var shootActionIsSelected = 0;
    var directionElements = $('#right, #left, #up, #down');
    var allElements = directionElements.add('#go, #shoot, #drop, #pick-up, #predict, #exit');
    function disableElements(elementSet) {
        elementSet.attr('disabled', 'disabled');
    }
    function enableElements(elementSet) {
        elementSet.removeAttr('disabled');
    }

    disableElements(directionElements);

    $('#actions').on('click', function(event) {
    	var target = $(event.target);
    	var targetId = target.attr('id');
        if(targetId == 'go') {
            if(goActionIsSelected) {
                enableElements(allElements);
                disableElements(directionElements);
                goActionIsSelected = 0;
            } else {
                disableElements(allElements);
                enableElements(directionElements.add($(event.target)));
                goActionIsSelected = 1;
            }
        } else if(targetId == 'shoot') {
            if(shootActionIsSelected) {
                enableElements(allElements);
                disableElements(directionElements);
                shootActionIsSelected = 0;
            } else {
                disableElements(allElements);
                enableElements(directionElements.add($(event.target)));
                shootActionIsSelected = 1;
            }
        } else if(targetId == 'right' || targetId == 'left' || targetId == 'up' || targetId == 'down') {
            if(goActionIsSelected || shootActionIsSelected) {
                var action = (goActionIsSelected) ? 'go' : 'shoot';
                $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    data: {
                        uid: $('#user-id').val(),
                        gameId: $('#game-id').val(),
                        action: action,
                        direction: targetId
                    },
                    url: 'MakeAction'
                })
                    .done(function() {
                        goActionIsSelected = 0;
                        shootActionIsSelected = 0;
                        enableElements(allElements);
                        disableElements(directionElements);
                    })
                    .fail(function() {
                        alert('ajax request is failed');
                    });
            }
        } else if(targetId == 'draw' || targetId == 'generate' || targetId == 'stop') {
            $.ajax({
                type: 'GET',
                data: {
                    action: targetId,
                    gameId: $('#game-id').val()
                },
                url: 'MakeAction'
            })
                .fail(function() {
                    alert('ajax request for draw, generate or stop action is failed');
                });
        }
    });
});

//-->