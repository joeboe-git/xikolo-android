package de.xikolo.network.jobs

import android.util.Log
import de.xikolo.config.Config
import de.xikolo.network.ApiService
import de.xikolo.network.jobs.base.NetworkJob
import de.xikolo.network.jobs.base.NetworkStateLiveData
import de.xikolo.network.sync.Sync
import ru.gildor.coroutines.retrofit.awaitResponse

class ListDatesJob(networkState: NetworkStateLiveData, userRequest: Boolean) : NetworkJob(networkState, userRequest, Precondition.AUTH) {

    companion object {
        val TAG: String = ListDatesJob::class.java.simpleName
    }

    override suspend fun onRun() {
        val response = ApiService.instance.listDates().awaitResponse()

        if (response.isSuccessful) {
            if (Config.DEBUG) Log.i(TAG, "Dates received")

            Sync.Data.with(response.body()!!).run()

            success()
        } else {
            if (Config.DEBUG) Log.e(TAG, "Error while fetching date list")
            error()
        }
    }

}
