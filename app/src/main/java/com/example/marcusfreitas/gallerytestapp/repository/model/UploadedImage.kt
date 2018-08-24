package com.example.marcusfreitas.gallerytestapp.repository.model

open class UploadedImage(override var name: String = "", override var url: String = "",
                         override var databaseReference: String = "") : UploadedImageInterface