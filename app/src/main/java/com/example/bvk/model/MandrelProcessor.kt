package com.example.bvk.model

import com.example.bvk.model.amount.MembraneLengthCounter
import com.example.bvk.model.sample.SampleCapParameters

class MandrelProcessor {
    companion object {

        private const val MEMBRANE_DEPTH: Double = 0.0060
        private const val PUSHER_HEIGHT: Int = 5
        private const val SAFE_SPACE_WEIGHT = 5
        private const val MAX_DIFFERENCE_COEFFICIENT = 1.0120
        private const val MAX_INFELICITY_HEIGHT = 30
        private const val INFELICITY_DECAY_FACTOR = (MAX_DIFFERENCE_COEFFICIENT -1)/ (MAX_INFELICITY_HEIGHT - PUSHER_HEIGHT)

        private fun getCircumferenceSoughtFor(mandrel: Mandrel, heightSoughtFor: Int): Double {
            return (((getTapper(mandrel) * heightSoughtFor) + (mandrel.vertexDiameter)) + MEMBRANE_DEPTH) * Math.PI
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
            ) + SAFE_SPACE_WEIGHT
            mandrel.adhesiveSleeveWeight =
                (getCircumferenceSoughtFor(
                    mandrel,
                    (sampleCapParameters.capHeight - PUSHER_HEIGHT)
                ) / 2) + mandrel.infelicity

            if (sampleCapParameters.capHeight <= 30){
                val differenceCoefficient = MAX_DIFFERENCE_COEFFICIENT - (INFELICITY_DECAY_FACTOR*(sampleCapParameters.capHeight - PUSHER_HEIGHT))
                mandrel.adhesiveSleeveWeight = mandrel.adhesiveSleeveWeight * differenceCoefficient
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