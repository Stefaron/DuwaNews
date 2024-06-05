package com.example.examplebotomnav


class CoffeeItem {
    var imageResourse = 0
    var title: String? = null
    var key_id: String? = null
    var favStatus: String? = null

    constructor()
    constructor(imageResourse: Int, title: String?, key_id: String?, favStatus: String?) {
        this.imageResourse = imageResourse
        this.title = title
        this.key_id = key_id
        this.favStatus = favStatus
    }

    companion object {
        val key_id: Any
            get() {
                TODO()
            }
        val title: Any
            get() {
                TODO()
            }
    }
}