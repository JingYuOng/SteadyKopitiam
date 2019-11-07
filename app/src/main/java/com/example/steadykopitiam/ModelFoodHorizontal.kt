package com.example.steadykopitiam

import android.R.attr.name



class ModelFoodHorizontal {

    var name: String? = null
    var description: String? = null
    var image_drawable: Int = 0
    var foodfocus: String? = null

    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }

    fun getDescriptions(): String {
        return description.toString()
    }

    fun setDescriptions(description: String) {
        this.description = description
    }

    fun getFoodFocus(): String {
        return foodfocus.toString()
    }

    fun setFoodFocus(foodfocus: String) {
        this.foodfocus = foodfocus
    }

    fun getImage_drawables(): Int {
        return image_drawable
    }

    fun setImage_drawables(image_drawable: Int) {
        this.image_drawable = image_drawable
    }

}