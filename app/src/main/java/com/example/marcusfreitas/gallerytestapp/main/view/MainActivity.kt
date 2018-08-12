package com.example.marcusfreitas.gallerytestapp.main.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.marcusfreitas.gallerytestapp.R
import com.example.marcusfreitas.gallerytestapp.main.contract.MainContract
import com.example.marcusfreitas.gallerytestapp.main.navigation.MainNavigationControllerInterface
import com.example.marcusfreitas.gallerytestapp.main.presenter.MainPresenter
import com.example.marcusfreitas.gallerytestapp.repository.provider.RepositoryProvider
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), MainNavigationControllerInterface, MainContract.MainActivityViewContract {

    var mPresenter: MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mPresenter = MainPresenter(this, RepositoryProvider.provideRepository())
        mPresenter?.attachActivityView(this)

        (fragment as MainActivityFragment).mPresenter = mPresenter

        fab.setOnClickListener {
            mPresenter?.fabButtonClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mPresenter?.detachActivityView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = CropImage.getActivityResult(data)
        mPresenter?.imagePickerResult(requestCode, resultCode, result.uri)

    }

    // Navigation Controller

    override fun openImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
    }

    // MainActivityViewContract

    private fun showToastMessage(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    override fun showSuccessMessage() {
        showToastMessage(getString(R.string.upload_success), Toast.LENGTH_SHORT)
    }

    override fun showErrorMessage(throwable: Throwable) {
        showToastMessage(throwable.localizedMessage, Toast.LENGTH_SHORT)
    }
}
