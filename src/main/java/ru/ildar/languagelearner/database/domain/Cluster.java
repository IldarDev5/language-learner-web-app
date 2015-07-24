package ru.ildar.languagelearner.database.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"language1_name", "language2_name", "user_id"})
})
public class Cluster
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clusterId;

    @ManyToOne
    @JoinColumn(name = "language1_name", nullable = false)
    private Language language1;

    @ManyToOne
    @JoinColumn(name = "language2_name", nullable = false)
    private Language language2;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLessonAddDate;


    public Long getClusterId()
    {
        return clusterId;
    }

    public void setClusterId(Long clusterId)
    {
        this.clusterId = clusterId;
    }

    public Language getLanguage1()
    {
        return language1;
    }

    public void setLanguage1(Language language1)
    {
        this.language1 = language1;
    }

    public Language getLanguage2()
    {
        return language2;
    }

    public void setLanguage2(Language language2)
    {
        this.language2 = language2;
    }

    public AppUser getAppUser()
    {
        return appUser;
    }

    public void setAppUser(AppUser appUser)
    {
        this.appUser = appUser;
    }

    public Date getLastLessonAddDate()
    {
        return lastLessonAddDate;
    }

    public void setLastLessonAddDate(Date lastLessonAddDate)
    {
        this.lastLessonAddDate = lastLessonAddDate;
    }
}
