package br.com.antares.domain.topic

import android.os.Parcel
import android.os.Parcelable
import br.com.antares.domain.course.Course
import br.com.antares.domain.lessons.Lesson
import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

/**
 * Representação dos Tópicos gerenciáveis pela aplicação.
 * Um ou mais tópicos definem a grade de um determinado curso.
 *
 * @see br.com.antares.domain.course.Course
 *
 * @author Augusto Santos
 * @version 1.0
 */
@DatabaseTable(tableName = "TOPIC")
data class Topic(@DatabaseField(columnName = "TOP_ID", generatedId = true) var id: Long? = null,
                 @DatabaseField(columnName = "TOP_NAME") var name: String? = null,
                 @DatabaseField(columnName = "TOP_PROFESSOR_NAME") var professorName: String? = null,
                 @DatabaseField(columnName = "TOP_COVER_PATH") var coverPath: String? = null,
                 @DatabaseField(canBeNull = false, foreign = true, columnName = "COU_ID") var course: Course? = null,
                 @ForeignCollectionField var lessons: ForeignCollection<Lesson>? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Course::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(professorName)
        parcel.writeString(coverPath)
        parcel.writeParcelable(course, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Topic> {

        const val TOPIC = "TOPIC"
        const val TOPIC_LIST = "TOPIC_LIST"

        override fun createFromParcel(parcel: Parcel): Topic {
            return Topic(parcel)
        }

        override fun newArray(size: Int): Array<Topic?> {
            return arrayOfNulls(size)
        }
    }
}