package com.example.bvk.model

import android.content.Context
import com.example.bvk.APP_PREFERENCES
import com.example.bvk.PREFERENCE_KEY_ADHESIVE_LINE
import com.example.bvk.PREFERENCE_KEY_MEMBRANE_WEIGHT
import com.example.bvk.model.amount.MembraneLengthCounter
import com.example.bvk.model.sample.SampleCapParameters

class MandrelParametersCalculator(context : Context) {

    private val prefs = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    private val membraneDepth: Int = prefs!!.getInt(PREFERENCE_KEY_MEMBRANE_WEIGHT, 60)

    private val safeSpaceWeight = prefs!!.getInt(PREFERENCE_KEY_ADHESIVE_LINE, 5)

    companion object{
        const val PUSHER_HEIGHT: Int = 5
    }

    private fun getCircumferenceSoughtFor(mandrel: Mandrel, heightSoughtFor: Int): Double {
        return (((getTapper(mandrel) * heightSoughtFor) + (mandrel.vertexDiameter)) + (membraneDepth/1000)) * Math.PI
    }

    private fun getTapper(mandrel: Mandrel): Double {
        return (mandrel.baseDiameter - mandrel.vertexDiameter) / mandrel.height
    }

    fun calculateDataForMandrel(
        mandrel: Mandrel,
        sampleCapParameters: SampleCapParameters
    ): Mandrel {
        mandrel.tapper = getTapper(mandrel)
        mandrel.membraneWight = getCircumferenceSoughtFor(
            mandrel,
            (sampleCapParameters.capHeight - PUSHER_HEIGHT)
        ) + safeSpaceWeight
        mandrel.adhesiveSleeveWeight =
            (getCircumferenceSoughtFor(
                mandrel,
                (sampleCapParameters.capHeight - PUSHER_HEIGHT)
            ) / 2)
        val infelicityDecayFactor = (mandrel.infelicityCoefficient - 1) / (mandrel.maxInfelicityHeight - PUSHER_HEIGHT)

        if (sampleCapParameters.capHeight <= mandrel.maxInfelicityHeight){
            val differenceCoefficient = mandrel.infelicityCoefficient - (infelicityDecayFactor*(sampleCapParameters.capHeight - PUSHER_HEIGHT))
            mandrel.adhesiveSleeveWeight = mandrel.adhesiveSleeveWeight * differenceCoefficient
            mandrel.membraneWight = (mandrel.adhesiveSleeveWeight*2) + safeSpaceWeight
        }

        mandrel.totalMembraneLength = MembraneLengthCounter.count(
            sampleCapParameters.capHeight,
            sampleCapParameters.capInversion,
            sampleCapParameters.totalCapAmount
        )
        return mandrel
    }
}