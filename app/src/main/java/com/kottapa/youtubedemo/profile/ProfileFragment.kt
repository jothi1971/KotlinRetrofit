package com.kottapa.youtubedemo.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.impl.ImageCaptureConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController


import com.google.android.material.snackbar.Snackbar

import com.kottapa.youtubedemo.R
import com.kottapa.youtubedemo.databinding.FragmentLocationBinding
import com.kottapa.youtubedemo.databinding.FragmentProfileBinding
import com.kottapa.youtubedemo.location.LocationFragment
import com.kottapa.youtubedemo.location.LocationFragmentDirections
import com.kottapa.youtubedemo.location.LocationViewModel
import com.kottapa.youtubedemo.location.REQUEST_LOCATION_PERMISSION_CODE
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val REQUEST_CAMERA_PERMISSION_CODE = 22

class ProfileFragment : Fragment() {

    lateinit var userPhotoPath: String

    companion object {
        private const val TAG = "ProfileFragment"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private val REQUEST_IMAGE_CAPTURE = 1

    }

    private lateinit var binding: FragmentProfileBinding

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)
        binding.profileViewModel = viewModel
        binding.setLifecycleOwner(this)

        binding.cameraImageButton.setOnClickListener(){
            if(allPermissionsGranted()) {
                TakePhoto()
            } else {
                requestPermission()
            }
        }

      //  binding.phonenumberEditText.error = "test error"
        //  binding.phonenumberEditText.error = null

        return binding.root
    }



    fun requestPermission() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.CAMERA)){

            Snackbar.make(
                binding.profilelayout,
                R.string.camera_permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    requestCameraPermission()
                }
                .show()
        } else {
            requestCameraPermission()
        }
    }

    fun requestCameraPermission() {
        Log.v(TAG,"jothi request camera permission " )

        requestPermissions(
            REQUIRED_PERMISSIONS, REQUEST_CAMERA_PERMISSION_CODE)

    }




    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            REQUEST_CAMERA_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.v(LocationFragment.TAG,"jothi location permission granted " )
                    TakePhoto()
                } else {
                    //Respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }


    }

    private fun TakePhoto() {
        Log.v(TAG, "jothi take photo")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            //when photofile is not null
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.kottapa.youtubedemo.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        scaleImage()
        }
    }

    //scale and set image
    private fun scaleImage() {
        // Get the dimensions of the View
        val targetW: Int = binding.userImageView.width
        val targetH: Int = binding.userImageView.height
        
       // Log.v(TAG, "jothi outwidth " +targetW)
      //  Log.v(TAG, "jothi outHeight " +targetH)
        

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(userPhotoPath, bmOptions)?.also { bitmap ->
            binding.userImageView.setImageBitmap(bitmap)
        }


    }


    //store file in App's private storage.
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
    //    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val timeStamp: String = "jothiface"

        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            userPhotoPath = absolutePath
        }
    }





}
