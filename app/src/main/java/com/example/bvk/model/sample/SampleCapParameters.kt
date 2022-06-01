package com.example.bvk.model.sample

data class SampleCapParameters(
    var capVertexDiameter: Int =0,
    var capHeight: Int = 0,
    var capInversion: Int = 0,
    var totalCapAmount: Int = 0
) {
    init {
        totalCapAmount *= 1000
    }
    override fun toString(): String {
        return "V=$capVertexDiameter, H=$capHeight, I=$capInversion, C=$totalCapAmount"
    }
}
