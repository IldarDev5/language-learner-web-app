#Language Learner Web Application

##Brief description
The application allows users to create sort of vocabularies - pairs of sentences in some languages,
and to study them, making exercises. Application will be sequentially showing sentences, and user
has to enter the translation he made for this sentence previously. Every completed exercise is evaluated
and the results are saved and shown to the user.

##Terminology
1) Cluster - cluster is a container identified by two languages(language pair). For example, "English=>German"
is one cluster, "Russian=>English" is another one.
2) Lesson - each cluster contains a set of lessons. Lesson is a container for translation pairs. Each of two
sentences of a translation pair corresponds to one of the languages of the cluster the lesson is located in.
3) Translation pair - two sentences(or words, in particular) in two different languages, one sentence is the
translation of the other in its language

##Business logic

###Database
PostgreSQL DBMS is used in this project. There are 5 tables and 5 functions in the project.
Tables are created by Hibernate from JPA entities, located in the ru.ildar.languagelearner.database.domain
package. Functions(some of them are used as triggers) are specified in data.sql file i.e. executed
automatically whenever this application is launched.
Tables:
* APP_USER - stores information about registered users;
* CLUSTER - stores created clusters of each user;
* LANGUAGE - languages supported by the system. By default, 3 languages are added into the table(in data.sql)
 - Russian, German, and English.
* LESSON - Lessons created by users pertaining to specific clusters;
* Translation - stores translation pairs, each pertaining to some lesson of some user.

Functions:
* check_languages_existence() - checks if there are languages in the LANGUAGE table. If the table is not
there, adds a set of basic languages.
* update_last_lesson_add_date() - trigger function, updates fields last_lesson_add_date and lessons_count 
of a cluster when a new lesson is added
* update_lessons_count_when_lesson_deleted() - trigger function, decrements the lessons_count field of a cluster
when some lesson is deleted
* get_top_popular_clusters(limit_count) - used for showing statistics to unauthenticated users - 
returns first limit_count of the top popular cluster definitions among users
* get_avg_lessons_count_of_clusters(limit_count) - used for showing statistics to unauthenticated users - 
returns first limit_count of the cluster definitions with the biggest average count of lessons

###Roles
There's only one role in this application - User, since users don't interact with each other(at the moment)
there's no need for admin or moderator.

###Showing statistics to a user
Both unauthenticated and authenticated users can see some statistics about the website. Authenticated
users see statistics regarding only them.
Unauthenticated users see:
* How many people are registered on the website
* Most popular clusters - sorted by how many users have created the clusters
* The average count of lessons in the same clusters(i.e. the same language pairs among users)
Authenticated users see:
* Forsaken lessons - the lessons that the user didn't exercise the longest time.
This statistics is shown on the right bar of the HTML layout.

###Search
Authenticated user can search for lessons or translations. By default search is performed for lessons.
Lesson searching consists of checking name and description of lessons using the specified search query.
Searching for translations consists of checking both sentences of each translation pair using the 
specified search query. Translation search results are shown, grouped by lessons, in which found translations
reside.

###Exercising
After creating a cluster and lesson(s) for it, user can exercise with any of created lessons. Exercise
is configurable - user can select how many questions there should be in the test, sentence in what language 
of the two must be shown(and, subsequently, the sentence in the other language is to be entered as a translation),
whether or not to show a list of sentences and translations for repetition before starting the exercise.
Correctness of each entered translation is evaluated by an algorithm that uses dynamic programming technique,
and wrongly entered places in the sentence are shown to the user.

##Technical details
The application is written using Spring Boot, but is deployed not with embedded Tomcat container, but
with a standalone version.

##Unit testing
Some parts of this application(some of the controllers and services) are covered by unit tests, some are not.

##Used technologies
The following technologies were used in writing this application:
* Java SE 1.8
* JPA/Hibernate ORM
* Hibernate validator
* Spring - Core, Boot, WebFlow, MVC, AOP, Data JPA, Security
* JavaScript, jQuery
* Thymeleaf
* Apache Tiles
* OpenShift (this application can be deployed to OpenShift)
* Maven
* jUnit, Mockito, Spring MVC Test