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

        var translationSentence = translation.val();
        $.ajax({
            url : '/translation/checkTranslation',
            type : 'post',
            contentType : 'application/json',
            data : JSON.stringify({
                translationId : transId,
                translationSentence : translationSentence,
                invertLanguages : invertLangs
            }),
            beforeSend : function(xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
            },
            success : function(resp) {
                if(resp.error) {
                    alert("Error: " + resp.error);
                }
                else {
                    showModificationsToUser(resp, translationSentence);
                    buttons.find("#submit").show();
                    buttons.find("#check").hide();
                }
            }
        });
    });
});

function showColorsDefinition(insertSpan, deleteSpan, replaceSpan, endSpan) {
    var ul = $("#colorsDefinition").find("ul");
    var insertLi = "<li>" + insertSpan + "Word" + endSpan + " - Symbols inserted</li>";
    var deleteLi = "<li>" + deleteSpan + "Word" + endSpan + " - Symbols to delete</li>";
    var replaceLi = "<li>" + replaceSpan + "Word" + endSpan + " - Symbols replaced</li>";
    ul.html(insertLi + deleteLi + replaceLi);
}

function showModificationsToUser(resp, sentence) {
    var modifs = resp.difference.modifications;
    var howMuchAdd = resp.difference.howMuchAdd;
    var correctSentence = resp.difference.correctSentence;

    var resultHtmlStr = "";
    var differenceMsg = $("#differenceMsg");

    var insertSpan = "<span style='background-color: #0abd1a;'>";
    var deleteSpan = "<span style='background-color: red;'>";
    var replaceSpan = "<span style='background-color: #3dc8ff'>";
    var endSpan = "</span>";

    showColorsDefinition(insertSpan, deleteSpan, replaceSpan, endSpan);

    for(index in modifs) {
        var indexModif = modifs[index];

        var i = indexModif.index;
        if(i == -1) continue;
        for(m in indexModif.modifications) {
            var modif = indexModif.modifications[m];

            switch(modif.modifOperation) {
                case "SYMBOLS_EQUAL":
                    resultHtmlStr += sentence[i];
                    break;
                case "DELETE" :
                    resultHtmlStr += deleteSpan + sentence[i] + endSpan;
                    break;
                case "INSERT" :
                    resultHtmlStr += insertSpan + modif.symbol + endSpan;
                    break;
                case "REPLACE" :
                    resultHtmlStr += replaceSpan + modif.symbol + endSpan;
                    break;
                case "DELETE_ALL" :
                    resultHtmlStr = deleteSpan;
                    for(var t = 0;t <= i;t++) {
                        resultHtmlStr += sentence[i];
                    }
                    resultHtmlStr += endSpan;
                    break;
                case "INSERT_ALL" :
                    resultHtmlStr = insertSpan;
                    for(var t = 0;t < howMuchAdd;t++) {
                        resultHtmlStr += correctSentence[t];
                    }
                    resultHtmlStr += endSpan;
                    break;
            }
        }
    }

    differenceMsg.html(resultHtmlStr);
    $("#translateData").hide();
    $("#resultData").show();
}








