package com.bbj.kinono.view.fragment

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bbj.kinono.R
import com.bbj.kinono.domain.models.FactsModel
import com.bbj.kinono.StateModel
import com.bbj.kinono.util.ID_KEY
import com.bbj.kinono.view.NetworkObserver
import com.bbj.kinono.util.isOnline
import com.bbj.kinono.view.MainActivity
import com.bbj.kinono.view.MainViewModel
import com.bbj.kinono.view.NavigateInterface
import com.bbj.kinono.view.adapter.CastListAdapter
import com.bbj.kinono.view.adapter.FactListAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.properties.Delegates

class MovieFragment : Fragment() {

    private val viewModel by sharedViewModel<MainViewModel>()

    private var movieId by Delegates.notNull<Int>()

    private val castListAdapter by lazy {CastListAdapter(requireContext())}

    private val factListAdapter by lazy { FactListAdapter(requireContext()) }

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
        movieId = arguments?.getInt(ID_KEY) ?: 0
        return inflater.inflate(R.layout.fragment_movie,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (requireContext().isOnline()) {
            claimData()
        } else
            showError()

        val background = view.findViewById<ImageView>(R.id.bg)

        val moviePoster = view.findViewById<ShapeableImageView>(R.id.movie_poster)

        val movieName = view.findViewById<TextView>(R.id.movie_name)
        val movieYear = view.findViewById<TextView>(R.id.movie_year)
        val movieCountry = view.findViewById<TextView>(R.id.movie_country)
        val movieGenre = view.findViewById<TextView>(R.id.movie_genre)
        val movieRating = view.findViewById<TextView>(R.id.movie_rating)
        val movieAgeLimit = view.findViewById<TextView>(R.id.movie_age_limit)
        val movieDuration = view.findViewById<TextView>(R.id.movie_duration)
        val movieDescribtion = view.findViewById<TextView>(R.id.movie_describtion)

        val describtionTitle = view.findViewById<TextView>(R.id.description)

        viewModel.liveMovieDetail.observe(viewLifecycleOwner){state ->
            when (state) {
                is StateModel.Success -> {
                    val movieDetail = state.data
                    describtionTitle.visibility = View.VISIBLE
                    movieDetail.let {
                        loadToImageView(it.posterURL,background)
                        loadToImageView(it.posterURL,moviePoster)
                        background.setImageResource(R.drawable.gradient)
                        movieName.text = it.movieName
                        movieYear.text = it.movieYear
                        movieCountry.text = it.movieCountry
                        movieGenre.text = it.movieGenre
                        movieRating.text = "${it.movieRating}/10"
                        movieAgeLimit.text = if (it.movieAgeLimit != null && it.movieAgeLimit.length > 0)
                            it.movieAgeLimit.filter { it.isDigit() } + "+"
                        else "0+"
                        movieDuration.text = it.movieDuration + " мин"
                        movieDescribtion.text = it.movieDescribtion
                    }
                }
                is StateModel.Loading -> {}
                is StateModel.Error -> {}
            }
        }

        val castTextView = view.findViewById<TextView>(R.id.cast)
        val castSeeMore = view.findViewById<Button>(R.id.movie_cast_see_more)
        val movieCastList = view.findViewById<RecyclerView>(R.id.movie_cast_list)
        movieCastList.adapter = castListAdapter

        viewModel.liveMovieCast.observe(viewLifecycleOwner){state ->
            when (state) {
                is StateModel.Success -> {
                    Toast.makeText(requireContext(),"Заходит в СУКСУСС!!",Toast.LENGTH_LONG).show()
                    castTextView.visibility = View.VISIBLE
                    castSeeMore.visibility = View.VISIBLE
                    movieCastList.visibility = View.VISIBLE
                    val castList = state.data
                    castListAdapter.addAll(castList)
                }
                is StateModel.Loading -> {

                }
                is StateModel.Error -> {
                    castTextView.visibility = View.INVISIBLE
                    castSeeMore.visibility = View.INVISIBLE
                    movieCastList.visibility = View.INVISIBLE
                }
            }
        }

        val factTextView = view.findViewById<TextView>(R.id.facts)
        val movieFactList = view.findViewById<RecyclerView>(R.id.movie_facts_list)
        movieFactList.adapter = factListAdapter

        viewModel.liveMovieFact.observe(viewLifecycleOwner){state ->
            when (state) {
                is StateModel.Success -> {
                    val factList = state.data
                    if (factList.size > 0) {
                        factTextView.visibility = View.VISIBLE
                        movieFactList.visibility = View.VISIBLE
                    }
                    factListAdapter.addAll((factList as ArrayList<FactsModel>))
                }
                is StateModel.Loading -> {
                }
                is StateModel.Error -> {
                    factTextView.visibility = View.INVISIBLE
                    movieFactList.visibility = View.INVISIBLE
                    showError(state.error.localizedMessage ?: "NULL")
                }
            }
        }

        val movieToFavoriteButton = view.findViewById<LinearLayoutCompat>(R.id.movie_to_favorite)
        val movieFavoriteAnim = view.findViewById<LottieAnimationView>(R.id.movie_bookmark_anim)

        movieToFavoriteButton.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(currentView: View?, event: MotionEvent?): Boolean {
                if (event!!.action == MotionEvent.ACTION_UP) {
                    movieFavoriteAnim.playAnimation()
                    return true
                }
                return false
            }
        })

        castSeeMore.setOnClickListener {
            (requireActivity() as NavigateInterface).navigateFromMainToSeeMoreCastFragment()
        }



    }

    private fun claimData(){
        viewModel.claimMovieDetail(movieId)
        viewModel.claimMovieCast(movieId)
        viewModel.claimMovieFact(movieId)
    }

    private fun showError(errorText : String = resources.getString(R.string.error_text)){
        networkObserver.registerCallBack()
        Toast.makeText(requireContext(),errorText, Toast.LENGTH_LONG).show()
    }

    private fun loadToImageView(url : String, poster : ImageView){
        Picasso.get()
            .load(url)
            .placeholder(R.color.white_dark)
            .error(R.color.white_dark)
            .fit()
            .into(poster)
    }

    override fun onDestroy() {
        networkObserver.removeCallback()
        super.onDestroy()
    }
}