package com.mpss.wheelnav.versionupdate;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionCheck {

        /**
         *this is the link for public version or copy information file, 
         *it could be server url or dropbox or googledocs
         */
        public static final String INFO_FILE = "http://mpss.csce.uark.edu/wheelnav/apps/updateinfo.txt";

        /**
         * The version code set in AndroidManifest.xml version
     * Installed application. Is the numerical value used for Android
     * Differentiate versions..
         */
        private int currentVersionCode;
        /**
         *The version name set in AndroidManifest.xml version
     * Installed. Is the text string that is used to identify the version
     * For the user.
         */
        private String currentVersionName;

        /**
         * The version code set in AndroidManifest.xml Last
     * Available version of the application..
         */
        private int latestVersionCode;
        /**
         * The version name set in the AndroidManifest.xml of the last
     * Version.
         */
        private String latestVersionName;

        /**
         *  Direct download link of the latest version.
         */
        private String downloadURL;

        /**
         * Method to initialize the object. You must call ahead to any
     * Other, and in a separate thread (or AsyncTask) to not block the interface
     * Because it makes use of the Internet.
     *
     * @ Param context
     * The context of the application, to obtain information
     * The current version.
         */
        public void getData(Context context) {
                try{
                        // Datos locales
                        PackageInfo pckginfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        currentVersionCode = pckginfo.versionCode;
                        currentVersionName = pckginfo.versionName;

                        // Datos remotos
                        String data = downloadHttp(new URL(INFO_FILE));
                        JSONObject json = new JSONObject(data);
                        latestVersionCode = json.getInt("versionCode");
                        latestVersionName = json.getString("versionName");
                        downloadURL = json.getString("downloadURL");
                        Log.d ( "AutoUpdate" , "Data obtained successfully" );
                } catch (JSONException e) {
                    Log.e ( "AutoUpdate" , "There was an error with JSON" , e);
                } catch (NameNotFoundException e) {
                    Log.e ( "AutoUpdate" , "There was an error with Packete: S" , e);
                } catch (IOException e) {
                    Log.e ( "AutoUpdate" , "There was an error downloading" , e);
                }
        }

        /**
         * Método para comparar la versión actual con la última .
         * 
         * @return true si hay una versión más nueva disponible que la actual.
         */
        public boolean isNewVersionAvailable() {
                return getLatestVersionCode() > getCurrentVersionCode();
        }

        /**
         * Devuelve el código de versión actual.
         * 
         * @return
         */
        public int getCurrentVersionCode() {
                return currentVersionCode;
        }

        /**
         * Devuelve el nombre de versión actual.
         * 
         * @return
         */
        public String getCurrentVersionName() {
                return currentVersionName;
        }

        /**
         * Devuelve el código de la última versión disponible.
         * 
         * @return
         */
        public int getLatestVersionCode() {
                return latestVersionCode;
        }

        /**
         * Devuelve el nombre de la última versión disponible.
         * 
         * @return
         */
        public String getLatestVersionName() {
                return latestVersionName;
        }

        /**
         * Devuelve el enlace de descarga de la última versión disponible
         * 
         * @return
         */
        public String getDownloadURL() {
                return downloadURL;
        }

        /**
         * Método auxiliar usado por getData() para leer el archivo de información.
         * Encargado de conectarse a la red, descargar el archivo y convertirlo a
         * String.
         * 
         * @param url
         *            La URL del archivo que se quiere descargar.
         * @return Cadena de texto con el contenido del archivo
         * @throws IOException
         *             Si hay algún problema en la conexión
         */
        private static String downloadHttp(URL url) throws IOException {
                HttpURLConnection c = (HttpURLConnection)url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(15 * 1000);
                c.setUseCaches(false);
                c.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null){
                        stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();
        }
}