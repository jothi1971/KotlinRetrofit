

package com.kottapa.youtubedemo.detail

import android.app.Application
import androidx.lifecycle.*
import com.kottapa.youtubedemo.network.Item

/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [Item].
 */
class DetailViewModel(item: Item,
                      app: Application) : AndroidViewModel(app) {

    // The internal MutableLiveData for the selected item
    private val _selectedItem = MutableLiveData<Item>()

    // The external LiveData for the SelectedItem
    val selectedItem: LiveData<Item>
        get() = _selectedItem

    // Initialize the _selectedItem MutableLiveData
    init {
        _selectedItem.value = item
    }


    val itemText = item.type


}

