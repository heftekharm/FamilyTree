package com.hfm.familytree;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FixedResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.LayoutGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
	
public class Tree extends LayoutGameActivity implements IPinchZoomDetectorListener,IScrollDetectorListener,IOnSceneTouchListener,Runnable{
	public static boolean nextSceneIsReady=false;
	private int HEIGHT;
	private int WIDTH;
	ZoomCamera cam;
	long start;
	long end;
	Font font;
	boolean updating=false;
	

	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		start=System.currentTimeMillis();
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		HEIGHT=dm.heightPixels;
		WIDTH=dm.widthPixels;
		cam=new ZoomCamera(0,0,WIDTH ,HEIGHT);	
		EngineOptions eo= new EngineOptions(true,ScreenOrientation.LANDSCAPE_SENSOR,new FixedResolutionPolicy(WIDTH,HEIGHT),cam);		
		return eo;
		
		
	}


	
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		
		found_list=(ListView) findViewById(R.id.found_list);
		find_text = (EditText) findViewById(R.id.find_Text);
		
		
		font=FontFactory.createFromAsset(getFontManager(), getTextureManager(), 128, 128,getAssets(),"BKARIM.TTF", 16f,true,Color.RED.getABGRPackedInt());
		font.load();		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	PinchZoomDetector pinch;
	SurfaceScrollDetector scroll;
	Scene scene;
	Scene splashScreen;
	boolean toggle=false;
	Rectangle rect;
	Line line;
	Text text;
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		
		Thread t=new Thread(this);
		t.start();
		splashScreen=new Scene();
		text=new Text(0,0,font,"Please Wait",getVertexBufferObjectManager());
		splashScreen.attachChild(text);
	
		cam.setCenter(0,0);	
		pinch=new PinchZoomDetector(this);
		scroll=new SurfaceScrollDetector(this);
		pinch.setEnabled(true);		

		pOnCreateSceneCallback.onCreateSceneFinished(splashScreen);
		
	}
	float i=0;
	@Override
	public void onPopulateScene(Scene pScene,OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		

		getEngine().registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				// TODO Auto-generated method stub
				if(nextSceneIsReady){
					getEngine().setScene(scene);
					
					getEngine().unregisterUpdateHandler(this);
				}
				
			}
		});
		
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}	
	
	
	BitmapTextureAtlas bta;
	ITextureRegion itr;
	

	
	
	float MAX_ZOOM_FACTOR=2;
	float MIN_ZOOM_FACTOR=2;
	float IZoom;
	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		
		IZoom=cam.getZoomFactor();
	}

	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
			TouchEvent e, float pZoomFactor) {
		// TODO Auto-generated method stub
		
		float nzf=IZoom*pZoomFactor;
		if(nzf<1&&nzf>0.0171)
		cam.setZoomFactor(nzf);
		//if (e.isActionMove()) cam.setCenter(e.getX(),e.getY());
		
	}

	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		// TODO Auto-generated method stub
		float nzf=IZoom*pZoomFactor;
		if(nzf<1&&nzf>0.0171){			
		cam.setZoomFactor(nzf);
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			Toast.makeText(Tree.this,String.valueOf(cam.getZoomFactor()),Toast.LENGTH_SHORT).show();	
			}
		});
		}
		
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		
		final float zoomFactor = this.cam.getZoomFactor();
		this.cam.offsetCenter(-pDistanceX / zoomFactor, pDistanceY / zoomFactor);
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		final float zoomFactor = this.cam.getZoomFactor();
		this.cam.offsetCenter(-pDistanceX / zoomFactor, pDistanceY / zoomFactor);
		
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		// TODO Auto-generated method stub
		final float zoomFactor = this.cam.getZoomFactor();
		this.cam.offsetCenter(-pDistanceX / zoomFactor, pDistanceY / zoomFactor);
		
		
		
	}

//-12355 +12355   3180 -2400


		@Override
		public boolean onSceneTouchEvent(Scene pScene, TouchEvent e) {
			if(!updating){
			if(!toggle) {
				//AddAllOfIndivitualsToScene();
				toggle=true;}
			pinch.onTouchEvent(e);

			if(pinch.isZooming()) {
				scroll.setEnabled(false);
			} else {
				if(e.isActionDown()) {
					scroll.setEnabled(true);
					
					
				}
				scroll.onTouchEvent(e);
			}
			}
			return true;		

	}



		@Override
		public void run() {
			// TODO Auto-generated method stub
			Prepare p=new Prepare(this,this,font);
			scene=p.createScene();
			byte c=3;
			//cam.setBounds(c*individual.X_MIN,c*individual.Y_MIN,c* individual.X_MAX,c*individual.Y_MAX);
			//cam.setBoundsEnabled(true);
			nextSceneIsReady=true;
		}



		@Override
		protected int getLayoutID() {
			// TODO Auto-generated method stub
			return R.layout.render_layout;
		}



		@Override
		protected int getRenderSurfaceViewID() {
			// TODO Auto-generated method stub
			return R.id.render_view;
		}
		
		
		ListView found_list;
		EditText find_text;
		public void find_Btn(View v){
			final ArrayList<individual> found_array=new ArrayList<individual>();
			found_array.clear();
			final String ftxt=find_text.getText().toString();
			
			if (ftxt.isEmpty()) {
				toastOnUiThread("field is Empty", Toast.LENGTH_SHORT);
				
				return;
			}
			Thread sarch_thread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					int found_cases=0;
					for(int i=0;i<xml.individuals.size();i++){
						if (xml.individuals.get(i)!=null ){
							individual ind=xml.individuals.get(i);
						if(ind.first.contains(ftxt)){
							found_array.add(ind);
							found_cases++;
						}
						}
					}
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							found_list.setAdapter(new found_adapter(found_array));
							found_list.invalidate();
						}
					});
					Tree.this.toastOnUiThread(found_cases+"found!", Toast.LENGTH_SHORT);
					
					
				}
			});
			sarch_thread.start();
			//find_text.setEnabled(false);
			
		}
		
		
		
		
		private class found_adapter extends BaseAdapter{
			ArrayList<individual> found;
			
			public found_adapter(ArrayList<individual> list) {
				// TODO Auto-generated constructor stub
			found=list;
			
			
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return found.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			ViewHolder holder;
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				if(convertView==null){
					convertView=getLayoutInflater().inflate(R.layout.list_view_cell, null);
					holder=new ViewHolder();
					
					holder.tview=(TextView) findViewById(R.id.text_cell);
					convertView.setTag(holder);		
					
				}
				else{
					holder=(ViewHolder) convertView.getTag();
					
					
				}
				holder.tview.setText(found.get(position).display);
				return convertView;
			}
			

			
			
		}

		class ViewHolder{
			TextView tview;
			
			
		}













	
}