package com.example.wallpaeradmin

class WallpaperModel {

    lateinit var img: String
    lateinit var key: String

    constructor() {

    }

    constructor(img: String, key: String) {
        this.img = img
        this.key = key
    }
}