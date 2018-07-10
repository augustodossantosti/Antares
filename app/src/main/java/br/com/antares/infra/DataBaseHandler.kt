package br.com.antares.infra

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import br.com.antares.domain.course.Course
import br.com.antares.domain.lessons.Lesson
import br.com.antares.domain.register.Register
import br.com.antares.domain.topic.Topic
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

class DataBaseHandler(context: Context) : OrmLiteSqliteOpenHelper(context, "ANTARES.DB",
        null, 1) {

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTable(connectionSource, Course::class.java)
        TableUtils.createTable(connectionSource, Topic::class.java)
        TableUtils.createTable(connectionSource, Lesson::class.java)
        TableUtils.createTable(connectionSource, Register::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?,
                           oldVersion: Int, newVersion: Int) { }

}