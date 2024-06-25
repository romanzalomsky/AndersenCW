package com.zalomsky.rickandmorty.features.characters

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.zalomsky.rickandmorty.R
import com.zalomsky.rickandmorty.api.RickAndMortyApi
import com.zalomsky.rickandmorty.databinding.FragmentCharactersBinding
import com.zalomsky.rickandmorty.domain.model.Character
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding
    private lateinit var adapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCharactersBinding.inflate(layoutInflater)

        adapter = CharactersAdapter(emptyList())
        binding.charactersList.layoutManager = LinearLayoutManager(context)
        binding.charactersList.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val rickAndMortyApi = retrofit.create(RickAndMortyApi::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            val characterResponse = rickAndMortyApi.getCharactersList()
            val characterList = characterResponse.results
            adapter.setCharacters(characterList)
        }

        clickers()
        return binding.root
    }

    private fun statusAlertDialog(

    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.status_alert)

            val dialogView = layoutInflater.inflate(R.layout.dialog_sort_status, null)
            builder.setView(dialogView)

            builder.setPositiveButton(R.string.submit) { dialog, which ->
                val radioButtonGroup = dialogView.findViewById<RadioGroup>(R.id.radioButtonGroup)
                val selectedRadioButtonId = radioButtonGroup.checkedRadioButtonId
                val selectedRadioButton =
                    dialogView.findViewById<RadioButton>(selectedRadioButtonId)
                val selectedOption = selectedRadioButton.text.toString()
            }
            builder.setNegativeButton(R.string.cancel) { dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun clickers(
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            binding.sortIcon.setOnClickListener {
                val popupMenu = PopupMenu(requireContext(), binding.sortIcon)
                popupMenu.menuInflater.inflate(R.menu.pop_up_menu, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.all_sort -> {
                            true
                        }

                        R.id.name_sort -> {
                            true
                        }

                        R.id.status_sort -> {
                            statusAlertDialog()
                            true
                        }

                        R.id.species_sort -> {
                            true
                        }

                        R.id.type_sort -> {
                            true
                        }

                        R.id.gender_sort -> {
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            }
            binding.sortedIcon.setOnClickListener {
                adapter.sortCharacters()
            }
        }
    }
}