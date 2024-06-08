//package com.example.quizzhub
//
//import com.example.quizzhub.model.Question
//import com.example.quizzhub.model.Quiz
//import java.lang.Exception
//
//
///**
//// without Generic
//sealed class ResponseResult(val data: Quiz? = null , val error: String? = null){
//
//    class Success(data: Quiz): ResponseResult(data = data)
//    class Error(error: String): ResponseResult(error = error)
//}
//**/
//
//sealed class ResponseResult<T>(val data: T? = null , val error: String? = null){
//
//    class Success<T>(data: T? = null): ResponseResult<T>(data = data)
//    class Error<T>(error: String): ResponseResult<T>(error = error)
//}