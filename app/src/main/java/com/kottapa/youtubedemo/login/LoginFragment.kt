package com.kottapa.youtubedemo.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.kottapa.youtubedemo.R
import com.kottapa.youtubedemo.TopFragment
import com.kottapa.youtubedemo.databinding.FragmentLoginBinding
import com.kottapa.youtubedemo.login.LoginViewModel.AuthenticationState

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
        const val FIREBASE_SIGN_IN_REQUEST_CODE = 1001

    }

    private lateinit var binding: FragmentLoginBinding

    private val loginViewModel by activityViewModels<LoginViewModel>()



    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //recomanded way of binding
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authButton.setOnClickListener { launchSignInFlow() }

        navController = findNavController()

        //when back button presses then go to top fragment
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true ) {
                override fun handleOnBackPressed() {
                   // loginViewModel.refuseAuthentication()
                    navController.popBackStack(R.id.topFragment, false)

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)



        // Observe the authentication state so we can know if the user has logged in successfully.
// If the user has logged in successfully, bring them back to the profile screen.
// If the user did not log in successfully, display an error message.
        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                // Since our login flow is only one screen instead of multiple
                // screens, we can utilize popBackStack(). If our login flow
                // consisted of multiple screens, we would have to call
                // popBackStack() multiple times.
                AuthenticationState.AUTHENTICATED -> navController.popBackStack()
                AuthenticationState.INVALID_AUTHENTICATION -> showErrorMessage()
                else -> Log.e(
                    TAG,
                    "Authentication state that doesn't require any UI change $authenticationState"
                )
            }
        })


    }

    private fun showErrorMessage() {
    }


    private fun launchSignInFlow() {

        //Authentication using Email and Google account
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch the sign-in intent.
        // We listen to the response of this activity with the
        // SIGN_IN_REQUEST_CODE.
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
        if (requestCode == TopFragment.FIREBASE_SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in.
                Log.i(TopFragment.TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                // Sign in failed. If response is null, the user canceled the
                // sign-in flow using the back button. Otherwise, check
                // the error code and handle the error.
                Log.i(TopFragment.TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }




}


