package edu.bogazici.cem.turkish_englishdictionary;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


/*
 * excel library'si kullanıldı
 * GUI can be used when offline
 * 61,197 İngilizce kelime var
 * 
 * result text box'u çok büyümesin
 */
public class Dictionary {

	
	public Dictionary(Context context) {
		eng2turk = new HashMap<String, List<String>>();
		turk2eng = new HashMap<String, List<String>>();
		setInputFile("lugat97.xls");
		createDict(context);
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

	}

	public List<String> turk2eng(String w) {
		return this.turk2eng.get(w);
	}

	public List<String> eng2turk(String w) {
		return this.eng2turk.get(w);
	}

	private String inputFile;
	public static Map<String, List<String>> eng2turk;
	public static Map<String, List<String>> turk2eng;

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

    public static boolean RIGHT_TO_LEFT = false;

	private File getFileFromRawResource(Uri rUri, Context context) {
		String uri = rUri.toString();
		String fn;
		// I've only tested this with raw resources
		if (uri.contains("/raw/")) {
			// Try to get the resource name
			String[] parts = uri.split("/");
			fn = parts[parts.length - 1];
		} else {
			return null;
		}
		// Notice that I've hard-coded the file extension to .jpg
		// I was working with getting a File object of a JPEG image from my raw resources
//		String dest = Environment.getExternalStorageDirectory() + "/lugat97Out.xls";
		String dest = context.getFilesDir().getAbsoluteFile() + "/lugat97Out.xls";//MainActivity.this.getFilesDir().getAbsolutePath()+"/text.txt"
//		String dest = "/tmp/lugat97Out.xls";
		try {
			// Use reflection to get resource ID of the raw resource
			// as we need to get an InputStream to it
			// getResources(),openRawResource() takes only a resource ID
			R.raw r = new R.raw();
			Field frame = R.raw.class.getDeclaredField(fn);
			frame.setAccessible(true);
			int id = (Integer) frame.get(r);
			// Get the InputStream
			InputStream inputStream = context.getResources().openRawResource(id);
			FileOutputStream fileOutputStream = new FileOutputStream(dest);
			// IOUtils is a class from Apache Commons IO
			// It writes an InputStream to an OutputStream
			IOUtils.copy(inputStream, fileOutputStream);
			fileOutputStream.close();
			return new File(dest);
		} catch (NoSuchFieldException e) {
			Log.e("MyApp", "NoSuchFieldException in getFileFromRawResource()");
		} catch (IllegalAccessException e) {
			Log.e("MyApp", "IllegalAccessException in getFileFromRawResource()");
		} catch (FileNotFoundException e) {
			Log.e("MyApp", "FileNotFoundException in getFileFromRawResource()");
		} catch (IOException e) {
			Log.e("MyApp", "IOException in getFileFromRawResource()");
		}
		return null;
	}

	public void createDict(Context c)  {
		File inputWorkbook = getFileFromRawResource(Uri.parse("/raw/lugat97"), c);
		Workbook w;

		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet(0);
			// Loop over first 10 column and lines

			for (int j = 1; j < sheet.getRows(); j++) {
				String engWord = sheet.getCell(1, j).getContents();

				for (int i = 2; i < sheet.getColumns(); i++) {
					//					CellType type = cell.getType();

					String turkMean = sheet.getCell(i, j).getContents().trim();
					if (turkMean.equals("")) 
						break;
					List<String> equiv = eng2turk.containsKey(engWord) ? eng2turk.get(engWord) :  new ArrayList<String>();
					equiv.add(turkMean);
					eng2turk.put(engWord, equiv);

				}
			}
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Map.Entry<String, List<String>> me: eng2turk.entrySet()) {
			String eng = me.getKey();
			for (String s: me.getValue()) {
				List<String> l = turk2eng.containsKey(s) ? turk2eng.get(s) : new ArrayList<String>();
				l.add(eng);
				turk2eng.put(s, l);
			}
		}
	}
}
