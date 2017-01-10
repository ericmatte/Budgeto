$(function() {
    $('.sidebar .sidebar-wrapper, .main-panel').perfectScrollbar();
});

type = ['','info','success','warning','danger'];

endless = {

	showNotification: function(from, align, icon, message, type){
    	$.notify({
        	icon: icon || "notifications",
        	message: message
        },{
            type: type,
            timer: 4000,
            placement: {
                from: from,
                align: align
            }
        });
	}

}
