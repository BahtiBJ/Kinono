package com.bbj.kinono

sealed class StateModel<out T>  {

    class Success<T>(val data : T) : StateModel<T>()

    object Loading : StateModel<Nothing>()

    class Error(val error : Throwable)  : StateModel<Nothing>()

}
