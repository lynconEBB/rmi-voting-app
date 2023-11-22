package unioeste.sd.client.ui;

import imgui.ImFont;
import imgui.ImFontConfig;
import imgui.ImGui;

public final class Fonts {
    public static ImFont titleFont;
    public static ImFont defaultFont;
    public static ImFont subtitleFont;
    public static ImFont bigFont;

    public static void InitFonts() {
        ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setSizePixels(20);
        fontConfig.setOversampleH(1);
        fontConfig.setOversampleV(1);
        fontConfig.setPixelSnapH(true);

        defaultFont = ImGui.getIO().getFonts().addFontDefault();
        titleFont = ImGui.getIO().getFonts().addFontDefault(fontConfig);
        fontConfig.setSizePixels(16);
        subtitleFont = ImGui.getIO().getFonts().addFontDefault(fontConfig);
        fontConfig.setSizePixels(40);
        bigFont = ImGui.getIO().getFonts().addFontDefault(fontConfig);
    }
}
