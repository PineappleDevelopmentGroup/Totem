package sh.miles.totem.json.keyed

import org.bukkit.Particle
import org.bukkit.Registry
import sh.miles.totem.json.KeyedJsonAdapter

object ParticleAdapter : KeyedJsonAdapter<Particle> {

    override fun registry(): Registry<Particle> = Registry.PARTICLE_TYPE

    override fun getAdapterType(): Class<Particle> {
        return Particle::class.java
    }
}
