package xyz.codenlife.fairtrade.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by maxsw on 2017-04-23.
 */

public class BarcodeInfo extends Thread {

	public interface OnBarcodeInfoUpdatedListener{
		void onUpdated(Boolean succeeded);
	}

	private String _code;
	private String _name;
	private OnBarcodeInfoUpdatedListener _listener;

	public BarcodeInfo(String code) {
		super();
		_code = code;
	}

	@Override
	public void run() {
		try {
			Document document = Jsoup.connect("http://www.koreannet.or.kr/home/hpisSrchGtin.gs1?gtin=" + _code).get();
			if (document.getElementsByClass("productDetailView").isEmpty() || !document.getElementsByClass("noresult").isEmpty()) {
				_listener.onUpdated(false);
			} else {
				_name = document.getElementsByClass("productTit").first().html().trim().substring(13).replace("&nbsp;","").trim();
				_listener.onUpdated(true);
			}
		}
		catch (IOException e){

		}
	}

	public String getCode() {
		return _code;
	}

	public String getProductName() {
		return _name;
	}

	public void setCode(String code) {
		_code = code;
	}

	public void setOnBarcodeInfoUpdatedListener(OnBarcodeInfoUpdatedListener listener) {
		_listener = listener;
	}
}
