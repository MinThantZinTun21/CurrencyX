package com.mtz.currencyex.fn

typealias RESULT<V> = Either<Failure, V>

sealed class Either<A, B> {
    class FAILED<A, B>(val ERROR: A) : Either<A, B>()
    class SUCCESS<A, B>(val DATA: B) : Either<A, B>()


}


abstract class Failure : Throwable() {
    abstract fun getErrorInfo(): String?
    data class Unknown(val errorMessage: String?) : Failure() {
        override fun getErrorInfo(): String? {
            return errorMessage
        }
    }

    data class Domain(val errorMessage: String?) : Failure() {
        override fun getErrorInfo(): String? {
            return errorMessage
        }
    }

    data class Network(val errorMessage: String?) : Failure() {
        override fun getErrorInfo(): String? {
            return errorMessage
        }
    }

    data class ThirdParty(val errorMessage: String?) : Failure() {
        override fun getErrorInfo(): String? {
            return errorMessage
        }
    }

    data class Http(val errorMessage: String?) : Failure() {
        override fun getErrorInfo(): String? {
            return errorMessage
        }
    }

    data class WrongFormat(val errorMessage: String?) : Failure() {

        override fun getErrorInfo(): String? {
            return errorMessage

        }

    }
}



