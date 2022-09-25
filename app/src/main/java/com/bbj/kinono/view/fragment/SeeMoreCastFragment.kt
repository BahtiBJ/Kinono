package com.bbj.kinono.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.StateModel
import com.bbj.kinono.view.MainActivity
import com.bbj.kinono.view.MainViewModel
import com.bbj.kinono.view.adapter.SeeMoreCastListAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SeeMoreCastFragment : Fragment() {

    private val viewModel by sharedViewModel<MainViewModel>()
    private val adapter by lazy { SeeMoreCastListAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_see_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val seeMoreList = view.findViewById<RecyclerView>(R.id.see_more_list)
        seeMoreList.adapter = adapter

        viewModel.liveMovieCast.observe(viewLifecycleOwner){state ->
            when (state){
                is StateModel.Success -> {
                    val castModel = state.data
                    adapter.addAll(castModel)
                }
                is StateModel.Loading -> {}
                is StateModel.Error -> {showError(state.error.localizedMessage ?: "NULL")}
            }
        }
    }

    private fun showError(errorText : String = resources.getString(R.string.error_text)){
        Toast.makeText(requireContext(),errorText, Toast.LENGTH_LONG).show()
    }


}