package xyz.codenlife.fairtrade.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.MultiFormatOneDReader;

/**
 * Created by maxsw on 2017-04-25.
 */

public final class Barcode {

	public static class EANFormatException extends Exception{

	}

	public static boolean checkBarCode(String code) {

		try {
			int totalsum = 0;
			int idx = 0;

			for (idx = 0; idx < 12; idx++) {
				if (idx % 2 == 1) {
					totalsum += Integer.parseInt(String.valueOf(code.charAt(idx))) * 3;
				} else {
					totalsum += Integer.parseInt(String.valueOf(code.charAt(idx)));
				}
			}

			// 마지막 체크섬을 더한다.
			totalsum += Integer.parseInt(String.valueOf(code.charAt(idx)));

			// 최종값이 10으로 나누어 떨어지면 정상
			if (totalsum % 10 == 0) {
				return true;
			} else {
				return false;
			}
		}
		catch (Exception e){
			return false;
		}
	}

	public static String ScanFromFile(Bitmap bm) throws NotFoundException, FormatException, EANFormatException{
		int[] pixels = new int[bm.getWidth()*bm.getHeight()];
		bm.getPixels(pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());

		RGBLuminanceSource source = new RGBLuminanceSource(bm.getWidth(), bm.getHeight(), pixels);

		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

		Result result = null;

		result = new MultiFormatOneDReader(null).decode(binaryBitmap);

		//Korea Uses EAN-13 Barcode. If the text length isn't 13, we return it.
		if(result.getText().length() != 13){
			throw new EANFormatException();
		}

		//Validate Barcode.
		if(!result.getText().matches("[\\d]*")){
			throw new EANFormatException();
		}

		if(!Barcode.checkBarCode(result.getText())){
			throw new EANFormatException();
		}

		return result.getText();
	}
}


