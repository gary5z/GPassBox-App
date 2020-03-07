package com.ghut.passbox.activity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghut.passbox.R;

public class FileChooserAdapter extends BaseAdapter {

	  private LayoutInflater mInflater;
	  private Bitmap mIcon1;
	  private Bitmap mIcon2;
	  private Bitmap mIcon3;
	  private Bitmap mIcon4;
	  private List<String> items;
	  private List<String> paths;

	  Map<Integer, Boolean> isCheckMap =  new HashMap<Integer, Boolean>();
	  int selectedPosition = -1;
	  
	  public FileChooserAdapter(Context context,List<String> it,List<String> pa)
	  {
	    mInflater = LayoutInflater.from(context);
	    items = it;
	    paths = pa;
	    mIcon1 = BitmapFactory.decodeResource(context.getResources(),R.drawable.back01);
	    mIcon2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.back02);
	    mIcon3 = BitmapFactory.decodeResource(context.getResources(),R.drawable.folder);
	    mIcon4 = BitmapFactory.decodeResource(context.getResources(),R.drawable.doc);
	    
	    // 初始设置为false
	    for(int i=0; i<items.size(); i++) {
	    	isCheckMap.put(i, false);
	    }
	  }
	  
	  public int getCount()
	  {
	    return items.size();
	  }

	  public Object getItem(int position)
	  {
	    return items.get(position);
	  }
	  
	  public long getItemId(int position)
	  {
	    return position;
	  }
	  
	  public int getSelectedPosition() {
		return selectedPosition;  
	  }
	  
	  public View getView(final int position,View convertView,ViewGroup parent)
	  {
	    ViewHolder holder;
	    
	    if(convertView == null)
	    {
	      convertView = mInflater.inflate(R.layout.activity_file_chooser_item, null);
	      holder = new ViewHolder();
	      holder.text = (TextView) convertView.findViewById(R.id.text);
	      holder.icon = (ImageView) convertView.findViewById(R.id.icon);
	      holder.check = (CheckBox) convertView.findViewById(R.id.check);
	      
//	      initListener(parent, holder);
	      
	      convertView.setTag(holder);
	    }
	    else
	    {
	      holder = (ViewHolder) convertView.getTag();
	    }
	    
	    holder.check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                // 当前点击的CB  
                boolean cu = !isCheckMap.get(position);  
                // 先将所有的置为FALSE  
                for(Integer p : isCheckMap.keySet()) {  
                	isCheckMap.put(p, false);  
                }
                
                // 再将当前选择CB的实际状态  
                isCheckMap.put(position, cu);  
                FileChooserAdapter.this.notifyDataSetChanged();  
                
                if(cu) {
                	selectedPosition = position;
                } else {
                	selectedPosition = -1;
                }
//                beSelectedData.clear();  
//                if(cu) beSelectedData.add(cs.get(position));  
			}
		});

	    File f=new File(paths.get(position).toString());
	    if(items.get(position).toString().equals("b1"))
	    {
	      holder.text.setText("返回根目录..");
	      holder.icon.setImageBitmap(mIcon1);
	      holder.check.setVisibility(View.INVISIBLE);
	    }
	    else if(items.get(position).toString().equals("b2"))
	    {
	      holder.text.setText("返回上一层..");
	      holder.icon.setImageBitmap(mIcon2);
	      holder.check.setVisibility(View.INVISIBLE);
	    }
	    else
	    {
	      holder.text.setText(f.getName());
	      holder.check.setVisibility(View.VISIBLE);
	      if(f.isDirectory())
	      {
	        holder.icon.setImageBitmap(mIcon3);
	      }
	      else
	      {
	        holder.icon.setImageBitmap(mIcon4);
	      }
	    }
	    
	    holder.check.setTag(position);
//	    if(isCheckMap.containsKey(position)) {
//	    	holder.check.setChecked(isCheckMap.get(position));
//	    } else {
//	    	holder.check.setChecked(false);
//	    }
	    holder.check.setChecked(isCheckMap.get(position));
	    
//	    if(position == checkedPosition) {
//	    	holder.check.setChecked(true);
//	    } else {
//	    	holder.check.setChecked(false);
//	    }
	    
	    return convertView;
	  }
	  

	private class ViewHolder
	  {
	    TextView text;
	    ImageView icon;
	    CheckBox check;
	  }


}
