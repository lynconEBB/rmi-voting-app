package unioeste.sd.client;

import unioeste.sd.common.Server;

import java.rmi.RemoteException;
import java.util.logging.Handler;

public class UpdateTask implements Runnable{
    private Client client;
    private Server server;
    public UpdateTask(Client client, Server server) {
        this.client = client;
        this.server = server;
    }

    public static UpdateTask startTask(Client client, Server server) {
        UpdateTask updateTask = new UpdateTask(client, server);
        new Thread(updateTask).start();
        return updateTask;
    }

    @Override
    public void run() {
        try {
            while (true) {
                client.availableVotings = server.getAllVotings();
                if (client.selectedVoting != null) {
                    client.selectedVoting = server.getVotingById(client.getUser(), client.selectedVoting.id);
                }
                Thread.sleep(2000);
            }
        } catch (RemoteException | InterruptedException e) {
            client.setLogged(false);
            throw new RuntimeException(e);
        }
    }
}
