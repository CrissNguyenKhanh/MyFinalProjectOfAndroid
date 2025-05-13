package com.example.retrofitwrooom.repository

import com.example.retrofitwrooom.api.RetrofitIntance
import com.example.retrofitwrooom.database.ArticleDatabase
import com.example.retrofitwrooom.model.Article

class NewsRepository(val db:ArticleDatabase) {

    //se goi tat ca cac ham tu ke ca tu retrofit va ke ca dao ( kieu dao se la bao gom cac phuong thuc lay du lieu ra ..vv,
    //con retroitapi la noi de day du lieu vao table de thang dao lay ra hoacj them vao :V

  suspend fun getHeadLines(countTryCode:String , pageNumber:Int) =
      RetrofitIntance.api.getHeadLines(countTryCode,pageNumber)

  suspend fun SearchNews(searchQuery :String , pageNumber: Int) =
      RetrofitIntance.api.SerachForNews(searchQuery,pageNumber)

  suspend fun  upsert(article:Article)  =db.getArticleDao().upsert(article)
  fun getFavouritesNews() =  db.getArticleDao().getAllArticle()
  suspend fun  deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)



}