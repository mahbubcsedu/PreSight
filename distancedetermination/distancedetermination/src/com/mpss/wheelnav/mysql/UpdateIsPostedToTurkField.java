package com.mpss.wheelnav.mysql;
import java.io.IOException;


public class UpdateIsPostedToTurkField {

	public static final String MYSQL_SERVER_URL = "mpss.csce.uark.edu";
	public static final String MYSQL_DB_TABLE_NAME = "accesspath_data";

	public static void main(String[] args) throws IOException {
		try {

			if(args.length!=5) {
				System.out.println("Usage: \n"
						+ "Arg0 : MYSQL USER \n"
						+ "Arg1 : MYSQL PWD \n"
						+ "Arg2 : MYSQL Database \n"
						+ "Arg3 : Success File (containing Accessibility requestIDs)\n"
						+ "Arg4 : isPostedToTurk Column Value to be set");
				return;
			}

			String mysql_user = args[0];
			String mysql_password = args[1];
			String mysql_db = args[2];
			String success_file = args[3];
			String isPostedToTurkValue = args[4];

			System.out.println("UpdateIsPostedToTurkField - Begin");
			UpdateIsPostedToTurkFieldHelper helper = new UpdateIsPostedToTurkFieldHelper();
			helper.updatePostedToTurk(MYSQL_SERVER_URL, mysql_user, mysql_password, mysql_db, MYSQL_DB_TABLE_NAME, isPostedToTurkValue, success_file);
			System.out.println("UpdateIsPostedToTurkField - Done");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
