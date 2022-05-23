package com.example.bvk.model.amount

class MembraneLengthCounter {
    companion object {
        fun count(capHeight: Int, capInversion: Int, totalCapAmount: Int): Double {
            val div = (capHeight.toDouble() + capInversion.toDouble())/1000
            ((capHeight + capInversion).toDouble() / 1000) * totalCapAmount
         return  div*totalCapAmount
        }
    }
}