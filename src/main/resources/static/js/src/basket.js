const ORDER_URL = "/order/";
const PUBLICATIONS_URL = "/publications/";

$(document).ready(function() {
    updateBasket();
    updateCost();
});

function updateBasket() {
    getBasket(function(basket) {
        getPublications(function(publications) {
            showBasket(basket, publications);
        })
    })
}

function updateCost() {
    getCost(function(cost) {
        if (cost.cents < 10) cost.cents = "0" + cost.cents;
        $("#basketCost").text(cost.dollars + "." + cost.cents + "$");
    })
}

function getCost(handler) {
    $.ajax({
        url: ORDER_URL + "cost"
    }).then(
        function(data) {
            handler(data);
        }
    )
}

function showBasket(basket, publications) {
    $("#basketItems").html("");
    basket.items.forEach(function(item) {
        var name = getPublicationName(item.publicationId, publications);
        $("#basketItems").append(createBasketItem(name, item.number));
    })
}

function getBasket(handler) {
    $.ajax({
        url: ORDER_URL + "basket"
    }).then(
        function(data) {
            handler(data);
        }
    )
}

function getPublications(handler) {
    $.ajax({
        url: PUBLICATIONS_URL
    }).then(
        function(data) {
            handler(data);
        }
    );
}

function createBasketItem(name, number) {
    return $('<tr></tr>').append(
        $('<td></td>').html(name),
        $('<td></td>').html(number));
}

function getPublicationName(id, publications) {
    for (var i = 0; i < publications.length; i++) {
        var publication = publications[i];
        if (publication.id === id) {
            return publication.name;
        }
    }
}