package com.example.chu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {


		static WebView webview;
 
	

	   private static boolean copyAssetFolder(AssetManager assetManager,
	            String fromAssetPath, String toPath) {
	        try {
	            String[] files = assetManager.list(fromAssetPath);
	            new File(toPath).mkdirs();
	            boolean res = true;
	            for (String file : files)
	                if (file.contains("."))
	                    res &= copyAsset(assetManager, 
	                            fromAssetPath + "/" + file,
	                            toPath + "/" + file);
	                else 
	                    res &= copyAssetFolder(assetManager, 
	                            fromAssetPath + "/" + file,
	                            toPath + "/" + file);
	            return res;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    private static boolean copyAsset(AssetManager assetManager,
	            String fromAssetPath, String toPath) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(fromAssetPath);
	          new File(toPath).createNewFile();
	          out = new FileOutputStream(toPath);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	          return true;
	        } catch(Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    private static void copyFile(InputStream in, OutputStream out) throws IOException {
	        byte[] buffer = new byte[1024];
	        int read;
	        while((read = in.read(buffer)) != -1){
	          out.write(buffer, 0, read);
	        }
	    }
	    
	@Override    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 String toPath = this.getFilesDir().toString();  // Your application path
		//AssetManager assetmanager= this.getAssets();
		copyAssetFolder(this.getAssets(),"test",toPath);
		webview = (WebView) findViewById (R.id.webView1);
		webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
      
		webview.setWebViewClient(new WebViewClient() {
		        @Override
		        public boolean shouldOverrideUrlLoading(WebView view, String url) {
		            view.loadUrl(url);
		            return false;
		        }
		    });

		webview.loadUrl("file://"+toPath+"/test.htm");
        

		}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	

}
}
