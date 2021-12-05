$(function(){
    const appendCategory = function(category){
        let element =  '<li><a href="/categories/'+ category.id + '" class="special-font-size">' + category.name + '<a/></li>';
        $('.left-panel ul').prepend(element);

    };

    $("#search-category").on('input', function(){
        let input = $(this);
        $.get('/categories/search?input=' + input.val().toLowerCase() , function(response){
            $(".left-panel ul").empty();
            for(prop in response){
                appendCategory(response[prop]);
            }
        });
    });

});