package com.thirdwavelist.coficiando.core.util.ext

/**
 * Utility method for easy mapping interface
 *
 * ```
 * class MapDtoToEntity(): Mapper<Dto, Entity> {
 *     override fun invoke(from: Dto): Entity = from.toEntity()
 * }
 * ```
 */
typealias Mapper<FROM, TO> = (from: FROM) -> TO