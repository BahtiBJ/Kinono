package com.bbj.kinono.view.fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bbj.kinono.R
import com.bbj.kinono.data.models.FactItem
import com.bbj.kinono.data.models.MovieCastModel
import com.bbj.kinono.data.models.MovieDetailModel
import com.bbj.kinono.data.models.MovieFactModel
import com.bbj.kinono.data.models.common.StateModel
import com.bbj.kinono.util.ID_KEY
import com.bbj.kinono.util.isOnline
import com.bbj.kinono.view.MainActivity
import com.bbj.kinono.view.MainViewModel
import com.bbj.kinono.view.NavigateInterface
import com.bbj.kinono.view.adapter.CastListAdapter
import com.bbj.kinono.view.adapter.FactListAdapter
import com.bbj.kinono.view.adapter.OnListItemClick
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Exception
import kotlin.properties.Delegates

class MovieFragment : Fragment() {

    private val viewModel by sharedViewModel<MainViewModel>()

    private var movieId by Delegates.notNull<Int>()

    private val castListAdapter by lazy {CastListAdapter(requireContext())}

    private val factListAdapter by lazy { FactListAdapter(requireContext()) }


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
                is StateModel.Success<*> -> {
                    val movieDetail = (state.data as MovieDetailModel)
                    describtionTitle.visibility = View.VISIBLE
                    movieDetail.run {
                        loadToImageView(posterUrl,background)
                        loadToImageView(posterUrl,moviePoster)
                        background.setImageResource(R.drawable.gradient)
                        movieName.text = nameRu
                        movieYear.text = year.toString()
                        movieCountry.text = getCountryListString()
                        movieGenre.text = getGenres()
                        movieRating.text = "$ratingKinopoisk/10"
                        movieAgeLimit.text = if (ratingAgeLimits != null && ratingAgeLimits.length > 0)
                            ratingAgeLimits.filter { it.isDigit() } + "+"
                        else "0+"
                        movieDuration.text = filmLength.toString() + " мин"



                        movieDescribtion.text = description
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
                is StateModel.Success<*> -> {
                    castTextView.visibility = View.VISIBLE
                    castSeeMore.visibility = View.VISIBLE
                    movieCastList.visibility = View.VISIBLE
                    val castList = (state.data as MovieCastModel)
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
                is StateModel.Success<*> -> {
                    val factList = (state.data as MovieFactModel).items
                    if (factList.size > 0) {
                        factTextView.visibility = View.VISIBLE
                        movieFactList.visibility = View.VISIBLE
                    }
                    factListAdapter.addAll((factList as ArrayList<FactItem>))
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

        if (requireContext().isOnline()) {
            viewModel.claimMovieDetail(movieId)
            viewModel.claimMovieCast(movieId)
            viewModel.claimMovieFact(movieId)
        } else
            showError()

    }

    private fun showError(errorText : String = resources.getString(R.string.error_text)){
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

    private class CustomTarget(private val loadBitmap : (bitmap : Bitmap) -> Unit) : Target {
        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            loadBitmap(bitmap!!)
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            loadBitmap(errorDrawable!!.toBitmap())
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            loadBitmap(placeHolderDrawable!!.toBitmap(200,200))
        }
    }

}