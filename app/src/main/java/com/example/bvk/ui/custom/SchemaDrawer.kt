package com.example.bvk.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.bvk.R

class SchemaDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defValues: Int = 0
) : View(context, attrs, defValues) {

    private var firstLineSchema = 1
    private var secondLineSchema = 1
    private var isStraightLayer = false

    private var startFX = 25f
    private var startFY = 25f
    private var circleRadius = 10f

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.GRAY
        textSize = resources.getDimension(R.dimen.schema_text_size)
    }

    fun setSchema(firstLineAmount: Int, secondLineAmount: Int, isStraightLayerSchema: Boolean) {
        firstLineSchema = firstLineAmount
        secondLineSchema = secondLineAmount
        isStraightLayer = isStraightLayerSchema
        postInvalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val maxLineNumbersCount =
            if (firstLineSchema > secondLineSchema) firstLineSchema.toString().length else secondLineSchema.toString().length

        //first line text
        startFX = 15f
        startFY = 45f
        canvas?.drawText(firstLineSchema.toString(), startFX, startFY, paint)
        //first line
        startFX = 50f
        for (i in 0..maxLineNumbersCount) {
            startFX += 10
        }
        startFY = 35f
        for (i in 1..firstLineSchema) {
            canvas?.drawCircle(startFX, startFY, circleRadius, paint)
            startFX += 40
        }
        //second line text
        startFX = 15f
        startFY = 85f
        canvas?.drawText(secondLineSchema.toString(), startFX, startFY, paint)
        //check if straight layer
        if (isStraightLayer) {
            startFX = 50f
            for (i in 0..maxLineNumbersCount) {
                startFX += 10
            }
        } else {
            startFX = 70f
            for (i in 0..maxLineNumbersCount) {
                startFX += 10
            }
        }
        // second line
        startFY = 72.5f
        for (i in 1..secondLineSchema) {
            canvas?.drawCircle(startFX, startFY, circleRadius, paint)
            startFX += 40
        }
        //third line text
        startFX = 15f
        startFY = 125f
        canvas?.drawText(firstLineSchema.toString(), startFX, startFY, paint)
        //third line
        startFY = 110f
        startFX = 50f
        for (i in 0..maxLineNumbersCount) {
            startFX += 10
        }
        for (i in 1..firstLineSchema) {
            canvas?.drawCircle(startFX, startFY, circleRadius, paint)
            startFX += 40
        }
    }
}