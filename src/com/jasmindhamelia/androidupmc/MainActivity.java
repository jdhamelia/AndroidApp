package com.jasmindhamelia.androidupmc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;


public class MainActivity extends Activity implements OnClickListener {
	
	public final static String SUCCESS_MESSAGE = "com.jasmindhamelia.androidupmc.SUCESS_MESSAGE";
	
	//below variables will be used to store values from View
	TextView answer1;
	TextView answer2;
	TextView answer3;
	TextView answer4;
	Button submit;
	Button reset;
	Button OkBtn;
    double longitude;
    double latitude;
    boolean finalLongitude=true;
    boolean finalLatitude=true;
    
    
	private boolean isNetworkAvailable() {
		
	    ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private boolean isGPSLocationkAvailable() {
	   
		
		 // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to 
        // go to the settings
       LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
	    return isGPSEnabled && isNetworkEnabled;
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
               }
           });
        
        alertDialog.show();
	}
	

	//Below method is the first method which gets called when we run android application
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleNotification(getNotification());

        if(!isNetworkAvailable() && !isGPSLocationkAvailable()){
        	        	
        	 showAlertDialog(MainActivity.this, "No GPS location service & Internet service available",
                     "GPS location service and internet service are not available on your devices, " +
                     "Please enbale GPS location and Internet services and try again !", false);

		}else if(!isNetworkAvailable()){
			
			  showAlertDialog(MainActivity.this, "No Internet Connection!",
	                    "You don't have internet connection. Please turn on the internet connection and try again.", false);
			
			
		}else if(!isGPSLocationkAvailable()){
			
			 showAlertDialog(MainActivity.this, "No GPS Service available",
	                    "You don't have GPS location service available. Please turn on the GPS service and try again.", false);
		}else{
	
			
			 LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			
			    //code to register GPS location tracker
			    LocationListener mlocListener = new LocationListener() {
					
					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
						// TODO Auto-generated method stub
						
						//this method is not required
					}
					
					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
						
						//this method is not required
					}
					
					@Override
					public void onProviderDisabled(String provider) {
						
						//this method is not required
					}
					
					@Override
					public void onLocationChanged(Location location) {
						
						//below code will set location of device
						
						location.getLatitude();
						
						location.getLongitude();
						
						longitude = location.getLongitude();
						
						latitude = location.getLatitude();
							
					}
				};			    
				
				//below code will fetch new location co-ordinates after 35 seconds or moved beyond 10 meter radius
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 10, mlocListener);

		        //reset text of all the text box
		        reset = (Button) findViewById(R.id.button2);
		        reset.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						
						answer1=(TextView)findViewById(R.id.editText1);
				    	answer2=(TextView)findViewById(R.id.EditText01);
				    	answer3=(TextView)findViewById(R.id.editText2);
				    	answer4=(TextView)findViewById(R.id.editText3);
						answer1.setText("");
						answer2.setText("");
						answer3.setText("");
						answer4.setText("");
						return;
						
					}
				});
				

		        
		        //if below condition true then user wants to close the application
		        if (getIntent().getBooleanExtra("EXIT", false)) {
		            finish();
		        }
		        
		        //below code handles submit button click event and will call onClick() method for further processing
		        submit =(Button) findViewById(R.id.button1);
		        submit.setOnClickListener(this);
			
		}
      
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
    
    private void scheduleNotification(Notification notification) {
    	 
       
    	 Intent notificationIntent = new Intent(this, NotificationPublisher.class);
         notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
         notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
         PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
  
        
         Calendar today = new GregorianCalendar();
         today.add(Calendar.DATE, 0);
         Calendar result = new GregorianCalendar(
         		today.get(Calendar.YEAR),
         		today.get(Calendar.MONTH),
         		today.get(Calendar.DATE),
         		9,
         		0);
         
         long futureInMillis = System.currentTimeMillis();
         
       //System.currentTimeMillis() gives time since the beginning of time "epoch"
         //result.getTimeInMillis() gives today's seconds count until 9 AM in the morning
         if((System.currentTimeMillis())<(result.getTimeInMillis())){
         	
         	futureInMillis = result.getTimeInMillis();
         	System.out.println("Its before 9 AM in the morning");
         	
         }else if((System.currentTimeMillis())>(result.getTimeInMillis()) && (System.currentTimeMillis())<((result.getTimeInMillis())+10800000)){
         	
         	futureInMillis = (result.getTimeInMillis())+10800000;
         	System.out.println("Its after 9 AM and before 12 PM noon");
         	
         }else if((System.currentTimeMillis())>((result.getTimeInMillis())+10800000) && (System.currentTimeMillis())<((result.getTimeInMillis())+21600000)){
         	
         	futureInMillis = (result.getTimeInMillis())+21600000;
         	System.out.println("Its after 12 PM and before 3 PM in the afternoon");
         	
         }else if((System.currentTimeMillis())>((result.getTimeInMillis())+21600000) && (System.currentTimeMillis())<((result.getTimeInMillis())+32400000)){
         	
         	futureInMillis = (result.getTimeInMillis())+32400000;
         	System.out.println("Its after 3 PM and before 6 PM in the evening");
         	
         }else if((System.currentTimeMillis())>((result.getTimeInMillis())+32400000) && (System.currentTimeMillis())<((result.getTimeInMillis())+43200000)){
         	
         	futureInMillis = (result.getTimeInMillis())+43200000;
         	System.out.println("Its after 6 PM and before 9 PM at night");
         	
         }else if(System.currentTimeMillis()>(result.getTimeInMillis()+43200000) && System.currentTimeMillis()<(result.getTimeInMillis()+54000000) ){
        	 
        	 Calendar tomorrow = new GregorianCalendar();
        	 tomorrow.add(Calendar.DATE, 1);
             Calendar tomorrowResult = new GregorianCalendar(
            		 tomorrow.get(Calendar.YEAR),
            		 tomorrow.get(Calendar.MONTH),
            		 tomorrow.get(Calendar.DATE),
             		9,
             		0);
        	 
             futureInMillis = tomorrowResult.getTimeInMillis();
             System.out.println("Its after 9 PM and before 12 AM at night");
         }
         else if(System.currentTimeMillis()>(result.getTimeInMillis()+54000000)){
        	 
        	  today = new GregorianCalendar();
              today.add(Calendar.DATE, 0);
              result = new GregorianCalendar(
             		today.get(Calendar.YEAR),
             		today.get(Calendar.MONTH),
             		today.get(Calendar.DATE),
             		9,
             		0);
        	 
             futureInMillis = today.getTimeInMillis();
             System.out.println("Its after 12 AM and before 9 AM in the morning");
         }
         else{
        	 
        	 //default value
        	 futureInMillis = System.currentTimeMillis()+10800000;
        	 
         }
         
         
         System.out.println("Future milli: "+futureInMillis+"\ntime since epoch:"+System.currentTimeMillis() +"\n time today 9 am:"+result.getTimeInMillis());
         AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
         alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }
    
    private Notification getNotification() {
    	
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("Health Survey App")
        .setContentText("Hey, fill out the survey !!")
        .setAutoCancel(true)
        .setContentIntent(PendingIntent.getActivity(this,0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
    	
        return builder.build();
    }



   public void onClick(View v) {
	  

	   //below code fetches values from TextView and stores in global variables which we created earlier at line 37
    	answer1=(TextView)findViewById(R.id.editText1);
    	answer2=(TextView)findViewById(R.id.EditText01);
    	answer3=(TextView)findViewById(R.id.editText2);
    	answer4=(TextView)findViewById(R.id.editText3);
    	
    	//if textbox is emply or only white spaces then show alert message informing user about required information
    	 if(answer1.getText().toString().trim().equals("") || answer2.getText().toString().trim().equals("") || 
    		answer3.getText().toString().trim().equals("") || answer4.getText().toString().trim().equals("")){
    		 
    		 Context context = getApplicationContext();
    		 CharSequence text = "Please enter valid answers";
    		 int duration = Toast.LENGTH_SHORT;
    		 
    		 //set position of error message to display
    		 DisplayMetrics metrics = new DisplayMetrics();
    		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
    		 int height = metrics.heightPixels/2;
    		 int width = metrics.widthPixels/2;

    		 //set the text and duration of alert message
    		 Toast toast = Toast.makeText(context, text, duration);
    		 toast.setGravity(Gravity.TOP|Gravity.LEFT , width-300 , height-250);
    		 toast.show();
 
    		 
    		     //else condition sends data to database with user answers and GPS location
    			 }else{
    				 
    					Log.e("Location", "Longitude: submit "+Double.toString(longitude)+"  "+"Latitude: submit "+Double.toString(latitude));
    				    
    					JSONObject obj = new JSONObject();
    					try {
    						
    						obj.put("answer1", answer1.getText().toString());
    						obj.put("answer2", answer2.getText().toString());
    						obj.put("answer3", answer3.getText().toString());
    						obj.put("answer4", answer4.getText().toString());
    						
    					} catch (JSONException e) {

    						e.printStackTrace();
    					}

    					//answer1.setText(obj.toString());
    					//below code will help the android application to communicate with server over HTTP request and response
    					//We can perform database related activity once we send data to server through below code
    					GetXMLTask task = new GetXMLTask();
    			        task.execute(obj.toString());// call to doInBackground() method
    				 
    				 
    			 }
	}
   
	  private class GetXMLTask extends AsyncTask<String, Void, String> {//first method gets called
	       
		  @Override
	        protected String doInBackground(String ... jsonObject) {
	            String output = null;
	            
	                output = getOutputFromUrl(jsonObject);//calls getOutputFromUrl() method
	           
	            return output;//calls onPostExecute
	        }
	        
	        private String getOutputFromUrl(String[] jsonObject) {
	            
	  		    
	  			    HttpClient httpclient = new DefaultHttpClient();
	  				HttpPost httppost = new HttpPost("http://192.168.137.1:8080/AndroidAppIS/AndroidAppServlet");
	  				String output = "";
	  				InputStream in = null;
	  				try
	  				{
	                    //storing answers json object in List
	  					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();              
	  					nameValuePairs.add(new BasicNameValuePair("Answers",jsonObject[0]));
	  					nameValuePairs.add(new BasicNameValuePair("Longitude",Double.toString(longitude)));
	  					nameValuePairs.add(new BasicNameValuePair("Latitude",Double.toString(latitude)));
	  					
	  					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

	  					//execution
	  					HttpResponse response = httpclient.execute(httppost);//calls servlet
	  					
	  					
	  					HttpEntity entity = response.getEntity();

	  					 in = entity.getContent();
	  					 
	  					 BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
	  	                 StringBuilder sb = new StringBuilder();
	  	                 String s = null;
	  	                 while ((s = buffer.readLine()) != null){
	  	                	 
	  	                                sb.append(s);
	  	                                
	  	                                }
	  	                 
	  	                                in.close();
	  	                                
	  	                                output = sb.toString();//data coming from servlet
                                              
	  				}

	              catch (UnsupportedEncodingException e) {
	                 e.printStackTrace();
	             } catch (ClientProtocolException e) {
	                 e.printStackTrace();
	             } catch (IOException e) {
	                 e.printStackTrace();
	             }
	  			return output;	
	         }
	        
	    	@Override
	        protected void onPostExecute(String output) {
                
	    		Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
	    		intent.putExtra(SUCCESS_MESSAGE,output.toString());
	    	    startActivity(intent);
                  
	         
	        }
}
	  

  
}

