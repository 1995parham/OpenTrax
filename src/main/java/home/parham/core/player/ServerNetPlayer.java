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

package home.parham.core.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetPlayer implements Player {

	private Socket player;

	public ServerNetPlayer(){
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(1373);
			System.out.println("Waiting for connection ..");
			player = serverSocket.accept();
			System.out.println("Connected to " + player.getInetAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String move(String otherPlayerMove){
		try {
			PrintWriter ostream = new PrintWriter(player.getOutputStream());
			ostream.println(otherPlayerMove);
			ostream.flush();
			System.out.println("Waiting for remote player");
			BufferedReader istream = new BufferedReader(new InputStreamReader(player.getInputStream()));
			return istream.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}