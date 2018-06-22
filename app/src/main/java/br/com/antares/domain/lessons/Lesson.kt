package br.com.antares.domain.lessons

import android.os.Parcel
import android.os.Parcelable
import br.com.antares.domain.register.Register
import br.com.antares.domain.topic.Topic
import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

/**
 * Representação de Lições gerenciáveis pela aplicação.
 * Lições são registros realizados em um determinado dia
 * e horário e pertencem a um único tópico.
 *
 * @see br.com.antares.domain.topic.Topic
 *
 * @author Augusto Santos
 * @version 1.0
 */
@DatabaseTable(tableName = "LESSON")
data class Lesson(@DatabaseField(columnName = "LES_ID", generatedId = true) var id: Long? = null,
                  @DatabaseField(columnName = "LES_SUBJECT") var subject: String? = null,
                  @DatabaseField(columnName = "LES_DESCRIPTION") var description: String? = null,
                  @DatabaseField(columnName = "LES_CREATE_DATE", dataType = DataType.DATE_STRING) var create_Date: Date? = null,
                  @DatabaseField(columnName = "TOP_ID", canBeNull = false, foreign = true) var topic: Topic? = null,
                  @ForeignCollectionField(eager = true) var registers: Collection<Register>? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            Date(parcel.readLong()),
            parcel.readParcelable(Topic::class.java.classLoader),
            parcel.createTypedArrayList(Register.CREATOR)
    )

    fun registersQuantity() = registers?.size

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(subject)
        parcel.writeString(description)
        parcel.writeLong(create_Date?.time!!)
        parcel.writeParcelable(topic, flags)
        parcel.writeTypedList(registers?.toList())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Lesson> {

        const val LESSON = "LESSON"
        const val LESSON_LIST = "LESSON_LIST"

        override fun createFromParcel(parcel: Parcel): Lesson {
            return Lesson(parcel)
        }

        override fun newArray(size: Int): Array<Lesson?> {
            return arrayOfNulls(size)
        }
    }

}