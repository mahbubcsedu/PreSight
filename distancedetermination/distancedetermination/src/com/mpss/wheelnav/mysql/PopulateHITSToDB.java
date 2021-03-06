package com.mpss.wheelnav.mysql;

public class PopulateHITSToDB {

	public static final String MYSQL_SERVER_URL = "mpss.csce.uark.edu";
	public static final String MYSQLDB_HITS_TABLE_NAME = "AMTSidewalkTileAnnotationHITS";
	
	public static void main(String[] args) {

		try {

			if(args.length!=4) {
				System.out.println("Usage: \n"
						+ "Arg0 : MYSQL USER \n"
						+ "Arg1 : MYSQL PWD \n"
						+ "Arg2 : MYSQL Database \n"
						+ "Arg3 : questionnaire.input.success file contaiing HITids\n");
				return;
			}

			String mysql_user = args[0];
			String mysql_password = args[1];
			String mysql_db = args[2];
			String questionnaireInputSuccessFile = args[3];
			
			PopulateHITSToDBHelper helper = new PopulateHITSToDBHelper();
			System.out.println("PopulateHITSToDB - Begin");
			helper.populateHITSToDB(MYSQL_SERVER_URL, mysql_user, mysql_password, mysql_db, MYSQLDB_HITS_TABLE_NAME, questionnaireInputSuccessFile);
			System.out.println("PopulateHITSToDB - Done");
		}
		catch (Exception ex) {
			ex.printStackTrace(); 
		}

	}

}
