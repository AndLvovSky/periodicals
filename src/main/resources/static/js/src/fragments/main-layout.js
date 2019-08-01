const ORDER_URL = "/order/";

$(document).ready(function() {
    updateBasketItemsNumber();
});

function updateBasketItemsNumber() {
    $.ajax({
        url: ORDER_URL + "basket"
    }).then(
        function(basket) {
            $("#basketItemsNumber").html(basket.items.length);
        }
    )
}