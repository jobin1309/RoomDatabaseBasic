package com.example.roomdb

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.text.Editable
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roomdb.fragments.add.list.AddFragment.AddFragment
import com.example.roomdb.model.User
import com.example.roomdb.viewModel.UserViewModel
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>();

    private lateinit var mUserViewModel: UserViewModel;
    private lateinit var mImageView: ImageView
    private var selectedImage: Uri? = null
    private var byteArray: ByteArray? = null;
    private var bitmap: Bitmap? = null;

    companion object {
        private const val REQUEST_IMAGE_PICKER = 1
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.findViewById<EditText>(R.id.updateFirstName).setText(args.currentUser.firstName);
        view.findViewById<EditText>(R.id.updateLocation).setText(args.currentUser.lastName);
        view.findViewById<EditText>(R.id.updateAge).setText(args.currentUser.age.toString());
        view.findViewById<ImageView>(R.id.profilePicUpdate)
            .setImageBitmap(args.currentUser.image.toBitmap())



        view.findViewById<Button>(R.id.updateBtn).setOnClickListener {
            updateItem();
        }

        mImageView = view.findViewById(R.id.profilePicUpdate);
        mImageView.setOnClickListener {
            selectImage()
        }


        //Add Menu
        setHasOptionsMenu(true);

        return view;
    }

    private fun selectImage() {
        val intent = Intent();
        intent.action = Intent.ACTION_GET_CONTENT;
        intent.type = "image/*";
        startActivityForResult(intent, UpdateFragment.REQUEST_IMAGE_PICKER);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UpdateFragment.REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            selectedImage = data?.data
            mImageView.setImageURI(selectedImage)
        }
    }

    private fun updateItem() {

        val firstName = view?.findViewById<EditText>(R.id.updateFirstName)?.text.toString();
        val location = view?.findViewById<EditText>(R.id.updateLocation)?.text.toString();
        val age = Integer.parseInt(view?.findViewById<EditText>(R.id.updateAge)?.text.toString());


        if (firstName.isNotEmpty() && location.isNotEmpty() && age != null && selectedImage != null) {
            val inputStream = requireContext().contentResolver.openInputStream(selectedImage!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            byteArray = byteArrayOutputStream.toByteArray()

            val user = User(args.currentUser.id, byteArray, firstName, location, age)
            mUserViewModel.updateUser(user);
            loadImageFromDatabase(user.id)
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_update_Fragment_to_listFragment);
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_LONG).show()
        }


    }

    private fun loadImageFromDatabase(userId: Int) {
        val user = mUserViewModel.getImage(userId)
        if (user != null) {

            val imageByteArray = byteArray
            if (imageByteArray != null) {
                bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                mImageView.setImageBitmap(bitmap);

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.delete_menu, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_delete) {
            deleteUser();
        }
        return super.onOptionsItemSelected(item)

    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext());
        builder.setPositiveButton("Yes") { _, _ ->
            mUserViewModel.deleteUser(args.currentUser);
            Toast.makeText(
                requireContext(),
                "Successfully removed: ${args.currentUser.firstName}",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.action_update_Fragment_to_listFragment);

        }

        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentUser.firstName}?");
        builder.setMessage("Are you sure you want to delete ${args.currentUser.firstName}");
        builder.create().show();

    }
}

private fun ByteArray?.toBitmap(): Bitmap? {
    if (this == null) {
        return null
    }
    return BitmapFactory.decodeByteArray(this, 0, size)
}

