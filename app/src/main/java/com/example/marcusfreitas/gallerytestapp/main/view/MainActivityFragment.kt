package com.example.marcusfreitas.gallerytestapp.main.view

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.marcusfreitas.gallerytestapp.R
import com.example.marcusfreitas.gallerytestapp.main.contract.MainContract
import com.example.marcusfreitas.gallerytestapp.main.presenter.MainPresenter
import com.example.marcusfreitas.gallerytestapp.main.view.adapter.GalleryAdapter
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment(), MainContract.MainFragmentViewContract {


    var mPresenter: MainPresenter? = null
    var mGalleryAdapter: GalleryAdapter? = null
    private var mImageUrlList = listOf<UploadedImage>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
    }

    override fun onStart() {
        super.onStart()

        mPresenter?.attachFragmentView(this)
        mPresenter?.startDataObserver()
    }

    private fun prepareRecyclerView() {

        val glide = Glide.with(this.context)

        mGalleryAdapter = GalleryAdapter(mImageUrlList, glide)
        galleryRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
        galleryRecyclerView.adapter = mGalleryAdapter
        galleryRecyclerView.setHasFixedSize(true)

        galleryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when(newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> glide.resumeRequests()
                    AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL, AbsListView.OnScrollListener.SCROLL_STATE_FLING -> glide.pauseRequests()
                }
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()

        mPresenter?.detachFragmentView()
    }

    override fun showToastMessage(message: String, length: Int) {
        Toast.makeText(activity, message, length).show()
    }

    override fun setImageUrlList(imageUrlList: List<UploadedImage>) {
        mGalleryAdapter?.setDataSource(imageUrlList)
    }
}
