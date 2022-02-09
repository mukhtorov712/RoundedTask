package uz.pdp.roundedtask.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.pdp.roundedtask.R
import uz.pdp.roundedtask.adapter.CharAdapter
import uz.pdp.roundedtask.databinding.FragmentHomeBinding
import uz.pdp.roundedtask.utils.hide
import uz.pdp.roundedtask.utils.show
import uz.pdp.roundedtask.viewmodel.CharViewModel


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    lateinit var charAdapter: CharAdapter
    private val charViewModel: CharViewModel by viewModels()

    @OptIn(ExperimentalPagingApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()

        binding.refresh.setOnRefreshListener {
            startLoadData()
        }

        startLoadData()

    }

    @ExperimentalPagingApi
    private fun startLoadData() {
        lifecycleScope.launch {
            charViewModel.getCharEntity().collectLatest {
                charAdapter.submitData(it)
            }
        }
    }

    private fun setUpAdapter() {
        charAdapter = CharAdapter(object :CharAdapter.OnCharItemClickListener{
            override fun itemClick(url: String) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        })

        binding.rv.adapter = charAdapter

        charAdapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading){
                if (charAdapter.snapshot().isEmpty()){
                    binding.refresh.isRefreshing = true
                }
            }else{
                binding.refresh.isRefreshing = false
                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error
                    else -> null
                }

                error?.let {
                    if (charAdapter.snapshot().isEmpty()) {
                        Toast.makeText(requireContext(), it.error.localizedMessage, Toast.LENGTH_SHORT).show()
                        binding.animationView.show()
                    }else{
                        binding.animationView.hide()
                    }

                }
            }
        }
    }


}