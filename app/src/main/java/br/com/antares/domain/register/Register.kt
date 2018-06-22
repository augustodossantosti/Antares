package br.com.antares.domain.register

import android.os.Parcel
import android.os.Parcelable
import br.com.antares.domain.lessons.Lesson
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Representação de um determinado tipo de registro, como uma imgagem
 * ou um vídeo, realizado para uma lição específica.
 *
 * @see Lesson
 *
 * @author Augusto Santos
 * @version 1.0
 */
@DatabaseTable(tableName = "REGISTER")
data class Register(@DatabaseField(generatedId = true, columnName = "REG_ID") var id: Long? = null,
                    @DatabaseField(columnName = "REG_TYPE", dataType = DataType.ENUM_STRING) var type: Type? = null,
                    @DatabaseField(columnName = "REG_ORDER") var order: Int? = null,
                    @DatabaseField(columnName = "REG_DESCRIPTION") var description: String? = null,
                    @DatabaseField(columnName = "REG_FILEPATH") var filepath: String? = null,
                    @DatabaseField(columnName = "LES_ID", canBeNull = false, foreign = true) var lesson: Lesson? = null) : Parcelable {

    constructor(parcel: Parcel) : this (
            parcel.readValue(Long::class.java.classLoader) as? Long,
            Type.valueOf(parcel.readString()),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(type?.name)
        parcel.writeValue(order)
        parcel.writeString(description)
        parcel.writeString(filepath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Register> {

        const val REGISTER = "REGISTER"
        const val REGISTER_LIST = "REGISTER_LIST"

        override fun createFromParcel(parcel: Parcel): Register {
            return Register(parcel)
        }

        override fun newArray(size: Int): Array<Register?> {
            return arrayOfNulls(size)
        }
    }
}