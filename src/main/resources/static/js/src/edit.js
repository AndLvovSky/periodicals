$(document).ready(function() {
    $("#edit").addClass("active");
    $('#selectPublication').click(function () {
        var id = $('#publicationId').val();
        getPublication(id, selectPublication);
    });
    $('#deletePublication').click(function () {
        var id = $('#publicationId').val();
        deletePublication(id);
    });
    $('#addPublication').click(addPublication);
    $('#replacePublication').click(function () {
        var id = $('#publicationId').val();
        replacePublication(id);
    });
    $("#showAllPublications").click(updatePublications);
    updatePublications();
});

function updatePublications() {
    getPublications(function(publications) {
        $("#ptbody").html("");
        publications.forEach(function(publication) {
            $("#ptbody").append(createPublicationRow(publication));
        });
    });
}

function selectPublication(publication) {
    $("#ptbody").html("").append(createPublicationRow(publication));
}

function createPublicationRow(publication) {
    return $("<tr></tr>").append(
        createCell(publication["id"]),
        createCell(publication["name"]),
        createCell(publication["period"]),
        createCell(publication["cost"]),
        createCell(publication["description"])
    );
}

function createCell(text) {
    return $("<td></td>").text(text);
}

function getPublication(id, handler) {
    $.ajax({
        url: PUBLICATIONS_URL + id
    }).then(
        function(data) {
            handler(data);
        },
        function(xhr) {
            showInfoMessage(xhr.responseText, false);
        }
    );
}

function getPublications(handler) {
    $.ajax({
        url: PUBLICATIONS_URL
    }).then(function(data) {
        handler(data);
    });
}

function deletePublication(id) {
    $.ajax({
        url: PUBLICATIONS_URL + id,
        method: "DELETE"
    }).then(
        function() {
            showInfoMessage("successfully deleted publication with id " + id);
            updatePublications();
        },
        function(xhr) {
            showInfoMessage(xhr.responseText, false);
        }
    );
}

function addPublication() {
    $.ajax({
        url: PUBLICATIONS_URL,
        method: "POST",
        data: getPublicationData(),
        contentType: "application/json"
    }).then(
        function() {
            showInfoMessage("successfully added publication");
            clearValidationErrors();
            updatePublications();
        },
        validationFailed
    );
}

function replacePublication(id) {
    $.ajax({
        url: PUBLICATIONS_URL + id,
        method: "PUT",
        data: getPublicationData(),
        contentType: "application/json"
    }).then(
        function() {
            showInfoMessage("successfully replaced publication with id " + id);
            updatePublications();
        },
        validationFailed
    );
}

function getPublicationData() {
    var data = {};
    $("form[name=publication]").serializeArray().map(function(input) {
        data[input.name] = input.value;
    });
    return JSON.stringify(data);
}

function showInfoMessage(msg, good) {
    if (good === false) {
        $("#info").addClass("bad");
    }
    $("#info").html(msg).removeClass("d-none");
    setTimeout(function () {
        $("#info").addClass("d-none").removeClass("bad");
    }, 5000);
}

function validationFailed(xhr) {
    showInfoMessage("publication validation failed", false);
    showValidationErrors(JSON.parse(xhr.responseText));
}

function showValidationErrors(errors) {
    clearValidationErrors();
    errors.forEach(function(error) {
        $("#" + error.field + "PublicationError").html(error.defaultMessage);
    })
}

function clearValidationErrors() {
    $(".validationError").html("");
}
