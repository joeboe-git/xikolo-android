package de.xikolo.models.dao

import androidx.lifecycle.LiveData
import de.xikolo.extensions.asCopy
import de.xikolo.extensions.asLiveData
import de.xikolo.managers.UserManager
import de.xikolo.models.User
import de.xikolo.models.dao.base.BaseDao
import io.realm.Realm
import io.realm.kotlin.where

class UserDao(realm: Realm) : BaseDao<User>(User::class, realm) {

    fun current(): LiveData<User> =
        query()
            .equalTo("id", UserManager.userId)
            .findFirstAsync()
            .asLiveData()

    class Unmanaged {
        companion object {

            @JvmStatic
            fun find(id: String?): User? =
                Realm.getDefaultInstance().use { realm ->
                    realm.where<User>()
                        .equalTo("id", id)
                        .findFirst()
                        ?.asCopy()
                }

            @JvmStatic
            val current: User? =
                Realm.getDefaultInstance().use { realm ->
                    realm.where<User>()
                        .equalTo("id", UserManager.userId)
                        .findFirst()
                        ?.asCopy()
                }

        }
    }

}
