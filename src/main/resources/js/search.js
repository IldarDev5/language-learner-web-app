/**
 * Created by Ildar on 01.09.2015.
 */

/** HTML element that'll hold found lessons/translation pairs */
var resultsList;
/** The search query entered by user */
var searchText;

$(function() {
    resultsList = $("#results");
});

/** Search lessons using this search query */
function searchLessons(caller) {
    toggleNavTabSelection(caller);

    $.getJSON('/search/lessonInfo', { searchText: searchText, page : 1 }, function(lessons) {
        //Got lesson list as lessons.content, insert into #results
        if(lessons.content.length != 0) {
            //There are lessons corresponding to this search query
            $.each(lessons.content, function (i, lesson) {
                addLessonToResultsList(lesson);
            });
        }
        else {
            //No lessons found, tell user about this
            resultsList.append("<i><br />No lessons found</i>");
        }
    });
}

/** Search translation pairs in this user's lessons using this search query */
function searchTranslations(caller) {
    toggleNavTabSelection(caller);

    $.getJSON('/search/translationInfo', { searchText : searchText, page : 1 }, function(lessons) {
        //lessons.content contains lessons that contain translation pairs, found using this search query.
        //Output each lesson with all translations within it, corresponding to this search query
        if(lessons.content.length != 0) {
            //There are translation pairs corresponding to this search query
            $.each(lessons.content, function(i, lesson) {
                addTranslationsOfThisLessonToResultsList(lesson);
            });
        }
        else {
            //No translation pairs found, tell user about this
            resultsList.append("<i><br />No translations found</i>");
        }
    });
}

function toggleNavTabSelection(caller) {
    $(caller).closest("ul").find("li[class='active']").removeAttr("class");
    $(caller).closest("li").attr("class", "active");

    resultsList.html("");
}

/** Appends to the resultList the specified lesson with its translation pairs suitable to the search query */
function addTranslationsOfThisLessonToResultsList(lesson) {
    var text = "<h3>" + lesson.lessonName + "</h3>";
    text += "<p>Translation pairs found within this lesson that suit the search query:<br /><ul>";
    $.each(lesson.translations, function(i, translation) {
        text += "<li>" + translation.sentence1 + " => " + translation.sentence2 + "</li>";
    });
    text += "</ul></p>";
    text += lessonViewFooter(lesson);
    resultsList.append(text);
}

/** Appends to the resultList the specified lesson suitable to the search query */
function addLessonToResultsList(lesson) {
    var text = "<h3>" + lesson.lessonName + "</h3>";
    text += "<p>" + lesson.description + "</p>";
    text += "<p style='font-size: 12px;'>" +
            "Average grade: <b>" + lesson.averageGrade + "%</b>" +
            "</p>";
    text += lessonViewFooter(lesson);
    resultsList.append(text);
}

function lessonViewFooter(lesson) {
    return "<p class='post-footer align-right'>" +
        "<span>" + lesson.translationsCount + " pair(s) of sentences</span>" +
        "<a href=\"javascript:deleteLesson('" + lesson.lessonId + "', '1')\" class='delete'>Delete</a>" +
        "<a href='/lessons/edit?lessonId=" + lesson.lessonId + "' class='readmore'>Edit lesson</a>" +
        "<a href='/doExercise?lessonId=" + lesson.lessonId + "'>Take lesson</a>" +
        "<span class='date'>" + lesson.addDateStr + "</span>" +
        "</p>";
}




