package com.example.user23.packagemanager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

/**
 * Created by User23 on 20.04.2017.
 */

public class PackageAdapter extends BaseAdapter {
    private List<PackageInfo> packages;


    public PackageAdapter(List<PackageInfo> packagesInfo) {
        this.packages = Collections.unmodifiableList(packagesInfo);
    }

    @Override
    public int getCount() {
        return packages.size();
    }

    @Override
    public PackageInfo getItem(int position) {
        return packages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.package_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.proc_image);
            holder.title = (TextView) view.findViewById(R.id.proc_title_text_textview);
            holder.details = (TextView) view.findViewById(R.id.proc_details_textview);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        PackageInfo packageInfo = getItem(position);

        PackageManager packageManager = parent.getContext().getPackageManager();
        holder.icon.setImageResource(R.mipmap.ic_launcher);
        holder.details.setText(packageInfo.packageName);

        if (packageInfo.applicationInfo != null){
            LoadInfoTask loadInfoTask = new LoadInfoTask(packageInfo,packageManager,holder.icon,holder.title);
            loadInfoTask.execute();

        }
        holder.details.setText(packageInfo.packageName);


        return view;
    }

    private static class ViewHolder {
        private ImageView icon;
        private TextView title;
        private TextView details;
    }


    private static class LoadInfoTask extends AsyncTask<Void,Void,ProcessInfo>{

        private PackageInfo packageInfo;
        private PackageManager packageManager;
        private WeakReference<ImageView> imageRef;
        private WeakReference<TextView> textViewRef;

        public LoadInfoTask(PackageInfo packageInfo,PackageManager packageManager,ImageView imageView,TextView textView) {
            this.packageInfo = packageInfo;
            this.packageManager = packageManager;
            this.imageRef= new WeakReference<ImageView>(imageView);
            this.textViewRef = new WeakReference<TextView>(textView);
        }

        @Override
        protected ProcessInfo doInBackground(Void... params) {

            Drawable icon = packageManager.getApplicationIcon(packageInfo.applicationInfo);
            CharSequence label = packageInfo.applicationInfo.loadLabel(packageManager);

            return new ProcessInfo(icon,label);
        }

        @Override
        protected void onPostExecute(ProcessInfo processInfo) {
            ImageView imageView = imageRef.get();
            TextView textView = textViewRef.get();
            if (imageView == null || textView == null){
                packageInfo.packageName.equals(imageView.getTag().toString());
                return;
            }
            imageView.setImageDrawable(processInfo.getIcon());
            textView.setText(processInfo.getTitle());
        }
    }

    static class ProcessInfo{
        private Drawable icon;
        private CharSequence title;



        public ProcessInfo(Drawable icon, CharSequence title) {
            this.icon = icon;
            this.title = title;

        }

        public Drawable getIcon() {
            return icon;
        }

        public CharSequence getTitle() {
            return title;
        }
    }
}
