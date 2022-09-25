package com.bbj.kinono.view.fragment

import android.animation.Animator
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.domain.models.PreviewModel
import com.bbj.kinono.StateModel
import com.bbj.kinono.util.ID_KEY
import com.bbj.kinono.view.NetworkObserver
import com.bbj.kinono.util.dip2px
import com.bbj.kinono.util.isOnline
import com.bbj.kinono.view.MainActivity
import com.bbj.kinono.view.MainViewModel
import com.bbj.kinono.view.NavigateInterface
import com.bbj.kinono.view.adapter.PopularListAdapter
import com.bbj.kinono.view.adapter.OnListItemClick
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SeeMoreMovieFragment : Fragment() {

    private val TAG = "SEEMOREMOVIEFRAGMENT"

    private val viewModel by sharedViewModel<MainViewModel>()

    private var isButtonShown = false
    private var page = 1
    private var pageCount = 0

    private val itemClick by lazy {
        object : OnListItemClick {
            override fun click(id: Int) {
                val bundle = Bundle().apply { putInt(ID_KEY, id) }
                (requireActivity() as NavigateInterface).navigateFromSeeMoreToMovieFragment(bundle)
            }
        }
    }

    private val adapter by lazy { PopularListAdapter(requireContext(), itemClick) }

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
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_see_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val seeMoreButton = view.findViewById<Button>(R.id.see_more_load_other)
        val seeMoreProgressBar = view.findViewById<ProgressBar>(R.id.see_more_progress_bar)
        val seeMoreList = view.findViewById<RecyclerView>(R.id.see_more_list)

        val mScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (page < pageCount) {
                        val mLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val visibleItemCount: Int = mLayoutManager.childCount
                        val totalItemCount: Int = mLayoutManager.itemCount
                        val pastVisibleItems: Int =
                            mLayoutManager.findFirstCompletelyVisibleItemPosition()
                        if (pastVisibleItems + visibleItemCount >= (totalItemCount * 9 / 10) && !isButtonShown) {
                            Log.d(TAG, "COME TO 2/3")
                            moveView(seeMoreButton, View.VISIBLE)
                        } else if (pastVisibleItems + visibleItemCount < (totalItemCount * 9 / 10) && isButtonShown) {
                            Log.d(TAG, "EXIT from 2/3")
                            moveView(seeMoreButton, View.GONE)
                        }
                    }
                }
            }

        seeMoreList.addOnScrollListener(mScrollListener)

        seeMoreList.adapter = adapter

        viewModel.liveSeeMorePopular.observe(viewLifecycleOwner) { state ->
            when (state) {
                is StateModel.Success -> {
                    val popular = state.data
                    pageCount = popular.pagesCount
                    seeMoreProgressBar.visibility = View.GONE
                    adapter.addAll(popular.films as ArrayList<PreviewModel>)
                    if (page > 1)
                        seeMoreList.scrollToPosition(adapter.itemCount * 4 / 5)
                }
                is StateModel.Loading -> {
                    moveView(seeMoreButton, View.GONE)
                    seeMoreProgressBar.visibility = View.VISIBLE
                }
                is StateModel.Error -> {
                    seeMoreProgressBar.visibility = View.GONE
                    --page
                    showError(state.error.localizedMessage ?: "NULL")
                }
            }
        }

        seeMoreButton.setOnClickListener {
            if (page < pageCount && requireContext().isOnline()) {
                viewModel.claimTopFilms(++page)
                if (page == pageCount) {
                    seeMoreProgressBar.visibility = View.GONE
                    seeMoreButton.visibility = View.GONE
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showError(errorText: String = resources.getString(R.string.error_text)) {
        networkObserver.registerCallBack()
        Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
    }

    private fun claimData(){
        viewModel.claimTopFilms(++page)
    }

    private fun moveView(view: View, visibility: Int) {
        if (visibility == View.VISIBLE) {
            isButtonShown = true
            view.visibility = View.VISIBLE
            view.animate().apply {
                setListener(null)
                duration = 300
                translationYBy(dip2px(requireContext(), -140f))
                interpolator = FastOutLinearInInterpolator()
                start()
            }
        } else {
            isButtonShown = false
            view.animate().apply {
                duration = 300
                translationYBy(dip2px(requireContext(), 140f))
                interpolator = FastOutLinearInInterpolator()
                start()
                setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        view.visibility = View.GONE
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        networkObserver.removeCallback()
        super.onDestroy()
    }
}