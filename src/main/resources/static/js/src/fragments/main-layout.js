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

function formatDollars(money) {
    var dollars = Math.floor(money);
    var cents = Math.round((money - dollars) * 100);
    if (cents < 10) cents = '0' + cents;
    return dollars + '.' + cents + '$';
}