package com.example.foundation.sideeffectso


import androidx.appcompat.app.AppCompatActivity
import com.example.foundation.sideeffects.SideEffectImplementation
import com.example.foundation.sideeffects.SideEffectMediatorsHolder
import com.example.foundation.sideeffects.SideEffectPlugin

@Suppress("UNCHECKED_CAST")
class SideEffectImplementationsHolder {

    private val _implementations = mutableMapOf<Class<*>, Any>()
    val implementations: Collection<SideEffectImplementation>
        get() = _implementations.values.filterIsInstance<SideEffectImplementation>()

    fun <Mediator, Implementation> getWithPlugin(plugin: SideEffectPlugin<Mediator, Implementation>): Implementation? {
        return _implementations[plugin.mediatorClass] as Implementation?
    }

    fun <Mediator, Implementation> putWithPlugin(
        plugin: SideEffectPlugin<Mediator, Implementation>,
        sideEffectMediatorsHolder: SideEffectMediatorsHolder,
        activity: AppCompatActivity
    ) {
        val sideEffectMediators = sideEffectMediatorsHolder.get(plugin.mediatorClass)
        val target = plugin.createImplementation(sideEffectMediators)
        if (target != null && target is SideEffectImplementation) {
            _implementations[plugin.mediatorClass] = target
            target.injectActivity(activity)
        }
    }

    fun clear() {
        _implementations.clear()
    }

}