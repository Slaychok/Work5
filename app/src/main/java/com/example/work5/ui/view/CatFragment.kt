package com.example.work5.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.example.work5.R
import com.example.work5.databinding.FragmentCatBinding
import com.example.work5.ui.viewmodel.CatViewModel
import com.squareup.picasso.Picasso

class CatFragment : Fragment() {
    private lateinit var binding: FragmentCatBinding
    private val catViewModel: CatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catViewModel.catImageUrl.observe(viewLifecycleOwner) { url ->
            loadImage(url)
        }

        // Загружаем кота из базы данных при запуске
        catViewModel.loadCatFromDb()

        // По клику запрашиваем кота из API
        binding.catBtn.setOnClickListener {
            catViewModel.fetchCat()
        }

    }

    private fun loadImage(imageUrl: String) {
        val imageView: ImageView = binding.catImageView
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.gear_spinner)
            .error(R.drawable.error)
            .into(imageView)
    }
}


