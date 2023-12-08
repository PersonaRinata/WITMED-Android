package com.handsome.module.video.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handsome.lib.api.server.service.IMineService
import com.handsome.lib.util.base.BaseBottomSheetDialogFragment
import com.handsome.lib.util.extention.toast
import com.handsome.lib.util.service.impl
import com.handsome.lib.util.util.myCoroutineExceptionHandler
import com.handsome.module.search.databinding.VideoDialogCommentBinding
import com.handsome.module.video.ui.adapter.CommentAdapter
import com.handsome.module.video.ui.viewmodel.CommentDialogViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CommentDialog : BaseBottomSheetDialogFragment() {
    private val mBinding by lazy { VideoDialogCommentBinding.inflate(layoutInflater) }
    private val mAdapter by lazy { CommentAdapter() }
    private val mViewModel by viewModels<CommentDialogViewModel>()
    private val mVideoId by arguments<Long>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObserve()
        initView()
        getCommentData()
        return mBinding.root
    }

    private fun getCommentData(){
        mViewModel.getVideoComment(mVideoId)
    }


    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch(myCoroutineExceptionHandler) {
            mViewModel.sendComment.collectLatest {
                if (it != null){
                    if (it.status_code == 0){
                        // 发布成功再次重新请求
                        getCommentData()
                    }else{
                        toast("发布失败")
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.videoComment.collectLatest {
                Log.d("lx", "videoFragment: observe${it}}")
                if (it != null){
                    if (it.status_code == 0){
                        mAdapter.submitList(it.comment_list)
                    }else{
                        toast("网络异常")
                    }
                }
            }
        }
    }

    private fun initView() {
        mBinding.apply {
            videoDialogCommentTvCancel.setOnClickListener {
                dialog?.cancel()
            }
            videoDialogCommentRv.apply {
                layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
                adapter = mAdapter.apply {
                    setOnClickUser {userId ->
                        IMineService::class.impl.startPersonActivity(userId)
                    }
                }
            }
            videoDialogCommentTvSend.setOnClickListener {
                val commentText = videoDialogCommentEtComment.text.toString().trim()
                // 加入候选队列
                if (commentText != "") mViewModel.sendComment(mVideoId,commentText)
            }
        }
    }

    companion object{
        fun newInstance(videoId : Long) = CommentDialog().apply {
            arguments = bundleOf(
                this::mVideoId.name to videoId
            )
        }
    }

}