package com.example.bvk.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class SchemaDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defValues: Int = 0
) : View(context, attrs, defValues) {

    var firstLineSchema = 1
    var secondLineSchema = 1

    var startFX = 25f
    var startFY = 25f
    private var circleRadius = 10f

    private val paint: Paint = Paint()

    fun setSchema(firstLineAmount: Int, secondLineAmount: Int) {
        firstLineSchema = firstLineAmount
        secondLineSchema = secondLineAmount
        postInvalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.apply {
            isAntiAlias = true
            color = Color.BLACK
        }
        startFX = 25f
        startFY = 25f
        for (i in 1..firstLineSchema) {
            canvas?.drawCircle(startFX, startFY, circleRadius, paint)
            startFX += 40
        }
        startFX = 45f
        startFY = 50f
        for (i in 1..secondLineSchema) {
            canvas?.drawCircle(startFX, startFY, circleRadius, paint)
            startFX += 40
        }
        startFY = 75f
        startFX = 25f
        for (i in 1..firstLineSchema) {
            canvas?.drawCircle(startFX, startFY, circleRadius, paint)
            startFX += 40
        }

    }
}