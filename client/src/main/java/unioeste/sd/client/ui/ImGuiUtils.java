package unioeste.sd.client.ui;

import imgui.ImGui;
import imgui.ImVec2;

public class ImGuiUtils {
    public static ImVec2 centerNextItem(ImVec2 itemSize) {
        ImVec2 centerPosition = new ImVec2((ImGui.getWindowWidth() - itemSize.x) / 2, (ImGui.getWindowHeight() - itemSize.y) / 2);
        ImGui.setCursorPos(centerPosition.x, centerPosition.y);
        return centerPosition;
    }

    public static void paddedRect(float pX, float pY, int col, float rounding) {
        ImGui.getBackgroundDrawList().addRectFilled(
                ImGui.getItemRectMinX() - pX,
                ImGui.getItemRectMinY() - pY,
                ImGui.getItemRectMaxX() + pX,
                ImGui.getItemRectMaxY() + pY,
                col, rounding);
    }
}
