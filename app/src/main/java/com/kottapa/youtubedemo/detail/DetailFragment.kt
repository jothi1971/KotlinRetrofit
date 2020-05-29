/*
 *  Display item details
 */

package com.kottapa.youtubedemo.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kottapa.youtubedemo.databinding.FragmentDetailBinding

/**
 * This [Fragment] shows the detailed information about a selected item.
 * It sets this information in the [DetailViewModel], which it gets as a Parcelable item
 * through Jetpack Navigation's SafeArgs.
 */
class DetailFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        val item = DetailFragmentArgs.fromBundle(arguments!!).selectedItem
        val viewModelFactory = DetailViewModelFactory(item, application)
        binding.viewModel = ViewModelProvider(
                this,viewModelFactory).get(DetailViewModel::class.java)

        return binding.root
    }
}