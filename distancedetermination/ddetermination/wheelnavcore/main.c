#include <opencv2/core/core.hpp>
#include <opencv2/calib3d/calib3d.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv/cv.h>
#include <stdio.h>
#include <stdlib.h>
#include<string.h>
#include <iostream>
#include <fstream>

using namespace std;
using namespace cv;

#define PI 3.1415926

/* Inputs to the module*/
std::string hitId;
std::string workerId;
std::string assignmentId;
std::string requestId;

std::string inputImageFile;
std::string outputDirectory;
std::string outputImageName;
std::string intrinsicParametersFile;
std::string distortionParametersFile;

double theta;
double measuredHeight;
double measuredSidewalkTileLength;  //y2-y0
double measuredSidewalkTileWidth;   //x1-x0
double measuredDistanceTowardsTile;
double measuredDistanceTowardIssue;

double sidewalkTileWidth;
double sidewalkTileLength;

double bBoxPoint1_x;
double bBoxPoint1_y;
double bBoxPoint2_x;
double bBoxPoint2_y;
double bBoxPoint3_x;
double bBoxPoint3_y;
double bBoxPoint4_x;
double bBoxPoint4_y;

double tilePoint1_x;
double tilePoint1_y;
double tilePoint2_x;
double tilePoint2_y;
double tilePoint3_x;
double tilePoint3_y;
double tilePoint4_x;
double tilePoint4_y;


/*Operational Parameters*/
CvPoint2D32f boundingBoxImagePoints[4];
CvPoint2D32f sidewalkTileImagePoints[4];
CvPoint2D32f sidewalkTileObjectPoints[4];

IplImage* originalImage;
cv::Mat originalImageMat;
cv::Mat topDownViewMat;

int flag=0, pointsCount=0;

/*Essential Output Parameters of the module*/
double dz;
double sin_theta;
double calculatedHeight;
double calculatedDistanceTowardsIssue;
double calculatedRelativeDistance;
double calculatedDistanceTowardsTile;
double  tileDistanceError;
double issueDistanceError;

CvMat *translationVector;
CvMat *homography;


CvPoint2D32f tranformPoint(CvMat* H, CvPoint2D32f p) {
    CvPoint2D32f newP;
    newP.x = (CV_MAT_ELEM(*H, float, 0, 0) * p.x) + (CV_MAT_ELEM(*H, float, 0, 1) * p.y) + (CV_MAT_ELEM(*H, float, 0, 2) * 1);
    newP.y = (CV_MAT_ELEM(*H, float, 1, 0) * p.x) + (CV_MAT_ELEM(*H, float, 1, 1) * p.y) + (CV_MAT_ELEM(*H, float, 1, 2) * 1);
    float z = (CV_MAT_ELEM(*H, float, 2, 0) * p.x) + (CV_MAT_ELEM(*H, float, 2, 1) * p.y) + (CV_MAT_ELEM(*H, float, 2, 2) * 1);
    newP.x = newP.x /z;
    newP.y = newP.y /z;
    return newP;
}

void writeOutput() {
    std::string header;
    header.append("RequestId");
    header.append("\tHitId");
    header.append("\tAssignmentId");
    header.append("\tWorkerId");
    header.append("\ttheta");
    header.append("\tsin_theta");
    header.append("\tcalculated_dz");
    header.append("\tmeasuredHeight");
    header.append("\tcalculatedHeight");
    header.append("\tcalculatedRelativeDistance");
    header.append("\tmeasuredDistanceTowardsTile");
    header.append("\tcalculatedDistanceTowardsTile");
    header.append("\ttileDistanceError");
    header.append("\tmeasuredDistanceTowardIssue");
    header.append("\tcalculatedDistanceTowardsIssue");
    header.append("\tissueDistanceError");
    header.append("\ttransX");
    header.append("\ttransY");
    header.append("\ttransZ");
    header.append("\tM11");
    header.append("\tM12");
    header.append("\tM13");
    header.append("\tM21");
    header.append("\tM22");
    header.append("\tM23");
    header.append("\tM31");
    header.append("\tM32");
    header.append("\tM33");

    std::string result;
    result.append("\"").append(requestId).append("\"");
    result.append("\t\"").append(hitId).append("\"");
    result.append("\t\"").append(assignmentId).append("\"");
    result.append("\t\"").append(workerId).append("\"");

    std::ostringstream sstream;

    sstream.str(""); sstream.clear();
    sstream << theta;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << sin_theta;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << dz;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << measuredHeight;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << calculatedHeight;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << calculatedRelativeDistance;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << measuredDistanceTowardsTile;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << calculatedDistanceTowardsTile;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << tileDistanceError;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << measuredDistanceTowardIssue;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << calculatedDistanceTowardsIssue;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << issueDistanceError;
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*translationVector, float, 0, 0);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*translationVector, float, 1, 0);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*translationVector, float, 2, 0);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*homography, float, 0, 0);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*homography, float, 0, 1);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*homography, float, 0, 2);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*homography, float, 1, 0);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*homography, float, 1, 1);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*homography, float, 1, 2);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*homography, float, 2, 0);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*homography, float, 2, 1);
    result.append("\t\"").append(sstream.str()).append("\"");

    sstream.str(""); sstream.clear();
    sstream << CV_MAT_ELEM(*homography, float, 2, 2);
    result.append("\t\"").append(sstream.str()).append("\"");

    std::string buffer;
    buffer = header.append("\n").append(result);
    ofstream outputFile;
    std::string tempPath = outputDirectory;
    outputFile.open(tempPath.append("/output_").append(requestId).append(".xls").c_str());
    outputFile << buffer;
    outputFile.close();
}

void executeCore() {
    CvMat *H = cvCreateMat( 3, 3, CV_32F);
    CvMat *H_invt = cvCreateMat(3,3,CV_32F);
    cvGetPerspectiveTransform(sidewalkTileObjectPoints, sidewalkTileImagePoints, H);
    cvInvert(H, H_invt);

    CvPoint2D32f centerOfBBInImage; //center point of the bounding box in the original image
    centerOfBBInImage.x = ((boundingBoxImagePoints[1].x - boundingBoxImagePoints[0].x)/2) + boundingBoxImagePoints[0].x;
    centerOfBBInImage.y = ((boundingBoxImagePoints[2].y - boundingBoxImagePoints[0].y)/2) + boundingBoxImagePoints[0].y;

    CvPoint2D32f bottomInterceptOfBBCenterInImage; // point at the bottom of the image exaclty below the center of bounding box in image
    bottomInterceptOfBBCenterInImage.x = centerOfBBInImage.x;
    bottomInterceptOfBBCenterInImage.y = originalImageMat.size().height-1;

    sidewalkTileObjectPoints[0] = tranformPoint(H_invt, sidewalkTileImagePoints[0]);
    sidewalkTileObjectPoints[1] = tranformPoint(H_invt, sidewalkTileImagePoints[1]);
    sidewalkTileObjectPoints[2] = tranformPoint(H_invt, sidewalkTileImagePoints[2]);
    sidewalkTileObjectPoints[3] = tranformPoint(H_invt, sidewalkTileImagePoints[3]);

    CvPoint2D32f centerOfSTInObject;
    centerOfSTInObject.x = ((sidewalkTileObjectPoints[1].x - sidewalkTileObjectPoints[0].x)/2) + sidewalkTileObjectPoints[0].x;
    centerOfSTInObject.y = ((sidewalkTileObjectPoints[2].y - sidewalkTileObjectPoints[0].y)/2) + sidewalkTileObjectPoints[0].y;

    CvPoint2D32f centerOfSTInImage;
    centerOfSTInImage = tranformPoint(H, centerOfSTInObject);

    CvPoint2D32f bottomInterceptOfSTCenterInImage;
    bottomInterceptOfSTCenterInImage.x =  centerOfSTInImage.x;
    bottomInterceptOfSTCenterInImage.y =  originalImageMat.size().height-1;

    /*relative distance calculation - Begin */
    ////override x co-ordinates of bounding box center and its bottom intercept in Image
    centerOfBBInImage.x = centerOfSTInImage.x;
    bottomInterceptOfBBCenterInImage.x = centerOfSTInImage.x;
    CvPoint2D32f centerofBBInObject = tranformPoint(H_invt, centerOfBBInImage);
    CvPoint2D32f bottomInterceptOfBBCenterInObject = tranformPoint(H_invt, bottomInterceptOfBBCenterInImage);

    CvPoint2D32f bottomInterceptOfSTCenterInObject = tranformPoint(H_invt, bottomInterceptOfSTCenterInImage);

    double delta_x = bottomInterceptOfBBCenterInObject.x - centerofBBInObject.x;
    double delta_Y = bottomInterceptOfBBCenterInObject.y - centerofBBInObject.y;
    double lengthBBCenterInterceptInObject = sqrt(((delta_x*delta_x)+(delta_Y*delta_Y)));

    delta_x = bottomInterceptOfSTCenterInObject.x - centerOfSTInObject.x;
    delta_Y = bottomInterceptOfSTCenterInObject.y - centerOfSTInObject.y;
    double lengthOfSTCenterInterceptInObject = sqrt(((delta_x*delta_x)+(delta_Y*delta_Y)));

    delta_x = bottomInterceptOfBBCenterInImage.x - centerOfBBInImage.x;
    delta_Y = bottomInterceptOfBBCenterInImage.y - centerOfBBInImage.y;
    double lengthOfBBCenterInterceptInImage = sqrt(((delta_x*delta_x)+(delta_Y*delta_Y)));

    delta_x = bottomInterceptOfSTCenterInImage.x - centerOfSTInImage.x;
    delta_Y = bottomInterceptOfSTCenterInImage.y - centerOfSTInImage.y;
    double lengthOfSTCenterInterceptInImage = sqrt(((delta_x*delta_x)+(delta_Y*delta_Y)));

    double relativeDistance = lengthBBCenterInterceptInObject - lengthOfSTCenterInterceptInObject;
    /* relative distace calculation - End*/

    /*Homography Decomposition - finding external camera parameters - Begin*/
    CvMat* image_points  = cvCreateMat(4,1,CV_32FC2);
    CvMat* object_points = cvCreateMat(4,1,CV_32FC3);
    for(int i=0;i<4;++i){
        CV_MAT_ELEM(*image_points,CvPoint2D32f,i,0) = sidewalkTileImagePoints[i];
        CV_MAT_ELEM(*object_points,CvPoint3D32f,i,0) = cvPoint3D32f(sidewalkTileObjectPoints[i].x, sidewalkTileObjectPoints[i].y, 0);
    }

    CvMat *intrinsic = (CvMat*)cvLoad(intrinsicParametersFile.c_str());
    CvMat *distortion = (CvMat*)cvLoad(distortionParametersFile.c_str());
    CvMat *RotRodrigues   = cvCreateMat(3,1,CV_32F);
    CvMat *Rot   = cvCreateMat(3,3,CV_32F);
    CvMat *Trans = cvCreateMat(3,1,CV_32F);
    cvFindExtrinsicCameraParams2(object_points,image_points,
            intrinsic,distortion,
            RotRodrigues,Trans);
    cvRodrigues2(RotRodrigues,Rot);

    std::string tempPath;

    tempPath = outputDirectory;
    cvSave(tempPath.append("/Rot.xml").c_str(), Rot);

    tempPath = outputDirectory;
    cvSave(tempPath.append("/Trans.xml").c_str(), Trans);

    tempPath = outputDirectory;
    cvSave(tempPath.append("/H.xml").c_str(), H);

    tempPath = outputDirectory;
    cvSave(tempPath.append("/H_invt.xml").c_str(), H_invt); //Bottom row of H invert is horizon line
    /*Homography Decomposition - finding external camera parameters - End*/

    /*Perspective Transformation - Top-Down View Generation -Begin*/
    cv::Mat trans = H;
    warpPerspective(originalImageMat, topDownViewMat, trans, originalImageMat.size() * 3, INTER_LANCZOS4 | WARP_INVERSE_MAP);
    IplImage* topDownView = new IplImage(topDownViewMat);
    tempPath = outputDirectory;
    cvSaveImage(tempPath.append("/").append(outputImageName).append("_tdv.jpg").c_str(), topDownView);
    /*Perspective Transformation - Top-Down View Generation -End*/

    /*Save origianl image and TDV with layers - Begin*/
    IplImage* originalImageWithLayers = new IplImage(originalImageMat);
    cvDrawRect(originalImageWithLayers, cvPoint(boundingBoxImagePoints[0].x, boundingBoxImagePoints[0].y), cvPoint(boundingBoxImagePoints[2].x, boundingBoxImagePoints[2].y), cvScalar(255, 0, 0), 3, 8, 0);

    cvDrawLine(originalImageWithLayers, cvPoint(sidewalkTileImagePoints[0].x, sidewalkTileImagePoints[0].y), cvPoint(sidewalkTileImagePoints[1].x, sidewalkTileImagePoints[1].y), cvScalar(255, 0, 0), 3, 8, 0);
    cvDrawLine(originalImageWithLayers, cvPoint(sidewalkTileImagePoints[1].x, sidewalkTileImagePoints[1].y), cvPoint(sidewalkTileImagePoints[2].x, sidewalkTileImagePoints[2].y), cvScalar(255, 0, 0), 3, 8, 0);
    cvDrawLine(originalImageWithLayers, cvPoint(sidewalkTileImagePoints[2].x, sidewalkTileImagePoints[2].y), cvPoint(sidewalkTileImagePoints[3].x, sidewalkTileImagePoints[3].y), cvScalar(255, 0, 0), 3, 8, 0);
    cvDrawLine(originalImageWithLayers, cvPoint(sidewalkTileImagePoints[3].x, sidewalkTileImagePoints[3].y), cvPoint(sidewalkTileImagePoints[0].x, sidewalkTileImagePoints[0].y), cvScalar(255, 0, 0), 3, 8, 0);

    cvDrawLine(originalImageWithLayers, cvPoint(centerOfBBInImage.x, centerOfBBInImage.y), cvPoint(bottomInterceptOfBBCenterInImage.x, bottomInterceptOfBBCenterInImage.y), cvScalar(255, 255, 255), 3, 8, 0);
    cvDrawLine(originalImageWithLayers, cvPoint(centerOfSTInImage.x, centerOfSTInImage.y), cvPoint(bottomInterceptOfSTCenterInImage.x, bottomInterceptOfSTCenterInImage.y), cvScalar(255, 0, 0), 3, 8, 0);
    tempPath = outputDirectory;
    cvSaveImage(tempPath.append("/").append(outputImageName).append("_layers.jpg").c_str(), originalImageWithLayers);

    cv::Mat originalImageWithLayersMat = originalImageWithLayers;
    cv::Mat topDownViewWithLayersMat;
    trans = H;
    warpPerspective(originalImageWithLayersMat, topDownViewWithLayersMat, trans, originalImageWithLayersMat.size() * 3, INTER_LANCZOS4 | WARP_INVERSE_MAP);
    IplImage* topDownViewWithLayers = new IplImage(topDownViewWithLayersMat);
    tempPath = outputDirectory;
    cvSaveImage(tempPath.append("/").append(outputImageName).append("_tdv_layers.jpg").c_str(), topDownViewWithLayers);
    /*Save origianl image and TDV with layers - End*/

    /*Assign Output Parameter Values* - Begin*/
    translationVector = Trans;
    CvMat tempMat = trans;
    homography = &tempMat;

    dz = cvGetReal2D(translationVector, 2, 0);
    sin_theta = sin (theta*PI/180);
    calculatedHeight = dz * sin_theta;
    calculatedRelativeDistance = relativeDistance;
    calculatedDistanceTowardsTile = sqrt(((dz*dz)-(calculatedHeight*calculatedHeight)));
    calculatedDistanceTowardsIssue = calculatedDistanceTowardsTile + relativeDistance;
    tileDistanceError = measuredDistanceTowardsTile - calculatedDistanceTowardsTile;
    issueDistanceError = measuredDistanceTowardIssue - calculatedDistanceTowardsIssue;
    /*Assign Output Parameter Values* - End*/

    /*Print to Output File*/
    writeOutput();
}

void assignInputValues(int argc, char* argv[]) {
    requestId = argv[1];

    hitId = argv[2];

    assignmentId = argv[3];

    workerId = argv[4];

    inputImageFile = argv[5];

    outputDirectory = argv[6];

    outputImageName = argv[7];

    intrinsicParametersFile = argv[8];

    distortionParametersFile = argv[9];

    std::string temp;

    temp = argv[10];
    theta = atof(temp.c_str());

    temp = argv[11];
    measuredHeight = atof(temp.c_str());

    temp = argv[12];
    measuredSidewalkTileLength = atof(temp.c_str());

    temp = argv[13];
    measuredSidewalkTileWidth = atof(temp.c_str());

    temp = argv[14];
    measuredDistanceTowardsTile = atof(temp.c_str());

    temp = argv[15];
    measuredDistanceTowardIssue = atof(temp.c_str());

    temp = argv[16];
    sidewalkTileLength = atof(temp.c_str());

    temp = argv[17];
    sidewalkTileWidth = atof(temp.c_str());

    temp = argv[18];
    bBoxPoint1_x = atof(temp.c_str());

    temp = argv[19];
    bBoxPoint1_y = atof(temp.c_str());

    temp = argv[20];
    bBoxPoint2_x = atof(temp.c_str());

    temp = argv[21];
    bBoxPoint2_y = atof(temp.c_str());

    temp = argv[22];
    bBoxPoint3_x = atof(temp.c_str());

    temp = argv[23];
    bBoxPoint3_y = atof(temp.c_str());

    temp = argv[24];
    bBoxPoint4_x = atof(temp.c_str());

    temp = argv[25];
    bBoxPoint4_y = atof(temp.c_str());

    temp = argv[26];
    tilePoint1_x = atof(temp.c_str());

    temp = argv[27];
    tilePoint1_y = atof(temp.c_str());

    temp = argv[28];
    tilePoint2_x = atof(temp.c_str());

    temp = argv[29];
    tilePoint2_y = atof(temp.c_str());

    temp = argv[30];
    tilePoint3_x = atof(temp.c_str());

    temp = argv[31];
    tilePoint3_y = atof(temp.c_str());

    temp = argv[32];
    tilePoint4_x = atof(temp.c_str());

    temp = argv[33];
    tilePoint4_y = atof(temp.c_str());
}

void printUsage() {
    std::string usage;
    usage.append("\n\nWheelNavCore-Test requires 33 parameters.");
    usage.append("\n\n\tUsage:");
    usage.append("\n\t1 : Request ID");
    usage.append("\n\t2 : HIT ID");
    usage.append("\n\t3 : Assignment ID");
    usage.append("\n\t4 : Worker ID");
    usage.append("\n\t5 : Input Image File");
    usage.append("\n\t6 : Output Directory Path");
    usage.append("\n\t7 : Output Image Name");
    usage.append("\n\t8 : Camera Intrinsic Parameters XML");
    usage.append("\n\t9 : Camera Distortion Parameters XML");
    usage.append("\n\t10 : theta (90-elevation angle of mobile device)");
    usage.append("\n\t11 : Measured height of mobile device");
    usage.append("\n\t12 : Sidewalk tile's measured length");
    usage.append("\n\t13 : Sidewalk tile's Measured width");
    usage.append("\n\t14 : Distance measured towards tile");
    usage.append("\n\t15 : Distance measured towards accessibility issue");
    usage.append("\n\t16 : Length of sidewalk tile (experimental)");
    usage.append("\n\t17 : Width of sidewalk tile (experimental)");
    usage.append("\n\t18 : Bounding Box Point 1 X Co-ordinate");
    usage.append("\n\t19 : Bounding Box Point 1 Y Co-ordinate");
    usage.append("\n\t20 : Bounding Box Point 2 X Co-ordinate");
    usage.append("\n\t21 : Bounding Box Point 2 Y Co-ordinate");
    usage.append("\n\t22 : Bounding Box Point 3 X Co-ordinate");
    usage.append("\n\t23 : Bounding Box Point 3 Y Co-ordinate");
    usage.append("\n\t24 : Bounding Box Point 4 X Co-ordinate");
    usage.append("\n\t25 : Bounding Box Point 4 Y Co-ordinate");

    usage.append("\n\t26 : Sidewalk Tile Point 1 X Co-ordinate");
    usage.append("\n\t27 : Sidewalk Tile Point 1 Y Co-ordinate");
    usage.append("\n\t28 : Sidewalk Tile Point 2 X Co-ordinate");
    usage.append("\n\t29 : Sidewalk Tile Point 2 Y Co-ordinate");
    usage.append("\n\t30 : Sidewalk Tile Point 3 X Co-ordinate");
    usage.append("\n\t31 : Sidewalk Tile Point 3 Y Co-ordinate");
    usage.append("\n\t32 : Sidewalk Tile Point 4 X Co-ordinate");
    usage.append("\n\t33 : Sidewalk Tile Point 4 Y Co-ordinate");

    cout << usage;
}

void printInputs() {
    std::string inputs;
    inputs.append("\n\tRequest Id : ").append(requestId);
    inputs.append("\n\tHIT Id : ").append(hitId);
    inputs.append("\n\tAssignment Id : ").append(assignmentId);
    inputs.append("\n\tWorker Id : ").append(workerId);
    inputs.append("\n\tInput Image File :").append(inputImageFile);
    inputs.append("\n\tOutput Directory Path :").append(outputDirectory);
    inputs.append("\n\tOutput Image Name : ").append(outputImageName);
    inputs.append("\n\tCamera Intrinsic Parameters XML : ").append(intrinsicParametersFile);
    inputs.append("\n\tCamera Distortion Parameters XML : ").append(distortionParametersFile);

    std::ostringstream sstream;

    sstream.str(""); sstream.clear();
    sstream << theta;
    inputs.append("\n\ttheta (90-elevation angle of mobile device) : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << measuredHeight;
    inputs.append("\n\tMeasured height of mobile device : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << measuredSidewalkTileLength;
    inputs.append("\n\tSidewalk tile's measured length : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << measuredSidewalkTileWidth;
    inputs.append("\n\tSidewalk tile's Measured width : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << measuredDistanceTowardsTile;
    inputs.append("\n\tDistance measured towards tile : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << measuredDistanceTowardIssue;
    inputs.append("\n\tDistance measured towards accessibility issue : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << sidewalkTileLength;
    inputs.append("\n\tLength of sidewalk tile (experimental) : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << sidewalkTileWidth;
    inputs.append("\n\tWidth of sidewalk tile (experimental) : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << bBoxPoint1_x;
    inputs.append("\n\tBounding Box Point 1 X Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << bBoxPoint1_y;
    inputs.append("\n\tBounding Box Point 1 Y Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << bBoxPoint2_x;
    inputs.append("\n\tBounding Box Point 2 X Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << bBoxPoint2_y;
    inputs.append("\n\tBounding Box Point 2 Y Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << bBoxPoint3_x;
    inputs.append("\n\tBounding Box Point 3 X Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << bBoxPoint3_y;
    inputs.append("\n\tBounding Box Point 3 Y Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << bBoxPoint4_x;
    inputs.append("\n\tBounding Box Point 4 X Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << bBoxPoint4_y;
    inputs.append("\n\tBounding Box Point 4 Y Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << tilePoint1_x;
    inputs.append("\n\tSidewalk Tile Point 1 X Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << tilePoint1_y;
    inputs.append("\n\tSidewalk Tile Point 1 Y Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << tilePoint2_x;
    inputs.append("\n\tSidewalk Tile Point 2 X Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << tilePoint2_y;
    inputs.append("\n\tSidewalk Tile Point 2 Y Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << tilePoint3_x;
    inputs.append("\n\tSidewalk Tile Point 3 X Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << tilePoint3_y;
    inputs.append("\n\tSidewalk Tile Point 3 Y Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << tilePoint4_x;
    inputs.append("\n\tSidewalk Tile Point 4 X Co-ordinate : ").append(sstream.str());

    sstream.str(""); sstream.clear();
    sstream << tilePoint4_y;
    inputs.append("\n\tSidewalk Tile Point 4 Y Co-ordinate : ").append(sstream.str());

    cout << inputs;
}

void assignOperationParameterValues() {

    boundingBoxImagePoints[0] = cvPoint2D32f(bBoxPoint1_x, bBoxPoint1_y);
    boundingBoxImagePoints[1] = cvPoint2D32f(bBoxPoint2_x, bBoxPoint2_y);
    boundingBoxImagePoints[2] = cvPoint2D32f(bBoxPoint3_x, bBoxPoint3_y);
    boundingBoxImagePoints[3] = cvPoint2D32f(bBoxPoint4_x, bBoxPoint4_y);

    sidewalkTileImagePoints[0] = cvPoint2D32f(tilePoint1_x, tilePoint1_y);
    sidewalkTileImagePoints[1] = cvPoint2D32f(tilePoint2_x, tilePoint2_y);
    sidewalkTileImagePoints[2] = cvPoint2D32f(tilePoint3_x, tilePoint3_y);
    sidewalkTileImagePoints[3] = cvPoint2D32f(tilePoint4_x, tilePoint4_y);

    sidewalkTileObjectPoints[0] = cvPoint2D32f(0, 0);
    sidewalkTileObjectPoints[1] = cvPoint2D32f(sidewalkTileWidth, 0);
    sidewalkTileObjectPoints[2] = cvPoint2D32f(sidewalkTileWidth, sidewalkTileLength);
    sidewalkTileObjectPoints[3] = cvPoint2D32f(0, sidewalkTileLength);

    originalImageMat = originalImage;
}

int main(int argc, char* argv[]) {

    //WheelNavCore : total 33 input parameters
    if(argc!=34) {
        printUsage();
        return -1;
    }
    assignInputValues(argc, argv);
    printInputs();
    if((originalImage = cvLoadImage(inputImageFile.c_str()))== 0) {
        cout <<"Error: Couldn't load - ";
        cout << inputImageFile;getchar();
        return -1;
    }
    assignOperationParameterValues();
    cout << "\n\nExecuting WheelNav-Core now.....";
    executeCore();
    cout << "\n\nWheelNav-Core execution completed...\n\n";
    return 0;
}
