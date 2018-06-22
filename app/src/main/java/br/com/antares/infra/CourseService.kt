package br.com.antares.infra

import android.content.Context
import br.com.antares.domain.course.Course
import br.com.antares.domain.lessons.Lesson
import br.com.antares.domain.register.Register
import br.com.antares.domain.topic.Topic
import com.j256.ormlite.dao.DaoManager

/**
 * Contém as operações referentes a manipulação de Cursos e
 * seus respectivos conteúdos.
 *
 * @author Augusto Santos
 * @version 1.0
 */
class CourseService(context: Context) {

    private val dataBaseHandler = DataBaseHandler(context)
    private val connectionSource = dataBaseHandler.connectionSource
    private val courseDao = DaoManager.createDao(connectionSource, Course::class.java)
    private val topicDao = DaoManager.createDao(connectionSource, Topic::class.java)
    private val lessonDao = DaoManager.createDao(connectionSource, Lesson::class.java)
    private val registerDao = DaoManager.createDao(connectionSource, Register::class.java)

    fun listAllCourses(): MutableList<Course> = courseDao.queryForAll()
    fun listAllTopics(id: Long?): MutableList<Topic> = topicDao.queryBuilder().where().eq("COU_ID", id).query()
    fun listAllLessons(id: Long?): MutableList<Lesson> = lessonDao.queryBuilder().where().eq("TOP_ID", id).query()
    fun listAllRegister(id: Long?): MutableList<Register> = registerDao.queryBuilder().where().eq("LES_ID", id).query()

    fun createOrUpdateCourse(course: Course): Boolean = courseDao.createOrUpdate(course).numLinesChanged == 1
    fun createOrUpdateTopic(topic: Topic): Boolean = topicDao.createOrUpdate(topic).numLinesChanged == 1
    fun createOrUpdateLesson(lesson: Lesson): Boolean = lessonDao.createOrUpdate(lesson).numLinesChanged == 1
    fun createOrUpdateRegister(register: Register): Boolean = registerDao.createOrUpdate(register).numLinesChanged == 1

    fun deleteCourse(course: Course): Boolean = courseDao.delete(course) == 1
    fun deleteTopic(topic: Topic): Boolean = topicDao.delete(topic) == 1
    fun deleteLesson(lesson: Lesson): Boolean = lessonDao.delete(lesson) == 1
    fun deleteRegister(register: Register): Boolean = registerDao.delete(register) == 1

    fun closeConnection() {
        dataBaseHandler.close()
    }

    fun deleteAssociatedFiles() {}

}