$(document).ready(function() {
    updateBasketItemsNumber();
});

function updateBasketItemsNumber() {
    $.ajax({
        url: BASKET_URL
    }).then(
        function(basket) {
            $("#basketItemsNumber").html(basket.items.length);
        }
    )
}