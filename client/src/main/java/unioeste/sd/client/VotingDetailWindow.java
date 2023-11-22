package unioeste.sd.client;

import imgui.ImGui;
import imgui.type.ImBoolean;
import unioeste.sd.client.ui.Fonts;
import unioeste.sd.client.ui.ImGuiUtils;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public class VotingDetailWindow {
    private final DateTimeFormatter dateFormatter;

    public VotingDetailWindow() {
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    public void draw(Client client) {
        ImGui.begin("Current Voting");
        {
            if (client.selectedVoting == null) {
                ImGui.pushFont(Fonts.titleFont);
                String noVotingMessage = "No voting selected, nothing to show here!";
                ImGuiUtils.centerNextItem(ImGui.calcTextSize(noVotingMessage));
                ImGui.text(noVotingMessage);
                ImGui.popFont();
            } else {
                ImGui.pushFont(Fonts.titleFont);
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
                ImGuiUtils.centerHorizontally(ImGui.calcTextSize(client.selectedVoting.title).x);
                ImGui.text(client.selectedVoting.title);
                ImGui.popFont();
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
                ImGui.separator();

                ImGui.setCursorPosX(ImGui.getCursorPosX() + 30);
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 30);
                ImGui.beginGroup();

                ImGui.text("Status:");
                ImGui.sameLine();
                ImGui.textColored(client.selectedVoting.status.col, client.selectedVoting.status.text);
                ImGui.text("Owner: " + client.selectedVoting.ownerUsername);
                ImGui.text("Description: " + client.selectedVoting.description);
                ImGui.text("Creation date:");
                if (client.selectedVoting.creationDate != null) {
                    ImGui.sameLine();
                    ImGui.text(client.selectedVoting.creationDate.format(dateFormatter));
                }
                ImGui.text("Start date:");
                if (client.selectedVoting.startDate != null) {
                    ImGui.sameLine();
                    ImGui.text(client.selectedVoting.startDate.format(dateFormatter));
                }
                ImGui.text("End date:");
                if (client.selectedVoting.endDate != null) {
                    ImGui.sameLine();
                    ImGui.text(client.selectedVoting.endDate.format(dateFormatter));
                }

                ImGui.endGroup();
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 20);
                ImGui.separator();
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 20);
                ImGui.pushFont(Fonts.subtitleFont);
                for (Map.Entry<String, Integer> option : client.selectedVoting.votes.entrySet()) {
                    if (ImGui.checkbox(option.getKey(), client.selectedVoting.userVotes.get(option.getKey()))) {
                        client.requestVote(option.getKey());
                    }
                    ImGui.progressBar((float) option.getValue() / client.selectedVoting.votes.size(), ImGui.getWindowWidth() - 40, 20);
                    ImGui.setCursorPosY(ImGui.getCursorPosY() + 20);
                }

                ImGui.setCursorPosY(ImGui.getCursorPosY() + 20);
                ImGui.separator();
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 20);
                ImGui.text("Voters");
                ImGui.popFont();
                for (String username : client.selectedVoting.voters.keySet()) {
                    ImGui.bulletText(username);
                }

            }
        }
        ImGui.end();
    }
}
