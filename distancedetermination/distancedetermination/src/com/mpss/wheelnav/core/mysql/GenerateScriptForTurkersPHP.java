/*
 * Distance determintaion
 * Author : Amol Deshpande, Mahbubur rahman
 * {date}
 * @author B. P. Hennessey
 * @version 1.0
 */
package com.mpss.wheelnav.core.mysql;

// TODO: Auto-generated Javadoc
/**
 * The Class GenerateScriptForTurkersPHP.
 */
public class GenerateScriptForTurkersPHP {

/** The Constant MYSQL_SERVER_URL. */
public static final String MYSQL_SERVER_URL = "mpss.csce.uark.edu";
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {

			if(args.length!=26) {
				System.out.println("Incorrect number of parameters");
				return;
			}

			String cameraParametersMappingFile = args[0];
			String inputImagesDirectory = args[1];
			String workingDirectory = args[2];
			String requestID = args[3];
			String sidewalkTileLength = args[4];
			String sidewalkTileWidth = args[5];
			String cvHeight = args[6];
			String cvWidth = args[7];
			String iHeight = args[8];
			String iWidth = args[9];
			String stp1x = args[10];
			String stp1y = args[11];
			String stp2x = args[12];
			String stp2y = args[13];
			String stp3x = args[14];
			String stp3y = args[15];
			String stp4x = args[16];
			String stp4y = args[17];
			String deviceType = args[18];
			String pitchString = args[19];
			String rollString= args[20];
			String bbleft= args[21];
			String bbright= args[22]; 
			String bbtop= args[23]; 
			String bbbottom= args[24];
			String answer_unit=args[25];
			
			
			GenerateScriptForTurkersPHPHelper helper = new GenerateScriptForTurkersPHPHelper();
			helper.createWheelNavCoreTestInputgenerateTurkInput(cameraParametersMappingFile, inputImagesDirectory, workingDirectory,  
					requestID, sidewalkTileLength, sidewalkTileWidth, cvHeight, cvWidth, iHeight, iWidth, stp1x, stp1y, stp2x, stp2y, stp3x, stp3y, stp4x, stp4y, deviceType, pitchString, rollString, bbleft, bbright, bbtop, bbbottom, answer_unit) ;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
