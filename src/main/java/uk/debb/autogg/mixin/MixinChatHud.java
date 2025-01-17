package uk.debb.autogg.mixin;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.debb.autogg.AutoGG;

@Mixin(ChatHud.class)
public abstract class MixinChatHud {
    @Unique private long lastTime = 0;

    @Shadow @Final private MinecraftClient client;

    @Unique private List<String> hypixelGGStrings = new ArrayList<String>();
    @Unique private List<String> bedwarsPracticeGGStrings = new ArrayList<String>();
    @Unique private List<String> pvpLandGGStrings = new ArrayList<String>();
    @Unique private List<String> minemenGGStrings = new ArrayList<String>();

    @Unique private List<String> hypixelGLHFStrings = new ArrayList<String>();
    @Unique private List<String> bedwarsPracticeGLHFStrings = new ArrayList<String>();
    @Unique private List<String> pvpLandGLHFStrings = new ArrayList<String>();
    @Unique private List<String> minemenGLHFStrings = new ArrayList<String>();

    @Unique private List<String> hypixelGFStrings = new ArrayList<String>();
    @Unique private List<String> bedwarsPracticeGFStrings = new ArrayList<String>();
    @Unique private List<String> pvpLandGFStrings = new ArrayList<String>();
    @Unique private List<String> minemenGFStrings = new ArrayList<String>();

    @Unique private void populateHypixelGGStrings() {
        hypixelGGStrings.add("1st Killer -");
        hypixelGGStrings.add("1st Place -");
        hypixelGGStrings.add("Winner:");
        hypixelGGStrings.add(" - Damage Dealt -");
        hypixelGGStrings.add("Winning Team -");
        hypixelGGStrings.add("1st -");
        hypixelGGStrings.add("Winners:");
        hypixelGGStrings.add("Winner:");
        hypixelGGStrings.add("Winning Team:");
        hypixelGGStrings.add(" won the game!");
        hypixelGGStrings.add("Top Seeker:");
        hypixelGGStrings.add("1st Place:");
        hypixelGGStrings.add("Last team standing!");
        hypixelGGStrings.add("Winner #1 (");
        hypixelGGStrings.add("Top Survivors");
        hypixelGGStrings.add("Winners -");
        hypixelGGStrings.add("Sumo Duel -");
        hypixelGGStrings.add("Most Wool Placed -");
        hypixelGGStrings.add("Your Overall Winstreak:");
    }
    @Unique private void populateBedwarsPracticeGGStrings() {
        bedwarsPracticeGGStrings.add("Winners -");
        bedwarsPracticeGGStrings.add("Game Won!");
        bedwarsPracticeGGStrings.add("Game Lost!");
        bedwarsPracticeGGStrings.add("The winning team is");
    }
    @Unique private void populatePvpLandGGStrings() {
        pvpLandGGStrings.add("The match has ended!");
        pvpLandGGStrings.add("Match Results");
        pvpLandGGStrings.add("Winner:");
        pvpLandGGStrings.add("Loser:");
    }
    @Unique private void populateMinemenGGStrings() {
        minemenGGStrings.add("Match Results");
    }

    @Unique private void populateHypixelGLHFStrings() {
        hypixelGLHFStrings.add("The game starts in 1 second!");
    }
    @Unique private void populateBedwarsPracticeGLHFStrings() {
        bedwarsPracticeGLHFStrings.add("Game starting in 1 seconds!");
        bedwarsPracticeGLHFStrings.add("Game has started!");
    }
    @Unique private void populatePvpLandGLHFStrings() {
        pvpLandGLHFStrings.add("The match is starting in 1 second.");
        pvpLandGLHFStrings.add("The match has started!");
    }
    @Unique private void populateMinemenGLHFStrings() {
        minemenGLHFStrings.add("1...");
    }

    @Unique private void populateHypixelGFStrings() {
        hypixelGFStrings.add("SkyWars Experience (Kill)");
        hypixelGFStrings.add("coins! (Final Kill)");
    }
    @Unique private void populateBedwarsPracticeGFStrings() {
        bedwarsPracticeGFStrings.add(client.getSession().getUsername() + " FINAL KILL!");
    }
    @Unique private void populatePvpLandGFStrings() {
        pvpLandGFStrings.add("slain by " + client.getSession().getUsername());
    }
    @Unique private void populateMinemenGFStrings() {
        minemenGFStrings.add("killed by " + client.getSession().getUsername() + "!");
    }

    @Unique private void processChat(Text messageRecieved, List<String> options, String messageToSend) {
        if (System.currentTimeMillis() - this.lastTime <= 3000) return;
        for (String s : options) {
            if (messageRecieved.getString().contains(s)) {
                client.player.sendChatMessage(messageToSend);
                this.lastTime = System.currentTimeMillis();
                return;
            }
        }
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At("HEAD"), cancellable = true)
    private void typeGG(Text message, int messageId, int timestamp, boolean bl, CallbackInfo ci) {
        if (client.getCurrentServerEntry().address.contains("hypixel.net")) {
            if (AutoGG.config.gfMessages) {
                if (hypixelGFStrings.size() == 0) populateHypixelGFStrings();
                processChat(message, hypixelGFStrings, "gf");
            }
            if (AutoGG.config.ggMessages) {
                if (hypixelGGStrings.size() == 0) populateHypixelGGStrings();
                processChat(message, hypixelGGStrings, "gg");
            }
            if (AutoGG.config.glhfMessages) {
                if (hypixelGLHFStrings.size() == 0) populateHypixelGLHFStrings();
                processChat(message, hypixelGLHFStrings, "glhf");
            }
        } else if (client.getCurrentServerEntry().address.contains("bedwarspractice.club")) {
            if (AutoGG.config.gfMessages) {
                if (bedwarsPracticeGFStrings.size() == 0) populateBedwarsPracticeGFStrings();
                processChat(message, bedwarsPracticeGFStrings, "gf");
            }
            if (AutoGG.config.ggMessages) {
                if (bedwarsPracticeGGStrings.size() == 0) populateBedwarsPracticeGGStrings();
                processChat(message, bedwarsPracticeGGStrings, "gg");
            }
            if (AutoGG.config.glhfMessages) {
                if (bedwarsPracticeGLHFStrings.size() == 0) populateBedwarsPracticeGLHFStrings();
                processChat(message, bedwarsPracticeGLHFStrings, "glhf");
            }
        } else if (client.getCurrentServerEntry().address.contains("pvp.land")) {
            if (AutoGG.config.gfMessages) {
                if (pvpLandGFStrings.size() == 0) populatePvpLandGFStrings();
                processChat(message, pvpLandGFStrings, "gf");
            }
            if (AutoGG.config.ggMessages) {
                if (pvpLandGGStrings.size() == 0) populatePvpLandGGStrings();
                processChat(message, pvpLandGGStrings, "gg");
            }
            if (AutoGG.config.glhfMessages) {
                if (pvpLandGLHFStrings.size() == 0) populatePvpLandGLHFStrings();
                processChat(message, pvpLandGLHFStrings, "glhf");
            }
        } else if (client.getCurrentServerEntry().address.contains("minemen.club")) {
            if (AutoGG.config.gfMessages) {
                if (minemenGFStrings.size() == 0) populateMinemenGFStrings();
                processChat(message, minemenGFStrings, "gf");
            }
            if (AutoGG.config.ggMessages) {
                if (minemenGGStrings.size() == 0) populateMinemenGGStrings();
                processChat(message, minemenGGStrings, "gg");
            }
            if (AutoGG.config.glhfMessages) {
                if (minemenGLHFStrings.size() == 0) populateMinemenGLHFStrings();
                processChat(message, minemenGLHFStrings, "glhf");
            }
        } else if (client.getCurrentServerEntry().address.contains("mcplayhd.net")) {
            if (AutoGG.config.lMessages) {
                if (message.toString().contains("Regardless, we wish you a lot of fun playing.")) {
                    client.player.sendChatMessage("L");
                }
            }
        }
    }
}
