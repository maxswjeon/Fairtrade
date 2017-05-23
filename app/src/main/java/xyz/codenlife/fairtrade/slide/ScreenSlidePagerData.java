package xyz.codenlife.fairtrade.slide;

import xyz.codenlife.fairtrade.R;

public enum ScreenSlidePagerData {

	START("What is FairTrade?", ""),
	AD("Advertisement?", "All profit from the advertisement is donated to fairtrade product producers through KFTA, Korea Fair Trade Association.", R.drawable.kfta),
	SCAN("Scan Product", "We can scan products by scanning the Barcode. Rotate the phone to scan barcode.", R.drawable.scan, -90),
	IMAGE("Scan from Image", "We can scan products by loading images. Select Image to scan."),
	CODE("Scan from Code", "We can scan products by typing code. Type Code to scan.", R.drawable.code),
	HISTORY("History", "You can see history that you searched.", R.drawable.history),
	RESULT("Result", "We show a  fair trade certificate if it tis fair traded.", R.drawable.result);

	ScreenSlidePagerData(String title, String content){
		Title = title;
		Content = content;
		Image = 0;
		Rotation = 0;
	}

	ScreenSlidePagerData(String title, String content, int image){
		Title = title;
		Content = content;
		Image = image;
		Rotation = 0;
	}

	ScreenSlidePagerData(String title, String content, int image, float rotation){
		Title = title;
		Content = content;
		Image = image;
		Rotation = rotation;
	}

	public String Title;
	public String Content;
	public int Image;
	public float Rotation;

}
