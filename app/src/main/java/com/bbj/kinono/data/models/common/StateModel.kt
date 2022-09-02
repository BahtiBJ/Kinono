package com.bbj.kinono.data.models.common

sealed class StateModel  {

    class Success<T>(val data : T) : StateModel()

    object Loading : StateModel()

    class Error(val error : Throwable)  : StateModel()

}
