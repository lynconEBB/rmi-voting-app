package unioeste.sd.client;

import imgui.ImGui;
import unioeste.sd.client.ui.Fonts;
import unioeste.sd.client.ui.ImGuiUtils;
import unioeste.sd.common.VotingStatus;

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

                ImGui.pushFont(Fonts.bigFont);
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
                ImGuiUtils.centerHorizontally(ImGui.calcTextSize(client.selectedVoting.title).x);
                ImGui.text(client.selectedVoting.title);
                ImGui.popFont();

                ImGui.setCursorPosY(ImGui.getCursorPosY() + 10);
                ImGui.separator();
                ImGui.setCursorPos(ImGui.getCursorPosX() + 30, ImGui.getCursorPosY() + 30);

                ImGui.beginChild("Info", (ImGui.getWindowWidth() - 10) / 2, ImGui.getTextLineHeightWithSpacing() * 6);

                ImGui.text("Status:");
                ImGui.sameLine();
                ImGui.textColored(client.selectedVoting.status.col, client.selectedVoting.status.text);

                if (client.getUser().username.equals(client.selectedVoting.ownerUsername)
                        && client.selectedVoting.status != VotingStatus.FINISHED) {
                    ImGui.sameLine();
                    ImGui.setCursorPosY(ImGui.getCursorPosY() - 5);
                    if(ImGui.button("Proceed")) {
                        client.requestProceed();
                    }
                }

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

                ImGui.endChild();

                if (client.selectedVoting.status == VotingStatus.FINISHED && client.selectedVoting.winners != null) {
                    ImGui.sameLine();
                    ImGui.beginChild("winner", 0, ImGui.getItemRectSizeY());
                    ImGui.pushFont(Fonts.titleFont);
                    ImGuiUtils.centerHorizontally(ImGui.calcTextSize("Winner").x);
                    ImGui.textColored(ImGui.colorConvertFloat4ToU32(1,0,1,1), "Winner");
                    ImGui.pushFont(Fonts.subtitleFont);
                    for (String winner : client.selectedVoting.winners) {
                        ImGuiUtils.centerHorizontally(ImGui.calcTextSize(winner).x);
                        ImGui.text(winner);
                    }
                    ImGui.popFont();
                    ImGui.popFont();
                    ImGui.endChild();
                }

                ImGui.setCursorPosY(ImGui.getCursorPosY() + 20);
                ImGui.separator();
                ImGui.setCursorPos(ImGui.getCursorPosX() + 30, ImGui.getCursorPosY() + 30);

                ImGui.beginChild("Progress", ImGui.getWindowWidth() - 50, 70 * client.selectedVoting.votes.size());
                boolean shouldDisable = false;
                if (client.selectedVoting.status != VotingStatus.STARTED) {
                    ImGui.beginDisabled();
                    shouldDisable = true;
                }
                ImGui.pushFont(Fonts.subtitleFont);
                for (Map.Entry<String, Integer> option : client.selectedVoting.votes.entrySet()) {
                    if (ImGui.checkbox(option.getKey(), client.userVotes.get(option.getKey()))) {
                        client.requestVote(option.getKey());
                    }
                    ImGui.progressBar((float) option.getValue() / client.selectedVoting.voters.size(), ImGui.getWindowWidth() - 40, 20);
                    ImGui.setCursorPosY(ImGui.getCursorPosY() + 20);
                }

                if (client.selectedVoting.status != VotingStatus.STARTED && shouldDisable)
                    ImGui.endDisabled();
                ImGui.popFont();
                ImGui.endChild();

                ImGui.setCursorPosY(ImGui.getCursorPosY() + 20);
                ImGui.separator();
                ImGui.setCursorPos(ImGui.getCursorPosX() + 30, ImGui.getCursorPosY() + 30);
                ImGui.beginGroup();
                ImGui.pushFont(Fonts.titleFont);
                ImGui.text("Voters (" + client.selectedVoting.voters.size() + ")");
                ImGui.popFont();
                for (String username : client.selectedVoting.voters.keySet()) {
                    ImGui.bulletText(username);
                }
                ImGui.endGroup();

            }
        }
        ImGui.end();
    }
}
