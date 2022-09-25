package com.bbj.kinono.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbj.kinono.StateModel
import com.bbj.kinono.domain.*
import com.bbj.kinono.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val getPremiereUseCase: GetPremiereUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieCastUseCase: GetCastUseCase,
    private val getMovieFactUseCase: GetMovieFactUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val TAG = "MAINVIEWMODEL"

    private val _livePremiere = MutableLiveData<StateModel<List<PosterInfo>>>()
    val livePremiere: LiveData<StateModel<List<PosterInfo>>>
        get() = _livePremiere

    private val _livePopular = MutableLiveData<StateModel<PreviewListModel>>()
    val livePopular: LiveData<StateModel<PreviewListModel>>
        get() = _livePopular

    private val _liveSeeMorePopular by lazy {MutableLiveData<StateModel<PreviewListModel>>().apply {
        value = livePopular.value
    }}
    val liveSeeMorePopular: LiveData<StateModel<PreviewListModel>>
        get() = _liveSeeMorePopular

    private val _liveMovieDetail = MutableLiveData<StateModel<MovieInfo>>()
    val liveMovieDetail: LiveData<StateModel<MovieInfo>>
        get() = _liveMovieDetail

    private val _liveSearchResult = MutableLiveData<StateModel<SearchResult>>()
    val liveSearchResult: LiveData<StateModel<SearchResult>>
        get() = _liveSearchResult

    private val _liveMovieCast = MutableLiveData<StateModel<List<CastingModel>>>()
    val liveMovieCast: LiveData<StateModel<List<CastingModel>>>
        get() = _liveMovieCast

    private val _liveMovieFactState = MutableLiveData<StateModel<List<FactsModel>>>()
    val liveMovieFact: LiveData<StateModel<List<FactsModel>>>
        get() = _liveMovieFactState

    fun claimPremiereList() {
        _livePremiere.value = StateModel.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val premiereList = getPremiereUseCase.execute()
                withContext(Dispatchers.Main) {
                    _livePremiere.value = StateModel.Success(premiereList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _livePremiere.value = StateModel.Error(error = e)
                }
            }
        }
    }

    fun claimPopularList(page: Int = 1) {
        _livePopular.value = StateModel.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val popularList = getPopularUseCase.execute(page)
                withContext(Dispatchers.Main) {
                    _livePopular.value = StateModel.Success(popularList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _livePopular.value = StateModel.Error(error = e)
                }
            }
        }
    }

    fun claimTopFilms(page: Int = 1){
        _liveSeeMorePopular.value = StateModel.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val popularList = getPopularUseCase.execute(page)
                withContext(Dispatchers.Main) {
                    _liveSeeMorePopular.value = StateModel.Success(popularList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _liveSeeMorePopular.value = StateModel.Error(error = e)
                }
            }
        }
    }

    fun claimMovieDetail(id: Int) {
        _liveMovieDetail.value = StateModel.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieDetail = getMovieDetailUseCase.execute(id)
                withContext(Dispatchers.Main) {
                    _liveMovieDetail.value = StateModel.Success(movieDetail)
                }
            } catch (e: Exception) {
                throw e
                withContext(Dispatchers.Main) {
                    _liveMovieDetail.value = StateModel.Error(error = e)
                }
            }
        }
    }

    fun claimMovieCast(id: Int) {
        _liveMovieCast.value = StateModel.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieCast = getMovieCastUseCase.execute(id)
                withContext(Dispatchers.Main) {
                    _liveMovieCast.value = StateModel.Success(movieCast)
                }
            } catch (e: Exception) {
                throw e
                withContext(Dispatchers.Main) {
                    _liveMovieCast.value = StateModel.Error(error = e)
                }
            }
        }
    }

    fun claimMovieFact(id: Int) {
        _liveMovieFactState.value = StateModel.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieFact = getMovieFactUseCase.execute(id)
                withContext(Dispatchers.Main) {
                    _liveMovieFactState.value = StateModel.Success(movieFact)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _liveMovieFactState.value = StateModel.Error(error = e)
                }
            }
        }
    }

    fun claimSearchResult(keyword: String,
                          page: Int = 1,
                          countries: Int? = null,
                          genres: Int? = null,
                          ratingFrom: Int = 0,
                          ratingTo: Int = 10,
                          yearFrom: Int = 1900,
                          yearTo: Int = 2100) {
        _liveSearchResult.value = StateModel.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val searchResult = searchUseCase.execute(keyword,
                    page,
                    countries,
                    genres,
                    ratingFrom,
                    ratingTo,
                    yearFrom,
                    yearTo)
                withContext(Dispatchers.Main) {
                    _liveSearchResult.value = StateModel.Success(searchResult)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                        _liveSearchResult.value = StateModel.Error(error = e)
                }
            }
        }
    }


}