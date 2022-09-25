package com.bbj.kinono.view.fragment

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.StateModel
import com.bbj.kinono.util.ID_KEY
import com.bbj.kinono.view.NetworkObserver
import com.bbj.kinono.util.isOnline
import com.bbj.kinono.view.MainViewModel
import com.bbj.kinono.view.NavigateInterface
import com.bbj.kinono.view.adapter.PopularListAdapter
import com.bbj.kinono.view.adapter.OnListItemClick
import com.bbj.kinono.view.adapter.PosterListAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private val TAG = "HOMEFRAGMENT"

    private var isRequestData = false

    private val viewModel by sharedViewModel<MainViewModel>()

    private val itemClick by lazy {
        object : OnListItemClick {
            override fun click(id: Int) {
                val bundle = Bundle().apply { putInt(ID_KEY, id) }
                (requireActivity() as NavigateInterface).navigateFromMainToMovieFragment(bundle)
            }
        }
    }

    private val premiereListAdapter by lazy { PosterListAdapter(requireContext(), itemClick) }
    private val popularAdapter by lazy { PopularListAdapter(requireContext(), itemClick) }

    private val networkObserver : NetworkObserver by lazy {
        NetworkObserver(requireContext(), object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                claimData()
                networkObserver.unregisterRequest()
                super.onAvailable(network)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val premiereList: RecyclerView = view.findViewById(R.id.premiere_list)
        premiereList.adapter = premiereListAdapter

        val seeMoreButton: Button = view.findViewById<Button?>(R.id.popular_see_more).apply {
            setOnClickListener {
                (requireActivity() as NavigateInterface).navigateFromMainToSeeMoreMovieFragment()
            }
        }

        viewModel.livePremiere.observe(viewLifecycleOwner) { state ->
            when (state) {
                is StateModel.Success -> {
                    premiereListAdapter.addAll(state.data)
                }
                is StateModel.Loading -> {}
                is StateModel.Error -> {
                    showError(state.error.localizedMessage ?: "NULL")
                }
            }
        }

        val popularList: RecyclerView = view.findViewById(R.id.popular_list)
        popularList.adapter = popularAdapter

        viewModel.livePopular.observe(viewLifecycleOwner) { state ->
            when (state) {
                is StateModel.Success -> {
                    seeMoreButton.isEnabled = true
                    popularAdapter.initData(state.data.films)
                }
                is StateModel.Loading -> {}
                is StateModel.Error -> {
                    seeMoreButton.isEnabled = false
                    showError(state.error.localizedMessage ?: "NULL")
                }
            }
        }

        if (requireContext().isOnline()) {
            claimData()
        } else {
            showError()
        }
    }

    private fun claimData(){
        viewModel.claimPremiereList()
        viewModel.claimPopularList()
    }

    private fun showError(errorText: String = resources.getString(R.string.error_text)) {
        networkObserver.registerCallBack()
        isRequestData = true
        Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        networkObserver.removeCallback()
        super.onDestroy()
    }

}