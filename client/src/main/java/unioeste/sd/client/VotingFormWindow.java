package unioeste.sd.client;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import unioeste.sd.client.ui.ImGuiUtils;
import unioeste.sd.common.Voting;
import unioeste.sd.common.VotingFormDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class VotingFormWindow {
    private ImString name;
    private ImString description;
    private List<ImString> options;
    private CompletableFuture<Voting> createdFuture;

    private String errorMessage;
    private boolean showErrorMessage;

    public VotingFormWindow() {
        this.name = new ImString();
        this.description = new ImString();
        this.options = new ArrayList<>();
        this.options.add(new ImString());
        this.options.add(new ImString());
        this.createdFuture = null;
    }

    public void draw(ImBoolean showWindow, Client client) {
        if (createdFuture != null && createdFuture.isDone()) {
            showWindow.set(false);
        }

        if (ImGui.begin("New Voting Form", showWindow, ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoDocking)) {

            if (showErrorMessage) {
                ImGui.textColored(255,0,0,255, errorMessage);
            }

            ImGui.inputText("Name", name);
            ImGui.inputTextMultiline("Description", description);
            ImGui.separator();

            if (ImGui.button("Add option")) {
                options.add(new ImString());
            }
            ImGui.sameLine();
            if (ImGui.button("Remove option") && options.size() > 2) {
                options.remove(options.size() - 1);
            }
            int count = 0;
            for (ImString opt : options) {
                ImGui.inputText("Option " + count, opt);
                count++;
            }

            ImGui.separator();

            boolean disabled = false;
            ImGuiUtils.centerHorizontally(ImGui.calcTextSize("Create").x);
            if (createdFuture != null && !createdFuture.isDone()) {
                disabled = true;
                ImGui.beginDisabled();
            }

            if (ImGui.button("Create")) {
                List<String> optionsStr = options.stream().map(str -> str.get()).collect(Collectors.toList());
                showErrorMessage = false;

                createdFuture = client.createVoting(
                        new VotingFormDTO(name.get(), description.get(), optionsStr));

                createdFuture.exceptionally(e -> {
                    showErrorMessage = true;
                    errorMessage = ExceptionsUtils.getRoot(e).getMessage();
                    createdFuture = null;
                    return null;
                });
            }

            if (disabled)
                ImGui.endDisabled();

            ImGui.setWindowSize(0,0);
            ImGui.setWindowPos((float)(ImGui.getIO().getDisplaySizeX() * 0.5) - ImGui.getWindowWidth()/2,
                    (float) (ImGui.getIO().getDisplaySizeY() * 0.5) - ImGui.getWindowHeight()/2);
            ImGui.end();
        }
    }
}

