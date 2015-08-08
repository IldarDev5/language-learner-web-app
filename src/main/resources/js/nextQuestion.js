/**
 * Created by Ildar on 08.08.2015.
 */

$(function() {
    var buttons = $("#buttons");
    var translation = $("#translation");
    buttons.find("#submit").hide();

    buttons.find("#check").on('click', function() {
        /* Send AJAX request to the server with the translation data
           Server must check user nickname, use the algorithm to verify
           correctness of the translation, and send grading results back */

        $.ajax({
            url : '/translations/checkTranslation',
            type : 'POST',
            contentType : 'application/json',
            data : JSON.stringify({
                translationId : transId,
                translation : translation.val(),
                invertLanguages : invertLangs
            }),
            beforeSend : function(xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
            },
            success : function(resp) {
                //TODO: Write this section
                alert("OK");
            }
        });
    });
});