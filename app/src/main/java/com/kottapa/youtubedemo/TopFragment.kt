package com.kottapa.youtubedemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.kottapa.youtubedemo.databinding.FragmentTopBinding
import com.kottapa.youtubedemo.login.LoginViewModel

/**
 * A simple [Fragment] subclass.
 */
class TopFragment : Fragment() {

    companion object {
        const val TAG = "TopFragment"
        const val FIREBASE_SIGN_IN_REQUEST_CODE = 1001
    }

    private lateinit var binding: FragmentTopBinding

    // Get a reference to the ViewModel
    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //recomanded way of binding
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_top, container, false)


       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()

        binding.loginButton.setOnClickListener { launchSignInFlow() }
        binding.downloadButton.setOnClickListener { downloadImages() }
        binding.profileButton.setOnClickListener { updateProfile() }
        binding.locationButton.setOnClickListener { displayLocation() }

    }

    private fun downloadImages() {

        val action = TopFragmentDirections.actionTopFragmentToOverviewFragment()
        findNavController().navigate(action)
    }

    private fun updateProfile() {

    }

    private fun displayLocation() {
        val action = TopFragmentDirections.actionTopFragmentToLocationFragment()
        findNavController().navigate(action)
    }

    private fun launchSignInFlow() {

        //Authentication using Email and Google account
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch the sign-in intent.
        // We listen to the response of this activity with the
        // FIREBASE_SIGN_IN_REQUEST_CODE.
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            FIREBASE_SIGN_IN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FIREBASE_SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in.
                Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                // Sign in failed. If response is null, the user canceled the
                // sign-in flow using the back button. Otherwise, check
                // the error code and handle the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    private fun observeAuthenticationState() {

        val stateObserver = Observer<LoginViewModel.AuthenticationState>{ newState ->
            when (newState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {

                    binding.loginButton.text = getString(R.string.logout_button_text)
                    binding.loginButton.setOnClickListener {
                        AuthUI.getInstance().signOut(requireContext())
                    }
                }//authenticated
                    LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {

                        binding.loginButton.text = getString(R.string.login_button_text)
                        binding.loginButton.setOnClickListener {
                            launchSignInFlow()
                        }

                    }//unauthenticated

                }
            }
        //observe authentication state
        loginViewModel.authenticationState.observe(viewLifecycleOwner,stateObserver)


    }

}
