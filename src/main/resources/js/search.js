/**
 * Created by Ildar on 01.09.2015.
 */

function searchLessons(caller) {
    toggleNavTabSelection(caller);
}

function searchTranslations(caller) {
    toggleNavTabSelection(caller);
}

function toggleNavTabSelection(caller) {
    $(caller).closest("ul").find("li[class='active']").removeAttr("class");
    $(caller).closest("li").attr("class", "active");
}