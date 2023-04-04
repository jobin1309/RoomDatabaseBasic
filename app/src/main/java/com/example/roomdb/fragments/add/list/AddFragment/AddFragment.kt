package com.example.roomdb.fragments.add.list.AddFragment


import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.roomdb.R
import com.example.roomdb.data.UserDao
import com.example.roomdb.model.User
import com.example.roomdb.viewModel.UserViewModel
import java.io.ByteArrayOutputStream

class AddFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel;
    private lateinit var mImageView: ImageView
    private var selectedImage: Uri? = null
    private var byteArray: ByteArray? = null;


    companion object {
        private const val REQUEST_IMAGE_PICKER = 1
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_add, container, false)


        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java);


        view.findViewById<Button>(R.id.updateBtn).setOnClickListener {
            insertDataToDatabase()
        }

        mImageView = view.findViewById(R.id.profilePic);

        mImageView.setOnClickListener {
            selectImage()
        }


        return view

    }

    private fun selectImage() {
        val intent = Intent();
        intent.action = Intent.ACTION_GET_CONTENT;
        intent.type = "image/*";
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK) {
            selectedImage = data?.data
            mImageView.setImageURI(selectedImage)
        }
    }


    private fun insertDataToDatabase() {
        val firstName = view?.findViewById<EditText>(R.id.updateFirstName)?.text.toString()
        val location = view?.findViewById<EditText>(R.id.updateLocation)?.text.toString()
        val age = view?.findViewById<EditText>(R.id.updateAge)?.text?.toString()?.toIntOrNull()

        if (firstName.isNotEmpty() && location.isNotEmpty() && age != null && selectedImage != null) {
            val inputStream = requireContext().contentResolver.openInputStream(selectedImage!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            byteArray = byteArrayOutputStream.toByteArray()

            val user = User(0, byteArray, firstName, location, age)
            mUserViewModel.addUser(user);
            loadImageFromDatabase(user.id);
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadImageFromDatabase(userId: Int) {
        val user = mUserViewModel.getImage(userId)
        if (user != null) {

            val imageByteArray = byteArray
            if (imageByteArray != null) {
                val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                mImageView.setImageBitmap(bitmap);

            }
        }
    }


}