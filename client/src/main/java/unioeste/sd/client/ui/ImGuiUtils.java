package unioeste.sd.client.ui;

import imgui.ImGui;
import imgui.ImVec2;

public class ImGuiUtils {
    public static ImVec2 centerNextItem(ImVec2 itemSize) {
        ImVec2 centerPosition = new ImVec2((ImGui.getWindowWidth() - itemSize.x) / 2, (ImGui.getWindowHeight() - itemSize.y) / 2);
        ImGui.setCursorPos(centerPosition.x, centerPosition.y);
        return centerPosition;
    }

    public static float centerHorizontally(float itemSize) {
        float centerPos =  (ImGui.getWindowWidth() - itemSize) / 2;
        ImGui.setCursorPosX(centerPos);
        return centerPos;
    }

    public static void paddedRect(float paddingLeft, float paddingRight,float paddingTop, float paddingBottom, int col, float rounding) {
        ImGui.getWindowDrawList().addRect(
                ImGui.getItemRectMinX() - paddingLeft,
                ImGui.getItemRectMinY() - paddingTop,
                ImGui.getItemRectMaxX() + paddingRight,
                ImGui.getItemRectMaxY() + paddingBottom,
                col, rounding);
    }
}
