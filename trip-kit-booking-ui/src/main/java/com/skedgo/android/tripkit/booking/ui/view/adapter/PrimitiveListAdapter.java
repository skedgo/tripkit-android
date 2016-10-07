package com.skedgo.android.tripkit.booking.ui.view.adapter;

import android.widget.BaseAdapter;

import java.util.List;

public abstract class PrimitiveListAdapter<T> extends BaseAdapter {
  private List<T> mList;

  protected PrimitiveListAdapter(List<T> list) {
    mList = list;
  }

  public List<T> getList() {
    return mList;
  }

  public void setList(List<T> list) {
    mList = list;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return (mList == null) ? 0 : mList.size();
  }

  @Override
  public T getItem(int position) {
    return mList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }
}