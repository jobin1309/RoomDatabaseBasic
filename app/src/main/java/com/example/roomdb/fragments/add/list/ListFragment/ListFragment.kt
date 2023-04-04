package com.example.roomdb.fragments.add.list.ListFragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.R
import com.example.roomdb.Util.onQueryTextChange
import com.example.roomdb.fragments.add.list.ListAdapter.ListAdapter
import com.example.roomdb.model.User
import com.example.roomdb.viewModel.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


@Suppress("DEPRECATION")
class ListFragment : Fragment(){

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var myAdapter: ListAdapter;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_list, container, false)


        //RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        myAdapter = ListAdapter()
        recyclerView.adapter = myAdapter;

        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        //UserVIewModel;

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java];

        mUserViewModel.readAllData?.observe(viewLifecycleOwner) { user ->
            myAdapter.setData(user);
        }



        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {

            findNavController().navigate(R.id.action_listFragment_to_addFragment)

        }

        //Add menu
        setHasOptionsMenu(true);

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete_menu, menu);
//        inflater.inflate(R.menu.main_menu, menu);



//

//        val searchItem= menu?.findItem(R.id.menu_search)
////
//        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView;
//
//        searchView.onQueryTextChange { it }

//
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteAllUsers()
        }


        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUsers() {
        val builder = AlertDialog.Builder(requireContext());
        builder.setPositiveButton("Yes") { _, _ ->
            mUserViewModel.deleteAllUsers();
            mUserViewModel.resetId();
            Toast.makeText(
                requireContext(),
                "Removed everything: ",
                Toast.LENGTH_LONG
            ).show()


        }

        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Everything?");
        builder.setMessage("Are you sure you want to delete everything?");
        builder.create().show();

    }




}






