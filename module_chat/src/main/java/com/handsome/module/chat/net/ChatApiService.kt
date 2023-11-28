package com.handsome.module.chat.net

import com.handsome.lib.util.network.ApiGenerator
import com.handsome.module.chat.bean.ChatFriendsList
import com.handsome.module.chat.bean.ContentListBean
import com.handsome.module.chat.bean.StatusBean
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApiService {

    @GET("relation/friend/list/")
    suspend fun getFriendChatList(@Query("user_id") userId : Long) : ChatFriendsList

    @GET("message/chat/")
    suspend fun getContentList(@Query("to_user_id") otherId : Long,@Query("pre_msg_time") time : Long) : ContentListBean

    @POST("message/action/")
    suspend fun uploadMessage(@Query("to_user_id") otherId: Long,@Query("content") content : String,@Query("action_type") type : Int = 1) : StatusBean

    companion object{
        val INSTANCE by lazy {
            ApiGenerator.getApiService(ChatApiService::class)
        }
    }
}