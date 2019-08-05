$(document).ready(function() {
    $("#catalog").addClass("active");
    updateBasketItemsNumber();
    loadPublications();
});

function addToBasket(publicationId, number) {
    $.ajax({
        url: BASKET_ITEMS_URL + "/add",
        method: "POST",
        data: JSON.stringify({
            "publicationId": publicationId,
            "number": number
        }),
        contentType: "application/json"
    }).then(
        function() {
            updateBasketItemsNumber();
        }
    )
}

function loadPublications() {
   $.ajax({
       url: PUBLICATIONS_URL
   }).then(
       function(publications) {
           $("#publications").html("");
           publications.forEach(function(publication) {
               $("#publications").append(createPublication(publication));
           });
           $('[data-toggle="tooltip"]').tooltip();
       }
   )
}

function createPublication(publication) {
    return $('<div class="publication list-group-item"></div>')
        .append(createTitle(publication.name))
        .append(createAbout(publication))
        .append(createAddToBasketDiv(publication.id));
}

function createTitle(publicationName) {
    return $('<h3 class="title"></h3>').text(publicationName);
}

function createAbout(publication) {
    return $('<div class="about">')
        .append('<div><span class="period" data-toggle="tooltip" ' + 'data-placement="right"' +
            'title="Time between publications">&pi; - ' + publication.period +' days</span></div>')
        .append($('<div class="cost"></div>').append(formatDollars(publication.cost)))
        .append($('<div class="description mb-3"></div>').text(publication.description));
}

function createAddToBasketDiv(publicationId) {
    return $('<div class="add-to-basket-div justify-content-center d-flex">')
        .append($('<input class="publications-number form-control" type="number" min="1" value="1" ' +
            'id="pn' + publicationId + '">'))
        .append($('<button class="add-to-basket btn btn-primary" type="button" '
            + 'id="ap' + publicationId + '">Add</button>')
            .click(addToBasketCallback(publicationId)));
}

function addToBasketCallback(publicationId) {
    return function() {
        addToBasket(publicationId, $("#pn" + publicationId).val());
    }
}

function formatDollars(money) {
    var dollars = Math.floor(money);
    var cents = Math.round((money - dollars) * 100);
    if (cents < 10) cents = '0' + cents;
    return dollars + '.' + cents + '$';
}
