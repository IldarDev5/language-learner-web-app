/**
 * Created by Ildar on 04.08.2015.
 */

function deleteLesson(lessonId, page) {
    var removeLessonForm = $('#removeLessonForm');
    removeLessonForm.prop('action', '/lessons/removeLesson?page=' + page + '&lessonId=' + lessonId);
    removeLessonForm.submit();
}