package com.ftpupload.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class FtpUploadAndDownloadActivity extends Activity {

	private FTPClient ftp;
	private String host = "ws.ikrave.net";
	private String path, path1, path2, new_dir, localfile;
	private final static String TAG = FtpUploadAndDownloadActivity.class
			.getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		path1 = File.separator + "public" + File.separator + "uploads"
				+ File.separator + "user" + File.separator + "96188"
				+ File.separator + "Test1" + ".jpg";
		path2 = File.separator + "public" + File.separator + "uploads"
				+ File.separator + "user" + File.separator + "96188"
				+ File.separator + "Test2" + ".jpg";
		new_dir = File.separator + "public" + File.separator + "uploads"
				+ File.separator + "user" + File.separator + "kets";

		ftpUpload();
		// ftpDownload();

		// String imgURL =
		// "http://4.bp.blogspot.com/-GDG0jraKnFI/TzZHQ5At0zI/AAAAAAAAAEY/TPl4U6vm5lw/s1600/android-example-code-demo-program.jpg";
		//
		// ImageView ivCurrent;
		// ivCurrent = (ImageView) findViewById(R.id.imageView1);
		//
		// DownloadAndReadImage dImage = new DownloadAndReadImage(imgURL,
		// 1,this);
		//
		// ivCurrent.setImageBitmap(dImage.getBitmapImage());

	}

	private void ftpDownload() {

		final String path = Environment.getExternalStorageDirectory()
				+ File.separator + "Krave" + File.separator + "Images"
				+ File.separator + "Bajaj" + ".jpg";

		try {
			ftp = new FTPClient();
			ftp.connect(host, 21);

			ftp.login("ikrave.net", "ikrave.net");
			Log.e("connected----------:", "" + ftp.isConnected());

			ftp.enterLocalActiveMode();
			ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);

			File f = new File(path);

			Log.e("File", "File Path == " + f);

			FileOutputStream fos = new FileOutputStream(f);

			boolean result = ftp.retrieveFile(
					"/public/uploads/user/96187/1340254095225.jpg", fos);
			Log.e("Result", "Result == " + result);

		} catch (IOException e) {
			Log.e("FTP IO", "Error Getting File");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("FTP Exc", "Error Getting File");
		}
	}

	private void ftpUpload() {
		path = Environment.getExternalStorageDirectory() + File.separator
				+ "Krave" + File.separator + "Images" + File.separator
				+ "test.jpg";
		localfile = path;
		try {
			ftp = new FTPClient();
			ftp.connect(host);
			ftp.enterLocalActiveMode();
			ftp.login("ikrave.net", "ikrave.net");

			Log.e("connected----------:", "" + ftp.isConnected());
			File fDir = new File("public" + File.separator + "uploads"
					+ File.separator + "user" + File.separator + "96188");
			Log.i("Path dir :", "path dir :" + fDir.getPath());
			boolean chngDir = ftp.changeWorkingDirectory(fDir.getPath());
			if (!chngDir) {
				ftp.makeDirectory(fDir.getPath());
				ftp.changeWorkingDirectory(fDir.getPath());

			}

			File f = new File(localfile);
			InputStream is = new FileInputStream(f);
			boolean rsult = ftp.storeFile(f.getName().toString(), is);

			Log.v("Result", "" + rsult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ftpGetCurrentWorkingDirectory();
		// ftpMakeDirectory(new_dir);
		// ftpRemoveDirectory(new_dir);
		// ftpRenameFile(path1, path2);
		// ftpRemoveFile(path2);

	}

	// Method to get current working directory:

	public String ftpGetCurrentWorkingDirectory() {
		try {
			String workingDir = ftp.printWorkingDirectory();
			Log.v("Working Directory", "" + workingDir);
			return workingDir;
		} catch (Exception e) {
			Log.d(TAG, "Error: could not get current working directory.");
		}

		return null;
	}

	// Method to change working directory:

	public boolean ftpChangeDirectory(String directory_path) {
		try {
			ftp.changeWorkingDirectory(directory_path);
		} catch (Exception e) {
			Log.d(TAG, "Error: could not change directory to " + directory_path);
		}

		return false;
	}

	// Method to list all files in a directory:

	public void ftpPrintFilesList(String dir_path) {
		try {
			FTPFile[] ftpFiles = ftp.listFiles(dir_path);
			int length = ftpFiles.length;

			for (int i = 0; i < length; i++) {
				String name = ftpFiles[i].getName();
				boolean isFile = ftpFiles[i].isFile();

				if (isFile) {
					Log.i(TAG, "File : " + name);
				} else {
					Log.i(TAG, "Directory : " + name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method to create new directory:
	public boolean ftpMakeDirectory(String new_dir_path) {
		try {
			boolean status = ftp.makeDirectory(new_dir_path);
			Log.i(TAG, "New Dir Status : " + status);
			return status;
		} catch (Exception e) {
			Log.d(TAG, "Error: could not create new directory named "
					+ new_dir_path);
		}

		return false;
	}

	// Method to delete/remove a directory:
	public boolean ftpRemoveDirectory(String dir_path) {
		try {
			boolean status = ftp.removeDirectory(dir_path);
			Log.i(TAG, "Remove Dir Status : " + status);
			return status;
		} catch (Exception e) {
			Log.d(TAG, "Error: could not remove directory named " + dir_path);
		}

		return false;
	}

	// Method to delete a file:
	public boolean ftpRemoveFile(String filePath) {
		try {
			boolean status = ftp.deleteFile(filePath);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// Method to rename a file:
	public boolean ftpRenameFile(String from, String to) {
		try {
			boolean status = ftp.rename(from, to);
			Log.i(TAG, "Rename Status : " + status);
			return status;
		} catch (Exception e) {
			Log.d(TAG, "Could not rename file: " + from + " to: " + to);
		}

		return false;
	}
}