/*
Main gragment to display all items from backend
 */

package com.kottapa.youtubedemo.overview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.kottapa.youtubedemo.databinding.FragmentGridviewBinding
import com.kottapa.youtubedemo.login.LoginViewModel
import com.kottapa.youtubedemo.login.LoginViewModel.AuthenticationState
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.kottapa.youtubedemo.R
/**
 * This fragment shows the list of images.
 */
class GridViewFragment : Fragment() {

    companion object {
        const val TAG = "GridViewFragment"
    }

    private val loginViewModel by activityViewModels<LoginViewModel>()



    private val gridViewModel by viewModels<GridviewViewModel>()

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the GridviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentGridviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = gridViewModel


        // Sets the adapter of the itemsGrid RecyclerView with clickHandler lambda that
        // tells the viewModel when our item is clicked
        binding.itemsGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {item ->
            gridViewModel.displayItemDetails(item)
        })

        // Observe the navigateToSelectedItem LiveData and Navigate when it isn't null
        // After navigating, call displayItemDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        gridViewModel.navigateToSelectedItem.observe(this, Observer {item ->
            if ( null != item ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(GridViewFragmentDirections.actionShowDetail(item))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                gridViewModel.displayItemDetailsComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()


        val stateObserver = Observer<AuthenticationState>{ authenticationState ->
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> showWelcomeMessage()
                AuthenticationState.UNAUTHENTICATED -> navController.navigate(R.id.loginFragment)
                else -> Log.e(
                    TAG, "New $authenticationState state that doesn't require any UI change"
                )
            }

        }

        loginViewModel.authenticationState.observe(viewLifecycleOwner,stateObserver)
    }//onViewCreated

    private fun showWelcomeMessage() {

    }

}
