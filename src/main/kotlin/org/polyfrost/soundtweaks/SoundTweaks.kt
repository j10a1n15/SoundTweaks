package org.polyfrost.soundtweaks

//#if FABRIC
//$$ import net.fabricmc.api.ModInitializer;
//#elseif FORGE
import dev.deftu.omnicore.api.client.commands.OmniClientCommands
import dev.deftu.omnicore.api.client.commands.command
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif
import dev.deftu.omnicore.api.client.sound.OmniClientSound
import net.minecraft.client.audio.SoundEventAccessorComposite
import net.minecraft.util.ResourceLocation
import org.polyfrost.oneconfig.utils.v1.dsl.openUI
import org.polyfrost.soundtweaks.config.SoundTweaksConfig
import org.polyfrost.soundtweaks.mixins.RegistrySimpleAccessor
import org.polyfrost.soundtweaks.mixins.SoundHandlerAccessor
import org.polyfrost.soundtweaks.mixins.SoundRegistryAccessor

//#if FORGE-LIKE
@Mod(modid = SoundTweaks.ID, name = SoundTweaks.NAME, version = SoundTweaks.VERSION)
//#endif
class SoundTweaks
//#if FABRIC
//$$ : ModInitializer
//#endif
{
    //#if FABRIC
    //$$ override
    //#elseif FORGE
    @Mod.EventHandler
    //#endif
    fun onInitialize(
        //#if FORGE
        event: FMLInitializationEvent
        //#endif
    ) {
        OmniClientCommands.command("soundtweaks") {
            runs {
                config.openUI()
                0
            }
        }.register()
    }

    companion object {
        const val ID = "@MOD_ID@"
        const val NAME = "@MOD_NAME@"
        const val VERSION = "@MOD_VERSION@"
        const val MC = "@MC_VERSION@"

        val volumes = mutableMapOf<ResourceLocation, Float>()

        private val isSchizoAsm = runCatching {
            listOf(
                "zone.rong.loliasm.api.mixins.RegistrySimpleExtender",
                "mirror.normalasm.api.mixins.RegistrySimpleExtender"
            ).any { Class.forName(it, false, this::class.java.getClassLoader()) != null }
        }.getOrElse { false }

        @JvmStatic
        lateinit var config: SoundTweaksConfig

        @JvmStatic
        fun onReloadResourceManager() {
            config = SoundTweaksConfig()
        }

        fun getSounds(): MutableMap<ResourceLocation, SoundEventAccessorComposite> {
            val registry = (OmniClientSound.soundManager as? SoundHandlerAccessor)?.getSoundRegistry()
            return (if (isSchizoAsm) (registry as? RegistrySimpleAccessor)?.registryObjects else (registry as? SoundRegistryAccessor)?.sounds) ?: mutableMapOf()
        }
    }
}
