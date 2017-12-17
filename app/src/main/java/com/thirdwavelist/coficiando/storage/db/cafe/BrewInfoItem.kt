package com.thirdwavelist.coficiando.storage.db.cafe

data class BrewInfoItem(val hasEspresso: Boolean,
                        val hasAeropress: Boolean,
                        val hasPourOver: Boolean,
                        val hasColdBrew: Boolean,
                        val hasSyphon: Boolean,
                        val hasFullImmersive: Boolean)