package com.example.mystoryapp.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mystoryapp.R

class MyEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var clearButtonImage: Drawable
    private var validationType: String? = null
    private var isValidEmail : Boolean = false
    private var isValidPassword : Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        checkAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        checkAttributes(attrs)
    }

    private fun checkAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyEditText)
        validationType = typedArray.getString(R.styleable.MyEditText_validationType)

        typedArray.recycle()
    }

    private fun init() {

        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
        clearButtonImage.setBounds(0, 0, clearButtonImage.intrinsicWidth, clearButtonImage.intrinsicHeight)
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(s.toString().isNotEmpty()) showClearButton() else hideClearButton()

                if(validationType == "EMAIL") {
                    isValidEmail = isValidEmail(s.toString())
                    if(!isValidEmail) {
                        val errorMessage = context.getString(R.string.email_error_message)
                        setError(errorMessage, null)
                    } else {
                        error = null
                    }
                }
                else if(validationType == "PASSWORD") {
                    isValidPassword = isValidPassword(s.toString())
                    if(!isValidPassword) {
                        val errorMessage = context.getString(R.string.password_error_message)
                        setError(errorMessage, null)
                    } else {
                        error = null
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }

        })
    }

    private fun isValidEmail(email: String) : Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPassword(password: String) : Boolean {
        return password.length >= 8
    }

    private fun showClearButton() {
        val isVisible = text?.isNotEmpty() == true
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, if (isVisible) clearButtonImage else null, null)
    }
    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                   event!!.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                   event!!.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        showClearButton()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        clearButtonImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return false
    }
}