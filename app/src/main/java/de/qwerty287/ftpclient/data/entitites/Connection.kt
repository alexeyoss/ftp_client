package de.qwerty287.ftpclient.data.entitites

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.qwerty287.ftpclient.ui.files.providers.Client
import de.qwerty287.ftpclient.ui.files.providers.Provider
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "connections")
data class Connection(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "server") var server: String,
    @ColumnInfo(name = "port") var port: Int,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "type") var type: Provider,
    @ColumnInfo(name = "implicit") var implicit: Boolean,
    @ColumnInfo(name = "utf8") var utf8: Boolean,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Int = 0
) : Parcelable {
    fun client(): Client {
        val client = type.get()

        client.implicit = implicit
        client.utf8 = utf8
        client.connect(server, port)
        client.login(username, password) // connect to server and login with login credentials

        return client
    }
}
