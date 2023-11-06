package com.example.mystoryapp

import com.example.mystoryapp.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "photoUrl $i",
                "createdAt $i",
                "name $i",
                "description $i",
            )
            items.add(story)
        }
        return items
    }
}