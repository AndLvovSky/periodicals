$(document).ready(function() {
    $("#basket").addClass("active");
    updateBasket();
    $("#clear").click(deleteAllItems);
});

function updateBasket() {
    getBasket(function(basket) {
        getPublications(function(publications) {
            showBasket(basket, publications);
        })
    });
    updateCost();
    updateBasketItemsNumber();
}

function updateCost() {
    getCost(function(cost) {
        if (cost.cents < 10) cost.cents = "0" + cost.cents;
        $("#basketCost").text(cost.dollars + "." + cost.cents + "$");
    })
}

function deleteItem(index) {
    $.ajax({
        url: ORDER_URL + "delete/" + index,
        method: "DELETE"
    }).then(
        function() {
            updateBasket();
        }
    )
}

function deleteAllItems() {
    $.ajax({
        url: ORDER_URL + "delete/",
        method: "DELETE"
    }).then(
        function() {
            updateBasket();
        }
    )
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
        var deleteButton = createDeleteButton();
        $("#basketItems").append(createBasketItem(name, item.number, deleteButton));
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

function createBasketItem(name, number, deleteButton) {
    return $('<tr></tr>').append(
        $('<td></td>').html(name),
        $('<td></td>').html(number),
        $('<td></td>').html(deleteButton));
}

function getPublicationName(id, publications) {
    for (var i = 0; i < publications.length; i++) {
        var publication = publications[i];
        if (publication.id === id) {
            return publication.name;
        }
    }
}

function createDeleteButton() {
    return $('<button type="button" class="btn btn-danger"></button>')
        .html('&#x2715')
        .click(onDeleteItemClicked);
}

function onDeleteItemClicked() {
    deleteItem($(this).parent().parent().index() + 1);
}