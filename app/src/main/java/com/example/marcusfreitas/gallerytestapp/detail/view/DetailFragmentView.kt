package com.example.marcusfreitas.gallerytestapp.detail.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.marcusfreitas.gallerytestapp.R
import com.example.marcusfreitas.gallerytestapp.detail.contract.DetailContract
import com.example.marcusfreitas.gallerytestapp.detail.presenter.DetailPresenter
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImage
import com.example.marcusfreitas.gallerytestapp.repository.model.UploadedImageInterface
import com.example.marcusfreitas.gallerytestapp.repository.provider.RepositoryProvider
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragmentView : Fragment(), DetailContract.DetailViewContract {

    override var uploadedImage: UploadedImageInterface? = null

    var presenter: DetailContract.DetailPresenterContract? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = DetailPresenter(RepositoryProvider.provideRepository())
        presenter?.attachView(this)

        presenter?.loadPhoto()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.detailDelete -> {
                AlertDialog.Builder(context).setTitle(getString(R.string.delete_confirm_title)).
                        setMessage(getString(R.string.delete_confirmation_text)).
                        setPositiveButton(getString(R.string.ok_button)) { dialog, _ ->
                            presenter?.deletePhoto()
                            dialog.dismiss()
                        }.
                        setNegativeButton(getString(R.string.cancel_button)) { dialog, _ ->
                            dialog.dismiss() }.show()

                true
            }
            else -> false
        }
    }

    override fun showPhoto(url: String) {
        Glide.with(this.context).load(url).into(photoImageView)
    }

    override fun getBundle(): Bundle? {
        return arguments
    }

    override fun showNoPhotos() {
        errorTextView.visibility = View.VISIBLE
        photoImageView.visibility = View.GONE
        errorTextView.text = getString(R.string.error_no_photo_load)
    }

    override fun closeDetail() {
        activity?.onBackPressed()
    }

    override fun deleteErrorMessage() {
        Toast.makeText(context, getString(R.string.delete_photo_error_message), Toast.LENGTH_SHORT).show()
    }

}