package com.example.bvk.model

class MandrelProcessor (private val mandrel: Mandrel, heightLookFor: Double)  {

    private val tapper =
        (mandrel.baseDiameter - mandrel.vertexDiameter) / mandrel.height

    val circumferenceLookFor =
        ((tapper * heightLookFor) + (mandrel.vertexDiameter)) * Math.PI

    private fun getCircumferenceDiameter(circumference: Double): Double = circumference / Math.PI
}