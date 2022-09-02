package com.bbj.kinono.view.fragment

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.data.models.FoundedFilm
import com.bbj.kinono.data.models.SearchResultModel
import com.bbj.kinono.data.models.common.StateModel
import com.bbj.kinono.util.ID_KEY
import com.bbj.kinono.util.dip2px
import com.bbj.kinono.util.isOnline
import com.bbj.kinono.view.MainViewModel
import com.bbj.kinono.view.NavigateInterface
import com.bbj.kinono.view.adapter.OnListItemClick
import com.bbj.kinono.view.adapter.SearchResultAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment() {

    private val TAG = "SEARCHFRAGMENT"

    private val viewModel by sharedViewModel<MainViewModel>()

    private var pageCount = 0
    private var page = 1

    private var keyWord = ""

    private val itemClick by lazy {
        object : OnListItemClick {
            override fun click(id: Int) {
                val bundle = Bundle().apply { putInt(ID_KEY, id) }
                (requireActivity() as NavigateInterface).navigateFromMainToMovieFragment(bundle)
            }
        }
    }

    private val adapter by lazy { SearchResultAdapter(requireContext(), itemClick) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchField = view.findViewById<EditText>(R.id.search_search_field)
        val searchResultList = view.findViewById<RecyclerView>(R.id.search_result_list)
        val searchProgressBar = view.findViewById<ProgressBar>(R.id.search_progress_bar)

        searchResultList.adapter = adapter

        viewModel.liveSearchResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is StateModel.Success<*> -> {
                    val searchResult = (state.data as SearchResultModel)
                    pageCount = searchResult.pagesCount
                    Log.d(TAG,"page count = $pageCount currentPage = $page")
                    adapter.addAll(searchResult.films as ArrayList<FoundedFilm>)
                    searchProgressBar.visibility = View.GONE
                    searchResultList.visibility = View.VISIBLE
                }
                is StateModel.Loading -> {
                    searchResultList.visibility = View.INVISIBLE
                    searchProgressBar.visibility = View.VISIBLE
                }
                is StateModel.Error -> {
                    throw state.error
                    searchProgressBar.visibility = View.GONE
                }
            }
        }

        searchField.setOnEditorActionListener { v, actionId, keyEvent ->
            val searchRequest = v.text.toString().trim()
            if (actionId == EditorInfo.IME_ACTION_GO && searchRequest != keyWord) {
                keyWord = searchRequest
                hideKeyboard(requireContext(), requireView())

                adapter.clearList()

                if (requireContext().isOnline()) {
                    viewModel.claimSearchResult(keyWord)
                } else {
                    keyWord = "   "
                    showError()
                }
                true
            } else false
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun hideKeyboard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showError(errorText : String = resources.getString(R.string.error_text)){
        Toast.makeText(requireContext(),errorText,Toast.LENGTH_LONG).show()
    }

}