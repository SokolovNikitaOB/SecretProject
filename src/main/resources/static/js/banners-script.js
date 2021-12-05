$(function(){
    const appendBanner = function(banner){
        let element =  '<li><a href="/banners/'+ banner.id + '" class="special-font-size">' + banner.name + '<a/></li>';
        $('.left-panel ul').prepend(element);

    };

    $("#search-banner").on('input', function(){
        let input = $(this);
        $.get('/banners/search?input=' + input.val().toLowerCase() , function(response){
            $(".left-panel ul").empty();
            for(prop in response){
                appendBanner(response[prop]);
            }
        });
    });

     $(".chosen-select").chosen({
     width: "313.5px",
     placeholder: ""});
});