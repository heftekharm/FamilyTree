package com.hfm.familytree;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ExtendedSprite extends Sprite {

	public ExtendedSprite(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,Activity c) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
		a=c;
	}
	private int ID;
	private Activity a;
	public void setID(int id){
		ID=id;
	}
	@Override
	public boolean onAreaTouched(TouchEvent e,float pTouchAreaLocalX, float pTouchAreaLocalY) {
		// TODO Auto-generated method stub
		if(e.isActionDown()){				
			Log.d("IIIID",String.valueOf(ID));
			//Toast.makeText(a,ID,Toast.LENGTH_SHORT).show();	
			return true;
		}
		
		
		
		
		return false;//return super.onAreaTouched(e, pTouchAreaLocalX, pTouchAreaLocalY);
		
	}


}
