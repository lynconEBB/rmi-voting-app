package unioeste.sd.client;

import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiMouseCursor;
import unioeste.sd.client.ui.Fonts;
import unioeste.sd.client.ui.ImGuiUtils;
import unioeste.sd.common.Voting;

public class VotingItemWidget {
    public static void draw(Voting voting) {
        ImGui.setCursorPosX(ImGui.getCursorPosX() + 10);
        ImGui.beginGroup();
        ImGui.dummy(ImGui.getWindowWidth(),10);
        ImGui.pushFont(Fonts.subtitleFont);
        ImGui.text(voting.title);
        ImGui.popFont();
        ImGui.text("Owner: " + voting.owner.name);
        ImGui.endGroup();
        if (ImGui.isItemHovered()) {
            ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
            ImGuiUtils.paddedRect(-10,-10, ImGui.colorConvertFloat4ToU32(1,0,0,1), 10);
        }
        ImGui.dummy(ImGui.getWindowWidth(),10);
        ImGui.separator();
    }
}
