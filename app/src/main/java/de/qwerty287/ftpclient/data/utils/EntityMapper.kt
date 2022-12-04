package de.qwerty287.ftpclient.data.utils

interface EntityMapper<DomainModel, ForeignModel> {
    fun mapToModel(fromModel: ForeignModel): DomainModel
}