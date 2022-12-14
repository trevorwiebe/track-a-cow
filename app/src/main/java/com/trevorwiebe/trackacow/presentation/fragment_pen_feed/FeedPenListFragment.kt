package com.trevorwiebe.trackacow.presentation.fragment_pen_feed

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.trevorwiebe.trackacow.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.trevorwiebe.trackacow.domain.utils.ItemClickListener
import android.content.Intent
import android.os.Build.VERSION
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.trevorwiebe.trackacow.presentation.feedlot.FeedLotActivity
import com.trevorwiebe.trackacow.domain.models.lot.LotModel
import com.trevorwiebe.trackacow.presentation.fragment_pen_feed.ui_model.FeedPenListUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FeedPenListFragment : Fragment(){
    private lateinit var mEmptyPen: TextView
    private lateinit var mFeedPenRv: RecyclerView
    private lateinit var mLotModel: LotModel
    private lateinit var feedPenRecyclerViewAdapter: FeedPenRecyclerViewAdapter
    private lateinit var feedPenListUiModelList: List<FeedPenListUiModel>

    @Inject lateinit var feedPenListViewModelFactory: FeedPenListViewModel.FeedPenListViewModelFactory

    private val feedPenListViewModel: FeedPenListViewModel by viewModels{
        FeedPenListViewModel.providesFactory(
            assistedFactory = feedPenListViewModelFactory,
            lotModel = getLotModel(arguments)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_pen_feed, container, false)

        mLotModel = getLotModel(arguments)

        mEmptyPen = rootView.findViewById(R.id.feed_pen_empty)
        mFeedPenRv = rootView.findViewById(R.id.feed_pen_rv)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        mFeedPenRv.layoutManager = linearLayoutManager

        feedPenRecyclerViewAdapter = FeedPenRecyclerViewAdapter()
        mFeedPenRv.adapter = feedPenRecyclerViewAdapter

        mFeedPenRv.addOnItemTouchListener(
            ItemClickListener(
                context,
                mFeedPenRv,
                object : ItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val feedLotIntent = Intent(activity, FeedLotActivity::class.java)
                        feedLotIntent.putExtra("feed_ui_model_date", feedPenListUiModelList[position].date)
                        feedLotIntent.putExtra("lot_id", mLotModel.lotCloudDatabaseId)
                        startActivity(feedLotIntent)
                    }

                    override fun onLongItemClick(view: View, position: Int) {}
                })
        )

        lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                feedPenListViewModel.uiState.collect{

                    feedPenListUiModelList = it.feedPenUiList

                    // update ui with lot list
                    if(feedPenListUiModelList.isEmpty() && !it.isLoading){
                        mEmptyPen.visibility = View.VISIBLE
                    }else{
                        mEmptyPen.visibility = View.INVISIBLE

                        feedPenRecyclerViewAdapter.setFeedPenList(feedPenListUiModelList)
                    }
                }
            }
        }

        return rootView
    }

    private fun getLotModel(bundle: Bundle?): LotModel{
        return if(VERSION.SDK_INT >= 33){
            bundle?.getParcelable("fragment_lot", LotModel::class.java) ?: LotModel(
                0, "", "", "", "", 0L, "")
        }else{
            @Suppress("DEPRECATION")
            bundle?.getParcelable("fragment_lot") ?: LotModel(
                0, "", "", "", "", 0L, "")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(penId: String?, lotModel: LotModel): FeedPenListFragment {
            val args = Bundle()
            args.putParcelable("fragment_lot", lotModel)
            args.putString("fragment_pen_id", penId)
            val fragment = FeedPenListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}