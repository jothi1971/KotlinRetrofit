package com.kottapa.youtubedemo.overview


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kottapa.youtubedemo.network.ItemApi
import com.kottapa.youtubedemo.network.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

enum class ApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [ImagesGridviewFragment].
 */
class ImagesGridviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<ApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of Items
    // with new values
    private val _items = MutableLiveData<List<Item>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val items: LiveData<List<Item>>
        get() = _items

    // LiveData to handle navigation to the selected property
    private val _navigateToSelectedItem = MutableLiveData<Item>()
    val navigateToSelectedItem: LiveData<Item>
        get() = _navigateToSelectedItem

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getAvailableItems() on init so we can display status immediately.
     */
    init {
        getAvailableItems()
    }

    /**
     * getAvailableItems from server
     */
    private fun getAvailableItems() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            var getItemsDeferred = ItemApi.retrofitService.getItems()
            Log.d("test", "jothi getItemsDeferred  " +getItemsDeferred)

            try {
                _status.value = ApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val listResult = getItemsDeferred.await()
                Timber.i("jothi image result " +listResult)

                _status.value = ApiStatus.DONE
                _items.value = listResult
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _items.value = ArrayList()
            }
        }
    }

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        //cancel download job when fragment closed
        viewModelJob.cancel()
    }



    /**
     * When the item is clicked
     */
    fun displayItemDetails(item: Item) {
        //navigate to selected variable is observed by fragment
        _navigateToSelectedItem.value = item
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedItem is set to null
     */
    fun displayItemDetailsComplete() {
        _navigateToSelectedItem.value = null
    }
}
