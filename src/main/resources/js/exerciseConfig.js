/**
 * Created by Ildar on 06.08.2015.
 */

$(function() {
    var form = $("form");
    var coq = form.find("#countOfQuestions");
    coq.val(25);
    form.find("#uniqueQuestions1").change(function() {
        var isChecked = this.checked;
        if(isChecked) {
            coq.val(translationsCount);
        }
    });

    form.find("#countOfQuestions").bind('input', function() {
        form.find("#uniqueQuestions1").prop('checked', false);
    });
});