package com.bbj.kinono.view.fragment

import android.animation.LayoutTransition
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bbj.kinono.R
import com.bbj.kinono.data.models.FoundedFilm
import com.bbj.kinono.data.models.SearchResultModel
import com.bbj.kinono.data.models.common.StateModel
import com.bbj.kinono.util.ID_KEY
import com.bbj.kinono.view.NetworkObserver
import com.bbj.kinono.util.isOnline
import com.bbj.kinono.view.MainViewModel
import com.bbj.kinono.view.NavigateInterface
import com.bbj.kinono.view.adapter.OnListItemClick
import com.bbj.kinono.view.adapter.SearchResultAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment(), FilterDialogFragment.OnFilterChanged {

    companion object {
        val FILTER_KEY = "FILTER"
    }

    private val TAG = "SEARCHFRAGMENT"

    private val viewModel by sharedViewModel<MainViewModel>()

    private var pageCount = 0

    private var keyWord = ""
    private var page = 1
    private var countries: Int? = null
    private var genres: Int? = null
    private var ratingFrom = 0
    private var ratingTo = 10
    private var yearFrom = 1900
    private var yearTo = 2100

    private lateinit var chipGroup: ChipGroup

    private val itemClick by lazy {
        object : OnListItemClick {
            override fun click(id: Int) {
                val bundle = Bundle().apply { putInt(ID_KEY, id) }
                (requireActivity() as NavigateInterface).navigateFromMainToMovieFragment(bundle)
            }
        }
    }

    private val adapter by lazy { SearchResultAdapter(requireContext(), itemClick) }

    private val networkObserver: NetworkObserver by lazy {
        NetworkObserver(requireContext(), object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                claimData()
                networkObserver.unregisterRequest()
                super.onAvailable(network)
            }
        })
    }

    private val filterButton by lazy { view?.findViewById<AppCompatImageButton>(R.id.search_filter_button) }
    private val searchField by lazy {view?.findViewById<TextInputEditText>(R.id.search_search_field)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rootView = view.findViewById<ConstraintLayout>(R.id.search_root)
        rootView.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        val searchResultList = view.findViewById<RecyclerView>(R.id.search_result_list)
        val searchProgressBar = view.findViewById<ProgressBar>(R.id.search_progress_bar)
        chipGroup = view.findViewById(R.id.search_chip_group)

        searchResultList.adapter = adapter

        viewModel.liveSearchResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is StateModel.Success<*> -> {
                    val searchResult = (state.data as SearchResultModel)
                    pageCount = searchResult.totalPages
                    Log.d(TAG, "page count = $pageCount currentPage = $page")
                    adapter.addAll(searchResult.items as ArrayList<FoundedFilm>)
                    searchProgressBar.visibility = View.GONE
                    searchResultList.visibility = View.VISIBLE
                }
                is StateModel.Loading -> {
                    searchResultList.visibility = View.INVISIBLE
                    searchProgressBar.visibility = View.VISIBLE
                }
                is StateModel.Error -> {
                    showError()
                    searchProgressBar.visibility = View.GONE
                }
            }
        }

        searchField?.setOnEditorActionListener { v, actionId, keyEvent ->
            val searchRequest = v.text.toString().trim()
            if (actionId == EditorInfo.IME_ACTION_GO) {
                keyWord = searchRequest
                hideKeyboard(requireContext(), requireView())

                adapter.clearList()

                if (requireContext().isOnline()) {
                    claimData()
                } else {
                    keyWord = "   "
                    showError()
                }
                true
            } else false
        }

        filterButton?.setOnClickListener {
            filterButton?.isEnabled = false
            showFilterDialog()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun showFilterDialog() {
        val dialogFragment = FilterDialogFragment(this)
        dialogFragment.arguments = Bundle().apply {
                putIntegerArrayList(
                    FILTER_KEY,
                    arrayListOf(genres, ratingFrom, ratingTo, yearFrom, yearTo)
                )
            }
        chipGroup.removeAllViews()
        hideKeyboard(requireContext(), requireView())
        dialogFragment.show(requireActivity().supportFragmentManager,"dialog")
    }

    private fun claimData() {
        viewModel.claimSearchResult(
            keyWord, page,
            countries,
            genres,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo
        )
    }

    private fun createChip(text: String): Chip {
        val chip = Chip(requireActivity()).apply {
            setText(text)
            isClickable = false
            layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return chip
    }

    private fun addChip(chip: View) {
        chipGroup.addView(chip)
    }

    override fun onFilterChange(
        genreId: Int,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int
    ) {
        Log.d(TAG,"filter changed")
        filterButton?.isEnabled = true
        var isFilterChanged = false
        genres = if (genreId == 0) null else {
            isFilterChanged = true
            addChip(createChip(FilterDialogFragment.genresList[genreId]))
            genreId
        }
        this.ratingFrom = if (ratingFrom == 0) 0 else {
            isFilterChanged = true
            addChip(createChip("с $ratingFrom★"))
            ratingFrom
        }
        this.ratingTo = if (ratingTo == 10) 10 else {
            isFilterChanged = true
            addChip(createChip("до $ratingTo★"))
            ratingTo
        }
        this.yearFrom = if (yearFrom == 1900) 1900 else {
            isFilterChanged = true
            addChip(createChip("с $yearFrom года"))
            yearFrom
        }
        this.yearTo = if (yearTo == 2100) 2100 else {
            addChip(createChip("до $yearTo года"))
            isFilterChanged = true
            yearTo
        }
        if (isFilterChanged && searchField?.text.toString().trim() != "") {
            isFilterChanged = false
            claimData()
        }
    }

    private fun hideKeyboard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showError(errorText: String = resources.getString(R.string.error_text)) {
        networkObserver.registerCallBack()
        Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
    }
}