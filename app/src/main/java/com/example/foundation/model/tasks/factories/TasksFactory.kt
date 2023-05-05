package com.example.foundation.model.tasks.factories


typealias TaskBody<T> = () -> T

 interface TasksFactory {

  fun <T> async(body: TaskBody<T>) : Task<T>

}