package com.akka.cluster.persistence.repository

import com.akka.cluster.persistence.repository.common.BaseRepository
import com.akka.cluster.persistence.model.User
import com.typesafe.config.ConfigFactory

class UserRepositoryImpl extends UserRepository {
  override val collectionName: String = ConfigFactory.load().getString("db.collections.user")
}

trait UserRepository extends BaseRepository[User]

object UserRepository {
  def apply() = new UserRepositoryImpl
}
