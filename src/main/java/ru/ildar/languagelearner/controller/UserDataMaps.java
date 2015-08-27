package ru.ildar.languagelearner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ildar.languagelearner.service.AppUserService;
import ru.ildar.languagelearner.service.ClusterService;
import ru.ildar.languagelearner.service.LessonService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataMaps
{
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private LessonService lessonService;

    private static final int CLUSTERS_TO_TAKE = 5;
    private static final int LESSONS_TO_TAKE = 5;

    public Map<String, Object> getNonAuthUserData()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("registeredPeopleCount", appUserService.getTotalUsersCount());
        map.put("popularClusters", clusterService.getMostPopularClusters(CLUSTERS_TO_TAKE));
        map.put("clusterLessonInfos", clusterService.getAvgLessonsCountOfClusters(CLUSTERS_TO_TAKE));
        return map;
    }

    public Map<String, Object> getAuthUserData(Principal principal)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("lessonsForsaken",
                lessonService.getLessonsNotTakenLongestTime(LESSONS_TO_TAKE, principal.getName()));
        return map;
    }
}
