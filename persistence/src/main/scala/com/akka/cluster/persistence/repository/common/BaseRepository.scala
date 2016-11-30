package com.akka.cluster.persistence.repository.common

import com.akka.cluster.persistence.model.Entity

trait BaseRepository[T <: Entity] extends Repository[T] with CRUDOps[T]
