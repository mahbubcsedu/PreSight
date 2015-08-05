/*
 * Distance determintaion
 * @Author : Amol Deshpande, Mahbubur rahman
 * @since {date}
 * @version 1.0
 */
package com.mpss.wheelnav.core.mysql;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateWheelNavCoreInput.
 */
public class CreateWheelNavCoreInput {

	/** The Constant MYSQL_SERVER_URL. */
	public static final String MYSQL_SERVER_URL = "localhost";
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {

			if(args.length!=7) {
				System.out.println("Usage: \n"
						+ "Arg0 : MYSQL USER \n"
						+ "Arg1 : MYSQL PWD \n"
						+ "Arg2 : MYSQL Database \n"
						+ "Arg3 : Camera Parameters Mapping File \n"
						+ "Arg4 : Input Images Directory \n"
						+ "Arg5 : Working Directory for WheelNavCore \n"
						+ "Arg6 : Output Direcotry For Scripts \n");
				return;
			}

			String mysql_user = args[0];
			String mysql_password = args[1];
			String mysql_db = args[2];
			String cameraParametersMappingFile = args[3];
			String inputImagesDirectory = args[4];
			String workingDirectory = args[5];
			String outputDirectory = args[6];
		
			//just call the helper functions 
			CreateWheelNavCoreInputHelper helper = new CreateWheelNavCoreInputHelper();
			System.out.println("CreateWheelNavCoreTestInput - Begin");
			helper.createWheelNavCoreTestInputgenerateTurkInput(MYSQL_SERVER_URL, mysql_user, mysql_password, mysql_db, cameraParametersMappingFile, inputImagesDirectory, workingDirectory, outputDirectory);
			System.out.println("CreateWheelNavCoreTestInput - Done");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
