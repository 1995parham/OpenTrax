/*
 * In The Name Of God
 * ======================================
 * [] Project Name : OpenTrax 
 * 
 * [] Package Name : home.parham.main.cli
 *
 * [] Creation Date : 11-02-2015
 *
 * [] Created By : Parham Alvani (parham.alvani@gmail.com)
 * =======================================
*/

package home.parham.main.cli;

import home.parham.main.TraxBoard;

import java.util.ArrayList;

public class CommandDispatcher {
	public static void dispatch(ArrayList<String> command){

		String line = command.get(0);

		if (line.equals("alarm")) {
			Commands.userAlarm(command);
		} else if (line.equals("analyze")) {
			Commands.userAnalyze();
		} else if (line.equals("annotate")) {
			Commands.userAnnotate();
		} else if (line.equals("back")) {
			Commands.userBack();
		} else if (line.equals("bench")) {
			Commands.userBench();
		} else if (line.equals("book")) {
			Commands.userBook(command);
		} else if (line.equals("books")) {
			Commands.userBooks(command);
		} else if (line.equals("black")) {
			Commands.userBlack();
		} else if (line.equals("clock")) {
			Commands.userClock();
		} else if (line.equals("display") || line.equals("d")) {
			if (command.size() == 1) {
				Commands.userDisplay();
			} else {
				Commands.userSetDisplay(command);
			}
		} else if (line.equals("edit")) {
			Commands.userEdit();
		} else if (line.equals("end")) {
			Commands.userEnd();
		} else if ((line.equals("exit")) || (line.equals("quit"))) {
			Commands.userExit();
		} else if (line.equals("force")) {
			Commands.userForce(command);
		} else if (line.equals("go")) {
			Commands.userGo();
		} else if (line.equals("help")) {
			Commands.userHelp(command);
		} else if (line.equals("hash")) {
			Commands.userHash(command);
		} else if (line.equals("history")) {
			Commands.userHistory();
		} else if (line.equals("import")) {
			Commands.userImport(command);
		} else if (line.equals("info")) {
			Commands.userInfo();
		} else if (line.equals("input")) {
			Commands.userInput(command);
		} else if (line.equals("learn")) {
			Commands.userLearn(command);
		} else if (line.equals("log")) {
			Commands.userLog(command);
		} else if (line.equals("move")) {
			Commands.userGo();
		} else if (line.equals("name")) {
			Commands.userName(command);
		} else if (line.equals("new")) {
			Commands.userNew();
		} else if (line.equals("noise")) {
			Commands.userNoise(command);
		} else if (line.equals("perf")) {
			Commands.userPerf(command);
		} else if (line.equals("perft")) {
			Commands.userPerft();
		} else if (line.equals("ponder")) {
			Commands.userPonder(command);
		} else if (line.equals("read")) {
			Commands.userRead(command);
		} else if (line.equals("reada")) {
			Commands.userReada(command);
		} else if (line.equals("reset")) {
			Commands.userReset(command);
		} else if (line.equals("savegame")) {
			Commands.userSavegame(command);
		} else if (line.equals("score")) {
			Commands.userScore();
		} else if (line.equals("sd")) {
			Commands.userSd(command);
		} else if (line.equals("search")) {
			Commands.userSearch(command);
		} else if (line.equals("settc")) {
			Commands.userSettc(command);
		} else if (line.equals("show")) {
			Commands.userShow(command);
		} else if (line.equals("st")) {
			Commands.userSt(command);
		} else if (line.equals("test")) {
			Commands.userTest(command);
		} else if (line.equals("time")) {
			Commands.userTime(command);
		} else if (line.equals("trace")) {
			Commands.userTrace(command);
		} else if (line.equals("white")) {
			Commands.userWhite();
		} else {
			if (GnuTrax.getInstance().getTraxBoard().isGameOver() == TraxBoard.NOPLAYER)
				GnuTrax.getInstance().gotAMove(line);
		}
	}
}