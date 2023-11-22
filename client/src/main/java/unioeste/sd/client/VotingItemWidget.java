package unioeste.sd.client;

import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiMouseCursor;
import unioeste.sd.client.ui.Fonts;
import unioeste.sd.client.ui.ImGuiUtils;
import unioeste.sd.common.Voting;
import unioeste.sd.common.VotingSimpleDTO;

public class VotingItemWidget {

    public static void draw(VotingSimpleDTO voting, Client client) {
        ImGui.setCursorPosX(ImGui.getCursorPosX() + 10);
        ImGui.beginGroup();
        ImGui.dummy(ImGui.getWindowWidth(),10);
        ImGui.pushFont(Fonts.subtitleFont);
        ImGui.text(voting.title);
        ImGui.popFont();
        ImGui.text("Owner: " + voting.owner.name);
        ImGui.dummy(ImGui.getWindowWidth(),10);
        ImGui.endGroup();
        if (ImGui.isItemHovered()) {
            ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
            ImGuiUtils.paddedRect(10,-30,0,0, ImGui.colorConvertFloat4ToU32(1,0,0,1), 10);
        }
        if (ImGui.isItemClicked()) {
            client.setSelectedVoting(voting.id);
        }
        ImGui.separator();
    }
}
