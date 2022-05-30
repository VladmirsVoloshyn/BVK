package com.example.bvk.model

import android.util.Log
import com.example.bvk.model.amount.MembraneLengthCounter
import com.example.bvk.model.sample.SampleCapParameters

class MandrelProcessor {
    companion object {

        private const val MEMBRANE_DEPTH: Double = 0.0060
        private const val PUSHER_HEIGHT: Int = 5
        private const val PERMISSIBLE_DIAMETER_DIFFERENCE = 1.0
        private val PROCESSOR_TAG = MandrelProcessor::class.java.name.toString()

        private fun getCircumferenceSoughtFor(mandrel: Mandrel, heightSoughtFor: Int): Double {
            Log.d(PROCESSOR_TAG, (((getTapper(mandrel) * heightSoughtFor) + (mandrel.vertexDiameter)) + MEMBRANE_DEPTH).toString())
            return (((getTapper(mandrel) * heightSoughtFor) + (mandrel.vertexDiameter)) + MEMBRANE_DEPTH) * Math.PI
        }

        private fun getTapper(mandrel: Mandrel): Double {
            return (mandrel.baseDiameter - mandrel.vertexDiameter) / mandrel.height
        }

        fun calculateDataForMandrel(
            mandrel: Mandrel,
            sampleCapParameters: SampleCapParameters
        ): Mandrel  {
            mandrel.tapper = getTapper(mandrel)
            mandrel.membraneWight = getCircumferenceSoughtFor(
                mandrel,
                (sampleCapParameters.capHeight - PUSHER_HEIGHT)
            ) + 5
            mandrel.adhesiveSleeveWeight =
                (getCircumferenceSoughtFor(
                    mandrel,
                    (sampleCapParameters.capHeight - PUSHER_HEIGHT)
                ) / 2) + mandrel.infelicity

            val adhesiveSleeveWeightDiameter = (mandrel.adhesiveSleeveWeight * 2) / Math.PI

            if ((adhesiveSleeveWeightDiameter - mandrel.vertexDiameter) < PERMISSIBLE_DIAMETER_DIFFERENCE) {
                val difference: Double = PERMISSIBLE_DIAMETER_DIFFERENCE - (adhesiveSleeveWeightDiameter - mandrel.vertexDiameter)
                mandrel.recommendedAdhesiveSleeveWeight =
                    ((adhesiveSleeveWeightDiameter + difference) * 3.14) / 2
            }

            mandrel.totalMembraneLength = MembraneLengthCounter.count(
                sampleCapParameters.capHeight,
                sampleCapParameters.capInversion,
                sampleCapParameters.totalCapAmount
            )
            return mandrel
        }
    }
}