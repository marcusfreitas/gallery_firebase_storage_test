package com.example.marcusfreitas.gallerytestapp.main

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.marcusfreitas.gallerytestapp.R
import com.example.marcusfreitas.gallerytestapp.main.contract.MainContract
import com.example.marcusfreitas.gallerytestapp.main.presenter.MainPresenter
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment(), MainContract.MainFragmentViewContract {


    var mPresenter: MainPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mPresenter?.attachFragmentView(this)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mPresenter?.detachFragmentView()
    }

    override fun showToastMessage(message: String, length: Int) {
        Toast.makeText(activity, message, length).show()
    }

    override fun setImageUrlList(imageUrlList: List<UploadedImage>) {

    }
}
