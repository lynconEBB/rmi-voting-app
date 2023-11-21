package unioeste.sd.client;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.type.ImBoolean;
import unioeste.sd.client.ui.Fonts;
import unioeste.sd.client.ui.ImGuiUtils;
import unioeste.sd.common.User;
import unioeste.sd.common.Voting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends Application {

    public final WelcomeWindow welcomeWindow;
    private final Client client;
    private Voting selectedVoting;
    private List<Voting> votings;

    public MainWindow() {
        this.welcomeWindow = new WelcomeWindow();
        this.client = new Client(this);
        this.votings = new ArrayList<>();
        this.votings.add(new Voting(new User("fdsfd", "fdsfd", "fdsfds"),"Votacao 1"));
        this.votings.add(new Voting(new User("fdsfd", "fdsfd", "fdsfds"),"Votacao 1"));
        this.votings.add(new Voting(new User("fdsfd", "fdsfd", "fdsfds"),"Votacao 1"));
        this.votings.add(new Voting(new User("fdsfd", "fdsfd", "fdsfds"),"Votacao 1"));
    }

    @Override
    public void process() {
        ImGui.dockSpaceOverViewport(ImGui.getMainViewport());

        if (client.isLogged()) {
            welcomeWindow.draw(client);
        }
        else {
            ImGui.begin("Votings Sidebar");
            {
                for (Voting voting : votings) {
                    VotingItemWidget.draw(voting);
                }
            }
            ImGui.end();

            ImGui.begin("Current Voting");
            {
                if (selectedVoting == null) {
                    ImGui.pushFont(Fonts.titleFont);
                    String noVotingMessage = "No voting selected, nothing to show here!";
                    ImGuiUtils.centerNextItem(ImGui.calcTextSize(noVotingMessage));
                    ImGui.text(noVotingMessage);
                    ImGui.popFont();
                } else {

                }
            }
            ImGui.end();
        }
    }

    @Override
    protected void initImGui(Configuration config) {
        super.initImGui(config);

        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.DockingEnable);
        Fonts.InitFonts();
    }

    @Override
    protected void configure(Configuration config) {
        config.setTitle("RMI Voting");
        config.setWidth(1920);
        config.setHeight(1080);
    }

    public static void main(String[] args) {
        launch(new MainWindow());
    }
}
