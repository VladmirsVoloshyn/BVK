package com.example.bvk.model

import com.example.bvk.model.amount.MembraneLengthCounter
import com.example.bvk.model.sample.SampleCapParameters
import java.text.DecimalFormat

class MandrelProcessor {
    companion object {

        private const val MEMBRANE_DEPTH: Double = 0.0060

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
                    (sampleCapParameters.capHeight - 5)
                ) + 5


            mandrel.adhesiveSleeveWeight =
                getCircumferenceSoughtFor(mandrel, (sampleCapParameters.capHeight - 5)) / 2

            mandrel.height = sampleCapParameters.capHeight
            mandrel.totalMembraneLength = MembraneLengthCounter.count(
                sampleCapParameters.capHeight,
                sampleCapParameters.capInversion,
                sampleCapParameters.totalCapAmount
            )
            return mandrel
        }
    }
}