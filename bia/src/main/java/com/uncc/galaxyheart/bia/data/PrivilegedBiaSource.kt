package com.uncc.galaxyheart.bia.data

/**
 * Placeholder for the REAL BIA source.
 *
 * Wiring this up requires the **Samsung Privileged Health SDK** (partnership/NDA),
 * which exposes an on-demand BIA tracker. Map its result to [BiaReading] in
 * [measure] and set [supported] from its capability check. Until then this
 * reports unsupported.
 */
class PrivilegedBiaSource : BiaSource {
    override val sourceName = "Galaxy Watch BIA (Privileged SDK — not wired)"
    override val supported = false
    override suspend fun measure(): BiaReading? = null
}
