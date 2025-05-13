package com.example.retrofitwrooom.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.retrofitwrooom.model.Article
import com.example.retrofitwrooom.model.NewResponse
import com.example.retrofitwrooom.repository.NewsRepository
import com.example.retrofitwrooom.util.Resouce
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(app:Application ,  val newsRepository: NewsRepository) :AndroidViewModel(app) {

    val headlines: MutableLiveData<Resouce<NewResponse>> = MutableLiveData()
    var headLinePages = 1
    var headlinesResponse: NewResponse? =null

    val SearchNews: MutableLiveData<Resouce<NewResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse:NewResponse? =null
    var newSearchQuery:String? = null
    var oldSearchQuery:String? = null


    init {
        getHeadlines("us")
    }

    fun getHeadlines(countryCode :String)  = viewModelScope.launch {
        headLinesInterNet(countryCode)
    }

    fun searchNews(searchQuery :String) = viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }


    // thuong se chay tu resoucre day len serve
    private fun handleHeadLinesResponse(response: Response<NewResponse>) :Resouce<NewResponse>{
        if(response.isSuccessful){
             response.body()?.let {resultResponse ->
                 headLinePages++
                 if(headlinesResponse  ==null){
                     headlinesResponse = resultResponse
                 }else{
                     val oldArticle = headlinesResponse?.articles
                     val newArticle = resultResponse.articles
                     oldArticle?.addAll(newArticle)
                 }
                 return Resouce.Success(headlinesResponse ?: resultResponse)
             }
        }
        return Resouce.Error(response.message())
    }

    private fun handleSearchResponse(response: Response<NewResponse>) :Resouce<NewResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                if(searchNewsResponse  ==null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                }else{
                    searchNewsPage++
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resouce.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resouce.Error(response.message())
    }
     fun addToFavourite(article:Article){
         viewModelScope.launch {
             newsRepository.upsert(article)
         }
     }
     //lay query xuong
     fun getFavouriteNews() = newsRepository.getFavouritesNews()
     fun deleteArticle(article:Article){
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
     }

     fun internetConnection(context:Context):Boolean{
         val connectivityManager =
             context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             val network = connectivityManager.activeNetwork ?: return false
             val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
             return when {
                 activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                 activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                 activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                 else -> false
             }
         } else {
             // Dành cho API dưới 23
             val networkInfo = connectivityManager.activeNetworkInfo ?: return false
             return networkInfo.isConnected
         }

     }

    private suspend fun headLinesInterNet(country :String){
        headlines.postValue(Resouce.Loading())
        try {
            if(internetConnection(this.getApplication())){
                val response = newsRepository.getHeadLines(country, headLinePages)
                headlines.postValue(handleHeadLinesResponse(response))
            }else{
                headlines.postValue(Resouce.Error("no internet Connection"))
            }
        } catch (t :Throwable){
            when(t){
                is IOException ->headlines.postValue(Resouce.Error("unable to connection intenet"))
                else -> headlines.postValue(Resouce.Error("No Signal"))
            }
        }

    }
    private suspend fun searchNewsInternet(searchQuery: String) {
        newSearchQuery = searchQuery
        SearchNews.postValue(Resouce.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = newsRepository.SearchNews(searchQuery, searchNewsPage)
                SearchNews.postValue(handleSearchResponse(response))
            } else {
                SearchNews.postValue(Resouce.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> SearchNews.postValue(Resouce.Error("Unable to connect to internet"))
                else -> SearchNews.postValue(Resouce.Error("Something went wrong"))
            }
        }
    }


}