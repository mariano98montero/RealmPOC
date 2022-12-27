package com.example.realmpoc.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realmpoc.R
import com.example.realmpoc.adapter.DogsAdapter
import com.example.realmpoc.databinding.ActivityMainBinding
import com.example.realmpoc.entity.Dog
import com.example.realmpoc.util.Utils.ALREADY_EXISTS
import com.example.realmpoc.util.Utils.EMPTY_STRING
import com.example.realmpoc.viewmodel.MainViewModel

interface DogListener {
    fun deleteDog(position: Int, dogName: String)
}

class MainActivity : AppCompatActivity(), DogListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var dogList: ArrayList<Dog> = ArrayList()
    private lateinit var dogAdapter: DogsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
        viewModel.getLiveData().observe({ lifecycle }, ::updateUI)
    }

    private fun updateUI(data: MainViewModel.MainData) {
        when (data.status) {
            MainViewModel.MainStatus.DATA_SUCCESS -> initDogList(data.dogList)
            MainViewModel.MainStatus.DATA_FAILURE -> showToast(data.errorMessage)
            MainViewModel.MainStatus.DELETE_OK -> dogAdapter.updateList(dogList)
            MainViewModel.MainStatus.SAVE_OK -> viewModel.getData()
            MainViewModel.MainStatus.SHOW_LOADER -> binding.loader.visibility = View.VISIBLE
            MainViewModel.MainStatus.HIDE_LOADER -> binding.loader.visibility = View.GONE
        }
    }

    private fun showToast(errorMessage: String) {
        if (errorMessage.equals(ALREADY_EXISTS)) {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.name_already_added_error),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.no_dogs_added_yet_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initDogList(dogs: List<Dog>) {
        if (dogList.isEmpty()) {
            binding.emptyStateImage.visibility = View.GONE
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.setHasFixedSize(true)
            dogAdapter = DogsAdapter(dogs as ArrayList<Dog>, this)
            binding.recyclerView.adapter = dogAdapter
        } else {
            dogAdapter.updateList(dogs as ArrayList<Dog>)
        }
        dogList = dogs
    }

    override fun deleteDog(position: Int, dogName: String) {
        dogList.removeAt(position)
        viewModel.deleteDog(dogName)
    }

    private fun setListeners() {
        viewModel.getData()
        with(binding) {
            addDogButton.setOnClickListener {
                if (binding.dogNameField.text.isNotBlank() && binding.dogAgeField.text.isNotBlank()) {
                    viewModel.addDog(
                        Dog(
                            name = binding.dogNameField.text.toString(),
                            age = Integer.parseInt(binding.dogAgeField.text.toString())
                        )
                    )
                    binding.dogNameField.setText(EMPTY_STRING)
                    binding.recyclerView.requestFocus()
                    binding.dogAgeField.setText(EMPTY_STRING)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.main_activity_empty_field_warning),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
