
package com.example.myapplication.ui.catalog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCatalogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment(R.layout.fragment_catalog) {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatalogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCatalogBinding.bind(view)

        setupComposeView()
        observeViewModel()
    }

    private fun setupComposeView() {
        binding.composeView.setContent {
            CatalogScreen(
                onCartClick = { /* Handle cart click */ },
                onProductClick = { /* Handle product click */ }
            )
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.flowers.collectLatest { flowers ->
                // Update Compose with new flowers data
                updateFlowersInCompose(flowers)
            }
        }

        lifecycleScope.launch {
            viewModel.categories.collectLatest { categories ->
                // Update Compose with new categories
                updateCategoriesInCompose(categories)
            }
        }
    }

    private fun updateFlowersInCompose(flowers: List<com.example.myapplication.model.Flower>) {
        // This would be handled by Compose recomposition with state
        binding.composeView.setContent {
            CatalogScreen(
                flowers = flowers,
                categories = viewModel.categories.value,
                onCartClick = { /* Handle cart click */ },
                onProductClick = { /* Handle product click */ },
                onCategorySelect = { category -> viewModel.selectCategory(category) }
            )
        }
    }

    private fun updateCategoriesInCompose(categories: List<String>) {
        // Compose will automatically recompose when state changes
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}