const ORDER_URL = "/order/";
const PUBLICATIONS_URL = "/publications/";

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