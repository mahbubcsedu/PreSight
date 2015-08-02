package com.mpss.wheelnav;

import java.util.HashMap;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

	private final float touchError=40.0f;

	private float heightScaleRatio;
	private float widthScaleRatio;
	private float heightOffset;
	private float widthOffset;
	private float intrinsicHeight;
	private float intrinsicWidth;
	private int pointerCount;
	private HashMap<Integer, Pointer> pointers;
	
	private enum EDGE {
		LEFT, RIGHT, TOP, BOTTOM;
	};

	private enum CORNER {
		LEFT, RIGHT, TOP, BOTTOM;
	};

	private Rect bitmapBoundingBox;
	private Rect canvasBoundingBox;
	
	public void setBitmapBoundingBox(Rect r) {
		this.bitmapBoundingBox = r;
	}
	
	public Rect getBitmapBoundingBox() {
		return getBitmapBoundingBox(this.getCanvasBoundingBox());
	}
	
	public void setCanvasBoundingBox(Rect r) {
		this.canvasBoundingBox = r;
	}
	
	public Rect getCanvasBoundingBox() {
		return this.canvasBoundingBox;
	}

	public class Pointer {

		private int id;
		private CORNER corner;
		private EDGE edge;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	public CustomImageView(Context context) {
		
		super(context);
		setBackgroundColor(Color.WHITE);
		this.pointerCount = 0;
		this.pointers = new HashMap<Integer, Pointer>();
		this.canvasBoundingBox = null;
		this.bitmapBoundingBox = null;
		this.heightOffset = 0;
		this.widthOffset = 0;
		this.intrinsicHeight = 0;
		this.intrinsicWidth = 0;
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		
		super(context, attrs);
	}

	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		if(this.intrinsicHeight == 0 && this.intrinsicWidth ==0) {
			ImageView imageView = this;
			Drawable drawable = imageView.getDrawable();
			this.intrinsicHeight = drawable.getIntrinsicHeight();
			this.intrinsicWidth = drawable.getIntrinsicWidth();
		}

		if(this.heightOffset==0 && this.widthOffset==0) {
			calculateOffsets();
		}

		if(this.canvasBoundingBox==null) {

			this.canvasBoundingBox = new Rect();
			if(this.bitmapBoundingBox==null) {
				Rect r = getBoundingBox(getInitialBitmapBoundingBox());
				this.canvasBoundingBox.set(r);
			}
			else {
				Rect r = getBoundingBox(this.bitmapBoundingBox);
				this.canvasBoundingBox.set(r);
			}
		}

		Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(10.0F);
		paint.setStyle(Paint.Style.STROKE);
		System.out.println("Rect:" + this.canvasBoundingBox.left + " " + this.canvasBoundingBox.top + " " + this.canvasBoundingBox.right + " " + this.canvasBoundingBox.bottom);
		canvas.drawRect(this.canvasBoundingBox, paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		super.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			System.out.println("ACTION_CANCEL " + event.getPointerCount());
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			this.pointerCount = 0;
			this.pointers.clear();
			addPointers(event);
			System.out.println("ACTION_DOWN " + event.getPointerCount());
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			performMove(event);
			this.postInvalidate();
			System.out.println("ACTION_MOVE " + event.getPointerCount());
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			System.out.println("ACTION_UP " + event.getPointerCount());
		}
		return true;
	}
	
	private Rect getBitmapBoundingBox(Rect canvasBoundingBox) {

		ImageView imageView = this;
		Drawable drawable = imageView.getDrawable();
		Rect imageBounds = drawable.getBounds();

		canvasBoundingBox.left = canvasBoundingBox.left - (int)widthOffset - imageBounds.left;
		canvasBoundingBox.right = canvasBoundingBox.right - (int)widthOffset - imageBounds.left;
		canvasBoundingBox.top = canvasBoundingBox.top - (int)heightOffset - imageBounds.top;
		canvasBoundingBox.bottom = canvasBoundingBox.bottom - (int)heightOffset - imageBounds.top;

		canvasBoundingBox.left = (int)(canvasBoundingBox.left * this.widthScaleRatio);
		canvasBoundingBox.right = (int)(canvasBoundingBox.right * this.widthScaleRatio);
		canvasBoundingBox.top = (int)(canvasBoundingBox.top * this.heightScaleRatio);
		canvasBoundingBox.bottom = (int)(canvasBoundingBox.bottom * this.heightScaleRatio);

		this.bitmapBoundingBox = canvasBoundingBox;
		return canvasBoundingBox;
	}
	
	private Rect getBoundingBox(Rect bitmapBoundingBox) {
		calculateScaleRatios();
		return getCanvasBoundingBox(bitmapBoundingBox);
	}

	private Rect getCanvasBoundingBox(Rect originalBox) {

		Rect box = new Rect();
		box.left = (int)(originalBox.left / this.widthScaleRatio);
		box.right = (int)(originalBox.right / this.widthScaleRatio);
		box.top = (int)(originalBox.top / this.heightScaleRatio);
		box.bottom = (int)(originalBox.bottom / this.heightScaleRatio);

		ImageView imageView = this;
		Drawable drawable = imageView.getDrawable();
		Rect imageBounds = drawable.getBounds();

		box.left = box.left + imageBounds.left + (int)widthOffset;
		box.right = box.right + imageBounds.left +(int) widthOffset;
		box.top = box.top + imageBounds.top + (int) heightOffset;
		box.bottom = box.bottom + imageBounds.top + (int)heightOffset;
		return box;
	}

	@SuppressLint("NewApi") private void calculateScaleRatios() {
		
		int intrinsicHeight = (int)this.intrinsicHeight;
		int intrinsicWidth = (int)this.intrinsicWidth;

		ImageView imageView = this;
		int scaledHeight = imageView.getMeasuredHeight() - (2 * (int)heightOffset);
		int scaledWidth = imageView.getMeasuredWidth()- (2 * (int)widthOffset);

		this.heightScaleRatio = (float)intrinsicHeight / (float)scaledHeight;
		this.widthScaleRatio = (float)intrinsicWidth / (float)scaledWidth;
	}

	private Rect getInitialBitmapBoundingBox() {

		float imageHeight = this.intrinsicHeight;
		float imageWidth = this.intrinsicWidth;
		float boxWidth = imageWidth/3;
		float boxHeight = imageHeight/3;
		Rect box = new Rect();
		box.left = (int)boxWidth;
		box.right = (int)(box.left+boxWidth);
		box.top = (int)boxHeight;
		box.bottom = (int)(box.top+boxHeight);
		return box;
	}

	private void calculateOffsets() {

		int[] offset = new int[2];
		float[] values = new float[9];
		Matrix m = this.getImageMatrix();
		m.getValues(values);
		offset[0] = (int) values[5];
		offset[1] = (int) values[2];

		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) this.getLayoutParams();
		int paddingTop = (int) (this.getPaddingTop() );
		int paddingLeft = (int) (this.getPaddingLeft() );
		offset[0] += paddingTop + lp.topMargin;
		offset[1] += paddingLeft + lp.leftMargin;

		this.heightOffset = offset[0];
		this.widthOffset = offset[1];
	}
	
	private Pointer ValidatePointer(float x, float y) {

		Pointer p = new Pointer();

		float left = this.canvasBoundingBox.left;
		float right = this.canvasBoundingBox.right;
		float top = this.canvasBoundingBox.top;
		float bottom = this.canvasBoundingBox.bottom;

		if (x < left + touchError && x > left - touchError && y < top + touchError
				&& y > top - touchError) {
			p.corner = CORNER.TOP;
		} else if (x < left + touchError && x > left - touchError && y < bottom + touchError
				&& y > bottom - touchError) {
			p.corner = CORNER.LEFT;
		} else if (x < right + touchError && x > right - touchError && y < top + touchError
				&& y > top - touchError) {
			p.corner = CORNER.RIGHT;
		} else if (x < right + touchError && x > right - touchError && y < bottom + touchError
				&& y > bottom - touchError) {
			p.corner = CORNER.BOTTOM;
		} else if (x < left + touchError && x > left - touchError && y >= top
				&& y <= bottom) {
			p.edge = EDGE.LEFT;
		} else if (y < top + touchError && y > top - touchError && x >= left && x <= right) {
			p.edge = EDGE.TOP;
		} else if (x < right + touchError && x > right - touchError && y >= top
				&& y <= bottom) {
			p.edge = EDGE.RIGHT;
		} else if (y < bottom + touchError && y > bottom - touchError && x >= left
				&& x <= right) {
			p.edge = EDGE.BOTTOM;
		}

		if (p.edge != null || p.corner != null) {
			return p;
		}
		return null;
	}

	private void addPointers(MotionEvent event) {

		int pointerCount = event.getPointerCount();
		for (int i = 0; i < pointerCount; i++) {
			if (!this.pointers.containsKey(event.getPointerId(i))) {
				float x = event.getX(i);
				float y = event.getY(i);
				Pointer p = ValidatePointer(x, y);
				if (p != null) {
					p.id = event.getPointerId(i);
					this.pointers.put(p.id, p);
					this.pointerCount++;
				}
			}
		}
	}

	private void removePointers(MotionEvent event) {

		HashSet<Integer> pointerSet = new HashSet<Integer>();

		for (int k : this.pointers.keySet()) {
			if(event.findPointerIndex(k)==-1) {
				pointerSet.add(k);
			}
		}
		for (int k : pointerSet) {
			this.pointers.remove(k);
			if (this.pointerCount > 0)
				this.pointerCount--;
		}

	}

	private void performMove(MotionEvent event) {

		int pointerCount = event.getPointerCount();

		if (this.pointerCount<pointerCount) {
			addPointers(event);
		}
		else if (this.pointerCount>pointerCount) {
			removePointers(event);
		}

		float newLeft=0, newTop=0, newRight=0, newBottom=0;
		float currentHeight=this.canvasBoundingBox.bottom-this.canvasBoundingBox.top; 
		float currentWidth=this.canvasBoundingBox.right-this.canvasBoundingBox.left;

		for (int id : this.pointers.keySet()) {
			Pointer p = this.pointers.get(id);
			int pointerIndex = event.findPointerIndex(id); 
			if(pointerIndex != -1) {
				if(p.edge!=null) {
					switch (p.edge) {
					case TOP: 
						newTop = event.getY(pointerIndex);
						break;
					case LEFT: 
						newLeft = event.getX(pointerIndex);
						break;
					case BOTTOM: 
						newBottom = event.getY(pointerIndex);
						break;
					case RIGHT: 
						newRight = event.getX(pointerIndex);
						break;
					}
				}
				else if (p.corner!=null) {
					switch (p.corner) {
					case TOP: 
						newTop = event.getY(pointerIndex);
						newLeft = event.getX(pointerIndex);
						break;
					case BOTTOM: 
						newBottom = event.getY(pointerIndex);
						newRight = event.getX(pointerIndex);
						break;
					case LEFT: 
						newLeft = event.getX(pointerIndex);
						newBottom = event.getY(pointerIndex);
						break;
					case RIGHT: 
						newTop = event.getY(pointerIndex);
						newRight = event.getX(pointerIndex);
						break;
					}
				}
			}
		}	

		if (this.pointerCount==1) {
			if (newLeft!=0) {
				newRight=newLeft+currentWidth;
			}  
			if (newRight!=0) {
				newLeft=newRight-currentWidth;
			}
			if (newTop!=0) {
				newBottom=newTop+currentHeight;
			}
			if (newBottom!=0) {
				newTop=newBottom-currentHeight;
			}
		}
		
		if (newLeft==0) 
			newLeft = (float)this.canvasBoundingBox.left;
		if (newRight==0)
			newRight = (float)this.canvasBoundingBox.right;
		if (newTop==0)
			newTop = (float)this.canvasBoundingBox.top;
		if (newBottom==0) 
			newBottom = (float)this.canvasBoundingBox.bottom;

		if (newTop<newBottom && newLeft<newRight) {
			this.canvasBoundingBox.set((int)newLeft, (int)newTop, (int)newRight, (int)newBottom);
		}
	}
}