package com.hfm.familytree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.StreamCorruptedException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Mesh;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.text.Text;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;

import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;
import org.andengine.util.algorithm.Spiral;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.widget.Toast;

public class Prepare {
	private BaseGameActivity bgActivity;
	private Scene scene;
	private IOnSceneTouchListener sceneListener;
	public static String wait_state = "Please Wait";
	private boolean readed = false;
	private int Z_tiled_INDEX = 1;
	private int Z_SPIRIT_Text_INDEX = 4;
	private int Z_SPIRIT_FRAME_INDEX = 3;
	private int Z_LINE_INDEX = 2;
	private int Empty_Bitmap_W = 169;
	private int Empty_Bitmap_H = 45;
	private int COFICIENT =5;
	private int atlasX = 0;
	private int atlasY = 0;
	BitmapTextureAtlas bta;
	ITextureRegion itr;
	Font font;
	private int SPIRIT_TAG = 888;
	ITextureRegion frame;
	SpriteBatch frameF_batch;
	SpriteBatch frameM_batch;
	Typeface typeface;
	BitmapTextureAtlas mBitmapTextureAtlas;
	private int line_width = 4;	
	private int LINE_TAG = 999;	
	float MAX_ZOOM_FACTOR = 2;
	float MIN_ZOOM_FACTOR = 2;
	float IZoom;
	HashMap<String, ITextureRegion> map;
	HashMap<String, ITextureRegion> first_hash;
	HashMap<String, ITextureRegion> last_hash;
	HashMap<String, ITextureRegion> last2_hash;
	private TMXTiledMap mTMXTiledMap;
	
	public Prepare(BaseGameActivity a, IOnSceneTouchListener listener, Font f) {
		// TODO Auto-generated constructor stub
		sceneListener = listener;
		bgActivity = a;
		this.font = f;
	}

	public Scene createScene() {
		// readMAP();
		scene = new Scene();
		//setBackground();
		prepareFrame();
		wait_state = "Parsing XML";
		xml.ParseXML(bgActivity);
		wait_state = "Adding Names And Lines ";
		AddAllOfIndivitualsToScene();
		wait_state = "Setting Scene Properties";
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		scene.setOnSceneTouchListener(sceneListener);
		Background bg = new Background(Color.WHITE);
		scene.setBackground(bg);
		frameM_batch.submit();frameF_batch.submit();
		frameM_batch.setZIndex(Z_SPIRIT_FRAME_INDEX);
		frameF_batch.setZIndex(Z_SPIRIT_FRAME_INDEX);
		scene.attachChild(frameM_batch);scene.attachChild(frameF_batch);	
		scene.sortChildren();
		wait_state = "Finishing...";
		// WriteMAP();
		return scene;
	}



	private void prepareFrame() {
		// TODO Auto-generated method stub
		BitmapTextureAtlas frame_Atlas = new BitmapTextureAtlas(
				bgActivity.getTextureManager(), 180, 130,
				BitmapTextureFormat.RGB_565);
		frame = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				frame_Atlas, bgActivity.getAssets(), "gfx/fr.png", 0, 0);
		frame_Atlas.load();
		frameM_batch = new SpriteBatch(frame_Atlas, 1400,bgActivity.getVertexBufferObjectManager());
		frameF_batch = new SpriteBatch(frame_Atlas, 1400,bgActivity.getVertexBufferObjectManager());
		frameM_batch.setPosition(0-80, 0-65);
		frameF_batch.setPosition(0-80, 0-65);
	}

	private void setBackground() {
		// TODO Auto-generated method stub

		/*
		 * AssetBitmapTexture bitTex=null; try { bitTex=new
		 * AssetBitmapTexture(bgActivity.getEngine().getTextureManager(),
		 * bgActivity
		 * .getAssets(),"gfx/wallpaper_texture.jpg",BitmapTextureFormat
		 * .RGB_565,TextureOptions.REPEATING_BILINEAR);
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } bitTex.load(); ITextureRegion
		 * reapitingT=TextureRegionFactory.extractFromTexture(bitTex); float
		 * cW=bgActivity.getEngine().getCamera().getWidth(); float
		 * cH=bgActivity.getEngine().getCamera().getHeight(); float scale=1f;
		 * RepeatingSpriteBackground bg=new RepeatingSpriteBackground(cW,
		 * cH,reapitingT,scale,bgActivity.getVertexBufferObjectManager());
		 * scene.setBackground(bg); scene.setBackgroundEnabled(true);
		 */
		try {
			final TMXLoader tmxLoader = new TMXLoader(bgActivity.getAssets(), bgActivity.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, bgActivity.getVertexBufferObjectManager());
			this.mTMXTiledMap = tmxLoader.loadFromAsset("gfx/desert.tmx");
			this.mTMXTiledMap.setOffsetCenter(0, 0);

			
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

		scene.attachChild(this.mTMXTiledMap);
		
		
/*		try {
			TMXLoader loader = new TMXLoader(bgActivity.getAssets(),bgActivity.getTextureManager(),	TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					bgActivity.getVertexBufferObjectManager());

			TMXTiledMap map = loader.loadFromAsset("gfx/desert.tmx");
			TMXLayer layer = map.getTMXLayers().get(0);
			layer.setZIndex(Z_tiled_INDEX);
			scene.attachChild(layer);
		} catch (TMXLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	private void WriteMAP() {
		if (readed)
			return;
		File file = new File("MAP.map");
		try {
			ObjectOutputStream obj = new ObjectOutputStream(
					new FileOutputStream(file));
			obj.writeObject(map);
			obj.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void readMAP() {
		// TODO Auto-generated method stub
		File file = new File("MAP.map");
		try {
			ObjectInputStream input = new ObjectInputStream(
					new FileInputStream(file));
			map = (HashMap<String, ITextureRegion>) input.readObject();
			readed = true;

		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	private void SpiriteCreate() {
		// TODO Auto-generated method stub
		bta = new BitmapTextureAtlas(bgActivity.getTextureManager(), 100, 100);
		itr = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta,
				bgActivity.getAssets(), "frame.jpg", 10, 10);
		bta.load();
	}

	
/*
	private void AddTextToScene(final String display, String first, String last, String last2, char gender, int xpos,
			int ypos, final Context c, int id) {
		xpos = COFICIENT * xpos;
		ypos = COFICIENT * ypos;
		if (map.containsKey(display)) {
			ExtendedSprite indSpirit = new ExtendedSprite(xpos, ypos,map.get(display),bgActivity.getVertexBufferObjectManager(), bgActivity);
			indSpirit.setID(id);
			indSpirit.setX(xpos);
			indSpirit.setY(ypos);
			indSpirit.setZIndex(Z_SPIRIT_Text_INDEX);
			indSpirit.setTag(SPIRIT_TAG);
			indSpirit.setCullingEnabled(true);
			scene.registerTouchArea(indSpirit);
			// Sprite frame_sprite=new Sprite(xpos, ypos, frame,
			// bgActivity.getVertexBufferObjectManager());

			// frame_sprite.setZIndex(Z_SPIRIT_FRAME_INDEX);
			frame_batch.drawWithoutChecks(frame, xpos, ypos, frame.getWidth(),
					frame.getHeight(), 1, 1, 1, 1);
			// scene.attachChild(frame_sprite);
			scene.attachChild(indSpirit);
			return;
		}

		typeface = Typeface.createFromAsset(bgActivity.getAssets(),"BKARIM.TTF");
		mBitmapTextureAtlas = new BitmapTextureAtlas(bgActivity.getTextureManager(), 170, 50, TextureOptions.NEAREST);
		ITextureRegion mDecoratedBalloonTextureRegion;
		
		
		
		final IBitmapTextureAtlasSource baseTextureSource = new EmptyBitmapTextureAtlasSource(
				Empty_Bitmap_W, Empty_Bitmap_H);
		final IBitmapTextureAtlasSource decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(
				baseTextureSource) {

			@Override
			protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
				// pCanvas.drawARGB(0xFF,0x4A,0xE6,0x98);
				this.mPaint.setAntiAlias(true);
				// this.mPaint.setStyle(Style.STROKE);
				// this.mPaint.setColor(Color.RED_ABGR_PACKED_INT);

				// pCanvas.drawRect(0,0,Empty_Bitmap_W,Empty_Bitmap_H,mPaint);//(Empty_Bitmap_W/2,Empty_Bitmap_H/2,Empty_Bitmap_H/2,mPaint);
				this.mPaint.setColor(Color.BLUE.getABGRPackedInt());
				this.mPaint.setStyle(Style.FILL);
				this.mPaint.setTextSize(32f);
				this.mPaint.setTypeface(typeface);
				this.mPaint.setTextAlign(Align.CENTER);

				// this.mPaint.setColor(Color.GREEN.getABGRPackedInt());
				pCanvas.drawText(display, Empty_Bitmap_W / 2,
						Empty_Bitmap_H / 2, this.mPaint);

			}

			@Override
			public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
		atlasX = atlasX + 100;
		if (atlasX > 10000) {
			atlasX = 0;
			atlasY = atlasY + 100;
		}

		mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromSource(mBitmapTextureAtlas,
						decoratedTextureAtlasSource, 0, 0);
		mBitmapTextureAtlas.load();
		map.put(display, mDecoratedBalloonTextureRegion);
		ExtendedSprite indSpirit = new ExtendedSprite(xpos, ypos,
				mDecoratedBalloonTextureRegion,
				bgActivity.getVertexBufferObjectManager(), bgActivity);
		indSpirit.setID(id);
		// Sprite indSpirit = new Sprite(xpos,ypos,itr,bgActivity
		// .getVertexBufferObjectManager());
		// Text indSpirit=new Text(xpos, ypos, font,display,bgActivity
		// .getVertexBufferObjectManager());

		indSpirit.setX(xpos);
		indSpirit.setY(ypos);
		indSpirit.setZIndex(Z_SPIRIT_Text_INDEX);
		indSpirit.setTag(SPIRIT_TAG);
		indSpirit.setCullingEnabled(true);
		scene.registerTouchArea(indSpirit);
		scene.attachChild(indSpirit);
		}*/
	int error;
	
	private void AddTextToScene(final String display, String first, String last, String last2, char gender, int xpos,int ypos, final Context c, int id) {
		boolean flag=gender=='M'?true:false;
		try{
		error=325;
		xpos = COFICIENT * xpos;
		ypos = COFICIENT * ypos;
		boolean first_attached=false;
		boolean last_attached=false;
		boolean last2_attached=false;
		boolean last_isEmpty=last.isEmpty();
		boolean last2_isEmpty=last2.isEmpty();
		error=333;
		if( first_hash.containsKey(first)){
			ExtendedSprite indSpirit = new ExtendedSprite(xpos, ypos+20,first_hash.get(first),bgActivity.getVertexBufferObjectManager(), bgActivity);
			indSpirit.setID(id);
			indSpirit.setZIndex(Z_SPIRIT_Text_INDEX);
			indSpirit.setTag(SPIRIT_TAG);
			error=339;
			indSpirit.setCullingEnabled(true);
			error=341;
			//scene.registerTouchArea(indSpirit);
			if (flag){
			frameM_batch.drawWithoutChecks(frame, xpos, ypos, frame.getWidth(),frame.getHeight(), 1, 1, 1, 1);
			flag=false;
			}else{
			frameF_batch.drawWithoutChecks(frame, xpos, ypos, frame.getWidth(),frame.getHeight(), 1, 1, 1, 1);
			flag=true;
			}
			error=343;
			scene.attachChild(indSpirit);
			first_attached=true;
		}
		error=345;
		
		if( !last.isEmpty() && last_hash.containsKey(first)){
			ExtendedSprite indSpirit = new ExtendedSprite(xpos, ypos-10,last_hash.get(last),bgActivity.getVertexBufferObjectManager(), bgActivity);
			indSpirit.setID(id);
			indSpirit.setZIndex(Z_SPIRIT_Text_INDEX);
			indSpirit.setTag(SPIRIT_TAG);
			indSpirit.setCullingEnabled(true);
			scene.attachChild(indSpirit);
			last_attached=true;
		}
		
		if( !last2.isEmpty() && last2_hash.containsKey(first)){
			ExtendedSprite indSpirit = new ExtendedSprite(xpos, ypos-20,last2_hash.get(last2),bgActivity.getVertexBufferObjectManager(), bgActivity);
			indSpirit.setID(id);
			indSpirit.setZIndex(Z_SPIRIT_Text_INDEX);
			indSpirit.setTag(SPIRIT_TAG);
			indSpirit.setCullingEnabled(true);			
			scene.attachChild(indSpirit);
			last2_attached=true;
		}
		int rows=0;
		rows=first_attached?rows:rows+1;
		rows=last_attached||last_isEmpty?rows:rows+1;
		rows=last2_attached||last2_isEmpty?rows:rows+1;
		if (rows==0) return;
		int atlas_width=170;
		int atlas_height=rows*50;
		
		int place=-1;
		mBitmapTextureAtlas = new BitmapTextureAtlas(bgActivity.getTextureManager(), atlas_width,atlas_height, TextureOptions.NEAREST);
		ITextureRegion first_ITR=first_attached?null:createITR(first,++place);
		ITextureRegion last_ITR=last_attached||last_isEmpty?null:createITR(last,++place);
		ITextureRegion last2_ITR=last2_attached||last2_isEmpty?null:createITR(last2,++place);
		mBitmapTextureAtlas.load();
		error=378;
		if(first_ITR!=null)
		{
		first_hash.put(first, first_ITR);
		ExtendedSprite first_sprite = new ExtendedSprite(xpos, ypos+20,first_ITR,bgActivity.getVertexBufferObjectManager(), bgActivity);
		first_sprite.setZIndex(Z_SPIRIT_Text_INDEX);
		first_sprite.setCullingEnabled(true);
		if (flag){
		frameM_batch.drawWithoutChecks(frame, xpos, ypos, frame.getWidth(),frame.getHeight(), 1, 1, 1, 1);
		flag=false;
		}else{
		frameF_batch.drawWithoutChecks(frame, xpos, ypos, frame.getWidth(),frame.getHeight(), 1, 1, 1, 1);
		flag=true;
		}
		scene.attachChild(first_sprite);
		}
		error=385;
		if(last_ITR!=null){
		last_hash.put(last, last_ITR);
		ExtendedSprite last_sprite = new ExtendedSprite(xpos, ypos-10,last_ITR,bgActivity.getVertexBufferObjectManager(), bgActivity);
		last_sprite.setZIndex(Z_SPIRIT_Text_INDEX);
		last_sprite.setCullingEnabled(true);
		scene.attachChild(last_sprite);}
		
		if(last2_ITR!=null){
		last2_hash.put(last2, last2_ITR);
		ExtendedSprite last2_sprite = new ExtendedSprite(xpos, ypos-20,last2_ITR,bgActivity.getVertexBufferObjectManager(), bgActivity);
		last2_sprite.setZIndex(Z_SPIRIT_Text_INDEX);	
		last2_sprite.setCullingEnabled(true);
		scene.attachChild(last2_sprite);	
		}
		//scene.registerTouchArea(indSpirit);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		
		}
	
		
	}
	
	
	private ITextureRegion createITR(final String display,int c_pos_on_atlas){
		
		
		ITextureRegion mDecoratedBalloonTextureRegion;		
		final IBitmapTextureAtlasSource baseTextureSource = new EmptyBitmapTextureAtlasSource(Empty_Bitmap_W, Empty_Bitmap_H);
		final IBitmapTextureAtlasSource decoratedTextureAtlasSource = new BaseBitmapTextureAtlasSourceDecorator(baseTextureSource) {

			@Override
			protected void onDecorateBitmap(Canvas pCanvas) throws Exception {
				
				this.mPaint.setAntiAlias(true);
				this.mPaint.setColor(Color.BLUE.getABGRPackedInt());
				this.mPaint.setStyle(Style.FILL);
				this.mPaint.setTextSize(32f);
				this.mPaint.setTypeface(typeface);
				this.mPaint.setTextAlign(Align.CENTER);
				pCanvas.drawText(display, Empty_Bitmap_W / 2,Empty_Bitmap_H / 2, this.mPaint);

			}

			@Override
			public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
		mDecoratedBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(mBitmapTextureAtlas,decoratedTextureAtlasSource, 0, c_pos_on_atlas*50);		
		return mDecoratedBalloonTextureRegion;		
	}
	
	
	private void BeforAdding() {
		SpiriteCreate();
		typeface = Typeface.createFromAsset(bgActivity.getAssets(),
				"BKARIM.TTF");
		mBitmapTextureAtlas = new BitmapTextureAtlas(
				bgActivity.getTextureManager(), 100050, 10050,
				TextureOptions.NEAREST);
	}

	

	private void AddAllOfIndivitualsToScene() {
		// BeforAdding();
		typeface = Typeface.createFromAsset(bgActivity.getAssets(),"BKARIM.TTF");
		map = new HashMap<String, ITextureRegion>();
		first_hash= new HashMap<String, ITextureRegion>();
		last_hash= new HashMap<String, ITextureRegion>();
		last2_hash= new HashMap<String, ITextureRegion>();
		
		int size = xml.families.size();
		for (int i = 0; i < size; i = i + 1) {
			try {
				if (xml.families.get(i) == null)
					continue;
				family f = xml.families.get(i);
				individual p1 = xml.individuals.get(f.getParent1());
				individual p2 = xml.individuals.get(f.getParent2());
				
				AddTextToScene(p1.display,p1.first,p1.last,p1.last2, p1.gender, p1.pos.x, p1.pos.y,	bgActivity, p1.id);
				AddTextToScene(p2.display,p2.first,p2.last,p2.last2, p2.gender, p2.pos.x, p2.pos.y,
						bgActivity, p2.id);
				for (int j = 0; j < f.getChildrens().size(); j++) {
					individual ind = xml.individuals.get(f.getChildrens()
							.get(j));
					if (ind.drawn)
						continue;
					if (ind.id==706){
						
						int xx=0;
						xx++;
					}
					AddTextToScene(ind.display,ind.first,ind.last,ind.last2, ind.gender, ind.pos.x,ind.pos.y, bgActivity, ind.id);
					ind.drawn = true;
				}
				
				f.setLines();
				
				ArrayList<even> pair_points = f.getLines();
				float[] basebuffer=new float[pair_points.size()*6];
				
				for (int q = 0; q < pair_points.size(); q = q + 2) {
					
					basebuffer[q*3]=pair_points.get(q).x*COFICIENT;basebuffer[q*3+1]=pair_points.get(q).y*COFICIENT; basebuffer[q*3+2]=0;  
					basebuffer[(q+1)*3]=pair_points.get(q+1).x*COFICIENT;basebuffer[(q+1)*3+1]=pair_points.get(q+1).y*COFICIENT; basebuffer[(q+1)*3+2]=0;

				}
				
				Mesh mesh_lines=new Mesh(0,0,basebuffer,pair_points.size(), DrawMode.LINES, bgActivity.getVertexBufferObjectManager());
				
				mesh_lines.setColor(Color.RED);
				scene.attachChild(mesh_lines);

				
/*				ArrayList<even> lines = f.getLines();
				float[] basebuffer=new float[lines.size()*6];
				
				for (int q = 0; q < lines.size(); q = q + 2) {
					
					Line line = new Line(lines.get(q).x * COFICIENT,lines.get(q).y * COFICIENT, lines.get(q + 1).x* COFICIENT,lines.get(q + 1).y * COFICIENT, line_width,
							bgActivity.getVertexBufferObjectManager());
					line.setZIndex(Z_LINE_INDEX);
					line.setColor(Color.YELLOW);
					scene.attachChild(line);

				}*/
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				final int x = i;
				bgActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(bgActivity, "Error:" + x,Toast.LENGTH_SHORT).show();
					}
				});
				e.printStackTrace();
			}

		}
	}



}
