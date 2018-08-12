package com.example.marcusfreitas.gallerytestapp.main.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.marcusfreitas.gallerytestapp.R
import com.example.marcusfreitas.gallerytestapp.main.contract.MainContract
import com.example.marcusfreitas.gallerytestapp.main.navigation.MainNavigationControllerInterface
import com.example.marcusfreitas.gallerytestapp.main.presenter.MainPresenter
import com.example.marcusfreitas.gallerytestapp.repository.provider.RepositoryProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), MainNavigationControllerInterface, MainContract.MainActivityViewContract {

    private lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mPresenter = MainPresenter(this, RepositoryProvider.provideRepository())
        mPresenter.attachActivityView(this)

        (fragment as MainActivityFragment).mPresenter = mPresenter

        fab.setOnClickListener { view ->
//            Snackbar.make(view, getString(R.string.pick_image_title), Snackbar.LENGTH_LONG)
//                    .setAction(getString(R.string.pick_image_action)) {
//
//                    }.show()
            mPresenter.fabButtonClick(MainPresenter.PICK_IMAGE_REQUEST_CODE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mPresenter.detachActivityView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mPresenter.imagePickerResult(requestCode, resultCode, data)

    }

    // Navigation Controller

    override fun openImagePicker(requestCode: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, requestCode)
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
