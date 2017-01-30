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