/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.core.player
 *
 * [] Creation Date : 03-03-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.trax.core.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetPlayer implements Player {

	private Socket player;

	public ServerNetPlayer(int port){
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Waiting for connection ..");
			player = serverSocket.accept();
			System.out.println("Connected to " + player.getInetAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getName(){
		return "None";
	}

	public String move(String otherPlayerMove){
		try {
			if (otherPlayerMove != null) {
				PrintWriter ostream = new PrintWriter(player.getOutputStream());
				ostream.println(otherPlayerMove);
				ostream.flush();
			}
			System.out.println("Waiting for remote player");
			BufferedReader istream = new BufferedReader(new InputStreamReader(player.getInputStream()));
			return istream.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}