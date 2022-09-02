package com.bbj.kinono.view

import android.os.Bundle
import com.bbj.kinono.R

interface NavigateInterface {

    fun navigateFromMainToMovieFragment(bundle: Bundle)

    fun navigateFromMainToSeeMoreMovieFragment()

    fun navigateFromMainToSeeMoreCastFragment()

    fun navigateFromSeeMoreToMovieFragment(bundle: Bundle)

}