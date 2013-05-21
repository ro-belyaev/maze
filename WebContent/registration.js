//<!--

$(function() {
    $('#reg').on('click', function() {
    	console.log('click');
        var name = $('#name').val();
        var pass = $('#pass').val();
        if(name != '' && pass != '') {
            $.ajax({
                type: 'GET',
                url: 'RegisterUser',
                data: {
                    name: name,
                    password: pass
                },
                dataType: 'text'
            })
                .done(function(id) {
                    console.log(id);
                })
                .fail(function() {
                    alert('Error while sending ajax-request to server');
                });
        }
    });
});

//-->