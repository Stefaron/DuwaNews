package com.example.examplebotomnav


class FavItem {
    var item_title: String? = null
    var key_id: String? = null
    var item_image = 0

    constructor()
    constructor(item_title: String?, key_id: String?, item_image: Int) {
        this.item_title = item_title
        this.key_id = key_id
        this.item_image = item_image
    }
}