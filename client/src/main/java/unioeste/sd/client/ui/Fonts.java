package unioeste.sd.client.ui;

import imgui.ImFont;
import imgui.ImFontConfig;
import imgui.ImGui;

public final class Fonts {
    public static ImFont titleFont;
    public static ImFont defaultFont;

    public static void InitFonts() {
        ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setSizePixels(20);
        fontConfig.setOversampleH(1);
        fontConfig.setOversampleV(1);
        fontConfig.setPixelSnapH(true);

        defaultFont = ImGui.getIO().getFonts().addFontDefault();
        titleFont = ImGui.getIO().getFonts().addFontDefault(fontConfig);
    }
}
