/**
 * Created by Ildar on 06.08.2015.
 */

$(function() {
    var form = $("form");
    var coq = form.find("#countOfQuestions");
    form.find("#uniqueQuestions1").change(function() {
        var isChecked = this.checked;
        if(isChecked) {
            coq.val(translationsCount);
            coq.prop('disabled', true);
        }
        else {
            coq.prop('disabled', false);
        }
    });
});