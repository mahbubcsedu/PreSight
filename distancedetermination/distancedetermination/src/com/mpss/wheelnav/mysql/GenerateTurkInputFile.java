package com.mpss.wheelnav.mysql;

public class GenerateTurkInputFile {

	public static final String MYSQL_SERVER_URL = "mpss.csce.uark.edu";
	public static final String MYSQL_DB_TABLE_NAME = "accesspath_data";

	public static void main(String[] args) {
		try {

			if(args.length!=6) {
				System.out.println("Usage: \n"
						+ "Arg0 : MYSQL USER \n"
						+ "Arg1 : MYSQL PWD \n"
						+ "Arg2 : MYSQL Database \n"
						+ "Arg3 : Vertical Images Output File Path\n"
						+ "Arg4 : Horizontal Images Output File Path\n"
						+ "Arg5 : Success File Path (Accessibility requestIDs)");
				return;
			}

			String mysql_user = args[0];
			String mysql_password = args[1];
			String mysql_db = args[2];
			String vertical_image_output_file = args[3];
			String horizontal_image_output_file = args[4];
			String success_file = args[5];

			GenerateTurkInputFileHelper helper = new GenerateTurkInputFileHelper();
			System.out.println("GenerateTurkInputFile - Begin");
			helper.generateTurkInput(MYSQL_SERVER_URL, mysql_user, mysql_password, mysql_db, MYSQL_DB_TABLE_NAME, vertical_image_output_file, horizontal_image_output_file, success_file);
			System.out.println("GenerateTurkInputFile - Done");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
