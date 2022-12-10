package de.qwerty287.ftpclient.data.utils

import de.qwerty287.ftpclient.data.entitites.Connection
import de.qwerty287.ftpclient.data.utils.EntityMapper

class ConnectionEntityMapper : EntityMapper<Connection, Connection> {
    override fun mapToModel(fromModel: Connection): Connection {
        return Connection(
            title = fromModel.title,
            server = fromModel.server,
            port = fromModel.port,
            username = fromModel.username,
            password = fromModel.password,
            type = fromModel.type,
            implicit = fromModel.implicit,
            utf8 = fromModel.utf8
        )
    }
}