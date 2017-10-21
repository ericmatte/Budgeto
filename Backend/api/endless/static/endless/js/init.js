$(function() {
    // Plugin initialization
    $('.side-nav').perfectScrollbar();
    $('.carousel.carousel-slider').carousel({fullWidth: true});
    $('.carousel').carousel();
    $('.slider').slider();
    $('.parallax').parallax();
    $('.modal').modal();
    $('.scrollspy').scrollSpy();
    $('.button-collapse').sideNav({'edge': 'left'});
    $('select').not('.disabled').material_select();
    $('.datepicker').pickadate({
        selectMonths: true, // Creates a dropdown to control month
        selectYears: 15, // Creates a dropdown of 15 years to control year
        close: 'Accept'
    });
});



endless = {
	post: function(url, data, errorMessage) {
        var call = $.ajax({
            type: "POST",
            url: url,
            data: data
        });
        if (errorMessage != undefined) {
            call.fail(function (jqXHR, textStatus, errorThrown) {
                Materialize.toast(errorMessage, 4000);
            });
        }
        return call;
    	// .done(function( data, textStatus, jqXHR ) {});
        // .fail(function( jqXHR, textStatus, errorThrown ) {});
        // .always(function( data|jqXHR, textStatus, jqXHR|errorThrown ) { });
	},

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
};