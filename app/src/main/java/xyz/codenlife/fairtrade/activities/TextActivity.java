package xyz.codenlife.fairtrade.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import xyz.codenlife.fairtrade.R;
import xyz.codenlife.fairtrade.tools.Barcode;


public class TextActivity extends AppCompatActivity {

	private Button button_scan;
	private EditText text_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);

		button_scan = (Button)findViewById(R.id.button_scan_code);
		text_code = (EditText)findViewById(R.id.textbox_code);

		text_code.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()==13){
					button_scan.setEnabled(true);
				}
				else{
					button_scan.setEnabled(false);
				}
			}
		});

		button_scan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!Barcode.checkBarCode(text_code.getText().toString())){
					new AlertDialog.Builder(TextActivity.this)
							.setMessage("Bad Barcode")
							.setNeutralButton("OK", null)
							.show();
					return;
				}
				startActivity(new Intent(TextActivity.this, ResultActivity.class)
								.putExtra("SCAN", false)
								.putExtra("CODE", text_code.getText().toString()));
			}
		});
	}
}
