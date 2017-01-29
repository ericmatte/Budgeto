$(function() {
    $('.button-collapse').sideNav();
    $('.side-nav').perfectScrollbar();
});


endless = {
	// type = ['','info','success','warning','danger'];
	showNotification: function(from, align, icon, message, type){
    	$.notify({
        	icon: icon || "notifications",
        	message: message
        },{
            type: type,
            timer: 3000,
            placement: {
                from: from,
                align: align
            }
        });
	}
}


/* Google Sign in */
function onLoad() {
    gapi.load('auth2', function() {
        gapi.auth2.init();
    });
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        $.ajax({
            url: "{{ url_for('main_services.logout') }}",
            method: 'post'
        }).done(function() {
            console.log('User signed out.');
            location.reload()
        });
    });
}
/* End of Google Sign in */