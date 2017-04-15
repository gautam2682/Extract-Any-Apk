package com.example.gautam.allinonelauncher;

import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Environment;

import java.io.*;
import java.nio.channels.FileChannel;

public class Extractor {
	public String extractWithoutRoot(ApplicationInfo info) throws Exception {
		File src = new File(info.sourceDir);
		File dst;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			dst = new File(Environment.getExternalStorageDirectory(), "Gautam/apk/" + info.packageName + ".apk");
		} else {
			dst = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Gautam your apks/" + info.packageName + ".apk");
		}
		dst = buildDstPath(dst);
		try {
			copy(src, dst);
		} catch (IOException ex) {
			throw new Exception(ex.getMessage());
		}
		if (!dst.exists()) {
			throw new Exception("cannot extract file [no root]");
		}
		return dst.toString();
	}



	private void copy(File src, File dst) throws IOException {
		FileInputStream inStream = new FileInputStream(src);
		FileOutputStream outStream = new FileOutputStream(dst);
		FileChannel inChannel = inStream.getChannel();
		FileChannel outChannel = outStream.getChannel();
		inChannel.transferTo(0, inChannel.size(), outChannel);
		inStream.close();
		outStream.close();
	}

	private File buildDstPath(File path) throws IOException {
		if ((!path.getParentFile().exists() && !path.getParentFile().mkdirs()) || !path.getParentFile().isDirectory()) {
			throw new IOException("Cannot create directory: " + path.getParentFile().getAbsolutePath());
		}
		if (!path.exists()) return path;

		File dst = path;
		String fname = path.getName();
		int index = fname.lastIndexOf(".");
		String ext = fname.substring(index);
		String name = fname.substring(0, index);

		for (int i = 0; dst.exists(); i++) {
			dst = new File(path.getParentFile(), name + "-" + String.valueOf(i) + ext);
		}

		return dst;
	}
}
