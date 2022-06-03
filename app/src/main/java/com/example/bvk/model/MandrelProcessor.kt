package com.example.bvk.model

import com.example.bvk.model.amount.MembraneLengthCounter
import com.example.bvk.model.sample.SampleCapParameters

class MandrelProcessor {
    companion object {

        private const val MEMBRANE_DEPTH: Double = 0.0060
        private const val PUSHER_HEIGHT: Int = 5
        private const val SAFE_SPACE_WEIGHT = 5

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
                ) / 2) + mandrel.infelicityCoefficient
            val infelicityDecayFactor = (mandrel.infelicityCoefficient - 1) / (mandrel.maxInfelicityHeight - PUSHER_HEIGHT)

            if (sampleCapParameters.capHeight <= mandrel.maxInfelicityHeight){
                val differenceCoefficient = mandrel.infelicityCoefficient - (infelicityDecayFactor*(sampleCapParameters.capHeight - PUSHER_HEIGHT))
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