package de.xikolo.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.xikolo.BuildConfig;
import de.xikolo.data.net.JsonRequest;
import de.xikolo.model.Course;
import de.xikolo.model.Item;
import de.xikolo.model.Module;
import de.xikolo.util.BuildType;
import de.xikolo.util.Path;
import de.xikolo.util.Network;

public abstract class ItemManager {

    public static final String TAG = ItemManager.class.getSimpleName();

    private Context mContext;

    public ItemManager(Context context) {
        super();
        this.mContext = context;
    }

    public void requestItems(Course course, Module module, boolean cache) {
        if (BuildConfig.buildType == BuildType.DEBUG)
            Log.i(TAG, "requestItems() called | cache " + cache);

        Type type = new TypeToken<List<Item>>() {
        }.getType();
        JsonRequest request = new JsonRequest(Path.API_SAP + Path.COURSES + course.id + "/"
                + Path.MODULES + module.id + "/" + Path.ITEMS, type, mContext) {
            @Override
            public void onRequestReceived(Object o) {
                if (o != null) {
                    List<Item> items = (List<Item>) o;
                    if (BuildConfig.buildType == BuildType.DEBUG)
                        Log.i(TAG, "Items received (" + items.size() + ")");
                    onItemsRequestReceived(items);
                } else {
                    if (BuildConfig.buildType == BuildType.DEBUG)
                        Log.w(TAG, "No Items received");
                    onItemsRequestCancelled();
                }
            }

            @Override
            public void onRequestCancelled() {
                if (BuildConfig.buildType == BuildType.DEBUG)
                    Log.w(TAG, "Items Request cancelled");
                onItemsRequestCancelled();
            }
        };
        request.setCache(cache);
        if (!Network.isOnline(mContext) && cache) {
            request.setCacheOnly(true);
        }
        request.setToken(TokenManager.getAccessToken(mContext));
        request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public abstract void onItemsRequestReceived(List<Item> items);

    public abstract void onItemsRequestCancelled();

}
