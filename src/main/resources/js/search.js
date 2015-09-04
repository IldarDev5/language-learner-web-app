/**
 * Created by Ildar on 01.09.2015.
 */

var resultsList;
var searchText;

$(function() {
    resultsList = $("#results");
});

function searchLessons(caller) {
    toggleNavTabSelection(caller);

    resultsList.html("");
    $.getJSON('/search/lessonInfo', { searchText: searchText, page : 1 }, function(lessons) {
        //Got lessons list as lessons.content, insert into #results
        $.each(lessons.content, function(i, lesson) {
            addLessonToResultsList(lesson);
        });
    });
}

function searchTranslations(caller) {
    toggleNavTabSelection(caller);
}

function toggleNavTabSelection(caller) {
    $(caller).closest("ul").find("li[class='active']").removeAttr("class");
    $(caller).closest("li").attr("class", "active");
}

function addLessonToResultsList(lesson) {
    var text = "<h3>" + lesson.lessonName + "</h3>";
    text += "<p>" + lesson.description + "</p>";
    text += "<p style='font-size: 12px;'>" +
            "Average grade: <b>" + lesson.averageGrade + "%</b>" +
            "</p>";
    text += "<p class='post-footer align-right'>" +
                "<span>" + lesson.translationsCount + " pair(s) of sentences</span>" +
            "<a href=\"javascript:deleteLesson('" + lesson.lessonId + "', '1')\" class='delete'>Delete</a>" +
            "<a href='/lessons/edit?lessonId=" + lesson.lessonId + "' class='readmore'>Edit lesson</a>" +
            "<a href='/doExercise?lessonId=" + lesson.lessonId + "'>Take lesson</a>" +
            "<span class='date'>" + lesson.addDateStr + "</span>" +
            "</p>";
    resultsList.append(text);
}




