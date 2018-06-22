package br.com.antares.domain.course

import android.os.Parcel
import android.os.Parcelable
import br.com.antares.domain.topic.Topic
import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

/**
 * Representação de um curso gerenciável pela aplicação.
 *
 * @author Augusto Santos
 * @version 1.0
 */
@DatabaseTable(tableName = "COURSE")
data class Course(@DatabaseField(columnName = "COU_ID", generatedId = true) var id: Long? = null,
                  @DatabaseField(columnName = "COU_NAME") var name: String? = null,
                  @DatabaseField(columnName = "COU_INSTITUTION") var institution: String? = null,
                  @DatabaseField(columnName = "COU_DESCRIPTION") var description: String? = null,
                  @DatabaseField(columnName = "COU_COVER_PATH") var coverPath: String? = null,
                  @ForeignCollectionField var topics: ForeignCollection<Topic>? = null) : Parcelable {

    constructor(parcel: Parcel) : this (
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(institution)
        parcel.writeString(description)
        parcel.writeString(coverPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Course> {

        const val COURSE = "COURSE"
        const val COURSE_LIST = "COURSE_LIST"

        override fun createFromParcel(parcel: Parcel): Course {
            return Course(parcel)
        }

        override fun newArray(size: Int): Array<Course?> {
            return arrayOfNulls(size)
        }
    }
}