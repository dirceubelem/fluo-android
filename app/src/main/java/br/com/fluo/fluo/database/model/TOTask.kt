package br.com.fluo.fluo.database.model

import br.com.fluo.fluo.database.fw.Column
import br.com.fluo.fluo.database.fw.TOBase
import br.com.fluo.fluo.database.fw.Table

@Table(name = "task")
class TOTask : TOBase() {

    @Column(name = "id", jsonName = "id")
    var id: String? = null

    @Column(name = "name", jsonName = "name")
    var name: String? = null

    @Column(name = "idProject", jsonName = "idProject")
    var idProject: String? = null

    @Column(name = "idAccountTo", jsonName = "idAccountTo")
    var idAccountTo: String? = null

    @Column(name = "description", jsonName = "description")
    var description: String? = null

    @Column(name = "tags", jsonName = "tags")
    var tags: String? = null

    @Column(name = "createdAt", jsonName = "createdAt")
    var createdAt: Float? = null

    @Column(name = "priority", jsonName = "priority")
    var priority: Float? = null

}
