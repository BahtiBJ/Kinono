# Kinono
The application shows information about movie from the Kinopoisk. Such as premieres, popular movies and movie details.

# Capabilities
On the first page you can watch premieres and popular films. By clicking on the  more detail button, you can see an extended list of popular films.

On the second page, you can use the search and find the movie you are interested in, you can also use the filter by date, rating and genre.

By clicking on a movie preview, a window opens with detailed information about the movie, its cast and interesting facts.

# Structure
Data obtained using an unofficial Kinopoisk API ([https://kinopoiskapiunofficial.tech/]). 
The following frameworks were used: ViewModel with LiveData for MVVM, Navigation Compontent for navigation, Retrofit with OkHttp for network requests,
Koin for dependency injection and Kotlin Coroutine for multithreading.

