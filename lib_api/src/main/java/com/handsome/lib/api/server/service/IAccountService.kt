package com.handsome.lib.api.server.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.handsome.lib.api.server.Value
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.io.Serializable

interface IAccountService : IProvider {

    fun observeUserInfoState(): Observable<Value<LoginBean>>

    /**
     * 观察学号的改变（事件）
     *
     * 没有数据倒灌的 Observable，即每次订阅不会发送之前的最新值
     */
    fun observeUserInfoEvent(): Observable<Value<LoginBean>>

    fun getUserInfo(): LoginBean?

    fun isLogin(): Boolean

    fun login(
        username: String,
        password: String
    ): Single<LoginBean>

    fun logout(): Completable

    fun register(
        username: String,
        password: String,
        rePassword: String
    ): Single<LoginBean>

    data class LoginBean(
        val status_code: Int,
        val status_msg: String,
        val token: String,
        val user_id: Long,
        val username : String,
        val password : String
    ) : Serializable
}