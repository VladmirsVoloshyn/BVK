package com.example.bvk.model

class MandrelProcessor() {

    companion object {

        private fun getCircumferenceSoughtFor(mandrel: Mandrel, heightSoughtFor: Int): Double {
            return ((getTapper(mandrel) * heightSoughtFor) + (mandrel.vertexDiameter)) * Math.PI
        }

        private fun getTapper(mandrel: Mandrel): Double {
            return (mandrel.baseDiameter - mandrel.vertexDiameter) / mandrel.height
        }

        fun setDataForMandrel(mandrel: Mandrel, heightLookFor: Int): Mandrel {
            mandrel.tapper = getTapper(mandrel)
            mandrel.membraneWight = getCircumferenceSoughtFor(mandrel, heightLookFor) + 5
            mandrel.adhesiveSleeveWeight = getCircumferenceSoughtFor(mandrel, heightLookFor) / 2
            return mandrel
        }
    }
}