package com.example.note

import android.util.Log
import org.junit.*
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class NoteNetworkTest {
    @Before
    fun init(){
        Log.d("before", "before")
    }

    @Test
    fun notesTest(){
        Log.d("test", "test")
        assertEquals(4, 3)
    }

    @After
    fun destroy(){
        Log.d("after", "after")
    }
}